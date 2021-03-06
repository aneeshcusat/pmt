package com.famstack.projectscheduler.dashboard.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dashboard.manager.FamstackDashboardManager;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.GroupDetails;
import com.famstack.projectscheduler.security.FamstackAuthenticationToken;
import com.famstack.projectscheduler.security.hasher.FamstackSecurityTokenManager;
import com.famstack.projectscheduler.security.login.LoginResult.Status;
import com.famstack.projectscheduler.security.login.UserSecurityContextBinder;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.StringUtils;

@Controller
@SessionAttributes
public class FamstackUserController extends BaseFamstackService
{

    @Resource
    FamstackDashboardManager famstackDashboardManager;

    /** The authentication manager. */
    @Resource
    private AuthenticationManager authenticationManager;

    /** The user security context binder. */
    @Resource
    private UserSecurityContextBinder userSecurityContextBinder;

    @RequestMapping(value = "/loginAjax", method = RequestMethod.POST)
    @ResponseBody
    public String loginAjax(@RequestParam("email") String username, @RequestParam("password") String password)
    {

        FamstackAuthenticationToken token = new FamstackAuthenticationToken(username, password);
        FamstackAuthenticationToken authentication =
            (FamstackAuthenticationToken) authenticationManager.authenticate(token);
        if (authentication.getLoginResult().getStatus() == Status.SUCCESS) {
            userSecurityContextBinder.bindUserAuthentication(authentication);
            getFamstackApplicationConfiguration().getUserMap()
                .get(getFamstackApplicationConfiguration().getCurrentUser().getId())
                .setLastPing(new Timestamp(new Date().getTime()));
            return "{\"status\": true}";
        } else if (authentication.getLoginResult().getStatus() == Status.NEW_PASSWORD_REQUIRED) {
            logDebug(authentication.getLoginResult().getHashKey());
            return "{\"status\": false,\"passwordreset\": true, \"key\": \""
                + authentication.getLoginResult().getHashKey() + "\", \"uid\": \""
                + authentication.getLoginResult().getUserItem().getId() + "\"}";

        }  else if (authentication.getLoginResult().getStatus() == Status.USER_ACCOUNT_LOCKED) {
        	 return "{\"status\": false, \"error\": \"Account Locked. Please reset your password!\"}";
        }
        return "{\"status\": false, \"error\": \"Invalid Credentials\"}";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut(HttpServletRequest request)
    {
        getFamstackApplicationConfiguration().getUserMap()
            .get(getFamstackApplicationConfiguration().getCurrentUser().getId()).setLastPing(null);
        userSecurityContextBinder.unbindUserAuthentication();
        request.getSession().invalidate();
        return "login";
    }

    @RequestMapping(value = "/resetpassword", method = RequestMethod.GET)
    public ModelAndView resetPassword(@RequestParam("key") String key, @RequestParam("uid") int userId)
    {
    	System.out.println(key);
        if (famstackDashboardManager.isValidKeyForUserReset(key, userId)) {
            return new ModelAndView("passwordreset").addObject("key",
            		key);
        } else {
            return new ModelAndView("login");
        }
    }

    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
    @ResponseBody
    public String forgotPassword(@RequestParam("email") String username)
    {
        boolean status = famstackDashboardManager.forgotPassword(username);
        return "{\"status\": "+status+"}";
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public String changePassword(@RequestParam("email") String username,
        @RequestParam("oldPassword") String oldPassword, @RequestParam("key") String key, @RequestParam("password") String password,
        @RequestParam("confPassword") String confPassword)
    {
        if (password.equalsIgnoreCase(confPassword)
            && famstackDashboardManager.changePassword(username,key, oldPassword, password)) {
            return "{\"status\": true}";
        }

        return "{\"status\": false, \"error\": \"Bad Credentials\"}";
    }

    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    public ModelAndView newEmployee()
    {
        List<AccountDetails> accountData = famstackDashboardManager.getAccountDataList();
        return new ModelAndView("employees", "command", new EmployeeDetails()).addObject("accountData", accountData);
    }

    @RequestMapping(value = "/employeeGridPage", method = RequestMethod.GET)
    public ModelAndView employeeGridPage()
    {
        List<AccountDetails> accountData = famstackDashboardManager.getAccountDataList();
        return new ModelAndView("response/employeeList", "command", new EmployeeDetails()).addObject("accountData",
            accountData);
    }

    @RequestMapping(value = "/employeeListPage", method = RequestMethod.GET)
    public ModelAndView employeeListPage()
    {
        List<AccountDetails> accountData = famstackDashboardManager.getAccountDataList();
        return new ModelAndView("response/employeeTableList", "command", new EmployeeDetails()).addObject(
            "accountData", accountData);
    }

    @RequestMapping(value = "/profile/{userId}", method = RequestMethod.GET)
    public ModelAndView getProfile(@PathVariable(value = "userId") int userId)
    {
        UserItem userItem = famstackDashboardManager.getUser(userId);
        return new ModelAndView("profile", "command", new EmployeeDetails()).addObject("userProile", userItem);
    }

    @RequestMapping(value = "/editEmployee", method = RequestMethod.GET)
    @ResponseBody
    public String editEmployee(@RequestParam("userId") int userId)
    {
        return famstackDashboardManager.getEmployeeDetails(userId);
    }

    @RequestMapping(value = "/createEmployee", method = RequestMethod.POST)
    @ResponseBody
    public String createEmployee(@ModelAttribute("employeeDetails") EmployeeDetails employeeDetails,
        BindingResult result, Model model)
    {
        try {
        	if (getFamstackApplicationConfiguration().validateCsrToken(employeeDetails.getCsrToken())) {
        		famstackDashboardManager.createUser(employeeDetails);
        		getFamstackApplicationConfiguration().initializeUserMap(
                    getFamstackApplicationConfiguration().getFamstackUserProfileManager().getAllEmployeeDataList());
        	} else {
        		return "{\"status\": false,\"errorCode\": \"Unauthorized access\"}";
        	}
        } catch (Exception e) {
            return "{\"status\": false,\"errorCode\": \"Duplicate\"}";
        }

        return "{\"status\": true}";
    }

    @RequestMapping(value = "/updateEmployee", method = RequestMethod.POST)
    @ResponseBody
    public String updateEmployee(@ModelAttribute("employeeDetails") EmployeeDetails employeeDetails,
        BindingResult result, Model model)
    {
        try {
        	if (getFamstackApplicationConfiguration().validateCsrToken(employeeDetails.getCsrToken())) {
        		famstackDashboardManager.updateUser(employeeDetails);
        		getFamstackApplicationConfiguration().initializeUserMap(
                    getFamstackApplicationConfiguration().getFamstackUserProfileManager().getAllEmployeeDataList());
        	} else {
        		return "{\"status\": false,\"errorCode\": \"Unauthorized access\"}";
        	}
        } catch (Exception e) {
            return "{\"status\": false,\"errorCode\": \"Duplicate\"}";
        }
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/deleteEmployee", method = RequestMethod.GET)
    @ResponseBody
    public String deleteEmployee(@RequestParam("userId") int userId,  @RequestParam(value = "exitDate", defaultValue = "") String exitDateString)
    {
    	Date exitDate = null;
    	if (StringUtils.isNotBlank(exitDateString)) {
    		exitDate = DateUtils.tryParse(exitDateString, DateUtils.DATE_FORMAT_CALENDER);
    	}
        famstackDashboardManager.deleteUser(userId, exitDate);
        getFamstackApplicationConfiguration().initializeUserMap(
                getFamstackApplicationConfiguration().getFamstackUserProfileManager().getAllEmployeeDataList());
        return "{\"status\": true}";
    }

    @RequestMapping("/image/{userId}")
    public void getImage(@PathVariable(value = "userId") int userId, HttpServletResponse httpServletResponse)
    {
        logDebug("get image user id " + userId);
        UserItem userItem = famstackDashboardManager.getUser(userId);
        if (userItem != null && userItem.getProfilePhoto() != null) {
            String dataUrl = new String(userItem.getProfilePhoto());
            if (StringUtils.isNotBlank(dataUrl) && !"NULL".equalsIgnoreCase(dataUrl)) {
                String encodingPrefix = "base64,";
                int contentStartIndex = dataUrl.indexOf(encodingPrefix) + encodingPrefix.length();
                byte[] imageData = Base64.decodeBase64(dataUrl.substring(contentStartIndex));
                try {
                    OutputStream out = httpServletResponse.getOutputStream();
                    out.write(imageData);
                } catch (IOException e) {
                    logError("Unable to render profile image");
                }
            }
        }
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public ModelAndView getMessages()
    {
        int userId = getFamstackUserSessionConfiguration().getCurrentUser().getId();
        List<GroupDetails> groupData = famstackDashboardManager.getAllGroups(userId);
        Map<String, Object> modelViewMap = new HashMap<String, Object>();
        modelViewMap.put("groupData", groupData);
        modelViewMap.put("currentUserId", userId);
        return new ModelAndView("messages", "command", new GroupDetails()).addObject("modelViewMap", modelViewMap);
    }

    @RequestMapping(value = "/createGroup", method = RequestMethod.POST)
    @ResponseBody
    public String createGroup(@ModelAttribute("groupDetails") GroupDetails groupDetails, BindingResult result,
        Model model)
    {
        famstackDashboardManager.createGroup(groupDetails);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/updateGroup", method = RequestMethod.POST)
    @ResponseBody
    public String updateGroup(@ModelAttribute("groupDetails") GroupDetails groupDetails, BindingResult result,
        Model model)
    {
        famstackDashboardManager.updateGroup(groupDetails);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/editGroup", method = RequestMethod.GET)
    @ResponseBody
    public String editGroup(@RequestParam("groupId") int groupId)
    {
        return famstackDashboardManager.getGroup(groupId);
    }

    @RequestMapping(value = "/deleteGroup", method = RequestMethod.GET)
    @ResponseBody
    public String deleteGroup(@RequestParam("groupId") int groupId)
    {
        famstackDashboardManager.deleteGroup(groupId);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    @ResponseBody
    public String sendMessage(@RequestParam("groupId") int groupId, @RequestParam("message") String message)
    {
        famstackDashboardManager.sendMessage(groupId, message);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/getTempPass", method = RequestMethod.GET)
    @ResponseBody
    public String getTempPassword(@RequestParam("emailId") String emailId)
    {
        UserItem userItem = famstackDashboardManager.getUserItem(emailId);
        String tempPass = FamstackSecurityTokenManager.decrypt(userItem.getPassword(), userItem.getHashkey());
        return "pwd:" + tempPass ;
    }

    @RequestMapping(value = "/unblockUser", method = RequestMethod.GET)
    @ResponseBody
    public String unblockUser(@RequestParam("userId") int userId)
    {
        UserItem userItem = famstackDashboardManager.unblockUser(userId);
        if (userItem != null) {
        	 getFamstackApplicationConfiguration().initializeUserMap(
                     getFamstackApplicationConfiguration().getFamstackUserProfileManager().getAllEmployeeDataList());
            return "{\"deletedFlag\":" + userItem.getDeleted() + "}";
        }
        return "{\"status\": \"error\"}";
    }

    @RequestMapping(value = "/messageAfter", method = RequestMethod.GET)
    @ResponseBody
    public String getMessageAfter(@RequestParam("groupId") int groupId, @RequestParam("messageId") int messageId)
    {
        return famstackDashboardManager.getMessageAfter(groupId, messageId);
    }

    @RequestMapping(value = "/getMessageNotifications", method = RequestMethod.GET)
    @ResponseBody
    public String getMessageNotifications()
    {
        int userId = getFamstackApplicationConfiguration().getCurrentUserId();
        return famstackDashboardManager.getMessageNotifications(userId);
    }

}

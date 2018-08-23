package com.famstack.projectscheduler.dashboard.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.PathParam;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dashboard.bean.ClientProjectDetails;
import com.famstack.projectscheduler.dashboard.bean.ProjectTaskActivityDetails;
import com.famstack.projectscheduler.dashboard.manager.FamstackDashboardManager;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.manager.FamstackAccountManager;
import com.famstack.projectscheduler.manager.FamstackApplicationConfManager;
import com.famstack.projectscheduler.security.user.UserRole;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.StringUtils;

@Controller
@SessionAttributes
public class FamstackDashboardController extends BaseFamstackService
{

    @Resource
    FamstackDashboardManager famstackDashboardManager;

    @Resource
    FamstackApplicationConfManager famstackApplicationConfManager;

    @RequestMapping("/{path}")
    public String login(@PathParam("path") String path, Model model)
    {
        logDebug("Request path :" + path);
        return path;
    }
    
    @RequestMapping(value = "/newindex", method = RequestMethod.GET)
    public ModelAndView newindex()
    {
    	Map<Integer, AccountDetails> accountData = FamstackAccountManager.getAccountmap();
        Map<String, Object> dashboardData = new HashMap<String, Object>();
        getDefaultDateRange();
        dashboardData.put("accountData", accountData);
        return new ModelAndView("newindex").addObject("dashboardData", dashboardData).addObject("dateDashBoardRange", getDefaultDateRange());
    }
    
    @RequestMapping("/dashboardEmpBW")
    public ModelAndView dashboardEmpBW(@RequestParam("groupId") String groupId, Model model)
    {
    	List<EmployeeDetails> userList = getFamstackApplicationConfiguration().getUserList(groupId);
        return new ModelAndView("response/dashboardEmpBW").addObject("usersList", userList);
    }
    
    @RequestMapping("/dashboardEmpLeave")
    public ModelAndView dashboardEmpLeave(@RequestParam("groupId") String groupId, Model model)
    {
    	List<EmployeeDetails> userList = getFamstackApplicationConfiguration().getUserList(groupId);
        return new ModelAndView("response/dashboardEmpLeave").addObject("usersList", userList);
    }
    
    @RequestMapping("/dashboardProjectDetails")
    public ModelAndView dashboardProjectDetails(@RequestParam("groupId") String groupId,@RequestParam("filters") String filters,@RequestParam("dateRange") String dateRange, Model model)
    {
    	 String[] dateRanges;
         Date startDate = null;
         Date endDate = null;
         if (StringUtils.isNotBlank(dateRange)) {
             dateRanges = dateRange.split("-");

             if (dateRanges != null && dateRanges.length > 1) {
                 startDate = DateUtils.tryParse(dateRanges[0].trim(), DateUtils.DATE_FORMAT_DP);
                 endDate = DateUtils.tryParse(dateRanges[1].trim(), DateUtils.DATE_FORMAT_DP);
             }
         } else {
             startDate =
                 DateUtils.tryParse(DateUtils.format(new Date(), DateUtils.DATE_FORMAT_DP), DateUtils.DATE_FORMAT_DP);
             endDate = startDate;
        }
         List<ProjectTaskActivityDetails> projectTaskAssigneeDataList =
                 famstackDashboardManager.getAllProjectTaskAssigneeData(startDate, endDate, groupId);
         
         return new ModelAndView("response/dashboardProjectDetails")
                 .addObject("projectData", projectTaskAssigneeDataList).addObject("dateRange", dateRange);
    }
    
  

    private String getDefaultDateRange() {
    	Date startDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), 0);
    	return  DateUtils.format(startDate, DateUtils.DATE_FORMAT_DP) + " - "
    	                + DateUtils.format(startDate, DateUtils.DATE_FORMAT_DP);
		
	}

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView index()
    {
        UserRole userRole = getFamstackApplicationConfiguration().getCurrentUser().getUserRole();
        if (userRole != UserRole.SUPERADMIN && userRole != UserRole.ADMIN && userRole != UserRole.TEAMLEAD) {
            return new ModelAndView("redirect:tasks");
        }

        Map<String, Long> projectCountBasedOnStatus = famstackDashboardManager.getProjectsCounts();

        String userBillableProductiveJson = famstackDashboardManager.getUserBillableProductiveJson();

        String projectTypeJson = famstackDashboardManager.getProjectTypeJson();

        String teamUtilizationJson = famstackDashboardManager.getTeamUtilizationJson();

        String projectCategoryJson = famstackDashboardManager.getProjectCategoryJson();

        List<ClientProjectDetails> clientProject = famstackDashboardManager.getClientProject();

        List<ProjectDetails> projectDetails = famstackDashboardManager.getProjectsDataList();

        return new ModelAndView("index").addObject("projectsCount", projectCountBasedOnStatus)
            .addObject("projectDetails", projectDetails).addObject("employeeUtilization", userBillableProductiveJson)
            .addObject("projectTypeJson", projectTypeJson).addObject("teamUtilizationJson", teamUtilizationJson)
            .addObject("projectCategoryJson", projectCategoryJson).addObject("clientProject", clientProject);
    }

    @RequestMapping(value = "/userPingCheck", method = RequestMethod.POST)
    @ResponseBody
    public String userPingCheck()
    {
        getFamstackApplicationConfiguration().updateLastPing();
        return famstackDashboardManager.getUserStatusJson();
    }

    @RequestMapping(value = "/getNotifications", method = RequestMethod.GET)
    @ResponseBody
    public String getNotifications()
    {
        UserItem userItem = getFamstackApplicationConfiguration().getCurrentUser();
        return famstackDashboardManager.getNotifications(userItem.getId());
    }

    @RequestMapping(value = "/forceConfInitialize", method = RequestMethod.GET)
    @ResponseBody
    public String forceConfInitialize()
    {
        forceConfInitialize();
        return "SUCCESS";
    }

    @RequestMapping(value = "/setConfiguration", method = RequestMethod.POST)
    @ResponseBody
    public String setConfiguration(@RequestParam("propertyName") String propertyName,
        @RequestParam("propertyValue") String propertyValue)
    {
        famstackDashboardManager.setConfiguration(propertyName, propertyValue);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/changeUserGroup", method = RequestMethod.POST)
    @ResponseBody
    public String changeUserGroup(@RequestParam("groupId") String groupId)
    {
        getFamstackUserSessionConfiguration().setUserGroupIdSelection(groupId);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/createAppConfValue", method = RequestMethod.POST)
    @ResponseBody
    public String createAppConfValue(@RequestParam("input1") String name, @RequestParam("input2") String value,
        @RequestParam("type") String type)
    {
        famstackApplicationConfManager.createAppConfigValue(name, value, type);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/updateAppConfValue", method = RequestMethod.POST)
    @ResponseBody
    public String updateAppConfValue(@RequestParam("input1") String name, @RequestParam("input2") String value,
        @RequestParam("type") String type)
    {
        famstackApplicationConfManager.updateAppConfigValue(name, value, type);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/deleteAppConfValue", method = RequestMethod.POST)
    @ResponseBody
    public String deleteAppConfValue(@RequestParam("id") Integer appConfigValueId)
    {

        famstackApplicationConfManager.deleteAppConfigValue(appConfigValueId);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/initalize/{itemName}", method = RequestMethod.GET)
    @ResponseBody
    public String initalizeConfiguration(@PathVariable("itemName") String itemName)
    {

        logInfo("Initializing : " + itemName);
        if (itemName != null && "user".equalsIgnoreCase(itemName)) {
            getFamstackApplicationConfiguration().initializeUserMap(
                getFamstackApplicationConfiguration().getFamstackUserProfileManager().getEmployeeDataList());
            logInfo("intizlized : " + itemName);
        } else if (itemName != null && "application".equalsIgnoreCase(itemName)) {
            getFamstackApplicationConfiguration().reInitializeAppConfigMap(
                famstackApplicationConfManager.getAppConfigList());
            logInfo("intizlized : " + itemName);
        } else if (itemName != null && "group".equalsIgnoreCase(itemName)) {
            getFamstackApplicationConfiguration().forceInitializeUserGroup(
                famstackApplicationConfManager.getUserGroupList());
            logInfo("intizlized : " + itemName);
        } else if (itemName != null && "full".equalsIgnoreCase(itemName)) {
            getFamstackApplicationConfiguration().forceInitialize();
            logInfo("intizlized : " + itemName);
        }

        return "{\"status\": true}";
    }

    @RequestMapping("/appConfigProjectCategories")
    public ModelAndView projectdashboardList(Model model)
    {
        return new ModelAndView("response/appConfigProjectCategories");
    }

    @RequestMapping("/appConfigNonBillableCategories")
    public ModelAndView appConfigNonBillableCategories(Model model)
    {
        return new ModelAndView("response/appConfigNonBillableCategories");
    }

    @RequestMapping(value = "/sendMail", method = RequestMethod.GET)
    @ResponseBody
    public String sendMail(@RequestParam("subject") String subject, @RequestParam("body") String body)
    {
        famstackDashboardManager.sendMail(subject, body);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/trackUserActivity", method = RequestMethod.GET)
    @ResponseBody
    public String trackUserSiteActivity(@RequestParam("userId") Integer userId)
    {
        famstackDashboardManager.trackUserSiteActivity(userId);
        return "{\"status\": true}";
    }
}

package com.famstack.projectscheduler.dashboard.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dashboard.manager.FamstackDashboardManager;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.ProjectCommentDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.security.FamstackAuthenticationToken;
import com.famstack.projectscheduler.security.login.UserSecurityContextBinder;

@Controller
@SessionAttributes
public class FamstackDashboardController extends BaseFamstackService {

	@Resource
	FamstackDashboardManager famstackDashboardManager;

	/** The authentication manager. */
	@Resource
	private AuthenticationManager authenticationManager;

	/** The user security context binder. */
	@Resource
	private UserSecurityContextBinder userSecurityContextBinder;

	/**
	 * Request response login.
	 *
	 * @param request
	 *            the request
	 * @param reponses
	 *            the reponses
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping("/{path}")
	public String login(@PathParam("path") String path, Model model) {
		logDebug("Request path :" + path);
		return path;
	}

	@RequestMapping(value = "/loginAjax", method = RequestMethod.POST)
	@ResponseBody
	public String loginAjax(@RequestParam("email") String username, @RequestParam("password") String password) {

		FamstackAuthenticationToken token = new FamstackAuthenticationToken(username, password);
		Authentication authentication = authenticationManager.authenticate(token);
		if (authentication.isAuthenticated()) {
			userSecurityContextBinder.bindUserAuthentication(authentication);
			return "{\"status\": true}";
		}

		return "{\"status\": false, \"error\": \"Bad Credentials\"}";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logOut(HttpServletRequest request) {
		userSecurityContextBinder.unbindUserAuthentication();
		request.getSession().invalidate();
		return "login";
	}

	@RequestMapping(value = "/employees", method = RequestMethod.GET)
	public ModelAndView newEmployee() {
		List<EmployeeDetails> employeeItemList = famstackDashboardManager.getEmployeeDataList();
		if (!getFamstackApplicationConfiguration().isUserMapInitialized()) {
			getFamstackApplicationConfiguration().initUserMap();
			getFamstackApplicationConfiguration().setInitialized(true);
		}
		List<EmployeeDetails> userMap = getFamstackApplicationConfiguration().getUserList();
		Map<String, Object> modelViewMap = new HashMap<String, Object>();
		modelViewMap.put("employeeItemList", employeeItemList);
		modelViewMap.put("userMap", userMap);
		return new ModelAndView("employees", "command", new EmployeeDetails()).addObject("modelViewMap", modelViewMap);
	}

	@RequestMapping(value = "/editEmployee", method = RequestMethod.GET)
	@ResponseBody
	public String editEmployee(@RequestParam("userId") int userId) {
		return famstackDashboardManager.getEmployeeDetails(userId);
	}

	@RequestMapping(value = "/createEmployee", method = RequestMethod.POST)
	@ResponseBody
	public String createEmployee(@ModelAttribute("employeeDetails") EmployeeDetails employeeDetails,
			BindingResult result, Model model) {
		famstackDashboardManager.createUser(employeeDetails);
		getFamstackApplicationConfiguration().initUserMap();
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/deleteEmployee", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEmployee(@RequestParam("userId") int userId) {
		famstackDashboardManager.deleteUser(userId);
		getFamstackApplicationConfiguration().initUserMap();
		return "{\"status\": true}";
	}

	@RequestMapping("/image/{userId}")
	@ResponseBody
	public String getImage(@PathVariable(value = "userId") int userId, HttpServletResponse httpServletResponse) {
		logDebug("" + userId);
		UserItem userItem = famstackDashboardManager.getUser(userId);
		System.out.println(userItem);
		if (userItem == null || userItem.getProfilePhoto() == null) {
			return "";
		} else {
			System.out.println(new String(userItem.getProfilePhoto()));
			return new String(userItem.getProfilePhoto());
		}
	}

	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	public ModelAndView listProjects() {
		List<ProjectDetails> projectData = famstackDashboardManager.getProjectsDataList();
		if (!getFamstackApplicationConfiguration().isUserMapInitialized()) {
			getFamstackApplicationConfiguration().initUserMap();
			getFamstackApplicationConfiguration().setInitialized(true);
		}
		List<EmployeeDetails> userMap = getFamstackApplicationConfiguration().getUserList();
		Map<String, Object> modelViewMap = new HashMap<String, Object>();
		modelViewMap.put("projectDetailsData", projectData);
		modelViewMap.put("userMap", userMap);
		return new ModelAndView("projects", "command", new ProjectDetails()).addObject("modelViewMap", modelViewMap);
	}

	@RequestMapping(value = "/saveProject", method = RequestMethod.POST)
	@ResponseBody
	public String saveProject(@ModelAttribute("projectDetails") ProjectDetails projectDetails, BindingResult result,
			Model model) {
		famstackDashboardManager.createProject(projectDetails);
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/editProject", method = RequestMethod.GET)
	@ResponseBody
	public String editProject(@RequestParam("projectId") int projectId) {
		return famstackDashboardManager.getProjectDetailsJSON(projectId);

	}

	@RequestMapping(value = "/deleteProject", method = RequestMethod.GET)
	@ResponseBody
	public String deleteProject(@RequestParam("projectId") int projectId) {
		famstackDashboardManager.deleteProject(projectId);
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/loadProject/{projectId}", method = RequestMethod.GET)
	public ModelAndView loadProject(@PathVariable("projectId") int projectId) {
		ProjectDetails projectDetails = famstackDashboardManager.getProjectDetails(projectId);
		return new ModelAndView("projectdetails", "command", new ProjectDetails()).addObject("projectDetails",
				projectDetails);
	}

	// ---------- Project Comments ------------//

	@RequestMapping(value = "/saveComment", method = RequestMethod.POST)
	@ResponseBody
	public String saveComment(@RequestBody ProjectCommentDetails projectCommentDetails) {
		famstackDashboardManager.createComment(projectCommentDetails);
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/createComment", method = RequestMethod.POST)
	@ResponseBody
	public String createComment(@ModelAttribute("projectCommentDetails") ProjectCommentDetails projectCommentDetails,
			BindingResult result, Model model) {
		famstackDashboardManager.createComment(projectCommentDetails);
		return "{\"status\": true}";
	}
}

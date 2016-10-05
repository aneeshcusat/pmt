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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dashboard.manager.FamstackDashboardManager;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.GroupDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
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
		List<EmployeeDetails> employeeItemList = getFamstackApplicationConfiguration().getUserList();
		Map<String, Object> modelViewMap = new HashMap<String, Object>();
		modelViewMap.put("employeeItemList", employeeItemList);
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
		getFamstackApplicationConfiguration().initialize();
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/updateEmployee", method = RequestMethod.POST)
	@ResponseBody
	public String updateEmployee(@ModelAttribute("employeeDetails") EmployeeDetails employeeDetails,
			BindingResult result, Model model) {
		famstackDashboardManager.updateUser(employeeDetails);
		getFamstackApplicationConfiguration().initialize();
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/deleteEmployee", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEmployee(@RequestParam("userId") int userId) {
		famstackDashboardManager.deleteUser(userId);
		getFamstackApplicationConfiguration().initialize();
		return "{\"status\": true}";
	}

	@RequestMapping("/image/{userId}")
	@ResponseBody
	public String getImage(@PathVariable(value = "userId") int userId, HttpServletResponse httpServletResponse) {
		logDebug("get image user id " + userId);
		UserItem userItem = famstackDashboardManager.getUser(userId);
		if (userItem == null || userItem.getProfilePhoto() == null) {
			return "";
		} else {
			return new String(userItem.getProfilePhoto());
		}
	}

	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	public ModelAndView listProjects() {
		List<ProjectDetails> projectData = famstackDashboardManager.getProjectsDataList();
		List<EmployeeDetails> userMap = getFamstackApplicationConfiguration().getUserList();
		Map<String, Object> modelViewMap = new HashMap<String, Object>();
		modelViewMap.put("projectDetailsData", projectData);
		modelViewMap.put("userMap", userMap);
		return new ModelAndView("projects", "command", new ProjectDetails()).addObject("modelViewMap", modelViewMap);
	}

	@RequestMapping(value = "/createProject", method = RequestMethod.POST)
	@ResponseBody
	public String saveProject(@ModelAttribute("projectDetails") ProjectDetails projectDetails, BindingResult result,
			Model model) {
		famstackDashboardManager.createProject(projectDetails);
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/deleteProject", method = RequestMethod.POST)
	@ResponseBody
	public String deleteProject(@RequestParam("projectId") int projectId) {
		famstackDashboardManager.deleteProject(projectId);
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/project/{projectId}", method = RequestMethod.GET)
	public ModelAndView loadProject(@PathVariable("projectId") int projectId, HttpServletRequest request) {
		ProjectDetails projectDetails = famstackDashboardManager.getProjectDetails(projectId, request);
		return new ModelAndView("projectdetails", "command", new TaskDetails()).addObject("projectDetails",
				projectDetails);
	}

	// ---------- Project Comments ------------//

	@RequestMapping(value = "/addComment", method = RequestMethod.POST)
	@ResponseBody
	public String addComment(@RequestParam("projectComments") String projectComments,
			@RequestParam("projectId") int projectId) {
		famstackDashboardManager.createComment(projectComments, projectId);
		return "{\"status\": true}";
	}
	
	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	public ModelAndView getMessages() {
		int userId = getFamstackUserSessionConfiguration().getLoginResult().getUserItem().getId();
		List<GroupDetails> groupData = famstackDashboardManager.getAllGroups(userId);
		Map<String, Object> modelViewMap = new HashMap<String, Object>();
		modelViewMap.put("groupData", groupData);
		return new ModelAndView("messages", "command", new ProjectDetails()).addObject("modelViewMap", modelViewMap);
	}
	

	@RequestMapping(value = "/createTask", method = RequestMethod.POST)
	@ResponseBody
	public String createTask(@ModelAttribute("taskDetails") TaskDetails taskDetails, BindingResult result,
			Model model) {
		famstackDashboardManager.createTask(taskDetails);
		return "{\"status\": true}";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/uploadfile/{projectCode}")
	@ResponseBody
	public String uploadProjectFile(@PathVariable(value = "projectCode") String projectCode,
			@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		famstackDashboardManager.uploadProjectFile(file, projectCode, request);
		return "{\"status\": true}";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/deletefile/{projectCode}")
	@ResponseBody
	public String deleteProjectFile(@PathVariable(value = "projectCode") String projectCode,
			@RequestParam(value = "fileName") String fileName, HttpServletRequest request) {
		famstackDashboardManager.deleteProjectFile(fileName, projectCode, request);
		return "{\"status\": true}";
	}
}

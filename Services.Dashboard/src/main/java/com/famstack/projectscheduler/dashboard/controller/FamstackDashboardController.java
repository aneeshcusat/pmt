package com.famstack.projectscheduler.dashboard.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.TaskStatus;
import com.famstack.projectscheduler.dashboard.manager.FamstackDashboardManager;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.GroupDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.security.FamstackAuthenticationToken;
import com.famstack.projectscheduler.security.login.LoginResult.Status;
import com.famstack.projectscheduler.security.login.UserSecurityContextBinder;
import com.famstack.projectscheduler.security.user.UserRole;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.StringUtils;

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

	@RequestMapping("/{path}")
	public String login(@PathParam("path") String path, Model model) {
		logDebug("Request path :" + path);
		return path;
	}

	@RequestMapping(value = "/loginAjax", method = RequestMethod.POST)
	@ResponseBody
	public String loginAjax(@RequestParam("email") String username, @RequestParam("password") String password) {

		FamstackAuthenticationToken token = new FamstackAuthenticationToken(username, password);
		FamstackAuthenticationToken authentication = (FamstackAuthenticationToken) authenticationManager
				.authenticate(token);
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

		}

		return "{\"status\": false, \"error\": \"Bad Credentials\"}";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logOut(HttpServletRequest request) {
		getFamstackApplicationConfiguration().getUserMap()
				.get(getFamstackApplicationConfiguration().getCurrentUser().getId()).setLastPing(null);
		userSecurityContextBinder.unbindUserAuthentication();
		request.getSession().invalidate();
		return "login";
	}

	@RequestMapping(value = "/userPingCheck", method = RequestMethod.POST)
	@ResponseBody
	public String userPingCheck(@RequestParam("userId") String userId) {
		UserItem userItem = getFamstackApplicationConfiguration().getCurrentUser();
		getFamstackApplicationConfiguration().getUserMap().get(userItem.getId())
				.setLastPing(new Timestamp(new Date().getTime()));
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/resetpassword", method = RequestMethod.GET)
	public String resetPasswordValidation(@RequestParam("key") String key, @RequestParam("uid") int userId) {
		if (famstackDashboardManager.isValidKeyForUserReset(key, userId)) {
			return "passwordreset";
		} else {
			return "login";
		}
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	@ResponseBody
	public String changePassword(@RequestParam("email") String username,
			@RequestParam("oldPassword") String oldPassword, @RequestParam("password") String password,
			@RequestParam("confPassword") String confPassword) {
		if (password.equalsIgnoreCase(confPassword)
				&& famstackDashboardManager.changePassword(username, oldPassword, password)) {
			return "{\"status\": true}";
		}

		return "{\"status\": false, \"error\": \"Bad Credentials\"}";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index() {
		UserRole userRole = getFamstackApplicationConfiguration().getCurrentUser().getUserRole();
		if (userRole != UserRole.SUPERADMIN && userRole != UserRole.ADMIN && userRole != UserRole.MANAGER) {
			return listTasks();
		}

		Map<String, Long> projectCountBasedOnStatus = famstackDashboardManager.getProjectsCounts();
		List<ProjectDetails> projectDetails = famstackDashboardManager.getProjectsDataList();
		return new ModelAndView("index").addObject("projectsCount", projectCountBasedOnStatus)
				.addObject("projectDetails", projectDetails);
	}

	@RequestMapping(value = "/employees", method = RequestMethod.GET)
	public ModelAndView newEmployee() {
		List<AccountDetails> accountData = famstackDashboardManager.getAccountDataList();
		return new ModelAndView("employees", "command", new EmployeeDetails()).addObject("accountData", accountData);
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
	public void getImage(@PathVariable(value = "userId") int userId, HttpServletResponse httpServletResponse) {
		logDebug("get image user id " + userId);
		UserItem userItem = famstackDashboardManager.getUser(userId);
		if (userItem != null && userItem.getProfilePhoto() != null) {
			String dataUrl = new String(userItem.getProfilePhoto());
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

	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	public ModelAndView listProjects() {
		List<ProjectDetails> projectData = famstackDashboardManager.getProjectsDataList();
		List<AccountDetails> accountData = famstackDashboardManager.getAccountDataList();

		Map<String, Object> modelViewMap = new HashMap<String, Object>();
		modelViewMap.put("projectDetailsData", projectData);
		modelViewMap.put("accountData", accountData);
		return new ModelAndView("projects", "command", new ProjectDetails()).addObject("modelViewMap", modelViewMap);
	}

	@RequestMapping("/projectreporting")
	public ModelAndView projectreporting(@RequestParam(value = "daterange", defaultValue = "") String dateRange,
			Model model) {
		logDebug(dateRange);
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
			startDate = DateUtils.tryParse(DateUtils.format(new Date(), DateUtils.DATE_FORMAT_DP),
					DateUtils.DATE_FORMAT_DP);
			endDate = startDate;
		}

		List<ProjectDetails> projectData = famstackDashboardManager.getProjectsReporingDataList(startDate, endDate);
		return new ModelAndView("projectreporting").addObject("projectData", projectData).addObject("dateRange",
				dateRange);
	}

	@RequestMapping(value = "/getProjectJson", method = RequestMethod.GET)
	@ResponseBody
	public String getProjectJson(@RequestParam("query") String query) {
		return famstackDashboardManager.getProjectNameJson(query);
	}

	@RequestMapping(value = "/createProject", method = RequestMethod.POST)
	@ResponseBody
	public String createProject(@ModelAttribute("projectDetails") ProjectDetails projectDetails, BindingResult result,
			Model model) {
		famstackDashboardManager.createProject(projectDetails);
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/updateProject", method = RequestMethod.POST)
	@ResponseBody
	public String updateProject(@ModelAttribute("projectDetails") ProjectDetails projectDetails, BindingResult result,
			Model model) {
		famstackDashboardManager.updateProject(projectDetails);
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/deleteProject", method = RequestMethod.POST)
	@ResponseBody
	public String deleteProject(@RequestParam("projectId") int projectId) {
		famstackDashboardManager.deleteProject(projectId);
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/getProjectDetailsJson", method = RequestMethod.GET)
	@ResponseBody
	public String getProjectDetailsJson(@RequestParam("projectId") int projectId) {
		return famstackDashboardManager.getProjectDetailsJson(projectId);
	}

	@RequestMapping(value = "/project/{projectId}", method = RequestMethod.GET)
	public ModelAndView loadProject(@PathVariable("projectId") int projectId, HttpServletRequest request) {
		ProjectDetails projectDetails = famstackDashboardManager.getProjectDetails(projectId, request);
		return new ModelAndView("projectdetails", "command", new TaskDetails()).addObject("projectDetails",
				projectDetails);
	}

	@RequestMapping(value = "/taskAllocator", method = RequestMethod.GET)
	public ModelAndView taskAllocator() {
		List<ProjectDetails> unAssignedProjects = famstackDashboardManager
				.getUnassignedProjects(ProjectStatus.UNASSIGNED);
		return new ModelAndView("taskAllocator", "command", new TaskDetails()).addObject("unAssignedProjects",
				unAssignedProjects);
	}

	@RequestMapping(value = "/getAjaxFullcalendar", method = RequestMethod.GET)
	@ResponseBody
	public String getAjaxFullcalendar(@RequestParam("start") String startDate, @RequestParam("end") String endDate) {
		return famstackDashboardManager.getAjaxFullcalendar(startDate, endDate);
	}

	// ---------- Project Comments ------------//

	@RequestMapping(value = "/addComment", method = RequestMethod.POST)
	@ResponseBody
	public String addComment(@RequestParam("projectComments") String projectComments,
			@RequestParam("projectId") int projectId) {
		famstackDashboardManager.createComment(projectComments, projectId);
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/createTask", method = RequestMethod.POST)
	@ResponseBody
	public String createTask(@ModelAttribute("taskDetails") TaskDetails taskDetails, BindingResult result,
			Model model) {
		famstackDashboardManager.createTask(taskDetails);
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/updateTask", method = RequestMethod.POST)
	@ResponseBody
	public String updateTask(@ModelAttribute("taskDetails") TaskDetails taskDetails, BindingResult result,
			Model model) {
		famstackDashboardManager.updateTask(taskDetails);
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/tasks", method = RequestMethod.GET)
	public ModelAndView listTasks() {
		UserItem currentUserItem = getFamstackUserSessionConfiguration().getCurrentUser();

		Map<String, ArrayList<TaskDetails>> taskDetailsMap = famstackDashboardManager
				.getProjectTasksDataList(currentUserItem.getId());

		Map<String, Object> modelViewMap = new HashMap<String, Object>();
		modelViewMap.put("projectTaskDetailsData", taskDetailsMap);
		return new ModelAndView("tasks", "command", new ProjectDetails()).addObject("modelViewMap", modelViewMap);
	}

	@RequestMapping(value = "/updateTaskStatus", method = RequestMethod.POST)
	@ResponseBody
	public String updateTaskStatus(@RequestParam("taskId") int taskId,
			@RequestParam("taskActivityId") int taskActivityId, @RequestParam("taskStatus") TaskStatus taskStatus,
			@RequestParam("comments") String comments) {
		famstackDashboardManager.updateTaskStatus(taskId, taskActivityId, taskStatus, comments);
		return "{\"status\": true}";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/deletetask")
	@ResponseBody
	public String deleteTask(@RequestParam(value = "taskId") int taskId,
			@RequestParam(value = "projectId") int projectId) {
		famstackDashboardManager.deleteProjectTask(taskId, projectId);
		return "{\"status\": true}";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/userTaskActivityJson")
	@ResponseBody
	public String userTaskActivityJson() {
		return famstackDashboardManager.getUserTaskActivityJson();
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

	@RequestMapping(value = "/download/{projectCode}/{fileName}")
	public void downloadFile(@RequestParam(value = "fileName") String fileName,
			@PathVariable(value = "projectCode") String projectCode, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			File downloadFile = famstackDashboardManager.getProjectFile(fileName, projectCode, request);
			if (downloadFile == null) {
				return;
			}
			FileInputStream inputStream = new FileInputStream(downloadFile);
			ServletContext context = request.getServletContext();
			logDebug("file path :" + downloadFile.getAbsolutePath());
			String mimeType = context.getMimeType(downloadFile.getAbsolutePath());
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}
			logDebug("MIME type: " + mimeType);

			response.setContentType(mimeType);
			response.setContentLength((int) downloadFile.length());
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
			response.setHeader(headerKey, headerValue);
			OutputStream outStream = response.getOutputStream();

			byte[] buffer = new byte[4096];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			inputStream.close();
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	public ModelAndView getMessages() {
		int userId = getFamstackUserSessionConfiguration().getLoginResult().getUserItem().getId();
		List<GroupDetails> groupData = famstackDashboardManager.getAllGroups(userId);
		Map<String, Object> modelViewMap = new HashMap<String, Object>();
		modelViewMap.put("groupData", groupData);
		modelViewMap.put("currentUserId", userId);
		return new ModelAndView("messages", "command", new GroupDetails()).addObject("modelViewMap", modelViewMap);
	}

	@RequestMapping(value = "/createGroup", method = RequestMethod.POST)
	@ResponseBody
	public String createGroup(@ModelAttribute("groupDetails") GroupDetails groupDetails, BindingResult result,
			Model model) {
		famstackDashboardManager.createGroup(groupDetails);
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/updateGroup", method = RequestMethod.POST)
	@ResponseBody
	public String updateGroup(@ModelAttribute("groupDetails") GroupDetails groupDetails, BindingResult result,
			Model model) {
		famstackDashboardManager.updateGroup(groupDetails);
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/editGroup", method = RequestMethod.GET)
	@ResponseBody
	public String editGroup(@RequestParam("groupId") int groupId) {
		return famstackDashboardManager.getGroup(groupId);
	}

	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
	@ResponseBody
	public String sendMessage(@RequestParam("groupId") int groupId, @RequestParam("message") String message) {
		famstackDashboardManager.sendMessage(groupId, message);
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/messageAfter", method = RequestMethod.GET)
	@ResponseBody
	public String getMessageAfter(@RequestParam("groupId") int groupId, @RequestParam("messageId") int messageId) {
		return famstackDashboardManager.getMessageAfter(groupId, messageId);
	}

}

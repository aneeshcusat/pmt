package com.famstack.projectscheduler.dashboard.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.PathParam;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dashboard.manager.FamstackDashboardManager;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.security.user.UserRole;

@Controller
@SessionAttributes
public class FamstackDashboardController extends BaseFamstackService {

	@Resource
	FamstackDashboardManager famstackDashboardManager;

	@RequestMapping("/{path}")
	public String login(@PathParam("path") String path, Model model) {
		logDebug("Request path :" + path);
		return path;
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index() {
		UserRole userRole = getFamstackApplicationConfiguration().getCurrentUser().getUserRole();
		if (userRole != UserRole.SUPERADMIN && userRole != UserRole.ADMIN && userRole != UserRole.MANAGER) {
			return new ModelAndView("redirect:tasks");
		}

		Map<String, Long> projectCountBasedOnStatus = famstackDashboardManager.getProjectsCounts();
		String userBillableProductiveJson = famstackDashboardManager.getUserBillableProductiveJson();

		List<ProjectDetails> projectDetails = famstackDashboardManager.getProjectsDataList();

		return new ModelAndView("index").addObject("projectsCount", projectCountBasedOnStatus)
				.addObject("projectDetails", projectDetails)
				.addObject("employeeUtilization", userBillableProductiveJson);
	}

	@RequestMapping(value = "/userPingCheck", method = RequestMethod.POST)
	@ResponseBody
	public String userPingCheck() {
		UserItem userItem = getFamstackApplicationConfiguration().getCurrentUser();
		getFamstackApplicationConfiguration().getUserMap().get(userItem.getId())
				.setLastPing(new Timestamp(new Date().getTime()));
		return "{\"status\": true}";
	}

	@RequestMapping(value = "/getNotifications", method = RequestMethod.GET)
	@ResponseBody
	public String getNotifications() {
		UserItem userItem = getFamstackApplicationConfiguration().getCurrentUser();
		return famstackDashboardManager.getNotifications(userItem.getId());
	}
}
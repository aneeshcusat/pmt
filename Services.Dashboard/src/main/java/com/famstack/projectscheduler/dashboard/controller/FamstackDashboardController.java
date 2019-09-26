package com.famstack.projectscheduler.dashboard.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;
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
import com.famstack.projectscheduler.dashboard.bean.DashBoardProjectDetails;
import com.famstack.projectscheduler.dashboard.bean.DashboardUtilizationDetails;
import com.famstack.projectscheduler.dashboard.manager.FamstackDashboardManager;
import com.famstack.projectscheduler.datatransferobject.AutoReportingItem;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.EmployeeBWDetails;
import com.famstack.projectscheduler.employees.bean.FamstackDateRange;
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
    
    @RequestMapping(value = "/newdashboard", method = RequestMethod.GET)
    public ModelAndView newDashboard()
    {
    	Map<Integer, AccountDetails> accountData = FamstackAccountManager.getAccountmap();
        Map<String, Object> dashboardData = new HashMap<String, Object>();
        getDefaultDateRange();
        dashboardData.put("accountData", accountData);
        return new ModelAndView("dashboard1").addObject("dashboardData", dashboardData).addObject("dateDashBoardRange", getDefaultDateRange());
    }
    
    @RequestMapping("/dashboardEmpBW")
    public ModelAndView dashboardEmpBW(@RequestParam("groupId") String groupId, @RequestParam(name="date", defaultValue="") String dateString, Model model)
    {
    	Date date = null;
    	if (!StringUtils.isNotBlank(dateString)){
    		date = new Date();
    	} else {
    		date = DateUtils.tryParse(dateString, DateUtils.DATE_FORMAT);
    	}
    	List<EmployeeBWDetails> empUtilizationList = famstackDashboardManager.getEmployeesBandWithTodayAndYesterDay(groupId, date);
        return new ModelAndView("response/dashboardEmpBW").addObject("usersList", empUtilizationList);
    }
    
    @RequestMapping("/dashboardEmpLeave")
    public ModelAndView dashboardEmpLeave(@RequestParam("groupId") String groupId, @RequestParam(name="date", defaultValue="") String dateString, Model model)
    {
    	Date date = null;
    	if (!StringUtils.isNotBlank(dateString)){
    		date = new Date();
    	} else {
    		date = DateUtils.tryParse(dateString, DateUtils.DATE_FORMAT);
    	}
    	List<EmployeeBWDetails> empLeaveList = famstackDashboardManager.getEmployeesOnLeaveToday(groupId, date);
        return new ModelAndView("response/dashboardEmpLeave").addObject("usersList", empLeaveList);
    }
    
    @RequestMapping("/dashboardProjectDetails")
    public ModelAndView dashboardProjectDetails(@RequestParam("groupId") String groupId,@RequestParam("filters") String filters,
    		@RequestParam("dateRange") String dateRange, Model model)
    {
    	 FamstackDateRange famstackDateRange = DateUtils.parseDateRangeString(dateRange);
         Date startDate = famstackDateRange.getStartDate();
         Date endDate = famstackDateRange.getEndDate();
         
         List<DashBoardProjectDetails> dashBoardProjectDetailsList =
                 famstackDashboardManager.getDashboardProjectData(startDate, endDate, groupId);
         return new ModelAndView("response/dashboardProjectDetails")
                 .addObject("projectData", dashBoardProjectDetailsList).addObject("dateRange", dateRange);
    }  

    private String getDefaultDateRange() {
    	Date startDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), 0);
    	return  DateUtils.format(startDate, DateUtils.DATE_FORMAT_DP) + " - "
    	                + DateUtils.format(startDate, DateUtils.DATE_FORMAT_DP);
		
	}
    
    @RequestMapping(value = "/getEmpUtlAjaxFullcalendar/{userGroupId}", method = RequestMethod.GET)
    @ResponseBody
    public String getEmpUtlAjaxFullcalendar(@RequestParam("start") String startDate, @RequestParam("end") String endDate,
    		@PathVariable(value = "userGroupId") String userGroupId)
    {
        return famstackDashboardManager.getEmpUtlAjaxFullcalendar(startDate, endDate, userGroupId);
    }
    
    @RequestMapping(value = "/getEmpBWAjaxFullcalendar/{userGroupId}", method = RequestMethod.GET)
    @ResponseBody
    public String getEmpBWAjaxFullcalendar(@RequestParam("start") String startDate, @RequestParam("end") String endDate,
    		@PathVariable(value = "userGroupId") String userGroupId)
    {
        return famstackDashboardManager.getEmpBWAjaxFullcalendar(startDate, endDate, userGroupId);
    }
    
    @RequestMapping(value = "/dashboardOverAllUtilizationPercentage", method = RequestMethod.GET)
    @ResponseBody
    public String dashboardOverAllUtilizationPercentage(@RequestParam("userGroupId") String userGroupId, @RequestParam("dateRange") String dateRange,
    		@RequestParam("accountId") String accountId,@RequestParam("teamId") String teamId,@RequestParam("subTeamId") String subTeamId,@RequestParam("userId") String userId)
    {
    	 FamstackDateRange famstackDateRange = DateUtils.parseDateRangeString(dateRange);
         Date startDate = famstackDateRange.getStartDate();
         Date endDate = famstackDateRange.getEndDate();
         
        return famstackDashboardManager.dashboardOverAllUtilizationPercentage(startDate, endDate, userGroupId, accountId, teamId, subTeamId,userId);
    }
    
    @RequestMapping(value = "/dashboardOverAllUtilization", method = RequestMethod.GET)
    @ResponseBody
    public String dashboardOverAllUtilization(@RequestParam("userGroupId") String userGroupId, @RequestParam("dateRange") String dateRange,
    		@RequestParam("accountId") String accountId,@RequestParam("teamId") String teamId,@RequestParam("subTeamId") String subTeamId,@RequestParam("userId") String userId)
    {
    	 FamstackDateRange famstackDateRange = DateUtils.parseDateRangeString(dateRange);
         Date startDate = famstackDateRange.getStartDate();
         Date endDate = famstackDateRange.getEndDate();
         
        return famstackDashboardManager.dashboardOverAllUtilization(startDate, endDate, userGroupId, accountId, teamId, subTeamId,userId);
    }

    @RequestMapping(value = "/dashboardAccountUtilizationChart", method = RequestMethod.GET)
    public ModelAndView dashboardAccountUtilizationChart(@RequestParam("userGroupId") String userGroupId, @RequestParam("dateRange") String dateRange,
    		@RequestParam("accountId") String accountId,@RequestParam("teamId") String teamId,@RequestParam("subTeamId") String subTeamId,@RequestParam("userId") String userId)
    {
    	FamstackDateRange famstackDateRange = DateUtils.parseDateRangeString(dateRange);
        Date startDate = famstackDateRange.getStartDate();
        Date endDate = famstackDateRange.getEndDate();
         
        List<DashboardUtilizationDetails> dashboardOverAllutilizationList = famstackDashboardManager.dashboarAllUtilizationList(startDate, endDate, userGroupId, accountId, teamId, subTeamId,userId, false,false);
        Double totalDashBoardTime = famstackDashboardManager.getTotalDashboardFilterdutilizationList(dashboardOverAllutilizationList);
        for (DashboardUtilizationDetails dashboardUtilizationDetails :dashboardOverAllutilizationList)
        {
        	dashboardUtilizationDetails.setGrandTotal(totalDashBoardTime);
        }
        return new ModelAndView("response/dashboardAccountUtilizationChart").addObject("dashboadAccountUtilization", dashboardOverAllutilizationList);
    }

    @RequestMapping(value = "/dashboardTotalUtilizationCompare", method = RequestMethod.GET)
    public ModelAndView dashboardTotalUtilizationCompare(@RequestParam("userGroupId") String userGroupId, @RequestParam("dateRange") String dateRange,
    		@RequestParam("accountIds") String accountIds,@RequestParam("teamIds") String teamIds,@RequestParam("subTeamIds") String subTeamIds,@RequestParam("userIds") String userIds)
    {
    	 FamstackDateRange famstackDateRange = DateUtils.parseDateRangeString(dateRange);
         Date startDate = famstackDateRange.getStartDate();
         Date endDate = famstackDateRange.getEndDate();
         
        List<DashboardUtilizationDetails> dashboardOverAllutilizationList = famstackDashboardManager.dashboardTotalUtilizationChart(startDate, endDate, userGroupId, accountIds, teamIds, subTeamIds,"", "month");
        Double totalDashBoardTime = famstackDashboardManager.getTotalDashboardFilterdutilizationList(dashboardOverAllutilizationList);
        for (DashboardUtilizationDetails dashboardUtilizationDetails :dashboardOverAllutilizationList)
        {
        	dashboardUtilizationDetails.setGrandTotal(totalDashBoardTime);
        }
        return new ModelAndView("response/dashboardTotalUtilizationCompare").addObject("dashboardOverAllutilization", dashboardOverAllutilizationList);
    }

    
    @RequestMapping(value = "/dashboardChartTeamsCompare/{type}", method = RequestMethod.GET)
    @ResponseBody
    public String dashboardChartTeamsCompare(@RequestParam("userGroupId") String userGroupId, @RequestParam("dateRange") String dateRange,
    		@RequestParam("accountIds") String accountIds,@RequestParam("teamIds") String teamIds,@RequestParam("subTeamIds") String subTeamIds,@RequestParam("userIds") String userIds, @PathVariable("type") String type)
    {
    	return dashboardChartResourcesCompare(userGroupId, dateRange, accountIds, teamIds, subTeamIds, "", type);
    }
    
    @RequestMapping(value = "/dashboardChartResourcesCompare/{type}", method = RequestMethod.GET)
    @ResponseBody
    public String dashboardChartResourcesCompare(@RequestParam("userGroupId") String userGroupId, @RequestParam("dateRange") String dateRange,
    		@RequestParam("accountIds") String accountIds,@RequestParam("teamIds") String teamIds,@RequestParam("subTeamIds") String subTeamIds,@RequestParam("userIds") String userIds, @PathVariable("type") String type)
    {
    	 FamstackDateRange famstackDateRange = DateUtils.parseDateRangeString(dateRange);
         Date startDate = famstackDateRange.getStartDate();
         Date endDate = famstackDateRange.getEndDate();
         
         Map<String,  Map<String, DashboardUtilizationDetails>> dashboardOverAllutilizationMap = famstackDashboardManager.dashboarAllUtilizationListCompare(startDate, endDate, userGroupId, accountIds, teamIds, subTeamIds,userIds,type);
        
         if (dashboardOverAllutilizationMap != null) {
        	 for (String key : dashboardOverAllutilizationMap.keySet()) {
        		 Map<String, DashboardUtilizationDetails> dbudMap = dashboardOverAllutilizationMap.get(key);
;		         Double totalDashBoardTime = famstackDashboardManager.getTotalDashboardFilterdutilizationList(new ArrayList<DashboardUtilizationDetails>(dbudMap.values()));
		         for (DashboardUtilizationDetails dashboardUtilizationDetails :dbudMap.values())
		         {
		         	dashboardUtilizationDetails.setGrandTotal(totalDashBoardTime);
		         }
        	 }
        	 return famstackDashboardManager.covertdashboardOverAllutilizationMapJson(dashboardOverAllutilizationMap, type);
         }
    
         
         return "{\"lineColors\":[],\"data\":[],\"ykeys\":[],\"labels\":[]}";
    }


	@RequestMapping(value = "/dashboardTotalUtilizationChart/{viewType}", method = RequestMethod.GET)
    public ModelAndView dashboardTotalUtilizationChart(@RequestParam("userGroupId") String userGroupId, @RequestParam("dateRange") String dateRange,
    		@RequestParam("accountId") String accountId,@RequestParam("teamId") String teamId,@RequestParam("subTeamId") String subTeamId,@RequestParam("userId") String userId, @PathVariable("viewType") String viewType)
    {
		 FamstackDateRange famstackDateRange = DateUtils.parseDateRangeString(dateRange);
	     Date startDate = famstackDateRange.getStartDate();
	     Date endDate = famstackDateRange.getEndDate();
	        
        List<DashboardUtilizationDetails> dashboardTotalUtilizationList = famstackDashboardManager.dashboardTotalUtilizationChart(startDate, endDate, userGroupId, accountId, teamId, subTeamId,userId,viewType);
        Double totalDashBoardTime = famstackDashboardManager.getTotalDashboardFilterdutilizationList(dashboardTotalUtilizationList);
        for (DashboardUtilizationDetails dashboardUtilizationDetails :dashboardTotalUtilizationList)
        {
        	dashboardUtilizationDetails.setGrandTotal(totalDashBoardTime);
        }
        return new ModelAndView("response/dashboardTotalUtilizationChart").addObject("dashboadTotalUtilization", dashboardTotalUtilizationList);
    }
    
    
    @RequestMapping(value = "/dashboardResourceUtilizationChart", method = RequestMethod.GET)
    public ModelAndView dashboardResourceUtilizationChart(@RequestParam("userGroupId") String userGroupId, @RequestParam("dateRange") String dateRange,
    		@RequestParam("accountId") String accountId,@RequestParam("teamId") String teamId,@RequestParam("subTeamId") String subTeamId,@RequestParam("userId") String userId)
    {
    	 FamstackDateRange famstackDateRange = DateUtils.parseDateRangeString(dateRange);
	     Date startDate = famstackDateRange.getStartDate();
	     Date endDate = famstackDateRange.getEndDate();
         
        List<DashboardUtilizationDetails> dashboardOverAllutilizationList = famstackDashboardManager.dashboarResourceUtilizationList(startDate, endDate, userGroupId, accountId, teamId, subTeamId,userId);
        Double totalDashBoardTime = famstackDashboardManager.getTotalDashboardFilterdutilizationList(dashboardOverAllutilizationList);
        Integer workingHours = DateUtils.getWorkingDaysBetweenTwoDates(startDate, endDate);
        Double workingHoursInMins = (double) (workingHours * 8 * 60);
        for (DashboardUtilizationDetails dashboardUtilizationDetails :dashboardOverAllutilizationList) {
        	dashboardUtilizationDetails.setGrandTotal(totalDashBoardTime);
        	dashboardUtilizationDetails.setTotalWorkingHoursInMis(workingHoursInMins);
        }
        return new ModelAndView("response/dashboardResourceUtilizationChart").addObject("dashboadResourceUtilization", dashboardOverAllutilizationList);
    }

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView index()
    {
		if (getFamstackApplicationConfiguration() != null && getFamstackApplicationConfiguration().getCurrentUser() != null) {
			UserRole userRole = getFamstackApplicationConfiguration().getCurrentUser().getUserRole();
	        if (userRole != null && userRole != UserRole.SUPERADMIN && userRole != UserRole.ADMIN && userRole != UserRole.TEAMLEAD) {
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
		return  new ModelAndView("login");
    }

    @RequestMapping(value = "/userPingCheck", method = RequestMethod.POST)
    @ResponseBody
    public String userPingCheck(@RequestParam(name="groupId", defaultValue="") String groupId)
    {
        getFamstackApplicationConfiguration().updateLastPing();
        return famstackDashboardManager.getUserStatusJson(groupId);
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
    	getFamstackApplicationConfiguration().forceInitialize();
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

    @RequestMapping(value = "/initialize/{itemName}", method = RequestMethod.GET)
    @ResponseBody
    public String initalizeConfiguration(@PathVariable("itemName") String itemName)
    {

        logInfo("Initializing : " + itemName);
        if (itemName != null && "user".equalsIgnoreCase(itemName)) {
            getFamstackApplicationConfiguration().forceInitializeUserMap(
                getFamstackApplicationConfiguration().getFamstackUserProfileManager().getAllEmployeeDataList());
            logInfo("intizlized : " + itemName);
        } else if (itemName != null && "application".equalsIgnoreCase(itemName)) {
            getFamstackApplicationConfiguration().forceInitializeAppConfigMap(
                famstackApplicationConfManager.getAppConfigList());
            logInfo("intizlized : " + itemName);
        } else if (itemName != null && "group".equalsIgnoreCase(itemName)) {
            getFamstackApplicationConfiguration().forceInitializeUserGroup(
                famstackApplicationConfManager.getUserGroupList());
            logInfo("intizlized : " + itemName);
        } else if (itemName != null && "account".equalsIgnoreCase(itemName)) {
        	famstackDashboardManager.initializeAccounts();
            logInfo("intizlized : " + itemName);
        }else if (itemName != null && "full".equalsIgnoreCase(itemName)) {
            getFamstackApplicationConfiguration().forceInitialize();
            famstackDashboardManager.initializeAccounts();
            logInfo("intizlized : " + itemName);
        }

        return "{\"status\": true}";
    }

    @RequestMapping("/appConfigProjectCategories")
    public ModelAndView appConfigProjectCategories(Model model)
    {
        return new ModelAndView("response/appConfigProjectCategories");
    }
    
    @RequestMapping("/appConfigNewProjectCategories")
    public ModelAndView appConfigNewProjectCategories(Model model)
    {
        return new ModelAndView("response/appConfigNewProjectCategories");
    }


    @RequestMapping("/appConfigNonBillableCategories")
    public ModelAndView appConfigNonBillableCategories(Model model)
    {
        return new ModelAndView("response/appConfigNonBillableCategories");
    }

    @RequestMapping(value = "/triggerEmail", method = RequestMethod.GET)
    @ResponseBody
    public String sendMail(@RequestParam(name="subject", defaultValue="Test Subject") String subject, @RequestParam(name="body", defaultValue="TestBody") String body)
    {
        famstackDashboardManager.sendMail(subject, body);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/trackUserActivity", method = RequestMethod.GET)
    @ResponseBody
    public String trackUserSiteActivity(@RequestParam("userId") Integer userId, @RequestParam(name="activityDateString", defaultValue="") String activityDateString, @RequestParam(name="status", defaultValue="true") boolean status)
    {
    	Date activityDate = new Date();
    	if (StringUtils.isNotBlank(activityDateString)) {
    		activityDate = DateUtils.tryParse(activityDateString, DateUtils.DATE_FORMAT_CALENDER);
    		activityDate = DateUtils.getNextPreviousDate(DateTimePeriod.HOUR, activityDate, 8);
    	}
        famstackDashboardManager.trackUserSiteActivity(userId, activityDate, status);
        return "{\"status\": true}";
    }
    
    @RequestMapping(value = "/getUserSiteActivityCalendarJson", method = RequestMethod.GET)
    @ResponseBody
    public String getUserSiteActivityCalendarJson(@RequestParam("start") String startDateString, @RequestParam("end") String endDateString,
        @RequestParam(value = "userId", defaultValue = "-1") int userId)
    {
    	if (userId != -1) {
    	  Date startDate = DateUtils.tryParse(startDateString, DateUtils.DATE_FORMAT_CALENDER);
          Date endDate = DateUtils.tryParse(endDateString, DateUtils.DATE_FORMAT_CALENDER);
          return famstackDashboardManager.getUserSiteActivityJson(startDate, endDate, userId);
    	}
        return "{}";
    }
    
    @RequestMapping(value = "/createAutoReportingConfig", method = RequestMethod.POST)
    @ResponseBody
    public String createAutoReportingConfig(@RequestParam("name") String name, 
    		@RequestParam("type") String type,
    		@RequestParam(name="configId", defaultValue="0") Integer configId,
    		@RequestParam("endDate") String endDateString,
    		@RequestParam(name="subject", defaultValue="") String subject,
    		@RequestParam("cron") String cron, 
    		@RequestParam(name="startDate", defaultValue="0") Integer startDate, 
    		@RequestParam(name="previousDate", defaultValue="0") Integer previousDate)
    {
    	Date endDate = null;
    	if (StringUtils.isNotBlank(endDateString)) {
    		endDate = DateUtils.tryParse(endDateString, DateUtils.DATE_FORMAT);
    	}
    	return famstackDashboardManager.createAutoReportingConfig(name, type, subject, cron, startDate, previousDate, endDate, configId);
    }
   
    @RequestMapping("/refreshAutoReportingConfig")
    public ModelAndView refreshAutoReportingConfig(Model model)
    {
    	List<AutoReportingItem> autoReportingList = famstackDashboardManager.refreshAutoReportingConfig();
    	return new ModelAndView("response/appConfigAutoReporting").addObject("autoReportingList", autoReportingList);
    }
    
    
    @RequestMapping(value = "/deleteAutoReportingConfig", method = RequestMethod.POST)
    @ResponseBody
    public String deleteAutoReportingConfig(@RequestParam("id") Integer id) 
    {
    	famstackDashboardManager.deleteAutoReportingConfig(id);
    	return "{\"status\": true}";
    }
    
    
    @RequestMapping(value = "/addEmailAutoReportingConfig", method = RequestMethod.POST)
    @ResponseBody
    public String addEmailAutoReportingConfig(@RequestParam("email") String email, @RequestParam("id") Integer id,
    		@RequestParam("type") String type) {
    	return famstackDashboardManager.addEmailAutoReportingConfig(email, type, id);
    }
    
    @RequestMapping(value = "/removeEmailAutoReportingConfig", method = RequestMethod.POST)
    @ResponseBody
    public String removeEmailAutoReportingConfig(@RequestParam("email") String email, @RequestParam("id") Integer id,
    		@RequestParam("type") String type) {
    	return famstackDashboardManager.removeEmailAutoReportingConfig(email, type, id);
    }
    @RequestMapping(value = "/enableOrDisableAutoReportingConfig", method = RequestMethod.POST)
    @ResponseBody
    public String enableOrDisableAutoReportingConfig(@RequestParam("id") Integer id,
    		@RequestParam("enable") Boolean enable) {
    	famstackDashboardManager.enableOrDisableAutoReportingConfig(id, enable);
    	return "{\"status\": true}";
    }
}

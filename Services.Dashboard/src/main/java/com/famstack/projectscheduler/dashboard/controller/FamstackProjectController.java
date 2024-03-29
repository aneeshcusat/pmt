package com.famstack.projectscheduler.dashboard.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.famstack.email.FamstackEmailSender;
import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.ReportType;
import com.famstack.projectscheduler.dashboard.bean.POEstimateResponse;
import com.famstack.projectscheduler.dashboard.bean.ProjectDetailsBySkillsResponse;
import com.famstack.projectscheduler.dashboard.bean.ProjectDetailsResponse;
import com.famstack.projectscheduler.dashboard.bean.ProjectTaskActivityDetails;
import com.famstack.projectscheduler.dashboard.bean.SearchRequest;
import com.famstack.projectscheduler.dashboard.bean.TeamResponse;
import com.famstack.projectscheduler.dashboard.manager.FamstackDashboardManager;
import com.famstack.projectscheduler.datatransferobject.UserTaskActivityItem;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.ApplicationDetails;
import com.famstack.projectscheduler.employees.bean.FamstackDateRange;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.employees.bean.UserWorkDetails;
import com.famstack.projectscheduler.manager.FamstackStaticXLSImportManager;
import com.famstack.projectscheduler.manager.FamstackXLSExportManager;
import com.famstack.projectscheduler.manager.MIMECheck;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.StringUtils;
import com.famstack.projectscheduler.utils.FamstackUtils;

@Controller
@SessionAttributes
public class FamstackProjectController extends BaseFamstackService
{

    @Resource
    FamstackDashboardManager famstackDashboardManager;

    @Resource
    FamstackXLSExportManager famstackXLSExportManager;
    
    @Resource
    FamstackStaticXLSImportManager famstackStaticXLSImportManager;
    
    @Resource
    FamstackEmailSender famstackEmailSender;
    
    private static final List<String> contentTypes = Arrays.asList("image/png","application/pdf", "image/jpeg", "image/gif", "text/plain", "application/msword", "application/vnd.ms-excel");

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public ModelAndView listProjects()
    {
        List<ProjectDetails> projectData = famstackDashboardManager.getProjects(false);
        return getProjectPageModelView(projectData);
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView projectDashboard()
    {
        /*
         * List<ProjectDetails> projectData = famstackDashboardManager.getLatestProjects(false); ModelAndView
         * modelAndView = getProjectPageModelView(projectData); modelAndView.setViewName("projectdashboard"); return
         * modelAndView;
         */
        String dateRange = getDefaultDateRange();
        ModelAndView modelAndView = getProjectPageModelView(null);
        modelAndView.setViewName("projectdashboard");
        return modelAndView.addObject("dateRange", dateRange).addObject("dateRangeLabel",
            getFamstackApplicationConfiguration().getDefaultDate());
    }

    private String getDefaultDateRange()
    {
        String dateRange = getFamstackApplicationConfiguration().getDefaultDate();
        Date startDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), -6);
        Date endDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), 0);
        if (StringUtils.isNotBlank(dateRange)) {
            switch (dateRange) {
                case "Today":
                    startDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), 0);
                    break;
                case "Yesterday":
                    startDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), -1);
                    endDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), -1);
                    break;
                case "Last 7 Days":
                    startDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), -6);
                    break;
                case "This Week":
                    startDate = DateUtils.getFirstDayOfThisWeek();
                    endDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, startDate, 6);
                    break;
                case "Last Week":
                    Date firstDayOfThisWeek = DateUtils.getFirstDayOfThisWeek();
                    startDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, firstDayOfThisWeek, -7);
                    endDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, startDate, 6);
                    break;
                case "Last 30 Days":
                    startDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), -29);
                    break;
                case "This Month":
                    startDate = DateUtils.getFirstDayOfThisMonth();
                    int numberOfDaysInThisMOnth = DateUtils.getNumberOfDaysInThisMonth();
                    endDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, startDate, numberOfDaysInThisMOnth - 1);
                    break;
                case "Last Month":
                    Date firstDayOfMonth = DateUtils.getFirstDayOfThisMonth();
                    int numberOfDaysInLastMOnth = DateUtils.getNumberOfDaysInThisMonth(firstDayOfMonth);
                    endDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, firstDayOfMonth, -1);
                    startDate =
                        DateUtils.getNextPreviousDate(DateTimePeriod.DAY, endDate, -1 * numberOfDaysInLastMOnth);

                    break;
                case "Last 3 Month":
                	startDate = DateUtils.getNextPreviousDate(DateTimePeriod.MONTH, new Date(), -3);
                    break;
                case "Last 6 Month":
                	startDate = DateUtils.getNextPreviousDate(DateTimePeriod.MONTH, new Date(), -6);
                    break;
                default:
                    break;
            }
        }

        dateRange =
            DateUtils.format(startDate, DateUtils.DATE_FORMAT_DP) + " - "
                + DateUtils.format(endDate, DateUtils.DATE_FORMAT_DP);
        return dateRange;
    }

    @RequestMapping("/projectdashboardList")
    public ModelAndView projectdashboardList(@RequestParam(value = "daterange", defaultValue = "") String dateRange,
        @RequestParam(value = "includeArchive", defaultValue = "false") Boolean includeArchive, Model model)
    {
        logDebug(dateRange);
        logDebug("includeArchive : " + includeArchive);
        FamstackDateRange famstackDateRange = DateUtils.parseDateRangeString(dateRange);
        Date startDate = famstackDateRange.getStartDate();
        Date endDate = famstackDateRange.getEndDate();

        List<ProjectDetails> projectData =
            famstackDashboardManager.getLatestProjects(startDate, endDate, includeArchive);

        ModelAndView modelAndView = getProjectPageModelView(projectData);
        modelAndView.setViewName("response/projectdashboardList");
        return modelAndView.addObject("dateRange", dateRange);
    }

    @RequestMapping("/searchProjectDetails")
    public ModelAndView searchProjectDetails(
        @RequestParam(value = "searchString", defaultValue = "") String searchString,
        @RequestParam(value = "includeArchive", defaultValue = "false") Boolean includeArchive, Model model)
    {
        logDebug("searchString : " + searchString);
        logDebug("includeArchive : " + includeArchive);

        List<ProjectDetails> projectData = famstackDashboardManager.searchProjectDetails(searchString, includeArchive);

        ModelAndView modelAndView = getProjectPageModelView(projectData);
        modelAndView.setViewName("response/projectdashboardList");
        return modelAndView;
    }
    
    @RequestMapping("/searchProjectByOrderBookRefOrProposal")
    public ModelAndView searchProjectByOrderBookRefOrProposal(
        @RequestParam(value = "proposalNumber", defaultValue = "") String proposalNumber,
        @RequestParam(value = "orderBookRefNo", defaultValue = "") String orderBookRefNo,
        @RequestParam(value = "includeArchive", defaultValue = "false") Boolean includeArchive, Model model)
    {
        
        List<ProjectDetails> projectData = famstackDashboardManager.searchProjectByOrderBookRefOrProposal(orderBookRefNo, proposalNumber, includeArchive);

        ModelAndView modelAndView = getProjectPageModelView(projectData);
        modelAndView.setViewName("response/projectdashboardList");
        return modelAndView;
    }

    @RequestMapping(value = "/mileStones", method = RequestMethod.GET)
    public ModelAndView getMilestones()
    {
        Map<String, Object> modelViewMap = new HashMap<String, Object>();
        List<ProjectDetails> projectData = famstackDashboardManager.getProjects(false);
        modelViewMap.put("projectDetailsData", projectData);
        return new ModelAndView("mileStones", "command", new ProjectDetails()).addObject("modelViewMap", modelViewMap);
    }

    @RequestMapping(value = "/projects/{projectStatus}", method = RequestMethod.GET)
    public ModelAndView listProjectsByStatus(@PathVariable("projectStatus") ProjectStatus projectStatus)
    {
        List<ProjectDetails> projectData = famstackDashboardManager.getProjectDetails(projectStatus);

        return getProjectPageModelView(projectData);
    }

    private ModelAndView getProjectPageModelView(List<ProjectDetails> projectData)
    {
        List<AccountDetails> accountData = famstackDashboardManager.getAccountDataList();

        Map<String, Object> modelViewMap = new HashMap<String, Object>();
        modelViewMap.put("projectDetailsData", projectData);
        modelViewMap.put("accountData", accountData);
        return new ModelAndView("projects", "command", new ProjectDetails()).addObject("modelViewMap", modelViewMap);
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public ModelAndView getAccounts()
    {
        List<AccountDetails> accountData = famstackDashboardManager.getAccountDataList();
        return new ModelAndView("accounts").addObject("accountData", accountData);
    }

    @RequestMapping(value = "/applicationConfig", method = RequestMethod.GET)
    public ModelAndView getApplicationConfig()
    {
        List<ApplicationDetails> accountData = famstackDashboardManager.getApplicationDetails();
        return new ModelAndView("applicationConfig").addObject("accountData", accountData);
    }

    @RequestMapping(value = "/projectreporting", method = RequestMethod.GET)
    public ModelAndView projectreporting(@RequestParam(value = "daterange", defaultValue = "") String dateRange)
    {
        if (!StringUtils.isNotBlank(dateRange)) {
            dateRange = getDefaultDateRange();
        }
        return new ModelAndView("projectreporting").addObject("dateRange", dateRange).addObject("dateRangeLabel",
            getFamstackApplicationConfiguration().getDefaultDate());
    }

    @RequestMapping(value = "/projectreportingResponse", method = RequestMethod.GET)
    public ModelAndView projectreportingVS(@RequestParam(value = "daterange", defaultValue = "") String dateRange,
        @RequestParam(value = "format", defaultValue = "default") String format, Model model)
    {
        logDebug(dateRange);
        FamstackDateRange famstackDateRange = DateUtils.parseDateRangeString(dateRange);
        Date startDate = famstackDateRange.getStartDate();
        Date endDate = famstackDateRange.getEndDate();

        if ("default".equalsIgnoreCase(format) || "format3".equalsIgnoreCase(format)) {
            List<ProjectTaskActivityDetails> projectTaskAssigneeDataList = new ArrayList<>();
        	projectTaskAssigneeDataList.addAll( famstackDashboardManager.getAllProjectTaskAssigneeData(startDate, endDate));
            projectTaskAssigneeDataList.addAll(famstackDashboardManager.getAllNonBillableTaskActivities(startDate, endDate, false, null));
            return new ModelAndView("response/reporting" + format)
                .addObject("projectData", projectTaskAssigneeDataList).addObject("dateRange", dateRange);
        } else {
            List<ProjectDetails> projectData = famstackDashboardManager.getAllProjectDetailsList(startDate, endDate);
            return new ModelAndView("response/reporting" + format).addObject("projectData", projectData).addObject(
                "dateRange", dateRange);
        }
    }
    @RequestMapping(value = "/projectTimeLineJson", method = RequestMethod.GET)
    @ResponseBody
    public String projectTimeLine(@RequestParam(value = "daterange", defaultValue = "") String dateRange)
    {
        logDebug(dateRange);
        FamstackDateRange famstackDateRange = DateUtils.parseDateRangeString(dateRange);
        Date startDate = famstackDateRange.getStartDate();
        Date endDate = famstackDateRange.getEndDate();
    
        List<ProjectDetails> projectData = famstackDashboardManager.getAllProjectDetailsList(startDate, endDate);
        if (projectData != null) {
        	return famstackDashboardManager.mapProjectDataToTimeline(projectData);
        }
		return "{\"data\": []}";
    }
    @RequestMapping(value = "/export/{templateName}")
    public void downloadReportingFile(@RequestParam(value = "daterange", defaultValue = "") String dateRange,
        @PathVariable(value = "templateName") String templateName, HttpServletRequest request,
        HttpServletResponse response)
    {

        logDebug(dateRange);
        FamstackDateRange famstackDateRange = DateUtils.parseDateRangeString(dateRange);
        Date startDate = famstackDateRange.getStartDate();
        Date endDate = famstackDateRange.getEndDate();
        
        Map<String, Map<Integer, UserWorkDetails>> employeeUtilizationData = null;
        Map<String, Map<Integer, Integer>> nonBillableTaskActivities = null;
        
        Map<String, Object> dataMap = new HashMap<>();
        List<ProjectTaskActivityDetails> projectTaskAssigneeDataList = new ArrayList<>();
        if ("default".equalsIgnoreCase(templateName)&& getFamstackApplicationConfiguration().isStaticReportEnabled()) {
        	Date initialStartDate = new Date(startDate.getTime());
        	Date initialEndDate= new Date(endDate.getTime());
    		Map<String, Object> staticReportData = famstackStaticXLSImportManager.getAllStaticProjectTaskAssigneeData(new FamstackDateRange(startDate, endDate), request, response);
    		List<ProjectTaskActivityDetails> staticProjectTaskAssigneeDataList = (List<ProjectTaskActivityDetails>) staticReportData.get("PROJECTDATALIST");
    		
    		Collections.sort(staticProjectTaskAssigneeDataList, new Comparator<ProjectTaskActivityDetails>()
	        {
	            @Override
	            public int compare(ProjectTaskActivityDetails projectDetails2, ProjectTaskActivityDetails projectDetails1)
	            {
	                Date date2 =projectDetails1.getTaskActivityStartTime();
	                Date date1 =projectDetails2.getTaskActivityStartTime();

	                if (date1.before(date2)) {
	                    return -1;
	                } else if (date1.after(date2)) {
	                    return 1;
	                }
	                return 0;
	            }
	        });
    		 System.out.println("staticProjectTaskAssigneeDataList size" + staticProjectTaskAssigneeDataList.size());
    		List<FamstackDateRange> dateRangeList = (List<FamstackDateRange>) staticReportData.get("NEWDATERANGELIST");
    		String insertLocation =  (String) staticReportData.get("INSERTLOCATION");
    		
    		List<ProjectTaskActivityDetails> projectTaskAssigneeDataList1 = new ArrayList<>();
    		List<ProjectTaskActivityDetails> projectTaskAssigneeDataList2 = new ArrayList<>();
    		
    		if (dateRangeList.size() > 0) {
    			startDate =  dateRangeList.get(0).getStartDate();
    			endDate =  dateRangeList.get(0).getEndDate();
    			projectTaskAssigneeDataList1 =  famstackDashboardManager.getBillableAndNonBillaleSortedListByStartDate(startDate, endDate,false, null);
    		} 
    		if (dateRangeList.size() > 1) {
    			startDate =  dateRangeList.get(1).getStartDate();
    			endDate =  dateRangeList.get(1).getEndDate();
    			projectTaskAssigneeDataList2 =  famstackDashboardManager.getBillableAndNonBillaleSortedListByStartDate(startDate, endDate,false, null);
    		}
    		
    		if ("F".equalsIgnoreCase(insertLocation)) {
    			projectTaskAssigneeDataList.addAll(staticProjectTaskAssigneeDataList);
    			projectTaskAssigneeDataList.addAll(projectTaskAssigneeDataList1);
    		} else if ("M".equalsIgnoreCase(insertLocation)) {
    			projectTaskAssigneeDataList.addAll(projectTaskAssigneeDataList1);
    			projectTaskAssigneeDataList.addAll(staticProjectTaskAssigneeDataList);
    			projectTaskAssigneeDataList.addAll(projectTaskAssigneeDataList2);
    		} else if ("E".equalsIgnoreCase(insertLocation)) {
    			projectTaskAssigneeDataList.addAll(projectTaskAssigneeDataList1);
    			projectTaskAssigneeDataList.addAll(staticProjectTaskAssigneeDataList);
    		}
    		
    		nonBillableTaskActivities = famstackDashboardManager.getAllNonBillableTaskActivityList(initialStartDate, initialEndDate);
    		dataMap.put("exportDataList", projectTaskAssigneeDataList);
            dataMap.put("nonBillableTaskActivities", nonBillableTaskActivities);
        } else if ("default".equalsIgnoreCase(templateName)) {
           projectTaskAssigneeDataList.addAll(famstackDashboardManager.getBillableAndNonBillaleSortedListByStartDate(startDate, endDate,false, null));
           nonBillableTaskActivities = famstackDashboardManager.getAllNonBillableTaskActivityList(startDate, endDate);
           dataMap.put("exportDataList", projectTaskAssigneeDataList);
           dataMap.put("nonBillableTaskActivities", nonBillableTaskActivities);
        } else if ("format3".equalsIgnoreCase(templateName)) {
            projectTaskAssigneeDataList = famstackDashboardManager.getBillableAndNonBillaleSortedListByAssignee(startDate, endDate,true, null);
            
            dataMap.put("exportDataList", projectTaskAssigneeDataList);
            dataMap.put("nonBillableTaskActivities", nonBillableTaskActivities);
        } else if ("useractivity".equalsIgnoreCase(templateName)) {
            Map<Integer, Map<String, String>> userSiteActivityMap =
                famstackDashboardManager.getAllUserSiteActivities(startDate, endDate);
            Map<Integer, Map<String, UserTaskActivityItem>> nonBillativityMap =
                famstackDashboardManager.getAllNonBillabileActivities(startDate, endDate);
            dataMap.put("nonBillativityMap", nonBillativityMap);
            dataMap.put("exportDataList", userSiteActivityMap);
        } else {
            List<ProjectDetails> projectData = famstackDashboardManager.getAllProjectDetailsList(startDate, endDate);
            employeeUtilizationData = famstackDashboardManager.getAllEmployeeUtilizationList(startDate, endDate);
            dataMap.put("exportDataList", projectData);
            dataMap.put("employeeUtilizationData", employeeUtilizationData);
            dataMap.put("nonBillableTaskActivities", nonBillableTaskActivities);
        }

        dataMap.put("dateString", dateRange);

        famstackXLSExportManager.exportXLS(templateName, dataMap, request, response);
    }

    @RequestMapping(value = "/getProjectJson", method = RequestMethod.GET)
    @ResponseBody
    public String getProjectNameJson(@RequestParam("query") String query)
    {
        return famstackDashboardManager.getProjectNameJson(query);
    }
    
    @RequestMapping(value = "/getProjectByOrderRefNoJson", method = RequestMethod.GET)
    @ResponseBody
    public String getProjectByOrderRefNoJson(@RequestParam("query") String orderRefNo)
    {
        return famstackDashboardManager.getProjectByOrderRefNoJson(orderRefNo);
    }
    
    
    @RequestMapping(value = "/getProjectNamesCodePoIdJson", method = RequestMethod.GET)
    @ResponseBody
    public String getProjectNamesCodePoIdJson(@RequestParam("query") String query)
    {
        return famstackDashboardManager.searchForProjectNamesCodePoIdJson(query);
    }
    
    @RequestMapping(value = "/getAllTasksJson", method = RequestMethod.GET)
    @ResponseBody
    public String getAllTasksJson(@RequestParam("projectId") Integer projectId, @RequestParam("howManyOldData") Integer howManyOldData)
    {
        return famstackDashboardManager.getAllTasksJson(projectId, howManyOldData);
    }

    @RequestMapping(value = "/createProject", method = RequestMethod.POST)
    @ResponseBody
    public String createProject(@ModelAttribute("projectDetails") ProjectDetails projectDetails, BindingResult result,
        Model model)
    {
    	int projectId = 0;
    	if (getFamstackApplicationConfiguration().validateCsrToken(projectDetails.getCsrToken())) {
    		 projectId = famstackDashboardManager.createProject(projectDetails);
    	} else {
    		 return "{\"status\": false,\"errorCode\": \"Unauthorized access\"}";
    	}
        return "{\"status\": true,\"projectId\": " + projectId + "}";
    }

    @RequestMapping(value = "/getAssigneesSlot", method = RequestMethod.GET)
    @ResponseBody
    public String getAssigneesSlot(@RequestParam(value = "assigneeId", defaultValue = "") int assigneeId,
        @RequestParam(value = "startDateTime", defaultValue = "") String startDateTime,
        @RequestParam(value = "endDateTime", defaultValue = "") String endDateTime)
    {
        Date date = null;
        if (StringUtils.isNotBlank("" + assigneeId) && StringUtils.isNotBlank(startDateTime)
            && StringUtils.isNotBlank(endDateTime)) {
            date = famstackDashboardManager.getAssigneeSlot(assigneeId, startDateTime, endDateTime);
        }
        return date == null ? "Not Available" : DateUtils.format(date, DateUtils.DATE_TIME_FORMAT);
    }

    @RequestMapping(value = "/loadDuplicateProjectsJSon", method = RequestMethod.GET)
    public ModelAndView loadDuplicateProjectsJSon(@RequestParam("projectId") int projectId,
        @RequestParam("projectCode") String projectCode,
        @RequestParam(value = "includeArchive", defaultValue = "false") Boolean includeArchive, Model model)
    {
        List<ProjectDetails> projectDetails =
            famstackDashboardManager.loadDuplicateProjectsJon(projectId, projectCode, includeArchive);
        return new ModelAndView("response/prjdashboadduplicate").addObject("projectDetailsData", projectDetails)
            .addObject("projectId", projectId);
    }

    @RequestMapping(value = "/loadTaskDetailsJSon", method = RequestMethod.GET)
    public ModelAndView loadTaskDetailsJSon(@RequestParam("projectId") int projectId, Model model)
    {

        List<TaskDetails> taskDetails = famstackDashboardManager.loadProjectTaskDetails(projectId);
        return new ModelAndView("response/projecttaskdetails").addObject("taskDetailsData", taskDetails).addObject(
            "projectId", projectId);
    }
    
    @RequestMapping(value = "/projectTaskCloneJson", method = RequestMethod.GET)
    public ModelAndView projectTaskCloneJson(@RequestParam("projectId") int projectId, Model model)
    {
    	ProjectDetails projectDetails = famstackDashboardManager.getProjectDetais(projectId);
        return new ModelAndView("response/quicktaskreassign").addObject("project", projectDetails);
    }

    @RequestMapping(value = "/quickDuplicateProject", method = RequestMethod.POST)
    @ResponseBody
    public String quickDuplicateProject(@RequestParam("projectId") int projectId,
        @RequestParam("projectName") String projectName, @RequestParam("projectDuration") Integer projectDuration,
        @RequestParam("projectStartTime") String projectStartTime,
        @RequestParam("projectEndTime") String projectEndTime, @RequestParam("taskDetails") String taskDetails)
    {

        famstackDashboardManager.quickDuplicateProject(projectId, projectName, projectDuration, projectStartTime,
            projectEndTime, taskDetails);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/weeklyTimeLog", method = RequestMethod.POST)
    @ResponseBody
    public String weeklyTimeLog(@RequestParam("projectDetails") String projectDetails, @RequestParam("weekStartDate") String weekStartDate)
    {
    	try{
	    	logInfo("Weekly time date : " +  weekStartDate);
	    	logInfo("Weekly Time data : " +  projectDetails);
	    	logInfo("Weekly Time group : " +  getFamstackApplicationConfiguration().getCurrentUserGroupId());
	    	logInfo("Weekly Time user id : " +  getFamstackApplicationConfiguration().getCurrentUserId());
	    	 famstackDashboardManager.weeklyTimeLog(projectDetails, weekStartDate);
	         return "{\"status\": true}";
    	} catch(Exception e) {
    		logError("Weekly Time logging failed : ", e);
    		famstackEmailSender.sendTextMessage("ALERT: ERROR - SERVER, weeklyTimeLog failed", "User id "
        			+ getFamstackApplicationConfiguration().getCurrentUserId() +", projectDetails " + projectDetails +", weekStartDate" + weekStartDate + ", Error " + e.getMessage());

    	}
    	 return "{\"status\": false}";
    }

    @RequestMapping(value = "/getWeeklyLogggedTime", method = RequestMethod.GET)
    @ResponseBody
    public String getWeeklyLogggedTime(@RequestParam("weekStartDate") String weekStartDate, @RequestParam("userId") Integer currentUserId)
    {
        return famstackDashboardManager.getWeeklyLogggedTime(weekStartDate, currentUserId);
    }
    
    @RequestMapping(value = "/getMonthlyLogggedTime", method = RequestMethod.GET)
    @ResponseBody
    public String getMonthlyLogggedTime(@RequestParam("monthFilter") String monthFilter, @RequestParam("userId") Integer currentUserId)
    {
    	if (currentUserId == 0) {
    		currentUserId = null;
    	}
        return famstackDashboardManager.getMonthlyLogggedTime(monthFilter, currentUserId);
    }
    
    
    @RequestMapping(value = "/updateProject", method = RequestMethod.POST)
    @ResponseBody
    public String updateProject(@ModelAttribute("projectDetails") ProjectDetails projectDetails, BindingResult result,
        Model model)
    {
    	if (getFamstackApplicationConfiguration().validateCsrToken(projectDetails.getCsrToken())) {
    		famstackDashboardManager.updateProject(projectDetails);
    	} else {
    		 return "{\"status\": false, \"error\": \"Unauthorized Access\"}";
    	}
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/deleteProjects", method = RequestMethod.POST)
    @ResponseBody
    public String deleteProjects(@RequestParam("projectIds[]") List<Integer> projectIds,
        @RequestParam("type") String type)
    {
        famstackDashboardManager.deleteProjects(projectIds, type);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/deleteProject", method = RequestMethod.POST)
    @ResponseBody
    public String deleteProject(@RequestParam("projectId") int projectId)
    {
        famstackDashboardManager.deleteProject(projectId);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/archiveProject", method = RequestMethod.POST)
    @ResponseBody
    public String archiveProject(@RequestParam("projectId") int projectId)
    {
        famstackDashboardManager.archiveProject(projectId);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/getProjectDetailsJson", method = RequestMethod.GET)
    @ResponseBody
    public String getProjectDetailsJson(@RequestParam("projectId") int projectId)
    {
        return famstackDashboardManager.getProjectDetailsJson(projectId);
    }

    @RequestMapping(value = "/project/{projectId}", method = RequestMethod.GET)
    public ModelAndView loadProject(@PathVariable("projectId") int projectId, HttpServletRequest request)
    {
        ProjectDetails projectDetails = famstackDashboardManager.getProjectDetails(projectId, request);
        AccountDetails accountDetails =   null;
        List<AccountDetails> accountDetailsList = famstackDashboardManager.getAccountDataList();
        if (projectDetails != null && accountDetailsList != null) {
         accountDetails =  famstackDashboardManager.getAccountDataList().stream().filter(account -> account.getAccountId() == projectDetails.getAccountId()).findAny().orElse(null);
        }
        return new ModelAndView("projectdetails", "command", new TaskDetails()).addObject("projectDetails",
            projectDetails).addObject("accountDetails", accountDetails);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/uploadfile/{projectCode}")
    @ResponseBody
    public String uploadProjectFile(@PathVariable(value = "projectCode") String projectCode,
        @RequestParam("file") MultipartFile file, HttpServletRequest request)
    {
    	
    	String fileContentType = file.getContentType();
    	System.out.println("fileContentType " + fileContentType);
    
        try {
        	int intArray[] = new int[10];
        	InputStream ins = file.getInputStream();
        	for (int i = 0; i < 8; i++) {
        		int intval = ins.read();
        		intArray[i] = intval;
            }
			if(MIMECheck.isAllowedFiles(intArray) && !MIMECheck.isHarmFul(intArray)) {
				System.out.println("Uploading file");
				famstackDashboardManager.uploadProjectFile(file, projectCode, request);
				 return "{\"status\": true}";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        return "{\"status\": false}";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/deletefile/{projectCode}")
    @ResponseBody
    public String deleteProjectFile(@PathVariable(value = "projectCode") String projectCode,
        @RequestParam(value = "fileName") String fileName, HttpServletRequest request)
    {
        famstackDashboardManager.deleteProjectFile(fileName, projectCode, request);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/download/{projectCode}/{fileName}")
    public void downloadFile(@RequestParam(value = "fileName") String fileName,
        @PathVariable(value = "projectCode") String projectCode, HttpServletRequest request,
        HttpServletResponse response)
    {
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

    // ---------- Project Comments ------------//

    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    @ResponseBody
    public String addComment(@RequestParam("projectComments") String projectComments,
        @RequestParam("projectId") int projectId)
    {
        famstackDashboardManager.createComment(projectComments, projectId);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/accountConfig", method = RequestMethod.POST)
    @ResponseBody
    public String accountConfig(@RequestParam("input1") String input1, @RequestParam("input2") String input2,
        @RequestParam("type") String type, @RequestParam("action") String action,
        @RequestParam("parentId") int parentId, @RequestParam("id") int id)
    {
        logDebug("input1 : " + input1 + " input2 : " + input2 + " type : " + type + " action : " + action
            + " parentId : " + parentId + " id : " + id);
        famstackDashboardManager.createAccountConfig(input1, input2, type, action, parentId, id);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/deleteAccountConfig", method = RequestMethod.POST)
    @ResponseBody
    public String deleteAccountConfig(@RequestParam("action") String action, @RequestParam("id") int id)
    {
        logDebug(" id : " + id + " action : " + action);
        famstackDashboardManager.delteAccountConfig(action, id);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/getRecurringProjectDetails", method = RequestMethod.GET)
    @ResponseBody
    public String getRecurringProjectDetails(@RequestParam("projectCode") String projectCode,
        @RequestParam("projectId") int projectId)
    {
        return famstackDashboardManager.getRecurringProjectDetails(projectCode, projectId);
    }

    @RequestMapping(value = "/createRecurringProject", method = RequestMethod.POST)
    @ResponseBody
    public String createRecurringProject(@RequestParam("projectCode") String projectCode,
        @RequestParam("projectId") int projectId, @RequestParam("cronExp") String cronExp,
        @RequestParam("recurringEndDate") String recurringEndDate, @RequestParam("recurreOriginal")  boolean recurreOriginal)
    {
        return famstackDashboardManager.createRecurringProject(projectCode, projectId, cronExp, recurringEndDate, recurreOriginal);
    }

    @RequestMapping(value = "/deleteRecuringProjectDetails", method = RequestMethod.POST)
    @ResponseBody
    public String deleteRecuringProjectDetails(@RequestParam("recurringId") int recurringId)
    {
        famstackDashboardManager.deleteRecuringProjectDetails(recurringId);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/getAllRecuringProjectCodes", method = RequestMethod.GET)
    @ResponseBody
    public String getAllRecuringProjectCodes()
    {
        return famstackDashboardManager.getAllRecuringProjectCodes();
    }
    

	@RequestMapping(value = "/sendAutoReportEmail", method = RequestMethod.GET)
    @ResponseBody
	public String sendAutoReportEmail(@RequestParam("reportId") Integer reportId, @RequestParam(name="howManyPreviousDays", defaultValue="8") Integer howManyPreviousDays)
    {
        famstackDashboardManager.sendAutoReportEmail(reportId, howManyPreviousDays);
    	        
    	return "{\"status\": true}";
    }
	
	@RequestMapping(value = "/getReportData", method = RequestMethod.GET)
    @ResponseBody
	public String getReportData(@RequestParam("userGroupIds") List<String> userGroupIds, @RequestParam("reportType") ReportType reportType, @RequestParam("reportStartDate") String reportStartDate, @RequestParam("reportEndDate") String reportEndDate)
    {
		/*List<String> userGroupIds = null;
		if (userGroupIdArray != null) {
			userGroupIds = Arrays.asList(userGroupIdArray);
		}*/
		System.out.println("User Group Ids " + userGroupIds);
        return famstackDashboardManager.getReportDataJson(reportType, reportStartDate, reportEndDate, userGroupIds);
    }
	
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void downloadReport(@RequestParam("userGroupIds") List<String> userGroupIds,@RequestParam("reportType") ReportType reportType,@RequestParam("fileName") String fileName, @RequestParam("reportStartDate") String reportStartDate, @RequestParam("reportEndDate") String reportEndDate, HttpServletRequest request,
	        HttpServletResponse response)
    {
		
		/*List<String> userGroupIds = null;
		if (userGroupIdArray != null) {
			userGroupIds = Arrays.asList(userGroupIdArray);
		}*/
		System.out.println("User Group Ids " + userGroupIds);
		 famstackXLSExportManager.downloadXLSReport(reportType, fileName, famstackDashboardManager.getReportData(reportType, reportStartDate, reportEndDate, userGroupIds), request, response);
    }
	
	@RequestMapping(value = "/downloadProjectReportBySkill", method = RequestMethod.GET)
	  public void downloadProjectReportBySkill(@RequestParam("fileName") String fileName,
	      @RequestParam("reportStartDate") String reportStartDate,
	      @RequestParam("reportEndDate") String reportEndDate,  @RequestParam("teamIds") List<String> userGroupIds,
	      HttpServletRequest request, HttpServletResponse response) {
	    Date startDate = DateUtils.tryParse(reportStartDate, DateUtils.DATE_FORMAT_CALENDER);
	    Date endDate = DateUtils.tryParse(reportEndDate, DateUtils.DATE_FORMAT_CALENDER);

	    List<ProjectDetailsBySkillsResponse> projectDetailsBySkillsResponse =
	        famstackDashboardManager.getProjectUtilizationBySkills(startDate, endDate, userGroupIds);
	    Map<String, Object> dataMap = new HashMap<>();
	    dataMap.put("DATA", projectDetailsBySkillsResponse);

	    famstackXLSExportManager.downloadProjectsBySkills(dataMap, fileName, request, response);
	  }

	
	@RequestMapping(value = "/getTeamUtilizationChartData", method = RequestMethod.GET)
	@ResponseBody
	public String getTeamUtilizationChartData(@RequestParam("groupIds") String groupIds, @RequestParam("reportStartDate") String reportStartDate, @RequestParam("reportEndDate") String reportEndDate)
    {
		return famstackDashboardManager.getTeamUtilizationChartData(groupIds, reportStartDate,  reportEndDate);
	}
	
	@RequestMapping(value = "/getTeamUtilizationComparisonChartData", method = RequestMethod.GET)
	@ResponseBody
	public String getTeamUtilizationComparisonChartData(@RequestParam("groupIds") String groupIds, @RequestParam("year") String year, @RequestParam("displayWise") String displayWise)
    {
		return famstackDashboardManager.getTeamUtilizationComparisonChartData(groupIds, year,  displayWise);
	}
	
	@RequestMapping(value = "/rest/projects/list", method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity getProjectList(@RequestBody String searchRequest, HttpServletRequest request) {
		String fClientId ="87534234598763332";
		String fSecretKey ="knRIhdlZ0eBiO3TGExwR5XbLdTNR2rdTBCYRJpaPAoh0h7HL21UBTR7JE7H43D6a71UYiWGhKn1g4nIQuhRHXxEbJfzRTzjoGLP0";
		String clientId = request.getHeader("clientId");
		String securityKey = request.getHeader("secretKey");
		
		if (fClientId.equals(clientId) && fSecretKey.equals(securityKey)) {
		SearchRequest sRequest = FamstackUtils.getObjectFromJson(searchRequest);
		Date startDate = DateUtils.tryParse(sRequest.getStartDate(), DateUtils.DATE_FORMAT_CALENDER);
		Date endDate =  DateUtils.tryParse(sRequest.getEndDate(), DateUtils.DATE_FORMAT_CALENDER);
		
		if (startDate == null || endDate == null || !StringUtils.isNotBlank(sRequest.getTeamId())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		List<ProjectDetailsResponse> projectDetailsResponse =  famstackDashboardManager.getProjectList(startDate, endDate, sRequest.getTeamId(), 0);
		  return ResponseEntity
		            .ok()
		            .header("famstackaccesscode", "qwero-234kwerlk-werekl1255")
		            .body(projectDetailsResponse);
		}
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	  }
	

	@RequestMapping(value = "/rest/projects/v1/list", method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity getProjectV1List(@RequestBody String searchRequest, HttpServletRequest request) {
		String fClientId ="87534234598763332";
		String fSecretKey ="knRIhdlZ0eBiO3TGExwR5XbLdTNR2rdTBCYRJpaPAoh0h7HL21UBTR7JE7H43D6a71UYiWGhKn1g4nIQuhRHXxEbJfzRTzjoGLP0";
		String clientId = request.getHeader("clientId");
		String securityKey = request.getHeader("secretKey");
		
		if (fClientId.equals(clientId) && fSecretKey.equals(securityKey)) {
		SearchRequest sRequest = FamstackUtils.getObjectFromJson(searchRequest);
		Date startDate = DateUtils.tryParse(sRequest.getStartDate(), DateUtils.DATE_FORMAT_CALENDER);
		Date endDate =  DateUtils.tryParse(sRequest.getEndDate(), DateUtils.DATE_FORMAT_CALENDER);
		
		if (startDate == null || endDate == null || !StringUtils.isNotBlank(sRequest.getTeamId())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		List<ProjectDetailsResponse> projectDetailsResponse =  famstackDashboardManager.getProjectList(startDate, endDate, sRequest.getTeamId(), 1);
		  return ResponseEntity
		            .ok()
		            .header("famstackaccesscode", "qwero-234kwerlk-werekl1255")
		            .body(projectDetailsResponse);
		}
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	  }
	
	@RequestMapping(value = "/rest/projects/poestimate", method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity getPOEstmateList(@RequestBody String searchRequest, HttpServletRequest request) {
		String fClientId ="87534234598763332";
		String fSecretKey ="knRIhdlZ0eBiO3TGExwR5XbLdTNR2rdTBCYRJpaPAoh0h7HL21UBTR7JE7H43D6a71UYiWGhKn1g4nIQuhRHXxEbJfzRTzjoGLP0";
		String clientId = request.getHeader("clientId");
		String securityKey = request.getHeader("secretKey");
		
		if (fClientId.equals(clientId) && fSecretKey.equals(securityKey)) {
		SearchRequest sRequest = FamstackUtils.getObjectFromJson(searchRequest);
		Date startDate = DateUtils.tryParse(sRequest.getStartDate(), DateUtils.DATE_FORMAT_CALENDER);
		Date endDate =  DateUtils.tryParse(sRequest.getEndDate(), DateUtils.DATE_FORMAT_CALENDER);
		
		if (startDate == null || endDate == null || !StringUtils.isNotBlank(sRequest.getTeamId())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		List<String> userGroupIds = Arrays.asList(sRequest.getTeamId().split(","));
		
		List<POEstimateResponse> poEstimateResponse = famstackDashboardManager.getPOEstimateList(startDate, endDate, userGroupIds);
		  return ResponseEntity
		            .ok()
		            .header("famstackaccesscode", "qwero-234kwerlk-werekl1255")
		            .body(poEstimateResponse);
		}
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	  }
	
	@RequestMapping(value = "/rest/projects/team", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity getTeam(HttpServletRequest request) {
		String fClientId ="87534234598763332";
		String fSecretKey ="knRIhdlZ0eBiO3TGExwR5XbLdTNR2rdTBCYRJpaPAoh0h7HL21UBTR7JE7H43D6a71UYiWGhKn1g4nIQuhRHXxEbJfzRTzjoGLP0";
		String clientId = request.getHeader("clientId");
		String securityKey = request.getHeader("secretKey");
		
		if (fClientId.equals(clientId) && fSecretKey.equals(securityKey)) {

		List<TeamResponse> teamList = famstackDashboardManager.getTeams();
		
		return ResponseEntity
		            .ok()
		            .header("famstackaccesscode", "qwero-234kwerlk-werekl1255")
		            .body(teamList);
		}
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	  }

	@RequestMapping(value = "/rest/reports", method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity getReports(@RequestParam("userGroupIds") List<String> userGroupIds, @RequestBody String searchRequest, @RequestParam("reportType") ReportType reportType, HttpServletRequest request) {
		String fClientId ="87534234598763332";
		String fSecretKey ="knRIhdlZ0eBiO3TGExwR5XbLdTNR2rdTBCYRJpaPAoh0h7HL21UBTR7JE7H43D6a71UYiWGhKn1g4nIQuhRHXxEbJfzRTzjoGLP0";
		String clientId = request.getHeader("clientId");
		String securityKey = request.getHeader("secretKey");
		
		if (fClientId.equals(clientId) && fSecretKey.equals(securityKey)) {
		SearchRequest sRequest = FamstackUtils.getObjectFromJson(searchRequest);
		Date startDate = DateUtils.tryParse(sRequest.getStartDate(), DateUtils.DATE_FORMAT_CALENDER);
		Date endDate =  DateUtils.tryParse(sRequest.getEndDate(), DateUtils.DATE_FORMAT_CALENDER);
		
		if (startDate == null || endDate == null || !StringUtils.isNotBlank(sRequest.getTeamId())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		Map<String, Object> reportResponse = famstackDashboardManager.getReportData(reportType, sRequest.getStartDate(), sRequest.getEndDate(), userGroupIds);
		  return ResponseEntity
		            .ok()
		            .header("famstackaccesscode", "qwero-234kwerlk-werekl1255")
		            .body(reportResponse);
		}
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	  }


@RequestMapping(value = "/rest/projects/detailsbyskills", method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public ResponseEntity getProjectUtilizationBySkills(@RequestBody String searchRequest, HttpServletRequest request) {
	String fClientId ="87534234598763332";
	String fSecretKey ="knRIhdlZ0eBiO3TGExwR5XbLdTNR2rdTBCYRJpaPAoh0h7HL21UBTR7JE7H43D6a71UYiWGhKn1g4nIQuhRHXxEbJfzRTzjoGLP0";
	String clientId = request.getHeader("clientId");
	String securityKey = request.getHeader("secretKey");
	
	if (fClientId.equals(clientId) && fSecretKey.equals(securityKey)) {
	SearchRequest sRequest = FamstackUtils.getObjectFromJson(searchRequest);
	Date startDate = DateUtils.tryParse(sRequest.getStartDate(), DateUtils.DATE_FORMAT_CALENDER);
	Date endDate =  DateUtils.tryParse(sRequest.getEndDate(), DateUtils.DATE_FORMAT_CALENDER);
	
	if (startDate == null || endDate == null || !StringUtils.isNotBlank(sRequest.getTeamId())) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	List<String> userGroupIds = Arrays.asList(sRequest.getTeamId().split(","));
	List<ProjectDetailsBySkillsResponse> projectDetailsBySkillsResponse = famstackDashboardManager.getProjectUtilizationBySkills(startDate, endDate, userGroupIds);
	logInfo("Returning projectDetails Size : " + projectDetailsBySkillsResponse.size());
	return ResponseEntity
	            .ok()
	            .header("famstackaccesscode", "qwero-234kwerlk-werekl1255")
	            .body(projectDetailsBySkillsResponse);
	}
	
	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}

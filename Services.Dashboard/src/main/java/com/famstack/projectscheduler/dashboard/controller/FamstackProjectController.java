package com.famstack.projectscheduler.dashboard.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.famstack.projectscheduler.dashboard.bean.ProjectTaskActivityDetails;
import com.famstack.projectscheduler.dashboard.manager.FamstackDashboardManager;
import com.famstack.projectscheduler.datatransferobject.UserTaskActivityItem;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.ApplicationDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.employees.bean.UserWorkDetails;
import com.famstack.projectscheduler.manager.FamstackXLSExportManager;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.StringUtils;

@Controller
@SessionAttributes
public class FamstackProjectController extends BaseFamstackService
{

    @Resource
    FamstackDashboardManager famstackDashboardManager;

    @Resource
    FamstackXLSExportManager famstackXLSExportManager;

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

        if ("default".equalsIgnoreCase(format) || "format3".equalsIgnoreCase(format)) {
            List<ProjectTaskActivityDetails> projectTaskAssigneeDataList =
                famstackDashboardManager.getAllProjectTaskAssigneeData(startDate, endDate);
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

        Map<String, Map<Integer, UserWorkDetails>> employeeUtilizationData = null;
        Map<String, Map<Integer, Integer>> nonBillableTaskActivities = null;
        Map<String, Object> dataMap = new HashMap<>();
        if ("default".equalsIgnoreCase(templateName) || "format3".equalsIgnoreCase(templateName)) {
            List<ProjectTaskActivityDetails> projectTaskAssigneeDataList =
                famstackDashboardManager.getAllProjectTaskAssigneeData(startDate, endDate);
            nonBillableTaskActivities = famstackDashboardManager.getAllNonBillableTaskActivityList(startDate, endDate);
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
    
    @RequestMapping(value = "/getProjectNamesCodePoIdJson", method = RequestMethod.GET)
    @ResponseBody
    public String getProjectNamesCodePoIdJson(@RequestParam("query") String query)
    {
        return famstackDashboardManager.searchForProjectNamesCodePoIdJson(query);
    }

    @RequestMapping(value = "/createProject", method = RequestMethod.POST)
    @ResponseBody
    public String createProject(@ModelAttribute("projectDetails") ProjectDetails projectDetails, BindingResult result,
        Model model)
    {
        int projectId = famstackDashboardManager.createProject(projectDetails);
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

        famstackDashboardManager.weeklyTimeLog(projectDetails, weekStartDate);
        return "{\"status\": true}";
    }

    
    
    
    @RequestMapping(value = "/updateProject", method = RequestMethod.POST)
    @ResponseBody
    public String updateProject(@ModelAttribute("projectDetails") ProjectDetails projectDetails, BindingResult result,
        Model model)
    {
        famstackDashboardManager.updateProject(projectDetails);
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
        return new ModelAndView("projectdetails", "command", new TaskDetails()).addObject("projectDetails",
            projectDetails);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/uploadfile/{projectCode}")
    @ResponseBody
    public String uploadProjectFile(@PathVariable(value = "projectCode") String projectCode,
        @RequestParam("file") MultipartFile file, HttpServletRequest request)
    {
        famstackDashboardManager.uploadProjectFile(file, projectCode, request);
        return "{\"status\": true}";
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
        @RequestParam("recurringEndDate") String recurringEndDate)
    {
        return famstackDashboardManager.createRecurringProject(projectCode, projectId, cronExp, recurringEndDate);
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

}

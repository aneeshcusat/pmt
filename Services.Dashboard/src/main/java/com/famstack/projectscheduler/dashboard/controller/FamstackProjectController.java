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
import com.famstack.projectscheduler.dashboard.manager.FamstackDashboardManager;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.StringUtils;

@Controller
@SessionAttributes
public class FamstackProjectController extends BaseFamstackService
{

    @Resource
    FamstackDashboardManager famstackDashboardManager;

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public ModelAndView listProjects()
    {
        List<ProjectDetails> projectData = famstackDashboardManager.getProjects();
        return getProjectPageModelView(projectData);
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

    @RequestMapping("/projectreporting")
    public ModelAndView projectreporting(@RequestParam(value = "daterange", defaultValue = "") String dateRange,
        Model model)
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

        List<ProjectDetails> projectData = famstackDashboardManager.getProjectsReporingDataList(startDate, endDate);
        return new ModelAndView("projectreporting").addObject("projectData", projectData).addObject("dateRange",
            dateRange);
    }

    @RequestMapping(value = "/getProjectJson", method = RequestMethod.GET)
    @ResponseBody
    public String getProjectNameJson(@RequestParam("query") String query)
    {
        return famstackDashboardManager.getProjectNameJson(query);
    }

    @RequestMapping(value = "/createProject", method = RequestMethod.POST)
    @ResponseBody
    public String createProject(@ModelAttribute("projectDetails") ProjectDetails projectDetails, BindingResult result,
        Model model)
    {
        famstackDashboardManager.createProject(projectDetails);
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

    @RequestMapping(value = "/deleteProject", method = RequestMethod.POST)
    @ResponseBody
    public String deleteProject(@RequestParam("projectId") int projectId)
    {
        famstackDashboardManager.deleteProject(projectId);
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
}

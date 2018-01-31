package com.famstack.projectscheduler.dashboard.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.TaskStatus;
import com.famstack.projectscheduler.dashboard.manager.FamstackDashboardManager;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.security.user.UserRole;

@Controller
@SessionAttributes
public class FamstackTaskController extends BaseFamstackService
{

    @Resource
    FamstackDashboardManager famstackDashboardManager;

    @RequestMapping(value = "/taskAllocator", method = RequestMethod.GET)
    public ModelAndView taskAllocator()
    {
        List<ProjectDetails> unAssignedProjects = famstackDashboardManager.getProjectDetails(ProjectStatus.UNASSIGNED);
        return new ModelAndView("taskAllocator", "command", new TaskDetails()).addObject("unAssignedProjects",
            unAssignedProjects);
    }

    @RequestMapping(value = "/getAjaxFullcalendar", method = RequestMethod.GET)
    @ResponseBody
    public String getAjaxFullcalendar(@RequestParam("start") String startDate, @RequestParam("end") String endDate)
    {
        return famstackDashboardManager.getAjaxFullcalendar(startDate, endDate);
    }

    @RequestMapping(value = "/createTask", method = RequestMethod.POST)
    @ResponseBody
    public String createTask(@ModelAttribute("taskDetails") TaskDetails taskDetails, BindingResult result, Model model)
    {
        famstackDashboardManager.createTask(taskDetails);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/createExtraTimeTask", method = RequestMethod.POST)
    @ResponseBody
    public String createExtraTimeTask(@ModelAttribute("taskDetails") TaskDetails taskDetails, BindingResult result,
        Model model)
    {
        famstackDashboardManager.createExtraTimeTask(taskDetails);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/updateTask", method = RequestMethod.POST)
    @ResponseBody
    public String updateTask(@ModelAttribute("taskDetails") TaskDetails taskDetails, BindingResult result, Model model)
    {
        famstackDashboardManager.updateTask(taskDetails);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public ModelAndView listTasks()
    {

        UserItem currentUserItem = getFamstackUserSessionConfiguration().getCurrentUser();
        Integer userId = currentUserItem.getId();
        if (currentUserItem.getUserRole() == UserRole.ADMIN || currentUserItem.getUserRole() == UserRole.SUPERADMIN
            || currentUserItem.getUserRole() == UserRole.MANAGER) {
            userId = null;
        }
        Map<String, List<TaskDetails>> taskDetailsMap = famstackDashboardManager.getProjectTasksDataList(userId);
        Map<String, Object> modelViewMap = new HashMap<String, Object>();
        modelViewMap.put("projectTaskDetailsData", taskDetailsMap);
        if (userId == null) {
            Set<Integer> taskOwners = famstackDashboardManager.getTaskOwners(taskDetailsMap);
            modelViewMap.put("taskOwners", taskOwners);
        }
        return new ModelAndView("tasks", "command", new ProjectDetails()).addObject("modelViewMap", modelViewMap);
    }

    @RequestMapping(value = "/listTaskListJson", method = RequestMethod.GET)
    @ResponseBody
    public String listTaskListJson()
    {
        UserItem currentUserItem = getFamstackUserSessionConfiguration().getCurrentUser();

        return famstackDashboardManager.getProjectTasksDataListJson(currentUserItem.getId());
    }

    @RequestMapping(value = "/updateTaskStatus", method = RequestMethod.POST)
    @ResponseBody
    public String updateTaskStatus(@RequestParam("taskId") int taskId,
        @RequestParam("taskStatus") TaskStatus taskStatus, @RequestParam("comments") String comments,
        @RequestParam("adjustCompletionTime") String adjustCompletionTime,
        @RequestParam("adjustStartTime") String adjustStartTime)
    {
        famstackDashboardManager.updateTaskStatus(taskId, taskStatus, comments, adjustStartTime, adjustCompletionTime);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/reAssignTask", method = RequestMethod.POST)
    @ResponseBody
    public String reAssignTask(@RequestParam("taskId") int taskId, @RequestParam("newUserId") int newUserId,
        @RequestParam("taskActivityId") int taskActivityId, @RequestParam("taskStatus") TaskStatus taskStatus)
    {
        famstackDashboardManager.reAssignTask(taskId, newUserId, taskActivityId, taskStatus);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/playTask", method = RequestMethod.POST)
    @ResponseBody
    public String playTask(@RequestParam("taskId") int taskId, @RequestParam("taskActivityId") int taskActivityId)
    {
        return famstackDashboardManager.playTask(taskId, taskActivityId);
    }

    @RequestMapping(value = "/pauseTask", method = RequestMethod.POST)
    @ResponseBody
    public String pauseTask(@RequestParam("taskId") int taskId)
    {
        famstackDashboardManager.pauseTask(taskId);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/createNonBillableTask", method = RequestMethod.POST)
    @ResponseBody
    public String createNonBillableTask(@RequestParam("userId") int userId, @RequestParam("type") String type,
        @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
        @RequestParam("comments") String comments)
    {
        famstackDashboardManager.createNonBillableTask(userId, type, startDate, endDate, comments);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/deleteTaskActivity", method = RequestMethod.POST)
    @ResponseBody
    public String deleteTaskActivity(@RequestParam("activityId") int activityId)
    {
        famstackDashboardManager.deleteTaskActivity(activityId);
        return "{\"status\": true}";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/deletetask")
    @ResponseBody
    public String deleteTask(@RequestParam(value = "taskId") int taskId,
        @RequestParam(value = "projectId") int projectId)
    {
        famstackDashboardManager.deleteProjectTask(taskId, projectId);
        return "{\"status\": true}";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/userTaskActivityJson")
    @ResponseBody
    public String userTaskActivityJson()
    {
        return famstackDashboardManager.getUserTaskActivityJson();
    }
}

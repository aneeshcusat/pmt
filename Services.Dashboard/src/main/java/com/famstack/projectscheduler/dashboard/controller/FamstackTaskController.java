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
        List<ProjectDetails> unAssignedProjects = famstackDashboardManager.getAllUnAssignedProjects();
        return new ModelAndView("taskAllocator", "command", new TaskDetails()).addObject("unAssignedProjects",
            unAssignedProjects);
    }

    @RequestMapping(value = "/getAjaxFullcalendar", method = RequestMethod.GET)
    @ResponseBody
    public String getAjaxFullcalendar(@RequestParam("start") String startDate, @RequestParam("end") String endDate,
        @RequestParam(value = "userId", defaultValue = "-1") int userId)
    {
        return famstackDashboardManager.getAjaxFullcalendar(startDate, endDate, userId);
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
    public ModelAndView listTasks(@RequestParam(value = "dayfilter", defaultValue = "15") int dayfilter)
    {

        UserItem currentUserItem = getFamstackUserSessionConfiguration().getCurrentUser();
        Integer userId = currentUserItem.getId();
        if (currentUserItem.getUserRole() == UserRole.ADMIN || currentUserItem.getUserRole() == UserRole.SUPERADMIN
            || currentUserItem.getUserRole() == UserRole.TEAMLEAD) {
            userId = null;
        }
        Map<String, List<TaskDetails>> taskDetailsMap =
            famstackDashboardManager.getProjectTasksDataList(userId, dayfilter);
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
        @RequestParam("taskActCategory") String taskActCategory, @RequestParam("startDate") String startDate,
        @RequestParam("endDate") String endDate, @RequestParam("comments") String comments)
    {
        famstackDashboardManager.createNonBillableTask(userId, type, taskActCategory, startDate, endDate, comments);
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
    public String userTaskActivityJson(@RequestParam(value = "dayfilter", defaultValue = "10") int dayfilter)
    {
        UserItem currentUserItem = getFamstackUserSessionConfiguration().getCurrentUser();
        Integer userId = currentUserItem.getId();
        if (currentUserItem.getUserRole() == UserRole.ADMIN || currentUserItem.getUserRole() == UserRole.SUPERADMIN
            || currentUserItem.getUserRole() == UserRole.TEAMLEAD) {
            userId = null;
        }
        return famstackDashboardManager.getUserTaskActivityJson(userId, dayfilter);
    }

    @RequestMapping(value = "/adjustTaskTime", method = RequestMethod.POST)
    @ResponseBody
    public String adjustTaskTime(@RequestParam("taskId") int taskId, @RequestParam("newDuration") int newDuration)
    {
        famstackDashboardManager.adjustTaskTime(taskId, newDuration);
        return "{\"status\": true}";
    }

    @RequestMapping(value = "/adjustTaskActivityTime", method = RequestMethod.POST)
    @ResponseBody
    public String adjustTaskActivityTime(@RequestParam("activityId") int activityId,
        @RequestParam("taskId") int taskId, @RequestParam("newDuration") int newDuration,
        @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime)
    {
        int taskActualDuration =
            famstackDashboardManager.adjustTaskActivityTime(activityId, taskId, newDuration, startTime, endTime);
        return "{\"status\": true, \"taskActualDuration\":" + taskActualDuration + "}";
    }

}

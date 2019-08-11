package com.famstack.projectscheduler.manager;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.famstack.email.FamstackEmailSender;
import com.famstack.projectscheduler.contants.HQLStrings;
import com.famstack.projectscheduler.contants.NotificationType;
import com.famstack.projectscheduler.contants.ProjectActivityType;
import com.famstack.projectscheduler.contants.ProjectPriority;
import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.ProjectTaskType;
import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.contants.TaskStatus;
import com.famstack.projectscheduler.contants.UserTaskType;
import com.famstack.projectscheduler.dashboard.bean.ClientProjectDetails;
import com.famstack.projectscheduler.dashboard.bean.DashBoardProjectDetails;
import com.famstack.projectscheduler.dashboard.bean.DashboardUtilizationDetails;
import com.famstack.projectscheduler.dashboard.bean.ProjectCategoryDetails;
import com.famstack.projectscheduler.dashboard.bean.ProjectStatusDetails;
import com.famstack.projectscheduler.dashboard.bean.ProjectTaskActivityDetails;
import com.famstack.projectscheduler.dashboard.bean.TeamUtilizatioDetails;
import com.famstack.projectscheduler.datatransferobject.ProjectItem;
import com.famstack.projectscheduler.datatransferobject.RecurringProjectItem;
import com.famstack.projectscheduler.datatransferobject.TaskItem;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.ProjectSubTeamDetails;
import com.famstack.projectscheduler.employees.bean.RecurringProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskActivityDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.notification.FamstackNotificationServiceManager;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.StringUtils;
import com.famstack.projectscheduler.util.TimeInType;
import com.famstack.projectscheduler.utils.FamstackUtils;

/**
 * The Class FamstackProjectManager.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
@Component
public class FamstackProjectManager extends BaseFamstackManager
{

    @Resource
    FamstackProjectCommentManager famstackProjectCommentManager;

    @Resource
    FamstackProjectActivityManager famstackProjectActivityManager;

    @Resource
    FamstackUserProfileManager famstackUserProfileManager;

    @Resource
    FamstackProjectTaskManager famstackProjectTaskManager;

    @Resource
    FamstackNotificationServiceManager famstackNotificationServiceManager;
    
    @Resource
    FamstackEmailSender famstackEmailSender;
    
    @Resource
    FamstackUserActivityManager famstackUserActivityManager;

    public int createProjectItem(ProjectDetails projectDetails)
    {
        ProjectItem projectItem = new ProjectItem();
        projectItem.setStatus(ProjectStatus.NEW);
        int projectId = saveProjectItem(projectDetails, projectItem);
        famstackProjectActivityManager.createProjectActivityItemItem(projectItem, ProjectActivityType.CREATED, null);
        return projectId;
    }

    public void quickDuplicateProject(int projectId, String projectName, Integer projectDuration,
        String projectStartTime, String projectEndTime, String taskDetailsList)
    {
        ProjectItem projectDetails = getProjectItemById(projectId);
        List<TaskDetails> allTaskDetailsList = new ArrayList<>();

        Date startDate = DateUtils.tryParse(projectStartTime, DateUtils.DATE_TIME_FORMAT);
        Date completionDate = DateUtils.tryParse(projectEndTime, DateUtils.DATE_TIME_FORMAT);
        Timestamp startTimeStamp = null;
        if (startDate != null) {
            startTimeStamp = new Timestamp(startDate.getTime());
        }

        Timestamp completionTimeStamp = null;
        if (completionDate != null) {
            completionTimeStamp = new Timestamp(completionDate.getTime());
        }

        if (taskDetailsList != null && !taskDetailsList.isEmpty()) {
            String[] taskList = taskDetailsList.split("#TD#");

            if (taskList.length > 0) {
                for (String taskData : taskList) {
                    String[] task = taskData.split("#TDD#");
                    if (task.length > 0) {
                        TaskDetails taskDetails = new TaskDetails();
                        taskDetails.setAssignee(Integer.parseInt(task[0]));
                        taskDetails.setName(task[1]);
                        taskDetails.setCanRecure(true);
                        taskDetails.setStartTime(task[2]);
                        taskDetails.setDuration(Integer.parseInt(task[3]));
                        taskDetails.setProjectTaskType(ProjectTaskType.valueOf(task[4]));
                        allTaskDetailsList.add(taskDetails);
                    }
                }
            }
        }

        quickDuplicateProject(projectDetails, projectName, projectDuration, startTimeStamp, completionTimeStamp,
            allTaskDetailsList);
    }

    
    public void quickDuplicateProject(ProjectItem projectDetails, String projectName, Integer projectDuration,
        Timestamp startTimeStamp, Timestamp completionTimeStamp, List<TaskDetails> taskItemList)
    {
        ProjectItem projectItem = new ProjectItem();

        if (projectDetails != null) {
            projectItem.setStatus(ProjectStatus.NEW);
            projectItem.setCategory(projectDetails.getCategory());
            projectItem.setCode(projectDetails.getCode());
            projectItem.setDescription(projectDetails.getDescription());
            projectItem.setName(projectName);
            projectItem.setQuantity(projectDetails.getQuantity());
            projectItem.setPriority(projectDetails.getPriority());
            projectItem.setProjectSubType(projectDetails.getProjectSubType());
            projectItem.setProjectLead(projectDetails.getProjectLead());

            projectItem.setPONumber(projectDetails.getPONumber());
            
            projectItem.setSowLineItem(projectDetails.getSowLineItem());
            projectItem.setNewCategory(projectDetails.getNewCategory());
            
            projectItem.setComplexity(projectDetails.getComplexity());

            projectItem.setStartTime(startTimeStamp);
            projectItem.setCompletionTime(completionTimeStamp);
            projectItem.setDurationHrs(projectDuration);

            projectItem.setAccountId(projectDetails.getAccountId());
            projectItem.setTeamId(projectDetails.getTeamId());
            projectItem.setClientId(projectDetails.getClientId());
            projectItem.setTags(projectDetails.getTags());
            projectItem.setType(projectDetails.getType());

            projectItem.setUserGroupId(projectDetails.getUserGroupId());

            projectItem.setReporter(projectDetails.getReporter());

            projectItem.setWatchers(projectDetails.getWatchers());
            famstackDataAccessObjectManager.saveOrUpdateItem(projectItem);
            famstackProjectActivityManager.createProjectActivityItemItem(projectItem, ProjectActivityType.CREATED,
                "Duplicated from " + projectDetails.getProjectId());

            for (TaskDetails taskDetails : taskItemList) {
                famstackProjectTaskManager.createTaskItem(taskDetails, projectItem, true);
            }

            updateProjectStatusBasedOnTaskStatus(projectItem.getProjectId());
            notifyProjectTaskAssignment(mapProjectItemToProjectDetails(projectItem), taskItemList);

        }

    }

	public void weeklyTimeLog(String projectDetails, String weekStartDate) {
		String []projectRowDetailsArray  = projectDetails.split("#PD#");
		//Billable ->projectIds->tasks->usersIds->daylist
		Map<String, Map<String, Map<String,Map<String, List<Integer>>>>> projectDetailsMap = new HashMap<>();
		Map<String, String> taskCommentsMap = new HashMap<>();
		for(String projectRowDetail : projectRowDetailsArray) {
			String []projectLineItemDetailsArray = projectRowDetail.split("#PID#");
			
			String userId = projectLineItemDetailsArray[0];
			String projectType = projectLineItemDetailsArray[1];
			String projectId = projectLineItemDetailsArray[2];
			
			
			String taskId = projectLineItemDetailsArray[3];
			String nonBillableTask = projectLineItemDetailsArray[4];
			String taskComments = projectLineItemDetailsArray[12];
		
			if (projectType.equalsIgnoreCase("NON_BILLABLE")) {
				projectId = "projectId";
				taskId = nonBillableTask;
			}
			
			taskCommentsMap.put(taskId, taskComments);
			
			Map<String, Map<String,Map<String, List<Integer>>>> projectTypeMap = projectDetailsMap.get(projectType);
			if (projectTypeMap == null) {
				projectTypeMap = new HashMap();
				projectDetailsMap.put(projectType, projectTypeMap);
			}

			
			Map<String,Map<String, List<Integer>>> projectIdsMap = projectTypeMap.get(projectId);
			if (projectIdsMap == null) {
				projectIdsMap = new HashMap<>();
				projectTypeMap.put(projectId, projectIdsMap);
			}

			Map<String, List<Integer>> tasksMap = projectIdsMap.get(taskId);
			if (tasksMap == null) {
				tasksMap = new HashMap<>();
				projectIdsMap.put(taskId, tasksMap);
			}
			
			 List<Integer> weekDayTaskTimeList = tasksMap.get(userId);
			 if (weekDayTaskTimeList == null) {
				 weekDayTaskTimeList = new ArrayList<>();
				tasksMap.put(userId, weekDayTaskTimeList);
			}
			
			for (int i = 1 ; i <=7;i++) {
				String taskTime = projectLineItemDetailsArray[i + 4];
				if (StringUtils.isNotBlank(taskTime)) {
					String[] taskTimeSplit = taskTime.split("[.]");
					int mins = 0;
			    	if (taskTimeSplit.length > 1) {
			    		if (StringUtils.isNotBlank(taskTimeSplit[1])) {
			    			mins = Integer.parseInt(taskTimeSplit[1]);
			    		}
			    		if (mins < 10) {
			    			mins*=10;
			    		}
			    	} 
			    	int hours = 0;
			    	if (StringUtils.isNotBlank(taskTimeSplit[0])) {
			    		hours = Integer.parseInt(taskTimeSplit[0]);
			    	}
					weekDayTaskTimeList.add((hours * 60)  + mins);
				} else {
					weekDayTaskTimeList.add(0);
				}
				
			}
			
		}
		
		createBillableWeeklyTasks(projectDetailsMap, DateUtils.tryParse(weekStartDate, DateUtils.DAY_MONTH_YEAR), taskCommentsMap);
		
	}

    private void createBillableWeeklyTasks(
			Map<String, Map<String, Map<String, Map<String, List<Integer>>>>> projectDetailsMap, Date weekStartTimeInitial, Map<String, String> taskCommentsMap) {
    	for (String projectType : projectDetailsMap.keySet()) {
    		Map<String, Map<String, Map<String, List<Integer>>>> projectIds = projectDetailsMap.get(projectType);
    		
    		for (String projectId : projectIds.keySet()) {
    			ProjectItem projectItem = null;
    			if (projectType.equalsIgnoreCase("BILLABLE")) {
    				projectItem = getProjectItemById(Integer.parseInt(projectId));
    			}
    			Map<String, Map<String, List<Integer>>> taskIds = projectIds.get(projectId);
    			int taskId = 0;
    			for (String taskNameOrId : taskIds.keySet()) {
    				TaskDetails taskDetails = new TaskDetails();
    				TaskItem taskItem = null;
    				if (projectType.equalsIgnoreCase("BILLABLE")) {
	    				if (org.apache.commons.lang.StringUtils.isNumeric(taskNameOrId) ) {
	    					taskItem = famstackProjectTaskManager.getTaskItemById(Integer.parseInt(taskNameOrId));
	    				} 
	    				if (taskItem == null) {
	    					taskItem = getTaskItemFromProject(projectItem, taskNameOrId);
	    				}
	    				if (taskItem == null) {
	    					taskDetails.setName(taskNameOrId);
	    					//taskDetails.setDescription("Weekly time log task for " + taskNameOrId);
	    					taskDetails.setStartTime(DateUtils.format(weekStartTimeInitial, DateUtils.DATE_TIME_FORMAT));
	    					taskDetails.setDuration(0);
	    					taskDetails.setProjectTaskType(ProjectTaskType.PRODUCTIVE);
	    					taskDetails.setPriority(ProjectPriority.HIGH);
	    					famstackProjectTaskManager.createTaskItem(taskDetails, projectItem, true);
	    					taskId = taskDetails.getTaskId();
	    					taskItem = famstackProjectTaskManager.getTaskItemById(taskId);
	    				} 
	    				String taskComments = taskCommentsMap.get(taskNameOrId);
	    				if (StringUtils.isNotBlank(taskComments)) {
	    					taskDetails.setDescription(taskComments);
	    				} else {
	    					taskDetails.setDescription(taskItem.getDescription());
	    				}
    				}
        			Map<String, List<Integer>> userIds = taskIds.get(taskNameOrId);
        			for (String userId : userIds.keySet()) {
        				taskDetails.setAssignee(Integer.parseInt(userId));
            			List<Integer> weekTaskTimeList = userIds.get(userId);
            			Date weekStartTime = new Date(weekStartTimeInitial.getTime());
            			taskDetails.setStartTime(DateUtils.format(weekStartTimeInitial, DateUtils.DATE_TIME_FORMAT));
            			for (int dayTaskTime : weekTaskTimeList) {
            				if (dayTaskTime != 0) {
		            			if (projectType.equalsIgnoreCase("BILLABLE")) {
		            				famstackProjectTaskManager.createCompletedTaskItem(taskDetails, projectItem, taskItem, dayTaskTime, UserTaskType.PROJECT); 
		            			} else {
		            				String userTaskType = "OTHER";
		            				if ("LEAVE".equalsIgnoreCase(taskNameOrId) || "MEETING".equalsIgnoreCase(taskNameOrId)) {
		            					userTaskType = taskNameOrId;
		            				}
		            				String taskName =
		            						dayTaskTime / 60 + " hours " + dayTaskTime % 60 + " Mins " + taskNameOrId;
		            				famstackUserActivityManager.createCompletedUserActivityItem(taskDetails.getAssignee(), weekStartTime, 0, taskName,
		            						dayTaskTime, UserTaskType.valueOf(userTaskType), taskNameOrId, ProjectType.NON_BILLABLE, taskCommentsMap.get(taskNameOrId));
		            			}
            				}
            				weekStartTime = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, weekStartTime, 1);
            				taskDetails.setStartTime(DateUtils.format(weekStartTime, DateUtils.DATE_TIME_FORMAT));
            			}
            		}	
        			
        			if (taskItem != null) {
        				taskItem.setStatus(TaskStatus.COMPLETED);
        				famstackDataAccessObjectManager.updateItem(taskItem);
        			}
        		}
    			
    		}
    	}
		
	}

	private TaskItem getTaskItemFromProject(ProjectItem projectItem,
			String taskNameOrId) {
		if (projectItem != null && projectItem.getTaskItems() != null) {
			for(TaskItem taskItem : projectItem.getTaskItems()) {
				if(taskItem.getName().equalsIgnoreCase(taskNameOrId)) {
					return taskItem;
				}
			}
		}
		return null;
	}

	private void notifyProjectTaskAssignment(ProjectDetails projectDetails, List<TaskDetails> allTaskDetailsList)
    {
        if (projectDetails != null) {
            for (TaskDetails taskDetails : allTaskDetailsList) {

                Map<String, Object> projectTaskMap = new HashMap<>();
                projectTaskMap.put("taskDetails", taskDetails);
                projectTaskMap.put("projectDetails", projectDetails);
                famstackNotificationServiceManager.notifyAll(NotificationType.TASK_ASSIGNED, projectTaskMap, null);
            }
        }
    }

    private int saveProjectItem(ProjectDetails projectDetails, ProjectItem projectItem)
    {
        projectItem.setCategory(projectDetails.getCategory());
        projectItem.setCode(projectDetails.getCode());
        projectItem.setDescription(projectDetails.getDescription());
        projectItem.setName(projectDetails.getName());
        projectItem.setQuantity(projectDetails.getQuantity());
        projectItem.setPriority(projectDetails.getPriority());
        projectItem.setProjectSubType(projectDetails.getProjectSubType());
        projectItem.setProjectLead(projectDetails.getProjectLead());

        projectItem.setPONumber(projectDetails.getPONumber());
        projectItem.setSowLineItem(projectDetails.getSowLineItem());
        projectItem.setNewCategory(projectDetails.getNewCategory());
        
        projectItem.setComplexity(projectDetails.getComplexity());
        Date startDate = DateUtils.tryParse(projectDetails.getStartTime(), DateUtils.DATE_TIME_FORMAT);
        Date completionDate = DateUtils.tryParse(projectDetails.getCompletionTime(), DateUtils.DATE_TIME_FORMAT);
        Timestamp startTimeStamp = null;
        if (startDate != null) {
            startTimeStamp = new Timestamp(startDate.getTime());
        }

        Timestamp completionTimeStamp = null;
        if (completionDate != null) {
            completionTimeStamp = new Timestamp(completionDate.getTime());
        }

        projectItem.setStartTime(startTimeStamp);
        projectItem.setCompletionTime(completionTimeStamp);
        projectItem.setDurationHrs(projectDetails.getDurationHrs());

        projectItem.setAccountId(projectDetails.getAccountId());
        projectItem.setTeamId(projectDetails.getTeamId());
        projectItem.setClientId(projectDetails.getClientId());
        projectItem.setTags(projectDetails.getTags());
        projectItem.setType(projectDetails.getType());
        projectItem.setReporter(getFamstackUserSessionConfiguration().getLoginResult().getUserItem());
        projectItem.setWatchers(projectDetails.getWatchers());
        projectItem = (ProjectItem) famstackDataAccessObjectManager.saveOrUpdateItem(projectItem);
        return projectItem.getProjectId();
    }

    public void deleteProjectItem(int projectId)
    {
        ProjectItem projectItem =
            (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId, ProjectItem.class);
        if (projectItem != null) {
            Set<TaskItem> taskItems = projectItem.getTaskItems();
            if (!taskItems.isEmpty()) {
                for (TaskItem taskItem : taskItems) {
                    famstackProjectTaskManager.deleteAllTaskActivitiesItem(taskItem.getTaskId());
                }
            }
            famstackDataAccessObjectManager.deleteItem(projectItem);
        }
    }

    public void archiveProjectItem(int projectId)
    {
        ProjectItem projectItem =
            (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId, ProjectItem.class);
        if (projectItem != null) {
            projectItem.setDeleted(true);
            famstackDataAccessObjectManager.updateItem(projectItem);
        }

    }

    public ProjectItem getProjectItemById(int projectId)
    {
        return (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId, ProjectItem.class);
    }

    public ProjectItem getLatestProjectByCode(String projectCode)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectCode", projectCode);

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeAllGroupQuery(HQLStrings.getString("getProjectTobeRecurring"),
                dataMap);

        if (projectItemList.size() > 0) {
            return (ProjectItem) projectItemList.get(0);
        }
        return null;
    }

    public void createProjectTask(TaskDetails taskDetails)
    {
        ProjectItem projectItem = getProjectItemById(taskDetails.getProjectId());
        famstackProjectTaskManager.createTaskItem(taskDetails, projectItem);
        updateProjectStatusBasedOnTaskStatus(taskDetails.getProjectId());
    }

    public void createProjectExtraTimeTask(TaskDetails taskDetails)
    {
        ProjectItem projectItem = getProjectItemById(taskDetails.getProjectId());
        famstackProjectTaskManager.createExtraTaskItem(taskDetails, projectItem);
        updateProjectStatusBasedOnTaskStatus(taskDetails.getProjectId());
    }

    public void reAssignTask(TaskDetails taskDetails, int newUserId, int taskActivityId, TaskStatus taskStatus)
    {
        famstackProjectTaskManager.reAssignTask(taskDetails, newUserId, taskActivityId, taskStatus);
    }

    public TaskActivityDetails playTask(int taskId, int taskActivityId)
    {
        return famstackProjectTaskManager.playTask(taskId, taskActivityId);

    }

    public void pauseTask(TaskDetails taskDetails)
    {
        famstackProjectTaskManager.pauseTask(taskDetails);
    }

    private void updateProjectStatusBasedOnTaskStatus(int projectId)
    {
        ProjectItem projectItem;
        projectItem = (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId, ProjectItem.class);
        ProjectStatus projectStatus = getProjectStatus(projectItem);
        updateProjectStatus(projectStatus, projectItem);
    }

    public void updateProjectTask(TaskDetails taskDetails)
    {
        ProjectItem projectItem = getProjectItemById(taskDetails.getProjectId());
        famstackProjectTaskManager.updateTask(taskDetails, projectItem);
        updateProjectStatusBasedOnTaskStatus(taskDetails.getProjectId());
    }

    public void updateProjectStatus(ProjectStatus projectStatus, ProjectItem projectItem)
    {
        projectItem.setStatus(projectStatus);
        getFamstackDataAccessObjectManager().updateItem(projectItem);

    }

    public ProjectStatus getProjectStatus(ProjectItem projectItem)
    {

        List<TaskStatus> taskStatsList = new ArrayList<>();
        if (projectItem != null) {
            for (TaskItem taskItem : projectItem.getTaskItems()) {
                TaskStatus taskStatus = taskItem.getStatus();
                taskStatsList.add(taskStatus);
            }
        }
        if (taskStatsList.isEmpty()) {
            return ProjectStatus.NEW;
        } else if (taskStatsList.contains(TaskStatus.NEW)) {
            return ProjectStatus.UNASSIGNED;
        } else if (taskStatsList.contains(TaskStatus.ASSIGNED)
            && (taskStatsList.contains(TaskStatus.INPROGRESS) || taskStatsList.contains(TaskStatus.COMPLETED))) {
            return ProjectStatus.INPROGRESS;
        } else if (taskStatsList.contains(TaskStatus.ASSIGNED)) {
            return ProjectStatus.ASSIGNED;
        } else if (taskStatsList.contains(TaskStatus.INPROGRESS)) {
            return ProjectStatus.INPROGRESS;
        } else if (taskStatsList.contains(TaskStatus.COMPLETED)) {
            return ProjectStatus.COMPLETED;
        }
        return ProjectStatus.NEW;
    }

    public void deleteProjectTask(int taskId, int projectId)
    {
        famstackProjectTaskManager.deleteTaskItem(taskId);
        updateProjectStatusBasedOnTaskStatus(projectId);
    }

    public String getUserTaskActivityJson(Integer userId, int dayfilter)
    {
        return famstackProjectTaskManager.getUserTaskActivityJson(userId, dayfilter);
    }

    public List<TaskActivityDetails> getUserTaskActivity(Integer userId, String monthFilter)
    {
        return famstackProjectTaskManager.getUserTaskActivity(userId, monthFilter);
    }

    public ProjectDetails mapProjectItemToProjectDetails(ProjectItem projectItem)
    {
        return mapProjectItemToProjectDetails(projectItem, true);
    }

    public ProjectDetails mapProjectItemToProjectDetails(ProjectItem projectItem, boolean isFullLoad)
    {
        return mapProjectItemToProjectDetails(projectItem, isFullLoad, false);
    }

    public ProjectDetails mapProjectItemToProjectDetails(ProjectItem projectItem, boolean isFullLoad,
        boolean includeDeleted)
    {
        if (projectItem != null) {
            if (!includeDeleted && projectItem.getDeleted() == true) {
                return null;
            }
            ProjectDetails projectDetails = new ProjectDetails();
            projectDetails.setCode(projectItem.getCode());
            projectDetails.setId(projectItem.getProjectId());
            projectDetails.setName(projectItem.getName());
            projectDetails.setDeleted(projectItem.getDeleted());
            projectDetails.setProjectTaskDeatils(famstackProjectTaskManager.mapProjectTaskDetails(
                projectItem.getTaskItems(), isFullLoad));
            projectDetails.setDurationHrs(projectItem.getDurationHrs());
            int unAssignedDuration = getUnAssignedDuration(projectDetails);
            projectDetails.setUnAssignedDuration(unAssignedDuration);
            String startDateString = DateUtils.format(projectItem.getStartTime(), DateUtils.DATE_TIME_FORMAT);
            String completionDateString = DateUtils.format(projectItem.getCompletionTime(), DateUtils.DATE_TIME_FORMAT);
            projectDetails.setStartTime(startDateString);
            projectDetails.setCompletionTime(completionDateString);
            projectDetails.setStatus(projectItem.getStatus());
            projectDetails.setCreatedDate(projectItem.getCreatedDate());
            projectDetails.setProjectSubType(projectItem.getProjectSubType());
            projectDetails.setProjectLead(projectItem.getProjectLead());
            projectDetails.setClientId(projectItem.getClientId());
            projectDetails.setAccountId(projectItem.getAccountId());
            projectDetails.setTeamId(projectItem.getTeamId());
            projectDetails.setDescription(projectItem.getDescription());
            projectDetails.setCategory(projectItem.getCategory());
            if (isFullLoad) {
                projectDetails.setCategory(projectItem.getCategory());
                projectDetails.setQuantity(projectItem.getQuantity());
                projectDetails.setPriority(projectItem.getPriority());
                projectDetails.setComplexity(projectItem.getComplexity());
                projectDetails.setPONumber(projectItem.getPONumber());

                projectDetails.setSowLineItem(projectItem.getSowLineItem());
                projectDetails.setNewCategory(projectItem.getNewCategory());
                
                projectDetails.setTags(projectItem.getTags());
                projectDetails.setType(projectItem.getType());
                if (projectItem.getReporter() != null) {
                    projectDetails.setReporterName(projectItem.getReporter().getFirstName() + " "
                        + projectItem.getReporter().getLastName());
                }
                projectDetails.setEmployeeDetails(famstackUserProfileManager.mapEmployeeDetails(projectItem
                    .getReporter()));
                projectDetails.setWatchers(projectItem.getWatchers());
                projectDetails.setLastModifiedDate(projectItem.getLastModifiedDate());

                projectDetails.setProjectComments(famstackProjectCommentManager.mapProjectCommentDetails(projectItem
                    .getProjectComments()));
                projectDetails.setProjectActivityItem(famstackProjectActivityManager
                    .mapProjectActivityDetails(projectItem.getProjectActivityItem()));
            }
            return projectDetails;

        }
        return null;
    }

    private int getUnAssignedDuration(ProjectDetails projectDetails)
    {
        Integer unassignedDuration = projectDetails.getDurationHrs();
        if (projectDetails.getProjectTaskDeatils() != null && unassignedDuration != 0) {
            for (TaskDetails taskDetails : projectDetails.getProjectTaskDeatils()) {
                if (!taskDetails.getExtraTimeTask()) {
                    unassignedDuration -= taskDetails.getDuration();
                }
            }
        }

        return unassignedDuration;
    }

    public List<ProjectDetails> getAllProjectDetailsList(Date startTime, boolean isFullLoad)
    {
        List<ProjectDetails> projectDetailsList = new ArrayList<>();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startTime", startTime);

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeQuery(HQLStrings.getString("getAllProjectItems"), dataMap);

        getProjectsList(projectDetailsList, projectItemList, isFullLoad);
        return projectDetailsList;
    }

    public List<ProjectDetails> getPrimaryProjectsDetailList(Date startDate, Date endDate, Boolean includeArchive)
    {
        List<ProjectDetails> projectDetailsList = new ArrayList<>();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startDate", startDate);
        dataMap.put("endDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0));

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeQueryOrderedBy(HQLStrings.getString("getPrimaryProjectsItems"),
                dataMap, HQLStrings.getString("getPrimaryProjectsItems-OrderBy"));

        getProjectsList(projectDetailsList, projectItemList, false, includeArchive);
        return projectDetailsList;

        /*
         * List<Object[]> projectItemResultList =
         * famstackDataAccessObjectManager.executeSQLQuery(HQLStrings.getString("getPrimaryProjectsItems"), dataMap);
         * for (int i = 0; i < projectItemResultList.size(); i++) { ProjectDetails projectDetails = new
         * ProjectDetails(); Object[] data = projectItemResultList.get(i); projectDetails.setId((Integer) data[0]);
         * projectDetails.setName((String) data[1]); projectDetails.setCreatedDate((Timestamp) data[2]); String
         * completionDateString = DateUtils.format((Date) data[3], DateUtils.DATE_TIME_FORMAT);
         * projectDetails.setCompletionTime(completionDateString); projectDetails.setCode((String) data[4]);
         * projectDetails.setStatus(ProjectStatus.valueOf((String) data[5])); projectDetailsList.add(projectDetails); }
         */

    }

    public List<ProjectDetails> searchProjectDetails(String searchString, Boolean includeArchive)
    {
        List<ProjectDetails> projectDetailsList = new ArrayList<>();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("searchString", "%" + searchString + "%");

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeQueryOrderedBy(
                HQLStrings.getString("getPrimaryProjectsSearchItems"), dataMap,
                HQLStrings.getString("getPrimaryProjectsItems-OrderBy"));

        getProjectsList(projectDetailsList, projectItemList, false, includeArchive);
        return projectDetailsList;
    }

    private void getProjectsList(List<ProjectDetails> projectDetailsList, List<?> projectItemList, boolean isFullLoad)
    {
        getProjectsList(projectDetailsList, projectItemList, isFullLoad, false);
    }

    private void getProjectsList(List<ProjectDetails> projectDetailsList, List<?> projectItemList, boolean isFullLoad,
        Boolean includeArchive)
    {
        if (projectItemList != null) {
            for (Object projectItemObj : projectItemList) {
                ProjectItem projectItem = (ProjectItem) projectItemObj;
                ProjectDetails projectDetails = mapProjectItemToProjectDetails(projectItem, isFullLoad, includeArchive);
                if (projectDetails != null) {
                    projectDetailsList.add(projectDetails);
                }
            }
        }
    }

    public List<ProjectDetails> getAllProjectDetailsList(ProjectStatus projectStatus, Date startTime)
    {
        List<ProjectDetails> projectDetailsList = new ArrayList<>();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("status", projectStatus);
        dataMap.put("startTime", startTime);

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeQuery(HQLStrings.getString("getProjectItemsByStatus"), dataMap);
        getProjectsList(projectDetailsList, projectItemList, false);
        return projectDetailsList;
    }

    public List<ProjectDetails> getMissedTimeLineProjectDetails(Date startTime)
    {
        Map<String, Object> dataMap = new HashMap<>();
        List<ProjectDetails> projectDetailsList = new ArrayList<>();
        dataMap.put("status", ProjectStatus.COMPLETED);
        dataMap.put("completionDate", new Date());
        dataMap.put("startTime", startTime);

        List<?> projectItemList =
            famstackDataAccessObjectManager
                .executeQuery(HQLStrings.getString("getMissedTimeLineProjectItems"), dataMap);
        getProjectsList(projectDetailsList, projectItemList, false);

        return projectDetailsList;
    }

    public List<ProjectDetails> getAllMissedTimeLineProjectDetails(Date startTime)
    {
        Map<String, Object> dataMap = new HashMap<>();
        List<ProjectDetails> projectDetailsList = new ArrayList<>();
        dataMap.put("status", ProjectStatus.COMPLETED);
        dataMap.put("completionDate", new Date());
        dataMap.put("startTime", startTime);

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeAllGroupQuery(HQLStrings.getString("getMissedTimeLineProjectItems"),
                dataMap);
        getProjectsList(projectDetailsList, projectItemList, false);

        return projectDetailsList;
    }

    public List<ProjectDetails> getAllProjectDetailsList(String projectCode, int projectId, Boolean includeArchive)
    {
        List<ProjectDetails> projectDetailsList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("code", projectCode);
        dataMap.put("projectId", projectId);

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeQueryOrderedBy(HQLStrings.getString("getProjectItemsByCode"),
                dataMap, HQLStrings.getString("getProjectItemsByCode-OrderBy"));

        getProjectsList(projectDetailsList, projectItemList, false, includeArchive);
        return projectDetailsList;
    }

    public long getAllProjectDetailsCount(ProjectStatus projectStatus, Date startTime)
    {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("startTime", startTime);
        dataMap.put("status", projectStatus);

        long count =
            famstackDataAccessObjectManager.getCount(HQLStrings.getString("getProjectItemCountByStatus"), dataMap);

        return count;
    }

    public long getAllMissedTimeLineProjectDetailsCount(Date startTime)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("status", ProjectStatus.COMPLETED);
        dataMap.put("completionDate", new Date());
        dataMap.put("startTime", startTime);

        long count =
            famstackDataAccessObjectManager.getCount(HQLStrings.getString("getMissedTimeLineProjectItemCountByStatus"),
                dataMap);

        return count;
    }

    public ProjectDetails getProjectDetails(int projectId)
    {
        ProjectDetails projectDetails =
            mapProjectItemToProjectDetails(
                (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId, ProjectItem.class), true);
        if (projectDetails != null) {
            projectDetails.setDuplicateProjects(getAllProjectDetailsList(projectDetails.getCode(),
                projectDetails.getId(), false));
        }
        return projectDetails;

    }

    public List<ProjectDetails> loadDuplicateProjects(int projectId, String projectCode, Boolean includeArchive)
    {
        return getAllProjectDetailsList(projectCode, projectId, includeArchive);
    }

    public Map<String, List<TaskDetails>> getProjectTasksDataList(Integer userId, int dayfilter)
    {
        Map<String, List<TaskDetails>> projectTaskDetailsMap = famstackProjectTaskManager.getAllProjectTask(userId);
        List<TaskDetails> backLogTasks = famstackProjectTaskManager.getBaklogProjectTasks(userId, dayfilter);
        moveInProgressBackLogTasksToInProgress(projectTaskDetailsMap, backLogTasks);
        projectTaskDetailsMap.put("BACKLOG", backLogTasks);

        sorTaksDetails(projectTaskDetailsMap);
        return projectTaskDetailsMap;
    }

    private void sorTaksDetails(Map<String, List<TaskDetails>> projectTaskDetailsMap)
    {
        List<TaskDetails> assignedTaskDetailsList = projectTaskDetailsMap.get(TaskStatus.ASSIGNED);
        List<TaskDetails> inProgressTaskDetailsList = projectTaskDetailsMap.get(TaskStatus.INPROGRESS);
        List<TaskDetails> completedTaskDetailsList = projectTaskDetailsMap.get(TaskStatus.COMPLETED);
        List<TaskDetails> backLogTaskDetailsList = projectTaskDetailsMap.get("BACKLOG");

        sortProjectData(assignedTaskDetailsList);
        sortProjectData(inProgressTaskDetailsList);
        sortProjectData(completedTaskDetailsList);
        sortProjectData(backLogTaskDetailsList);
    }

    private void sortProjectData(List<TaskDetails> taskDetailsList)
    {
        if (taskDetailsList != null) {
            Collections.sort(taskDetailsList, new Comparator<TaskDetails>()
            {
                @Override
                public int compare(TaskDetails taskDetails1, TaskDetails taskDetails2)
                {
                    if (taskDetails1.getPriorityInt() > taskDetails2.getPriorityInt()) {
                        return -1;
                    } else if (taskDetails1.getPriorityInt() < taskDetails2.getPriorityInt()) {
                        return 1;
                    }
                    return 0;
                }
            });
        }
    }

    private void moveInProgressBackLogTasksToInProgress(Map<String, List<TaskDetails>> projectTaskDetailsMap,
        List<TaskDetails> backLogTasks)
    {
        List<TaskDetails> inProgressTaskList = new ArrayList<TaskDetails>();

        if (backLogTasks != null) {
            for (TaskDetails taskDetails : backLogTasks) {
                if (taskDetails.getStatus() == TaskStatus.INPROGRESS) {
                    inProgressTaskList.add(taskDetails);
                }
            }

            projectTaskDetailsMap.get(TaskStatus.INPROGRESS.value()).addAll(inProgressTaskList);
            backLogTasks.removeAll(inProgressTaskList);
        }
    }

    public void updateTaskStatus(int taskId, TaskStatus taskStatus, String comments, Date adjustStartTime,
        Date adjustCompletionTimeDate)
    {
        TaskItem taskItem =
            famstackProjectTaskManager.updateTaskStatus(taskId, taskStatus, comments, adjustStartTime,
                adjustCompletionTimeDate);
        if (taskItem != null) {
        	updateProjectStatusBasedOnTaskStatus(taskItem.getProjectItem().getProjectId());
        }

    }

    public String getUserTaskActivityForCalenderJson(String startDate, String endDate, int userId)
    {
    	String userGroupId = getFamstackUserSessionConfiguration().getUserGroupId();
        return famstackProjectTaskManager.getUserTaskActivityJson(startDate, endDate, userId, userGroupId);
    } 
    
    public String getUserTaskActivityForEmpUtlCalenderJson(String startDate, String endDate, String userGroupId)
    {
        return famstackProjectTaskManager.getUserTaskActivityJson(startDate, endDate, -1, userGroupId);
    }

    public String getProjectNameJson(String query)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", query + "%");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonProductListObject = new JSONObject();
        List<?> projectItems =
            famstackDataAccessObjectManager.executeQuery(HQLStrings.getString("searchForProjectNames"), dataMap);

        for (Object projectItemObj : projectItems) {
            ProjectItem projectItem = (ProjectItem) projectItemObj;

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", projectItem.getName());
            jsonObject.put("data", projectItem.getProjectId());
            jsonArray.put(jsonObject);
        }
        jsonProductListObject.put("suggestions", jsonArray);
        return jsonProductListObject.toString();
    }
    
    public String searchForProjectNamesCodePoIdJson(String query)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", query + "%");
        dataMap.put("code", query + "%");
        dataMap.put("PONumber", query + "%");
        
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonProductListObject = new JSONObject();
        List<?> projectItems =
            famstackDataAccessObjectManager.executeQuery(HQLStrings.getString("searchForProjectNamesCodePoId"), dataMap);

        for (Object projectItemObj : projectItems) {
            ProjectItem projectItem = (ProjectItem) projectItemObj;
            
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", projectItem.getName());
            jsonObject.put("name", projectItem.getName());
            jsonObject.put("data", projectItem.getProjectId());
            jsonObject.put("projectCode", projectItem.getCode());
            jsonObject.put("projectPOId", projectItem.getPONumber());
            jsonObject.put("projectCategory", projectItem.getCategory());
            jsonObject.put("projectDate", projectItem.getCompletionTime());
            
            if (projectItem.getTaskItems() != null) {
            	JSONArray jsonTaskArray = new JSONArray();
            	for (TaskItem taskItem : projectItem.getTaskItems()) {
            		JSONObject jsonTaskObject = new JSONObject();
            		jsonTaskObject.put("name", taskItem.getName());
            		jsonTaskObject.put("id", taskItem.getTaskId());
            		jsonTaskObject.put("status", taskItem.getStatus());
            		jsonTaskObject.put("timeTaken", taskItem.getActualTimeTaken());
            		jsonTaskObject.put("startTime", taskItem.getStartTime());
            		jsonTaskObject.put("endTime", taskItem.getCompletionTime());
            		jsonTaskArray.put(jsonTaskObject);
            	}
            	
            	jsonObject.put("tasks", jsonTaskArray);
            }
            jsonArray.put(jsonObject);
        }
        jsonProductListObject.put("suggestions", jsonArray);
        return jsonProductListObject.toString();
    }
    
    

    public List<ProjectDetails> getAllProjectDetailsList(Date startDate, Date endDate)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startDate", startDate);
        dataMap.put("endDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0));

        List<ProjectDetails> projectDetailsList = new ArrayList<>();

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeQuery(HQLStrings.getString("searchForProjectsForRepoting"), dataMap);
        logDebug("projectItemList" + projectItemList);
        logDebug("startDate" + startDate);
        logDebug("endDate" + endDate);
        getProjectsList(projectDetailsList, projectItemList, true);
        return projectDetailsList;
    }

    public List<DashBoardProjectDetails> getDashboardProjectData(Date startDate, Date endDate, String userGroupId)
    {
    	 Map<String, Object> dataMap = new HashMap<>();
         dataMap.put("startDate", startDate);
         dataMap.put("endDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0));

         List<DashBoardProjectDetails> dashBoardProjectDetailsList = new ArrayList<>();

         String sqlQuery = HQLStrings.getString("projectTeamAssigneeReportSQL");
         sqlQuery += " and utai.user_grp_id = " + userGroupId;

         List<Object[]> projectItemList = famstackDataAccessObjectManager.executeAllSQLQueryOrderedBy(sqlQuery, dataMap);
         logDebug("projectItemList" + projectItemList);
         logDebug("startDate" + startDate);
         logDebug("endDate" + endDate);
         mapDashBoardProjectsList(dashBoardProjectDetailsList, projectItemList);
         return dashBoardProjectDetailsList;

    }
    

	private List<String> getArrayToList(String ids) {
		List<String>  idList = null;
		if (StringUtils.isNotBlank(ids)){
			 idList = new ArrayList(java.util.Arrays.asList(ids.split("#")));
		}
		return idList;
	}
	

	public List<DashboardUtilizationDetails> dashboardAllUtilization(Date startDate, Date endDate,
			String userGroupId, String accountId, String teamId, String subTeamId, String userId, boolean isResouceUtilization, boolean isTotalUtilization) {
		 Map<String, Object> dataMap = new HashMap<>();
         dataMap.put("startDate", startDate);
         dataMap.put("endDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0));
         String type;
         List<String> accountIdsList = getArrayToList(accountId);
         List<String> subTeamIdsList = getArrayToList(subTeamId);
         List<String> resourceIdsList = getArrayToList(userId);
         
         String sqlQuery = HQLStrings.getString("projectOverAllUtilizationSQL");
         String groupBy = " group by ";
         if (StringUtils.isNotBlank(userId) && !isResouceUtilization) {
        	 sqlQuery += " and "+getCompareQuery("uai.id", resourceIdsList);
        	 groupBy += "pi.account_id";
        	 type = "Accounts";
         } else if (StringUtils.isNotBlank(subTeamId)) {
        	 sqlQuery += " and "+getCompareQuery("pi.team_id", subTeamIdsList);
        	 groupBy += "pi.team_id";
        	 type = "Sub Teams";
         } else if (StringUtils.isNotBlank(teamId)) {
        	 sqlQuery += " and "+getCompareQuery("pi.account_id", accountIdsList);
        	 groupBy += "pi.team_id";
        	 type = "Sub Teams";
         } else  if (StringUtils.isNotBlank(accountId)) {
        	 sqlQuery += " and "+getCompareQuery("pi.account_id", accountIdsList);
        	 groupBy += "pi.team_id";
        	 type = "Teams";
         }  else {
        	 groupBy += "pi.account_id";
        	 type = "Accounts";
         }
         
         if (isResouceUtilization) {
        	 groupBy = " group by uai.id";
         }
         
         if (isTotalUtilization) {
        	 groupBy += ", DATE_FORMAT(utai.actual_start_time, '%Y-%m-%d')";
         }
         
         
       	 sqlQuery += " and utai.user_grp_id = " + userGroupId;
       	 logInfo("dashboardAllUtilization Date " + dataMap);
       	 logInfo("dashboardAllUtilization : " + sqlQuery + groupBy);
       	 
         List<Object[]> overAllUtilization = famstackDataAccessObjectManager.executeAllSQLQueryOrderedBy(sqlQuery + groupBy, dataMap);
         return mapDashboardUtilizationDetails(overAllUtilization, type);
         
	}

    private String getCompareQuery(String coloumn, List<String> IdsList) {
    	String query ="";
    	if (IdsList != null) {
    		if (IdsList.size() == 1) {
    			return coloumn+"="+IdsList.get(0);
    		} else {
    			for(String id : IdsList){
    				if (query == "") {
    					query+="(";
    				} else {
    					query+=" or ";
    				}
    				query += coloumn+" = "+id;
    			}
    			query+=")";
    		}
    	}
    	return query;
	}

	private List<DashboardUtilizationDetails> mapDashboardUtilizationDetails(
			List<Object[]> overAllUtilization, String type) {
    	List<DashboardUtilizationDetails> dashboardUtilizationDetailsList = new ArrayList<>();
    	for (int i = 0; i < overAllUtilization.size(); i++) {
    		DashboardUtilizationDetails dashboardUtilizationDetails = new DashboardUtilizationDetails();
            Object[] data = overAllUtilization.get(i);
            dashboardUtilizationDetails.setActualTaskStartTime((Date) data[0]);
            if (data[1] != null) {
            	dashboardUtilizationDetails.setBillableMins(Double.valueOf(((BigDecimal)data[1]).doubleValue()));
            }
            if (data[2] != null) {
            	dashboardUtilizationDetails.setNonBillableMins(Double.valueOf(((BigDecimal)data[2]).doubleValue()));
            }
            dashboardUtilizationDetails.setAccountId((Integer) data[3]);
            dashboardUtilizationDetails.setSubTeamId((Integer) data[4]);
            dashboardUtilizationDetails.setUserId((Integer) data[5]);
            dashboardUtilizationDetails.setType(type);
            ProjectSubTeamDetails projectSubTeamDetails = FamstackAccountManager.getSubteammap().get(dashboardUtilizationDetails.getSubTeamId());
            if (projectSubTeamDetails != null) {
            	 dashboardUtilizationDetails.setTeamId(projectSubTeamDetails.getTeamId());
            }
            dashboardUtilizationDetailsList.add(dashboardUtilizationDetails);
        }
    	return dashboardUtilizationDetailsList;
	}

	@Deprecated
    private String getFilterQuery(String filters) {
    	String query = null;
    	if (StringUtils.isNotBlank(filters)) {
    		String filter[] = filters.split("#");
    		if (filter.length > 0) {
    			for (String filterData : filter) {
    				String filterItem[] =filterData.split("$");
    				if (filterItem.length > 0) {
    					query +=" and pi.";
    					query+=filterItem[0] + "=" + filterItem[1];
    				}
    			}
    		}
    	}
    	logDebug("Dashboard Filter query "+ query);
    	return query == null ? "" : query;
	}

	public List<ProjectTaskActivityDetails> getAllProjectTaskAssigneeData(Date startDate, Date endDate, boolean getUnique,boolean addSameTaskActTime, Integer userId)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startDate", startDate);
        dataMap.put("endDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0));

        List<ProjectTaskActivityDetails> projectDetailsList = new ArrayList<>();
        List<ProjectTaskActivityDetails>  projectDetailsUniqueTasksList= new ArrayList<>();
        List<ProjectTaskActivityDetails>  allTaskActProjectDetailsList= new ArrayList<>();
        String sqlQuery = HQLStrings.getString("projectTeamAssigneeReportSQL");
        String userGroupId = getFamstackUserSessionConfiguration().getUserGroupId();
        if(userId == null) {
        	sqlQuery += " and utai.user_grp_id = " + userGroupId;
        }
        if(userId != null) {
        	sqlQuery += " and uai.id = " + userId;
        }
        
        sqlQuery += " " + HQLStrings.getString("projectTeamAssigneeReportSQL-OrderBy");

        List<Object[]> projectItemList = famstackDataAccessObjectManager.executeAllSQLQueryOrderedBy(sqlQuery, dataMap);
        logDebug("projectItemList" + projectItemList);
        logDebug("startDate" + startDate);
        logDebug("endDate" + endDate);
        mapProjectsList(projectDetailsList,projectDetailsUniqueTasksList, allTaskActProjectDetailsList, projectItemList);
        
        if (!addSameTaskActTime) {
        	return allTaskActProjectDetailsList;
        }
        
        if (getUnique) {
        	return projectDetailsUniqueTasksList;
        }
        return projectDetailsList;
    }

    private void mapProjectsList(List<ProjectTaskActivityDetails> projectDetailsList, List<ProjectTaskActivityDetails>  projectDetailsUniqueTasksList, List<ProjectTaskActivityDetails> allTaskActProjectDetailsList, List<Object[]> projectItemList)
    {

        /*
         * projectStartTime projectCompletionTime projectCode projectId projectNumber projectName projectStatus
         * projectType projectCategory projectTeamId projectClientId taskName taskActivityStartTime taskActivityDuration
         * userId
         */

        Map<String, ProjectTaskActivityDetails> projectCacheMap = new HashMap<>();
        Map<String, ProjectTaskActivityDetails> projectUniqueItemCacheMap = new HashMap<>();
        
        ProjectTaskActivityDetails projectTaskActivityDetailsTmp;
        ProjectTaskActivityDetails projectUniqueItemDetails;

        for (int i = 0; i < projectItemList.size(); i++) {
            ProjectTaskActivityDetails projectTaskActivityDetails = new ProjectTaskActivityDetails();
            Object[] data = projectItemList.get(i);
            projectTaskActivityDetails.setProjectStartTime((Date) data[0]);
            projectTaskActivityDetails.setProjectCompletionTime((Date) data[1]);
            projectTaskActivityDetails.setProjectCode((String) data[2]);
            projectTaskActivityDetails.setProjectId((Integer) data[3]);
            projectTaskActivityDetails.setProjectNumber((String) data[4]);
            projectTaskActivityDetails.setProjectName((String) data[5]);
            projectTaskActivityDetails.setProjectStatus(ProjectStatus.valueOf((String) data[6]));
            if ( data[7] != null) {
            	projectTaskActivityDetails.setProjectType(ProjectType.valueOf((String) data[7]));
            }
            projectTaskActivityDetails.setProjectCategory((String) data[8]);
            projectTaskActivityDetails.setProjectTeamId((Integer) data[9]);
            projectTaskActivityDetails.setProjectClientId((Integer) data[10]);

            projectTaskActivityDetails.setTaskName((String) data[11]);
            projectTaskActivityDetails.setTaskActivityStartTime((Date) data[12]);
            projectTaskActivityDetails.setTaskActivityEndTime((Date) data[17]);

            projectTaskActivityDetails.setTaskPausedTime((Date) data[18]);
            projectTaskActivityDetails.setTaskStatus(TaskStatus.valueOf((String) data[19]));
            Integer taskActivityDuration = (Integer) data[13];

            if (projectTaskActivityDetails.getTaskStatus() != TaskStatus.COMPLETED
                && projectTaskActivityDetails.getTaskActivityEndTime() == null
                && projectTaskActivityDetails.getTaskPausedTime() != null) {

                taskActivityDuration =
                    DateUtils.getTimeDifference(TimeInType.MINS, projectTaskActivityDetails.getTaskPausedTime()
                        .getTime(), projectTaskActivityDetails.getTaskActivityStartTime().getTime());

            } else if (projectTaskActivityDetails.getTaskStatus() == TaskStatus.INPROGRESS
                && projectTaskActivityDetails.getTaskActivityEndTime() == null
                && projectTaskActivityDetails.getTaskPausedTime() == null) {

                taskActivityDuration =
                    DateUtils.getTimeDifference(TimeInType.MINS, new Date().getTime(), projectTaskActivityDetails
                        .getTaskActivityStartTime().getTime());

            }

            projectTaskActivityDetails.setTaskActivityDuration(taskActivityDuration);
            projectTaskActivityDetails.setTaskActActivityDuration(taskActivityDuration);

            projectTaskActivityDetails.setUserId((Integer) data[14]);

            projectTaskActivityDetails.setTaskId((Integer) data[15]);
            projectTaskActivityDetails.setTaskActivityId((Integer) data[16]);
            projectTaskActivityDetails.setProjectAccountId((Integer) data[20]);
            
            projectTaskActivityDetails.setSowLineItem((String) data[22]);
            
            projectTaskActivityDetails.setNewProjectCategory((String) data[23]);

            projectTaskActivityDetails.setTaskCompletionComments((String) data[24]);

            String key = "D" + DateUtils.format((Date) data[12], DateUtils.DATE_FORMAT);
            key += "T" + data[15];
            key += "U" + data[14];

            String uniqueItemKey = "T" + data[15];
            uniqueItemKey += "U" + data[14];
            
            projectTaskActivityDetailsTmp = projectCacheMap.get(key);
            projectUniqueItemDetails = projectUniqueItemCacheMap.get(uniqueItemKey);
            allTaskActProjectDetailsList.add(projectTaskActivityDetails);
            if (projectTaskActivityDetailsTmp != null) {
                projectTaskActivityDetailsTmp.addToChildProjectActivityDetailsMap(projectTaskActivityDetails);
                projectTaskActivityDetailsTmp.setTaskActivityDuration(projectTaskActivityDetailsTmp
                    .getTaskActivityDuration() + projectTaskActivityDetails.getTaskActivityDuration());
            } else {
                projectCacheMap.put(key, projectTaskActivityDetails);
                projectDetailsList.add(projectTaskActivityDetails);
                
                if (projectUniqueItemDetails != null) {
                	projectUniqueItemDetails.getSubItems().add(projectTaskActivityDetails);
                } else {
                	projectUniqueItemCacheMap.put(uniqueItemKey, projectTaskActivityDetails);
                	projectDetailsUniqueTasksList.add(projectTaskActivityDetails);
                }
            }
           
        }
    }
    
    private void mapDashBoardProjectsList(List<DashBoardProjectDetails> projectDetailsList, List<Object[]> projectItemList)
    {

        Map<String, DashBoardProjectDetails> projectCacheMap = new HashMap<>();
        DashBoardProjectDetails projectTaskActivityDetailsTmp;

        for (int i = 0; i < projectItemList.size(); i++) {
        	DashBoardProjectDetails dashBoardProjectDetails = new DashBoardProjectDetails();
            Object[] data = projectItemList.get(i);
            dashBoardProjectDetails.setProjectStartTime((Date) data[0]);
            dashBoardProjectDetails.setProjectCompletionTime((Date) data[1]);
            dashBoardProjectDetails.setProjectCode((String) data[2]);
            dashBoardProjectDetails.setProjectId((Integer) data[3]);
            dashBoardProjectDetails.setProjectNumber((String) data[4]);
            dashBoardProjectDetails.setProjectName((String) data[5]);
            dashBoardProjectDetails.setProjectStatus(ProjectStatus.valueOf((String) data[6]));
            dashBoardProjectDetails.setProjectType(ProjectType.valueOf((String) data[7]));
            dashBoardProjectDetails.setProjectCategory((String) data[8]);
            dashBoardProjectDetails.setProjectTeamId((Integer) data[9]);
            dashBoardProjectDetails.setProjectClientId((Integer) data[10]);

            dashBoardProjectDetails.setTaskName((String) data[11]);
            dashBoardProjectDetails.setTaskActivityStartTime((Date) data[12]);
            dashBoardProjectDetails.setTaskActivityEndTime((Date) data[17]);

            dashBoardProjectDetails.setTaskPausedTime((Date) data[18]);
            dashBoardProjectDetails.setTaskStatus(TaskStatus.valueOf((String) data[19]));
            Integer taskActivityDuration = (Integer) data[13];

            if (dashBoardProjectDetails.getTaskStatus() != TaskStatus.COMPLETED
                && dashBoardProjectDetails.getTaskActivityEndTime() == null
                && dashBoardProjectDetails.getTaskPausedTime() != null) {

                taskActivityDuration =
                    DateUtils.getTimeDifference(TimeInType.MINS, dashBoardProjectDetails.getTaskPausedTime()
                        .getTime(), dashBoardProjectDetails.getTaskActivityStartTime().getTime());

            } else if (dashBoardProjectDetails.getTaskStatus() == TaskStatus.INPROGRESS
                && dashBoardProjectDetails.getTaskActivityEndTime() == null
                && dashBoardProjectDetails.getTaskPausedTime() == null) {

                taskActivityDuration =
                    DateUtils.getTimeDifference(TimeInType.MINS, new Date().getTime(), dashBoardProjectDetails
                        .getTaskActivityStartTime().getTime());

            }

            dashBoardProjectDetails.setTaskActivityDuration(taskActivityDuration);
            dashBoardProjectDetails.setTaskActActivityDuration(taskActivityDuration);

            dashBoardProjectDetails.setUserId((Integer) data[14]);
            dashBoardProjectDetails.getAssigneeIdList().add( dashBoardProjectDetails.getUserId());
            dashBoardProjectDetails.setTaskId((Integer) data[15]);
            dashBoardProjectDetails.setTaskActivityId((Integer) data[16]);
            dashBoardProjectDetails.setProjectAccountId((Integer) data[20]);
            dashBoardProjectDetails.setProjectLead((Integer) data[21]);

            String key = "PRJ" + dashBoardProjectDetails.getProjectId();

            projectTaskActivityDetailsTmp = projectCacheMap.get(key);
            if (projectTaskActivityDetailsTmp != null) {
            	projectTaskActivityDetailsTmp.getAssigneeIdList().add(dashBoardProjectDetails.getUserId());
                projectTaskActivityDetailsTmp.addToChildProjectActivityDetailsMap(dashBoardProjectDetails);
                projectTaskActivityDetailsTmp.setTaskActivityDuration(projectTaskActivityDetailsTmp
                    .getTaskActivityDuration() + dashBoardProjectDetails.getTaskActivityDuration());
            } else {
                projectCacheMap.put(key, dashBoardProjectDetails);
                projectDetailsList.add(dashBoardProjectDetails);
            }
        }
    }

    public void updateProjectItem(ProjectDetails projectDetails)
    {
        ProjectItem projectItem = getProjectItemById(projectDetails.getId());
        if (projectItem != null) {
            saveProjectItem(projectDetails, projectItem);
            famstackProjectActivityManager.createProjectActivityItemItem(projectItem,
                ProjectActivityType.PROJECT_DETAILS_UPDATED, null);
        }
    }

    public void updateProjectActivityItem(int projectId, ProjectActivityType projectActivityType, String description)
    {
        ProjectItem projectItem = getProjectItemById(projectId);
        if (projectItem != null) {
            famstackProjectActivityManager.createProjectActivityItemItem(projectItem,
                ProjectActivityType.PROJECT_DETAILS_UPDATED, description);
        }
    }

    public TaskDetails getProjectTaskById(int taskId)
    {
        return famstackProjectTaskManager.getTaskDetailsById(taskId);
    }

    public List<ProjectStatusDetails> getProjectItemByTypeCount(Date startTime, Date endTime)
    {
        List<ProjectStatusDetails> projectStatusDetailsList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startTime", startTime);
        List<Object[]> result =
            getFamstackDataAccessObjectManager().executeSQLQuery(HQLStrings.getString("projectItemByTypeCountSQL"),
                dataMap);

        for (int i = 0; i < result.size(); i++) {
            ProjectStatusDetails projectStatusDetails = new ProjectStatusDetails();
            Object[] data = result.get(i);
            logDebug("project count " + data[1]);
            logDebug("Project type " + data[0]);
            projectStatusDetails.setLabel((String) data[0]);
            projectStatusDetails.setValue(data[1]);
            projectStatusDetailsList.add(projectStatusDetails);
        }

        return projectStatusDetailsList;
    }

    public List<TeamUtilizatioDetails> getTeamUtilizationJson(Date startTime, Date endTime)
    {
        List<TeamUtilizatioDetails> teamUtilizatioDetailsList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startTime", startTime);
        List<Object[]> result =
            getFamstackDataAccessObjectManager().executeSQLQuery(HQLStrings.getString("teamUtilizationSQL"), dataMap);

        for (int i = 0; i < result.size(); i++) {
            TeamUtilizatioDetails teamUtilizatioDetails = new TeamUtilizatioDetails();
            Object[] data = result.get(i);
            logDebug("team Name " + data[0]);
            logDebug("billable " + data[2]);
            logDebug("non billable " + data[3]);
            ProjectSubTeamDetails projectSubTeamDetails = FamstackAccountManager.getSubteammap().get(data[0]);
            if (projectSubTeamDetails != null) {
                teamUtilizatioDetails.setName(projectSubTeamDetails.getName());
            } else {
                teamUtilizatioDetails.setName(data[0]);
            }

            teamUtilizatioDetails.setBillable(data[2]);
            teamUtilizatioDetails.setNonBillable(data[3]);

            teamUtilizatioDetailsList.add(teamUtilizatioDetails);
        }

        return teamUtilizatioDetailsList;
    }

    public List<ProjectCategoryDetails> getProjectCategoryJson(Date startTime, Date endTime)
    {
        List<ProjectCategoryDetails> projectCategoryDetailsList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startTime", startTime);
        List<Object[]> result =
            getFamstackDataAccessObjectManager().executeSQLQuery(HQLStrings.getString("projectCategorySQL"), dataMap);

        for (int i = 0; i < result.size(); i++) {
            ProjectCategoryDetails projectCategoryDetails = new ProjectCategoryDetails();
            Object[] data = result.get(i);
            logDebug("category Name " + data[0]);
            logDebug("coutn " + data[1]);

            projectCategoryDetails.setCategoryName(data[0]);
            projectCategoryDetails.setCount(data[1]);
            projectCategoryDetailsList.add(projectCategoryDetails);
        }

        return projectCategoryDetailsList;
    }

    public List<ClientProjectDetails> getClientProject(Date startTime, Date endTime)
    {
        List<ClientProjectDetails> clientProjectDetailsList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startTime", startTime);
        List<Object[]> result =
            getFamstackDataAccessObjectManager().executeSQLQuery(HQLStrings.getString("clientProjectStatusSQL"),
                dataMap);

        for (int i = 0; i < result.size(); i++) {
            ClientProjectDetails clientProjectDetails = new ClientProjectDetails();
            Object[] data = result.get(i);
            logDebug("account Name " + data[0]);
            logDebug("completed " + data[2]);
            logDebug("not ocmpleted " + data[1]);

            AccountDetails accountDetails = FamstackAccountManager.getAccountmap().get(data[0]);
            if (accountDetails != null) {
                clientProjectDetails.setClientName(accountDetails.getName());
            } else {
                clientProjectDetails.setClientName(data[0]);
            }
            clientProjectDetails.setCompletedCount(data[2]);
            clientProjectDetails.setNoCompletedCount(data[1]);
            clientProjectDetailsList.add(clientProjectDetails);
        }

        return clientProjectDetailsList;
    }

    public List<ProjectDetails> getAllProjectDetailsWithIn(Date startDate, Date startDate2)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startDate", startDate);
        dataMap.put("startDate2", startDate2);

        List<ProjectDetails> projectDetailsList = new ArrayList<>();

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeAllGroupQuery(HQLStrings.getString("searchForProjectsWithIn"),
                dataMap);
        logDebug("projectItemList" + projectItemList);
        logDebug("startDate" + startDate);
        logDebug("startDate 2" + startDate2);
        getProjectsList(projectDetailsList, projectItemList, true);
        return projectDetailsList;
    }

    public List<ProjectDetails> getAllProjectDetailsEndTimeWithIn(Date endDate, Date endDate2)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("endDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0));
        dataMap.put("endDate2", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate2, 0));

        List<ProjectDetails> projectDetailsList = new ArrayList<>();

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeAllGroupQuery(HQLStrings.getString("searchForProjectsEndWithIn"),
                dataMap);
        logDebug("projectItemList" + projectItemList);
        logDebug("endDate" + endDate);
        logDebug("endDate 2" + endDate2);
        getProjectsList(projectDetailsList, projectItemList, true);
        return projectDetailsList;
    }

    public List<TaskDetails> getProjectTaskDetails(int projectId)
    {
        return famstackProjectTaskManager.getProjectDetailsTaskDetailsByProjectId(projectId);
    }

    public RecurringProjectDetails mapRecurringProjectItem(RecurringProjectItem recurringProjectItem)
    {
        RecurringProjectDetails recurringProjectDetails = null;
        if (recurringProjectItem != null) {
            recurringProjectDetails = new RecurringProjectDetails();
            recurringProjectDetails.setId(recurringProjectItem.getId());

            String cronExpression = recurringProjectItem.getCronExpression();

            if (cronExpression.contains("|")) {
                cronExpression = cronExpression.substring(0, cronExpression.indexOf("|"));
            }

            recurringProjectDetails.setCronExpression(cronExpression);
            recurringProjectDetails.setLastRun(recurringProjectItem.getLastRun());
            recurringProjectDetails.setName(recurringProjectItem.getName());
            recurringProjectDetails.setNextRun(recurringProjectItem.getNextRun());
            recurringProjectDetails.setProjectId(recurringProjectItem.getProjectId());
            recurringProjectDetails.setProjectCode(recurringProjectItem.getProjectCode());
            recurringProjectDetails.setRequestedBy(recurringProjectItem.getRequestedBy());
            recurringProjectDetails.setUserGroupId(recurringProjectItem.getUserGroupId());
            Date endDate = recurringProjectItem.getEndDate();
            if (endDate != null) {
                recurringProjectDetails.setEndDateString(DateUtils.format(endDate, DateUtils.DATE_FORMAT));
            }
        }
        return recurringProjectDetails;
    }

    public RecurringProjectItem mapRecurringProjectDetails(RecurringProjectDetails recurringProjectDetails)
    {
        RecurringProjectItem recurringProjectItem = null;
        if (recurringProjectDetails != null) {
            recurringProjectItem = new RecurringProjectItem();
            recurringProjectItem.setId(recurringProjectDetails.getId());
            recurringProjectItem.setCronExpression(recurringProjectDetails.getCronExpression());
            recurringProjectItem.setName(recurringProjectDetails.getName());
            recurringProjectItem.setProjectId(recurringProjectDetails.getProjectId());
            recurringProjectItem.setProjectCode(recurringProjectDetails.getProjectCode());
            recurringProjectItem.setRequestedBy(recurringProjectDetails.getRequestedBy());
            recurringProjectItem.setUserGroupId(recurringProjectDetails.getUserGroupId());
        }
        return recurringProjectItem;
    }

    public List<RecurringProjectDetails> getAllRecurringProjects()
    {
        List<RecurringProjectItem> allRecurringProjectItems =
            (List<RecurringProjectItem>) famstackDataAccessObjectManager.getAllItems("RecurringProjectItem");
        List<RecurringProjectDetails> allRecurringProjectDetails = new ArrayList<>();
        if (allRecurringProjectDetails != null) {
            for (RecurringProjectItem recurringProjectItem : allRecurringProjectItems) {
                allRecurringProjectDetails.add(mapRecurringProjectItem(recurringProjectItem));
            }
        }

        return allRecurringProjectDetails;
    }

    public RecurringProjectDetails getRecurringProjectDetails(String projectCode, int projectId)
    {
        return mapRecurringProjectItem(getRecurringProjectItem(projectCode, projectId));
    }

    public RecurringProjectItem getRecurringProjectItem(String projectCode, int projectId)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectCode", projectCode);
        List<?> RecurringProjectItems =
            famstackDataAccessObjectManager.executeQuery(HQLStrings.getString("getRecurringProject"), dataMap);

        if (RecurringProjectItems != null && RecurringProjectItems.size() > 0) {
            return (RecurringProjectItem) RecurringProjectItems.get(0);
        }

        return null;
    }

    public RecurringProjectDetails createRecurringProject(String projectCode, int projectId, String cronExpression,
        String recurringEndDate)
    {
        return mapRecurringProjectItem(saveOrUpdateRecurringProject(projectCode, projectId, cronExpression,
            recurringEndDate));
    }

    private RecurringProjectItem saveOrUpdateRecurringProject(String projectCode, int projectId, String cronExpression,
        String recurringEndDate)
    {
        RecurringProjectItem recurringProjectItem = getRecurringProjectItem(projectCode, projectId);
        if (recurringProjectItem == null) {
            recurringProjectItem = new RecurringProjectItem();
            recurringProjectItem.setName(projectCode);
            recurringProjectItem.setProjectId(projectId);
            recurringProjectItem.setProjectCode(projectCode);
        }

        Date nextRun = FamstackUtils.getNextRunFromCron(cronExpression, new Date());

        String newCronExpression = cronExpression;
        if (cronExpression.contains("#")) {
            String secondCronExpression =
                cronExpression.contains("#1") ? cronExpression.replace("#1", "#3") : cronExpression.contains("#2")
                    ? cronExpression.replace("#2", "#4") : "";
            Date nextRun2 = FamstackUtils.getNextRunFromCron(secondCronExpression, new Date());

            if (nextRun.after(nextRun2)) {
                nextRun = nextRun2;
            }

            newCronExpression = cronExpression + "|" + secondCronExpression;
        }
        if (StringUtils.isNotBlank(recurringEndDate)) {
            Date recurringEndTime = DateUtils.tryParse(recurringEndDate, DateUtils.DATE_FORMAT);
            recurringEndTime = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, recurringEndTime, 0);
            recurringProjectItem.setEndDate(new Timestamp(recurringEndTime.getTime()));
        } else {
            recurringProjectItem.setEndDate(null);
        }
        recurringProjectItem.setCronExpression(newCronExpression);
        recurringProjectItem.setNextRun(nextRun == null ? null : new Timestamp(nextRun.getTime()));
       
        famstackDataAccessObjectManager.saveOrUpdateItem(recurringProjectItem);
        return recurringProjectItem;
    }

    public void deleteRecuringProjectDetails(int recurringProjectId)
    {
        RecurringProjectItem recurringProjectItem =
            (RecurringProjectItem) famstackDataAccessObjectManager.getItemById(recurringProjectId,
                RecurringProjectItem.class);
        if (recurringProjectItem != null) {
            famstackDataAccessObjectManager.deleteItem(recurringProjectItem);
        }
    }

    public List<Object> getAllRecuringProjectCodes()
    {
        List<Object[]> result =
            getFamstackDataAccessObjectManager()
                .executeSQLQuery(HQLStrings.getString("recurringProjectCodesSQL"), null);
        List<Object> projectCodes = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            projectCodes.add(result.get(i));
        }
        return projectCodes;
    }

    public void createRecurringProjects()
    {
        List<RecurringProjectItem> recurringProjectItems = getAllrecurringProjectsForCreation();
        logDebug("recurringProjectItems :" + recurringProjectItems);
        if (recurringProjectItems != null) {
            for (RecurringProjectItem recurringProjectItem : recurringProjectItems) {
                String projectCode = recurringProjectItem.getProjectCode();
                if (recurringProjectItem.getEndDate() != null
                    && new Date().after(new Date(recurringProjectItem.getEndDate().getTime()))) {
                    famstackDataAccessObjectManager.deleteItem(recurringProjectItem);
                    logInfo("Deleting recurring project after expiry : " + projectCode);
                    continue;
                }
                int projectId = recurringProjectItem.getProjectId();
                ProjectItem projectItem = null;
                if (getFamstackApplicationConfiguration().isRecurringByCode(recurringProjectItem.getUserGroupId())) {
                	projectItem = getLatestProjectByCode(projectCode);
               	 	logInfo("Recurring project by code : " + projectId + ", Recurring id " + recurringProjectItem.getId());
                } else {
                	projectItem = getProjectItemById(projectId);
                	logInfo("Recurring project by id : " + projectId + ", Recurring id " + recurringProjectItem.getId());
                }
                if (projectItem != null) {
                    Timestamp projectStartTime = getNewTimeForDuplicate(projectItem.getStartTime());

                    int projectDuration =
                        DateUtils.getTimeDifference(TimeInType.MINS, projectItem.getCompletionTime().getTime(),
                            projectItem.getStartTime().getTime());

                    if (projectDuration < 0) {
                        projectDuration = 60 * 24 * 15;
                    }

                    Timestamp projectEndTime =
                        new Timestamp(DateUtils.getNextPreviousDate(DateTimePeriod.MINUTE, projectStartTime,
                            projectDuration).getTime());

                    List<TaskDetails> taskItemList = getUpdatedRecureTaskDetailsList(projectItem);

                    quickDuplicateProject(projectItem, projectItem.getName(), projectItem.getDurationHrs(),
                        projectStartTime, projectEndTime, taskItemList);
                } else {
                	logError("Recurring project not found : " + projectId + ", Recurring id " + recurringProjectItem.getId());
                	famstackEmailSender.sendTextMessage("ALERT ERROR - SERVER Recurring project not found, Project Id " + projectId + ", Recurring id " + recurringProjectItem.getId(), "Reccuring failed"); 
                }

                recurringProjectItem.setLastRun(recurringProjectItem.getNextRun());
                String cronExpression = recurringProjectItem.getCronExpression();
                Date nextRun = null;
                if (cronExpression.contains("|")) {
                    String cronExpression1 = cronExpression.split("[|]")[0];
                    String cronExpression2 = cronExpression.split("[|]")[1];

                    nextRun = FamstackUtils.getNextRunFromCron(cronExpression1, recurringProjectItem.getNextRun());
                    Date nextRun2 =
                        FamstackUtils.getNextRunFromCron(cronExpression2, recurringProjectItem.getNextRun());

                    if (nextRun.after(nextRun2)) {
                        nextRun = nextRun2;
                    }

                } else {
                    nextRun = FamstackUtils.getNextRunFromCron(cronExpression, recurringProjectItem.getNextRun());
                }

                recurringProjectItem.setNextRun(nextRun == null ? null : new Timestamp(nextRun.getTime()));

                famstackDataAccessObjectManager.saveOrUpdateItem(recurringProjectItem);
            }
        }

    }

    private List<TaskDetails> getUpdatedRecureTaskDetailsList(ProjectItem projectItem)
    {
        List<TaskDetails> taskDetailsList = new ArrayList<>();

        for (TaskItem taskItem : projectItem.getTaskItems()) {
            Date taskStartTime = getNewTimeForDuplicate(taskItem.getStartTime());
            if (taskItem.getCanRecure()) {
                TaskDetails taskDetails = new TaskDetails();
                taskDetails.setAssignee(taskItem.getAssignee());
                //TODO : checkif the user in same group
                taskDetails.setName(taskItem.getName());
                taskDetails.setCanRecure(taskItem.getCanRecure());
                taskDetails.setDescription(taskItem.getDescription());
                taskDetails.setStartTime(DateUtils.format(taskStartTime, DateUtils.DATE_TIME_FORMAT));
                taskDetails.setDuration(taskItem.getDuration());
                taskDetails.setProjectTaskType(taskItem.getProjectTaskType());
                taskDetailsList.add(taskDetails);
            }

        }
        return taskDetailsList;
    }

    private Timestamp getNewTimeForDuplicate(Timestamp startTime)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);

        Calendar todaysCalender = Calendar.getInstance();
        todaysCalender.setTime(new Date());

        todaysCalender.set(todaysCalender.get(Calendar.YEAR), todaysCalender.get(Calendar.MONTH),
            todaysCalender.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));

        logDebug("todaysCalender :" + todaysCalender);
        return new Timestamp(todaysCalender.getTimeInMillis());
    }

    private List<RecurringProjectItem> getAllrecurringProjectsForCreation()
    {
        Map<String, Object> dataMap = new HashMap<>();
        Date startTime = new Date();
        dataMap.put("startTime", startTime);

        List<?> recurrinProjectItems =
            famstackDataAccessObjectManager.executeAllGroupQuery(HQLStrings.getString("recurringProjectsWithIn"),
                dataMap);

        logDebug("recurrinProjectItems" + recurrinProjectItems);
        logDebug("startTime" + startTime);
        return (List<RecurringProjectItem>) recurrinProjectItems;

    }

    public void deleteProjects(List<Integer> projectIds, String type)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectIds", projectIds);
        if ("soft".equalsIgnoreCase(type)) {
            famstackDataAccessObjectManager.executeSQLUpdate(HQLStrings.getString("projectSoftDeleteSQL"), dataMap);

        } else if ("hard".equalsIgnoreCase(type)) {

            for (Integer projectId : projectIds) {
                try {
                    deleteProjectItem(projectId);
                } catch (Exception e) {
                    logError("Unable to delete project : " + projectId, e);
                }
            }
        } else if ("undoarchived".equalsIgnoreCase(type)) {
            famstackDataAccessObjectManager.executeSQLUpdate(HQLStrings.getString("projectUndoSoftDeleteSQL"), dataMap);
        }

    }

    public void softDeleteProjectOlderThan(int days)
    {

        Date dateTill = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), -1 * days);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("dateTill", dateTill);
        famstackDataAccessObjectManager.executeSQLUpdate(HQLStrings.getString("projectSoftDeleteOlderSQL"), dataMap);
    }


}

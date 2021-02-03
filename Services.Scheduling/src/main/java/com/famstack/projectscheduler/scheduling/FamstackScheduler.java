package com.famstack.projectscheduler.scheduling;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.contants.NotificationType;
import com.famstack.projectscheduler.datatransferobject.TaskItem;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.manager.FamstackProjectManager;
import com.famstack.projectscheduler.manager.FamstackProjectTaskManager;
import com.famstack.projectscheduler.manager.FamstackRemoteServiceRefreshManager;
import com.famstack.projectscheduler.manager.FamstackUserActivityManager;
import com.famstack.projectscheduler.notification.FamstackNotificationServiceManager;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;

public class FamstackScheduler extends BaseFamstackService
{

    @Resource
    private FamstackUserActivityManager famstackUserActivityManager;

    @Resource
    private FamstackProjectManager famstackProjectManager;

    @Resource
    private FamstackProjectTaskManager famstackProjectTaskManager;
    
    @Resource
    private FamstackRemoteServiceRefreshManager famstackRemoteServiceRefreshManager;

    @Resource
    private FamstackNotificationServiceManager famstackNotificationServiceManager;
    
    private boolean enabled;

    public void scheduleJob()
    {
    	if (enabled) {
	        logInfo("Running scheduleJob scheduler");
	        userTaskStatusRefresh();
	        creatingRecurringProjects();
	        checkProjectDeadlineMissed();
	        checkProjectStartTimeReminder();
	        checkProjectEndTimeReminder();
	        checkTaskStartTimeReminder();
	        checkTaskEndTimeReminder();
	        checkTaskDeadlineMissed();
	        setTaskRemainingTimeJob();
	        deleteOlderProjects();
	        autoPauseTaskExceedsTimeLimitHrs(10);
	        refreshRemoteCachedItems();
			sendUserAutoReportEmail();
    	}
    }

	@Async
    private void refreshRemoteCachedItems() {
    	logDebug("Running refreshRemoteCachedItems scheduler");
        try {
        	famstackRemoteServiceRefreshManager.triggerRemoteItemRefresh();
        } catch (Exception e) {
            logError("Unable to run refreshRemoteCachedItems", e);
        }
	}

	@Async
    private void deleteOlderProjects()
    {
        logDebug("Running deleteOlderProjects scheduler");
        try {
            famstackProjectManager.softDeleteProjectOlderThan(90);
        } catch (Exception e) {
            logError("Unable to run deleteOlderProjects", e);
        }
    }

    @Async
    public void creatingRecurringProjects()
    {
        logDebug("Running creatingRecurringProjects scheduler");
        try {
            famstackProjectManager.createRecurringProjects();
        } catch (Exception e) {
            logError("Unable to run creatingRecurringProjects", e);
        }
    }

    @Async
    public void setTaskRemainingTimeJob()
    {
        logDebug("Running setTaskRemainingTimeJob scheduler");
        try {
            famstackProjectTaskManager.updateTaskRemaingTime();
        } catch (Exception e) {
            logError("Unable to run setTaskRemainingTimeJob", e);
        }
    }

    public void userTaskStatusRefresh()
    {
        logDebug("Running scheduler - userTaskStatusRefresh");
        try {
            famstackUserActivityManager.updateAllUserAvailableTime();
        } catch (Exception e) {
            logError("Unable to run userTaskStatusRefresh", e);
        }
        logDebug("Running scheduler complted- userTaskStatusRefresh");
    }

    public void checkProjectDeadlineMissed()
    {
        logDebug("Running scheduler - checkProjectDeadlineMissed");
        try {
            List<ProjectDetails> projectDetailsList =
                famstackProjectManager.getAllMissedTimeLineProjectDetails(DateUtils.getNextPreviousDate(
                    DateTimePeriod.MINUTE, new Date(), -5));

            for (ProjectDetails prodDetails : projectDetailsList) {
                famstackNotificationServiceManager.notifyAll(NotificationType.PROJECT_DEADLINE_MISSED, prodDetails,
                    null);
            }
        } catch (Exception e) {
            logError(e.getMessage(), e);
        }

        logDebug("Running scheduler completed- checkProjectStartTimeReminder");
    }

    public void checkProjectStartTimeReminder()
    {
        logDebug("Running scheduler - checkProjectStartTimeReminder");
        try {
            List<ProjectDetails> projectDetailsList =
                famstackProjectManager.getAllProjectDetailsWithIn(new Date(),
                    DateUtils.getNextPreviousDate(DateTimePeriod.MINUTE, new Date(), 5));

            for (ProjectDetails prodDetails : projectDetailsList) {
                famstackNotificationServiceManager
                    .notifyAll(NotificationType.PROJECT_START_REMINDER, prodDetails, null);
            }
        } catch (Exception e) {
            logError(e.getMessage(), e);
        }
        logDebug("Running scheduler completed- checkProjectStartTimeReminder");
    }

    public void checkProjectEndTimeReminder()
    {
        logDebug("Running scheduler - checkProjectEndTimeReminder");
        try {
            List<ProjectDetails> projectDetailsList =
                famstackProjectManager.getAllProjectDetailsEndTimeWithIn(new Date(),
                    DateUtils.getNextPreviousDate(DateTimePeriod.MINUTE, new Date(), 5));

            for (ProjectDetails prodDetails : projectDetailsList) {
                famstackNotificationServiceManager.notifyAll(NotificationType.PROJECT_END_REMINDER, prodDetails, null);
            }
        } catch (Exception e) {
            logError(e.getMessage(), e);
        }
    }

    public void checkTaskStartTimeReminder()
    {
        logDebug("Running scheduler - checkTaskStartTimeReminder");
        try {
            List<TaskDetails> taskDetailsList =
                famstackProjectTaskManager.getAllTaskStartWithin(new Date(),
                    DateUtils.getNextPreviousDate(DateTimePeriod.MINUTE, new Date(), 5));

            for (TaskDetails taskDetails : taskDetailsList) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("taskDetails", taskDetails);
                famstackNotificationServiceManager.notifyAll(NotificationType.TASK_START_REMINDER, dataMap, null);
            }
        } catch (Exception e) {
            logError(e.getMessage(), e);
        }
        logDebug("Running scheduler completed- checkTaskStartTimeReminder");
    }

    public void checkTaskEndTimeReminder()
    {
        logDebug("Running scheduler - checkTaskEndTimeReminder");
        try {
            List<TaskDetails> taskDetailsList =
                famstackProjectTaskManager.getAllTaskEndWithin(new Date(),
                    DateUtils.getNextPreviousDate(DateTimePeriod.MINUTE, new Date(), 5));

            for (TaskDetails taskDetails : taskDetailsList) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("taskDetails", taskDetails);
                famstackNotificationServiceManager.notifyAll(NotificationType.TASK_END_REMINDER, dataMap, null);
            }
        } catch (Exception e) {
            logError(e.getMessage(), e);
        }
        logDebug("Running scheduler completed- checkTaskEndTimeReminder");
    }

    public void checkTaskDeadlineMissed()
    {
        logDebug("Running scheduler - checkTaskDeadlineMissed");
        try {
            List<TaskDetails> taskDetailsList =
                famstackProjectTaskManager.getAllProjectTaskMissedDeadLine(DateUtils.getNextPreviousDate(
                    DateTimePeriod.MINUTE, new Date(), -5));

            for (TaskDetails taskDetails : taskDetailsList) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("taskDetails", taskDetails);
                famstackNotificationServiceManager.notifyAll(NotificationType.TASK_DEADLINE_MISSED, dataMap, null);
            }
        } catch (Exception e) {
            logError(e.getMessage(), e);
        }
        logDebug("Running scheduler completed - checkTaskDeadlineMissed");
    }

    @Async
    private void autoPauseTaskExceedsTimeLimitHrs(int timeInHrs)
    {
        logDebug("Running scheduler - autoPauseTaskExceedsTimeLimitHrs");
        try {
            List<TaskItem> taskItemList =
                famstackProjectTaskManager.pauseAllProjectTaskExceedsTimeLimit(DateUtils.getNextPreviousDate(
                    DateTimePeriod.HOUR, new Date(), -1 * timeInHrs));
            for (TaskItem taskItem : taskItemList) {
                TaskDetails taskDetails = famstackProjectTaskManager.mapTask(taskItem);
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("taskDetails", taskDetails);
                famstackNotificationServiceManager.notifyAll(NotificationType.TASK_AUTO_PAUSED, dataMap, null);
            }
        } catch (Exception e) {
            logError(e.getMessage(), e);
        }
        logDebug("Running scheduler completed - autoPauseTaskExceedsTimeLimitHrs");

    }


    @Async
	private void sendUserAutoReportEmail() {
		logDebug("Running sendUserAutoReportEmail scheduler");
        try {
			famstackProjectManager.sendAutoReportEmail();
        } catch (Exception e) {
			logError("Unable to run sendUserAutoReportEmail", e);
        }
	}
    
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	
}

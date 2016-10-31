package com.famstack.projectscheduler.scheduling;

import java.text.ParseException;

import javax.annotation.Resource;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.util.StringUtils;

/**
 * @author Aneeshkumar
 * @version 1.0
 */
public class FamstackSchedulerInitializer extends BaseFamstackService {

	private static final String SCHEDULE_CRON = "scheduleCron";
	private static final String SCHEDULE_TRIGGER = "scheduleTrigger";
	/** The scheduler factory. */
	@Resource
	Scheduler schedulerFactory;

	@Resource
	CronTriggerImpl scheduleTrigger;

	/**
	 * Initialize.
	 *
	 * @param cronTrigger
	 *            the cron trigger
	 * @param triggerName
	 *            the trigger name
	 * @param cronExpression
	 *            the cron expression
	 */
	private void initialize(CronTriggerImpl cronTrigger, String triggerName, String cronExpression) {
		try {
			logDebug("Initializing scheduler : " + triggerName);
			logDebug("CronExpression : " + cronExpression);

			cronTrigger.setCronExpression(cronExpression);

			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName);
			schedulerFactory.rescheduleJob(triggerKey, cronTrigger);
		} catch (ParseException | SchedulerException e) {
			logError("Unable to initialize scheduler :" + triggerName);
		}

	}

	/**
	 * Reinitialize all schedulers.
	 */
	public void reInitializeAllSchedulers() {

		logDebug("Reinitialize All Schedulers");
		String batchOrderProcessingCron = getFamstackApplicationConfiguration()
				.getConfiguraionItem(SCHEDULE_CRON);

		if (StringUtils.isNotBlank(batchOrderProcessingCron)) {
			initialize(scheduleTrigger, SCHEDULE_TRIGGER, batchOrderProcessingCron);
		}

	}
}

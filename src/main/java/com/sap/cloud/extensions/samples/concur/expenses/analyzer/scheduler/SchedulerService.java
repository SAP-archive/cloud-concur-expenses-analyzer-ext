package com.sap.cloud.extensions.samples.concur.expenses.analyzer.scheduler;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.extensions.samples.concur.expenses.analyzer.scheduler.jobs.ExpensesJob;

/**
 * Scheduler service.
 *
 */
public class SchedulerService {

	private static final Long UPDATE_EXPENSES_JOB_INTERVAL = TimeUnit.MILLISECONDS
			.convert(5, TimeUnit.MINUTES);

	private static final String DEBUG_SCHEDULER_IS_SHUT_DOWN = "Scheduler is shut down.";
	private static final String DEBUG_SHUTTING_DOWN_SCHEDULER = "Shutting down scheduler...";
	private static final String DEBUG_JOB_SCHEDULING_COMPLETED = "Job scheduling completed.";
	private static final String DEBUG_SCHEDULING_JOB = "Scheduling job {}:{}...";
	private static final String DEBUG_STARTING_SCHEDULER = "Starting scheduler...";
	private static final String DEBUG_SCHEDULER_IS_STARTED = "Scheduler is started.";

	private static final String ERROR_PROBLEM_OCCURED_WHILE_INITIALIZING_0_1 = "Problem occured while initializing [{0}]: {1}";

	private static final Logger logger = LoggerFactory
			.getLogger(SchedulerService.class);

	private static final String UPDATE_EXPENSES_KEY = "update-expenses-repeat";
	private static final String UPDATE_EXPENSES_GROUP = "expenses-analyzer";

	private static final JobKey UPDATE_EXPENSES_JOB_KEY = new JobKey(
			UPDATE_EXPENSES_KEY, UPDATE_EXPENSES_GROUP);
	private static final TriggerKey UPDATE_EXPENSES_TRIGGER_KEY = new TriggerKey(
			UPDATE_EXPENSES_KEY, UPDATE_EXPENSES_GROUP);

	private Scheduler scheduler;

	/**
	 * Creates a new scheduler service.
	 * 
	 * @throws SchedulerException
	 */
	public SchedulerService() throws SchedulerException {
		try {
			this.scheduler = new StdSchedulerFactory().getScheduler();
		} catch (SchedulerException e) {
			logger.error(MessageFormat.format(
					ERROR_PROBLEM_OCCURED_WHILE_INITIALIZING_0_1,
					SchedulerService.class.getSimpleName(), e.getMessage()), e);
			throw e;
		}
	}

	/**
	 * Starts the scheduler and schedules all jobs.
	 * 
	 * @throws SchedulerException
	 */
	public void start() throws SchedulerException {
		if (!scheduler.isStarted()) {
			logger.debug(DEBUG_STARTING_SCHEDULER);
			scheduler.start();
		}
		logger.debug(DEBUG_SCHEDULER_IS_STARTED);

		scheduleJobs();
	}

	private void scheduleJobs() throws SchedulerException {
		JobDetail updateExpensesJobDetail = newJob(ExpensesJob.class)
				.withIdentity(UPDATE_EXPENSES_JOB_KEY).build();
		Trigger updateExpensesJobTrigger = newTrigger()
				.withIdentity(UPDATE_EXPENSES_TRIGGER_KEY)
				.startNow()
				.withSchedule(
						simpleSchedule().withIntervalInMilliseconds(
								UPDATE_EXPENSES_JOB_INTERVAL).repeatForever())
				.build();

		logger.debug(DEBUG_SCHEDULING_JOB, updateExpensesJobDetail.getKey(),
				updateExpensesJobTrigger.getKey());
		scheduler
				.scheduleJob(updateExpensesJobDetail, updateExpensesJobTrigger);

		logger.debug(DEBUG_JOB_SCHEDULING_COMPLETED);
	}

	/**
	 * Stops the scheduler.
	 * 
	 * @throws SchedulerException
	 */
	public void stop() throws SchedulerException {
		logger.debug(DEBUG_SHUTTING_DOWN_SCHEDULER);

		scheduler.shutdown();

		logger.debug(DEBUG_SCHEDULER_IS_SHUT_DOWN);
	}
}

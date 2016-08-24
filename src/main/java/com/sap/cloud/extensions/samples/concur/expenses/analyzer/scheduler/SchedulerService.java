package com.sap.cloud.extensions.samples.concur.expenses.analyzer.scheduler;

import java.text.MessageFormat;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Scheduler service.
 *
 */
public class SchedulerService {

	private static final String DEBUG_SCHEDULER_IS_SHUT_DOWN = "Scheduler is shut down.";
	private static final String DEBUG_SHUTTING_DOWN_SCHEDULER = "Shutting down scheduler...";
	private static final String DEBUG_JOB_SCHEDULING_COMPLETED = "Job scheduling completed.";
	private static final String DEBUG_SCHEDULING_JOB = "Scheduling job {}:{}...";
	private static final String DEBUG_STARTING_SCHEDULER = "Starting scheduler...";
	private static final String DEBUG_SCHEDULER_IS_STARTED = "Scheduler is started.";

	private static final String ERROR_PROBLEM_OCCURED_WHILE_INITIALIZING_0_1 = "Problem occured while initializing [{0}]: {1}";

	private static final Logger logger = LoggerFactory
			.getLogger(SchedulerService.class);

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
	}

	/**
	 * Schedules a job with the given job detail and trigger.
	 * 
	 * @param jobDetail
	 * @param trigger
	 * @throws SchedulerException
	 */
	public void scheduleJob(JobDetail jobDetail, Trigger trigger)
			throws SchedulerException {
		logger.debug(DEBUG_SCHEDULING_JOB, jobDetail.getKey(), trigger.getKey());

		scheduler.scheduleJob(jobDetail, trigger);

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

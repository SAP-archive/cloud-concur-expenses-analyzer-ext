package com.sap.cloud.extensions.samples.concur.expenses.analyzer.application;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.extensions.samples.concur.expenses.analyzer.scheduler.SchedulerService;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.scheduler.jobs.UpdateExpensesJob;

/**
 * The main servlet context listener class.
 * 
 */
public class MainServletContextListener implements ServletContextListener {

	private static final String DEBUG_INITIALIZING_APPLICATION = "Initializing application...";
	private static final String DEBUG_APPLICATION_INITIALIZED = "Application initialized.";
	private static final String DEBUG_STARTING_SCHEDULER_SERVICE = "Starting scheduler service...";
	private static final String DEBUG_SCHEDULER_SERVICE_STARTED = "Scheduler service started.";
	private static final String DEBUG_SCHEDULING_JOB = "Scheduling job {}:{}...";
	private static final String DEBUG_JOB_SCHEDULED = "Job {}:{} is scheduled.";
	private static final String DEBUG_PERFORMING_APPLICATION_CLEANUP = "Performing application cleanup...";
	private static final String DEBUG_APPLICATION_CLEANUP_DONE = "Application cleanup done.";
	private static final String DEBUG_STOPPING_SCHEDULER = "Stopping scheduler...";
	private static final String DEBUG_SCHEDULER_STOPPED = "Scheduler stopped.";

	private static final String ERROR_PROBLEM_OCCURED_AFTER_INITIALIZING_APPLICATION_CONTEXT = "Problem occured after initializing application context: {0}";
	private static final String ERROR_PROBLEM_OCCURED_AFTER_DESTROYING_APPLICATION_CONTEXT = "Problem occured after destroying application context: {0}";

	private static final Logger logger = LoggerFactory.getLogger(MainServletContextListener.class);

	private static final int UPDATE_EXPENSES_JOB_INTERVAL_IN_MINUTES = 5;
	private static final Long UPDATE_EXPENSES_JOB_INTERVAL_IN_MILLISECONDS = TimeUnit.MILLISECONDS
			.convert(UPDATE_EXPENSES_JOB_INTERVAL_IN_MINUTES, TimeUnit.MINUTES);

	private static SchedulerService schedulerService;

	/**
	 * Initializes application - creates a scheduler service and starts it.
	 * 
	 */
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		logger.debug(DEBUG_INITIALIZING_APPLICATION);

		try {
			startSchedulerService();
			scheduleJobs();
		} catch (SchedulerException e) {
			String errorMessage = MessageFormat.format(ERROR_PROBLEM_OCCURED_AFTER_INITIALIZING_APPLICATION_CONTEXT,
					e.getMessage());
			logger.error(errorMessage, e);
			throw new RuntimeException(errorMessage, e);
		}

		logger.debug(DEBUG_APPLICATION_INITIALIZED);
	}

	/**
	 * Does cleanup - stops the scheduler.
	 * 
	 */
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		logger.debug(DEBUG_PERFORMING_APPLICATION_CLEANUP);

		stopSchedulerService();

		logger.debug(DEBUG_APPLICATION_CLEANUP_DONE);
	}

	private void startSchedulerService() throws SchedulerException {
		logger.debug(DEBUG_STARTING_SCHEDULER_SERVICE);

		schedulerService = new SchedulerService();
		schedulerService.start();

		logger.debug(DEBUG_SCHEDULER_SERVICE_STARTED);
	}

	private void scheduleJobs() throws SchedulerException {
		JobDetail updateExpensesJobDetail = UpdateExpensesJob.createJobDetail();
		Trigger updateExpensesTrigger = UpdateExpensesJob.createTrigger(UPDATE_EXPENSES_JOB_INTERVAL_IN_MILLISECONDS);

		logger.debug(DEBUG_SCHEDULING_JOB, updateExpensesJobDetail.getKey(), updateExpensesTrigger.getKey());

		schedulerService.scheduleJob(updateExpensesJobDetail, updateExpensesTrigger);

		logger.debug(DEBUG_JOB_SCHEDULED, updateExpensesJobDetail.getKey(), updateExpensesTrigger.getKey());
	}

	private void stopSchedulerService() {
		logger.debug(DEBUG_STOPPING_SCHEDULER);

		try {
			if (schedulerService != null) {
				schedulerService.stop();
			}
		} catch (SchedulerException e) {
			logger.error(
					MessageFormat.format(ERROR_PROBLEM_OCCURED_AFTER_DESTROYING_APPLICATION_CONTEXT, e.getMessage()),
					e);
		}

		logger.debug(DEBUG_SCHEDULER_STOPPED);
	}
}

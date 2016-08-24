package com.sap.cloud.extensions.samples.concur.expenses.analyzer.application;

import java.text.MessageFormat;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.extensions.samples.concur.expenses.analyzer.scheduler.SchedulerService;

/**
 * The main servlet context listener class.
 * 
 */
public class MainServletContextListener implements ServletContextListener {

	private static final String ERROR_PROBLEM_OCCURED_AFTER_INITIALIZING_APPLICATION_CONTEXT = "Problem occured after initializing application context: {0}";
	private static final String ERROR_PROBLEM_OCCURED_AFTER_DESTROYING_APPLICATION_CONTEXT = "Problem occured after destroying application context: {0}";

	private static final Logger logger = LoggerFactory
			.getLogger(MainServletContextListener.class);

	private static SchedulerService schedulerService;

	/**
	 * Initializes application - creates a scheduler service and starts it.
	 * 
	 */
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		startSchedulerService();
	}

	/**
	 * Does cleanup - stops the scheduler.
	 * 
	 */
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		stopSchedulerService();
	}
	
	private void startSchedulerService(){
		try {
			schedulerService = new SchedulerService();
			schedulerService.start();
		} catch (SchedulerException e) {
			String errorMessage = MessageFormat
					.format(ERROR_PROBLEM_OCCURED_AFTER_INITIALIZING_APPLICATION_CONTEXT,
							e.getMessage());
			logger.error(errorMessage, e);
			throw new RuntimeException(errorMessage, e);
		}
	}
	
	private void stopSchedulerService(){
		try {
			if (schedulerService != null) {
				schedulerService.stop();
			}
		} catch (SchedulerException e) {
			logger.error(MessageFormat.format(
					ERROR_PROBLEM_OCCURED_AFTER_DESTROYING_APPLICATION_CONTEXT,
					e.getMessage()), e);
		}
	}
}

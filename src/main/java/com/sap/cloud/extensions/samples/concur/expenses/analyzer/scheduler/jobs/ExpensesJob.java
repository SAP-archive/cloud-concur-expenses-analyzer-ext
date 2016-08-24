package com.sap.cloud.extensions.samples.concur.expenses.analyzer.scheduler.jobs;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos.ExpenseEntryDto;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos.JobExecutionDto;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos.JobStatus;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.facades.ConcurFacade;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.persistence.ExpenseJobDao;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.persistence.ExpenseReportsDao;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.utils.DestinationValidationException;

/**
 * Job for retrieving expenses from Concur and persisting them in the database.
 *
 */
public class ExpensesJob implements Job {

	private static final String DEBUG_EXECUTING_JOB = "Executing [{}] job...";
	private static final String DEBUG_EXECUTED_JOB = "Executed [{}] job.";
	private static final String DEBUG_PERSISTING_EXPENSE_ENTRY_WITH_ID = "Persisting expense entry with id [{}]...";
	private static final String DEBUG_PERSISTED_EXPENSE_ENTRY_WITH_ID = "Persisted expense entry with id [{}].";
	private static final String DEBUG_EXPENSE_ENTRY_WITH_ID_ALREADY_EXISTS = "Expense entry with id [{}] already exists. Won't persist.";

	private static final String WARN_PROBLEM_OCCURED_WHILE_RETRIEVING_ACCOUNT_CONFIGURATIONS = "Problem occured while retrieving account configurations: {}. Make sure the needed destination is set.";
	private static final String MESSAGE_PROBLEM_OCCURED_WHILE_RETRIEVING_ACCOUNT_CONFIGURATIONS = "Problem occured while retrieving account configurations. Make sure the needed destination is set.";
	private static final String ERROR_IO_PROBLEM_OCCURED_WHILE_EXECUTING_EXPENSES_JOB = "Problem occured while executing Expenses Job: {0}";
	private static final String MESSAGE_IO_PROBLEM_OCCURED_WHILE_EXECUTING_EXPENSES_JOB = "Problem occured while executing retrieving expenses from Concur. Make sure the OAuth token is correct.";
	private static final String ERROR_SQL_PROBLEM_OCCURED_WHILE_EXECUTING_EXPENSES_JOB = "Problem occured while persisting expenses: {0}";
	private static final String MESSAGE_SQL_PROBLEM_OCCURED_WHILE_EXECUTING_EXPENSES_JOB = "Problem occured while persisting expenses. Make sure the database configurations are correct. Check the database user credentials and access rights.";
	private static final String ERROR_PROBLEM_OCCURED_WHILE_CREATING_JOB_STATUS = "Problem occured while creating job status: {0}";

	private static final Logger logger = LoggerFactory
			.getLogger(ExpensesJob.class);

	private ExpenseReportsDao expensesDao = new ExpenseReportsDao();
	private ExpenseJobDao expenseJobDao = new ExpenseJobDao();

	private static final Long ONE_DAY_IN_MILLISECONDS = TimeUnit.MILLISECONDS
			.convert(1, TimeUnit.DAYS);

	/**
	 * Retrieves from Concur and persists in the database all paid expense
	 * entries for the last day.
	 * 
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger.debug(DEBUG_EXECUTING_JOB, ExpensesJob.class.getSimpleName());

		Date today = new Date();
		Date yesterday = new Date(today.getTime() - ONE_DAY_IN_MILLISECONDS
				* 100);
		Date tomorrow = new Date(today.getTime() + ONE_DAY_IN_MILLISECONDS);

		JobExecutionDto jobExecution = new JobExecutionDto();
		jobExecution.setJobDate(today);
		jobExecution.setJobStatus(JobStatus.SUCCESS);

		try {
			List<ExpenseEntryDto> expenseEntries = ConcurFacade
					.retrievePaidExpenseEntriesForPeriod(yesterday, tomorrow);
			for (ExpenseEntryDto expenseEntryDto : expenseEntries) {
				persistExpenseEntry(expenseEntryDto);
			}
		} catch (DestinationValidationException e) {
			logger.warn(
					WARN_PROBLEM_OCCURED_WHILE_RETRIEVING_ACCOUNT_CONFIGURATIONS,
					e.getMessage());
			jobExecution.setJobStatus(JobStatus.FAILURE);
			jobExecution
					.setMessage(MESSAGE_PROBLEM_OCCURED_WHILE_RETRIEVING_ACCOUNT_CONFIGURATIONS);
		} catch (IOException e) {
			logger.error(MessageFormat.format(
					ERROR_IO_PROBLEM_OCCURED_WHILE_EXECUTING_EXPENSES_JOB,
					e.getMessage()), e);
			jobExecution.setJobStatus(JobStatus.FAILURE);
			jobExecution
					.setMessage(MESSAGE_IO_PROBLEM_OCCURED_WHILE_EXECUTING_EXPENSES_JOB);
		} catch (SQLException e) {
			logger.error(MessageFormat.format(
					ERROR_SQL_PROBLEM_OCCURED_WHILE_EXECUTING_EXPENSES_JOB,
					e.getMessage()), e);
			jobExecution.setJobStatus(JobStatus.FAILURE);
			jobExecution
					.setMessage(MESSAGE_SQL_PROBLEM_OCCURED_WHILE_EXECUTING_EXPENSES_JOB);
		} finally {
			try {
				expenseJobDao.create(jobExecution);
			} catch (SQLException e) {
				logger.error(MessageFormat.format(
						ERROR_PROBLEM_OCCURED_WHILE_CREATING_JOB_STATUS,
						e.getMessage()), e);
				e.printStackTrace();
			}
		}
		logger.debug(DEBUG_EXECUTED_JOB, ExpensesJob.class.getSimpleName());
	}

	private void persistExpenseEntry(ExpenseEntryDto expenseEntry)
			throws SQLException {
		if (!expensesDao
				.doesExpenseEntryExists(expenseEntry.getReportEntryID())) {
			logger.debug(DEBUG_PERSISTING_EXPENSE_ENTRY_WITH_ID,
					expenseEntry.getReportEntryID());

			expensesDao.create(expenseEntry);

			logger.debug(DEBUG_PERSISTED_EXPENSE_ENTRY_WITH_ID,
					expenseEntry.getReportEntryID());
		} else {
			logger.debug(DEBUG_EXPENSE_ENTRY_WITH_ID_ALREADY_EXISTS,
					expenseEntry.getReportEntryID());
		}
	}
}

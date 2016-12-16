package com.sap.cloud.extensions.samples.concur.expenses.analyzer.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos.JobExecutionDto;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos.JobStatus;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.facades.PersistenceFacade;

public class ExpenseJobDao {

	private static final String TABLE_NAME = "com.sap.hcp.extensions.concur.trialeac::EXPENSES_JOB_TABLE";
	private static final String STMT_INSERT = "INSERT INTO \"" + ExpenseAnalysesSchema.NAME + "\".\"" + TABLE_NAME
			+ "\" (JOB_EXECUTION_TIMESTAMP, JOB_EXECUTION_STATUS, JOB_EXECUTION_MESSAGE) VALUES (?, ?, ?)";
	private static final String STMT_FIND_LATEST_RECORD = "SELECT JOB_EXECUTION_TIMESTAMP, JOB_EXECUTION_STATUS, JOB_EXECUTION_MESSAGE FROM \""
			+ ExpenseAnalysesSchema.NAME + "\".\"" + TABLE_NAME
			+ "\" WHERE (JOB_EXECUTION_TIMESTAMP) IN (SELECT MAX(JOB_EXECUTION_TIMESTAMP) FROM \""
			+ ExpenseAnalysesSchema.NAME + "\".\"" + TABLE_NAME + "\")";

	private static final String DEBUG_CREATING_JOB_EXECUTION_STATUS = "Creating job execution with date [{}] and status [{}]...";
	private static final String ERROR_CREATING_JOB_EXECUTION_STATUS = "Problem occured while creating job execution with date [{0}] and status [{1}]: {2}";
	private static final String DEBUG_CREATED_JOB_EXECUTION_STATUS = "Created job execution with date [{}] and status [{}].";
	private static final String DEBUG_SEARCHING_FOR_THE_LATEST_JOB_EXECUTION_STATUS = "Searching for the latest job execution status...";
	private static final String ERROR_SEARCHING_FOR_THE_LATEST_JOB_EXECUTION_STATUS = "Problem occured while searching for the latest job execution status: {0}";
	private static final String DEBUG_SEARCH_RESULT_FOR_THE_LATEST_JOB_EXECUTION_STATUS = "The last execution is with date [{}] and status [{}].";
	private static final String DEBUG_NO_RESULT_FOR_THE_LATEST_JOB_EXECUTION_STATUS = "No result for latest job exection status is found.";

	private static final Logger logger = LoggerFactory.getLogger(ExpenseJobDao.class);

	/**
	 * Persists job execution.
	 * 
	 * @param jobExecution
	 *            the job execution entry to be persisted.
	 * @throws SQLException
	 */
	public void create(JobExecutionDto jobExecution) throws SQLException {
		logger.debug(DEBUG_CREATING_JOB_EXECUTION_STATUS, jobExecution.getJobDate(), jobExecution.getJobStatus());

		Connection connection = null;

		try {
			connection = PersistenceFacade.createConnection();

			PreparedStatement pstmt = connection.prepareStatement(STMT_INSERT);
			pstmt.setTimestamp(1, new Timestamp(jobExecution.getJobDate().getTime()));
			pstmt.setString(2, jobExecution.getJobStatus().name());
			pstmt.setString(3, jobExecution.getMessage());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.error(MessageFormat.format(ERROR_CREATING_JOB_EXECUTION_STATUS, jobExecution.getJobDate(),
					jobExecution.getJobStatus(), e.getMessage()), e);
			throw e;
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		logger.debug(DEBUG_CREATED_JOB_EXECUTION_STATUS, jobExecution.getJobDate(), jobExecution.getJobStatus());
	}

	/**
	 * Retrieves the latest job execution from the database.
	 * 
	 * @return the latest job execution. Empty object if no job execution is
	 *         found.
	 * @throws SQLException
	 *             in case of database problems.
	 */
	public JobExecutionDto retrieveLatestJobExecution() throws SQLException {
		logger.debug(DEBUG_SEARCHING_FOR_THE_LATEST_JOB_EXECUTION_STATUS);

		Connection connection = null;
		JobExecutionDto result = new JobExecutionDto();
		try {
			connection = PersistenceFacade.createConnection();

			PreparedStatement pstmt = connection.prepareStatement(STMT_FIND_LATEST_RECORD);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				result = new JobExecutionDto();
				result.setJobDate(new Date(rs.getTimestamp(1).getTime()));
				result.setJobStatus(JobStatus.valueOf(rs.getString(2)));
				result.setMessage(rs.getString(3));
				logger.debug(DEBUG_SEARCH_RESULT_FOR_THE_LATEST_JOB_EXECUTION_STATUS, result.getJobDate(),
						result.getJobStatus());
			} else {
				logger.debug(DEBUG_NO_RESULT_FOR_THE_LATEST_JOB_EXECUTION_STATUS);
			}

			return result;
		} catch (SQLException e) {
			logger.error(MessageFormat.format(ERROR_SEARCHING_FOR_THE_LATEST_JOB_EXECUTION_STATUS, e.getMessage()), e);
			throw e;
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
}

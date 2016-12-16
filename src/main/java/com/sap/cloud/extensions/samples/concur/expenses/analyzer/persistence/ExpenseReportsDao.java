package com.sap.cloud.extensions.samples.concur.expenses.analyzer.persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos.ExpenseEntryDto;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.facades.PersistenceFacade;

/**
 * DAO for
 * {@link com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos.ExpenseEntryDto}
 *
 */
public class ExpenseReportsDao {

	private static final String TABLE_NAME = "com.sap.hcp.extensions.concur.trialeac::EXPENSES_REPORTS_TABLE";
	private static final String STMT_INSERT = "INSERT INTO \"" + ExpenseAnalysesSchema.NAME + "\".\"" + TABLE_NAME
			+ "\" (ID, TRANSACTION_DATE, TOTAL_AMOUNT, TRANSACTION_CURRENCY, EXPENSE_TYPE) VALUES (?, ?, ?, ?, ?)";
	private static final String STMT_FIND_RECORD = "SELECT ID FROM \"" + ExpenseAnalysesSchema.NAME + "\".\""
			+ TABLE_NAME + "\" WHERE ID = ?";

	private static final String DEBUG_CREATING_EXPENSE = "Creating expense with id [{}]...";
	private static final String ERROR_CREATING_EXPENSE = "Problem occured while creating expense with id [{0}]: {1}";
	private static final String DEBUG_CREATED_EXPENSE = "Created expense with id [{}].";
	private static final String DEBUG_SEARCHING_FOR_EXPENSE = "Searching for expense record with id [{}]...";
	private static final String ERROR_SEARCHING_FOR_EXPENSE = "Problem occured while searching for expense record with id [{0}]: {1}";
	private static final String DEBUG_SEARCH_RESULT_FOR_EXPENSE = "Search result for expense record with id [{}] is: [{}].";

	private static final Logger logger = LoggerFactory.getLogger(ExpenseReportsDao.class);

	/**
	 * Persists expense.
	 * 
	 * @param expenseEntry
	 *            the expense entry to be persisted.
	 * @throws SQLException
	 */
	public void create(ExpenseEntryDto expenseEntry) throws SQLException {
		logger.debug(DEBUG_CREATING_EXPENSE, expenseEntry.getReportEntryID());

		Connection connection = null;

		try {
			connection = PersistenceFacade.createConnection();

			PreparedStatement pstmt = connection.prepareStatement(STMT_INSERT);
			pstmt.setString(1, expenseEntry.getReportEntryID());
			pstmt.setDate(2, new Date(expenseEntry.getTransactionDate().getTime()));
			pstmt.setDouble(3, expenseEntry.getApprovedAmount());
			pstmt.setString(4, (expenseEntry.getTransactionCurrencyName()));
			pstmt.setString(5, (expenseEntry.getExpenseTypeName()));

			pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.error(MessageFormat.format(ERROR_CREATING_EXPENSE, expenseEntry.getReportEntryID(), e.getMessage()),
					e);
			throw e;
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		logger.debug(DEBUG_CREATED_EXPENSE, expenseEntry.getReportEntryID());
	}

	/**
	 * Checks if expense exists in the database.
	 * 
	 * @param id
	 *            the id of the expense.
	 * @return if the entry exists in the database.
	 * @throws SQLException
	 */
	public boolean doesExpenseEntryExist(String id) throws SQLException {
		logger.debug(DEBUG_SEARCHING_FOR_EXPENSE, id);

		Connection connection = null;
		try {
			connection = PersistenceFacade.createConnection();

			PreparedStatement pstmt = connection.prepareStatement(STMT_FIND_RECORD);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();

			boolean exists = rs.next();

			logger.debug(DEBUG_SEARCH_RESULT_FOR_EXPENSE, id, exists);

			return exists;
		} catch (SQLException e) {
			logger.error(MessageFormat.format(ERROR_SEARCHING_FOR_EXPENSE, id, e.getMessage()), e);
			throw e;
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
}

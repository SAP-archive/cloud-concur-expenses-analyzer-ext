package com.sap.cloud.extensions.samples.concur.expenses.analyzer.persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos.ExpensesAnalysisDto;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.facades.PersistenceFacade;

/**
 * DAO for
 * {@link com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos.ExpensesAnalysisDto}
 *
 */
public class ExpenseAnalysesDao {

	private static final String EXPENSES_REPORTS_VIEW = "com.sap.hcp.extensions.concur.trialeac/EXPENSES_REPORTS_VIEW";
	private static final String STMT_SELECT_ANALYTICS_VIEW_IN_PERIOD = "SELECT TRANSACTION_DATE, SUM(TOTAL_AMOUNT) AS TOTAL_AMOUNT , TRANSACTION_CURRENCY, EXPENSE_TYPE FROM \"_SYS_BIC\".\""
			+ EXPENSES_REPORTS_VIEW
			+ "\" WHERE TRANSACTION_DATE > ? AND TRANSACTION_DATE < ? GROUP BY TRANSACTION_DATE, TRANSACTION_CURRENCY, EXPENSE_TYPE";

	private static final String DEBUG_SEARCHING_EXPENSE_REPORTS_ANALYSES = "Searching for expense reports analyses...";
	private static final String ERROR_SEARCHING_EXPENSE_REPORTS_ANALYSES = "Problem occured while searching for expense reports analyses: {0}";
	private static final String DEBUG_SEARCH_RESULT_FOR_EXPENSE_REPORTS_ANALYSES = "Search result size for expense reports analyses is: [{}].";

	private static final Logger logger = LoggerFactory.getLogger(ExpenseAnalysesDao.class);

	/**
	 * Finds expense reports analyses in given period of time. Calls the
	 * "EXPENSES_REPORTS_VIEW" analytic view, which is located in the
	 * "com.sap.hcp.extensions.concur.eac" package.
	 * 
	 * @param fromDate
	 *            the start date of the period.
	 * @param toDate
	 *            the end date of the period.
	 * @return list of all expense analyses in the given period. An empty list
	 *         if no expense analyses were found.
	 * @throws SQLException
	 */
	public List<ExpensesAnalysisDto> findExpenseReportsAnalyses(Long fromDate, Long toDate) throws SQLException {
		logger.debug(DEBUG_SEARCHING_EXPENSE_REPORTS_ANALYSES);

		Connection connection = null;
		try {
			connection = PersistenceFacade.createConnection();

			PreparedStatement pstmt = connection.prepareStatement(STMT_SELECT_ANALYTICS_VIEW_IN_PERIOD);
			pstmt.setDate(1, new Date(fromDate));
			pstmt.setDate(2, new Date(toDate));
			ResultSet rs = pstmt.executeQuery();
			List<ExpensesAnalysisDto> list = retrieveExpenseReportAnalyses(rs);
			logger.debug(DEBUG_SEARCH_RESULT_FOR_EXPENSE_REPORTS_ANALYSES, list.size());
			return list;
		} catch (SQLException e) {
			logger.error(MessageFormat.format(ERROR_SEARCHING_EXPENSE_REPORTS_ANALYSES, e.getMessage()), e);
			throw e;
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	private List<ExpensesAnalysisDto> retrieveExpenseReportAnalyses(ResultSet rs) throws SQLException {
		ArrayList<ExpensesAnalysisDto> list = new ArrayList<ExpensesAnalysisDto>();
		while (rs.next()) {
			ExpensesAnalysisDto ea = new ExpensesAnalysisDto();
			ea.setTransactionDate(new java.util.Date(rs.getDate(1).getTime()));
			ea.setTotalAmount(rs.getDouble(2));
			ea.setTransactionCurrency(rs.getString(3));
			ea.setExpenseType(rs.getString(4));
			list.add(ea);
		}
		return list;
	}
}

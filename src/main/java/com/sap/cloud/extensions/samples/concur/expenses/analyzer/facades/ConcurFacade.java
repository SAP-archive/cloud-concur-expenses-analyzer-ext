package com.sap.cloud.extensions.samples.concur.expenses.analyzer.facades;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos.DetailedExpenseReportDto;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos.ExpenseEntryDto;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos.ExpenseReportDto;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos.ExpenseReportsDto;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.utils.ConcurAPIsUtil;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.utils.DestinationValidationException;

/**
 * A facade class for Concur API REST calls.
 *
 */
public class ConcurFacade {

	/**
	 * @see <a
	 *      href="https://developer.concur.com/api-reference/expense/expense-report/reports.html">
	 *      Concur Reports API Reference</a>
	 */
	private static final String EXPENSE_REPORTS_V30_PATH = "/api/v3.0/expense/reports";
	private static final String EXPENSE_REPORTS_PAID_FOR_PERIOD_V30_PATH = EXPENSE_REPORTS_V30_PATH
			+ "?user=all&limit={0}&paymentStatusCode=p_paid&paidDateAfter={1}&paidDateBefore={2}";
	private static final int EXPENSE_SEARCH_LIMIT = 100;

	/**
	 * @see <a
	 *      href="https://developer.concur.com/api-reference-deprecated/version-two/expense-reports/expense-report-get.html">
	 *      Concur Reports API Reference</a>
	 */
	private static final String EXPENSE_REPORT_ENTRY_V20_PATH = "/api/expense/expensereport/v2.0/report/{0}";

	private static final String DEBUG_RETRIEVING_CONCUR_EXPENSES_FOR_PERIOD = "Retrieving Concur expenses for the period from [{}] to [{}]...";
	private static final String DEBUG_RETRIEVED_CONCUR_EXPENSES_FOR_PERIOD = "Retrieved [{}] Concur expenses  for the period from [{}] to [{}].";
	private static final String DEBUG_RETRIEVING_EXPENSE_REPORT_ENTRY_WITH_ID = "Retrieving expense report entry with id [{}]...";
	private static final String DEBUG_RETRIEVED_EXPENSE_REPORT_ENTRY_WITH_ID = "Retrieved expense report entry with id [{}].";

	private static final Logger logger = LoggerFactory
			.getLogger(ConcurFacade.class);

	private static final String CONCUR_EXPENSES_REPORTS_DATE_PATTERN = "yyyy-MM-dd";
	private static final SimpleDateFormat expensesReportsDateFormat = new SimpleDateFormat(
			CONCUR_EXPENSES_REPORTS_DATE_PATTERN);

	private static final String CONCUR_EXPENSES_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
	private static final Gson concurExpensesGson = new GsonBuilder()
			.setDateFormat(CONCUR_EXPENSES_DATE_PATTERN).create();

	/**
	 * Retrieves all paid expenses reports for a given period.
	 * 
	 * @param fromDate
	 *            the start date of the period.
	 * @param toDate
	 *            the end date of the period.
	 * @return
	 * @throws IOException
	 * @throws DestinationValidationException
	 */
	public static List<ExpenseEntryDto> getExpenseEntriesPaidForPeriod(
			Date fromDate, Date toDate) throws IOException,
			DestinationValidationException {
		logger.debug(DEBUG_RETRIEVING_CONCUR_EXPENSES_FOR_PERIOD, fromDate,
				toDate);

		String expenseReportsPaidForPeriodPath = MessageFormat.format(
				EXPENSE_REPORTS_PAID_FOR_PERIOD_V30_PATH, EXPENSE_SEARCH_LIMIT,
				expensesReportsDateFormat.format(fromDate),
				expensesReportsDateFormat.format(toDate));

		ExpenseReportsDto еxpenseReportsPaidForPeriod = ConcurAPIsUtil
				.callConcurApi(expenseReportsPaidForPeriodPath,
						ExpenseReportsDto.class, concurExpensesGson);

		List<ExpenseEntryDto> result = new ArrayList<ExpenseEntryDto>();

		for (ExpenseReportDto expenseReport : еxpenseReportsPaidForPeriod
				.getItems()) {
			DetailedExpenseReportDto detailedExpenseReport = getExpenseReportEntry(expenseReport
					.getID());
			result.addAll(detailedExpenseReport.getExpenseEntriesList());
		}

		logger.debug(DEBUG_RETRIEVED_CONCUR_EXPENSES_FOR_PERIOD, result.size(),
				fromDate, toDate);

		return result;
	}

	private static DetailedExpenseReportDto getExpenseReportEntry(
			String expenseReportEntryId) throws IOException,
			DestinationValidationException {
		logger.debug(DEBUG_RETRIEVING_EXPENSE_REPORT_ENTRY_WITH_ID,
				expenseReportEntryId);

		String expenseReportEntryPath = MessageFormat.format(
				EXPENSE_REPORT_ENTRY_V20_PATH, expenseReportEntryId);
		DetailedExpenseReportDto detailedExpenseReport = ConcurAPIsUtil
				.callConcurApi(expenseReportEntryPath,
						DetailedExpenseReportDto.class, concurExpensesGson);

		logger.debug(DEBUG_RETRIEVED_EXPENSE_REPORT_ENTRY_WITH_ID,
				expenseReportEntryId);
		return detailedExpenseReport;
	}
}

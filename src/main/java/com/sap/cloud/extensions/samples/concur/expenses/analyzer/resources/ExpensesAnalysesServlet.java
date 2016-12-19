package com.sap.cloud.extensions.samples.concur.expenses.analyzer.resources;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos.ExpensesAnalysesDto;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos.ExpensesAnalysisDto;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos.JobExecutionDto;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.persistence.ExpenseAnalysesDao;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.persistence.ExpenseJobDao;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.utils.HttpUtil;

/**
 * Expenses analyses endpoint.
 *
 */
@WebServlet("/rest/expenses/analyses")
public class ExpensesAnalysesServlet extends HttpServlet {

	private static final long serialVersionUID = -4159363782231288445L;

	private static final Logger logger = LoggerFactory.getLogger(ExpensesAnalysesServlet.class);

	private static final String ERROR_PROBLEM_OCCURED_WHILE_RETRIEVING_EXPENSES_ANALYSES_FOR = "Problem occured while retrieving expenses analyses: [{0}]";
	private static final String MESSAGE_PROBLEM_OCCURED_WHILE_RETRIEVING_EXPENSES_ANALYSES = "Problem occured while retrieving expenses analyses.";
	private static final String ERROR_PROBLEM_OCCURED_WHILE_RETRIEVING_LATEST_JOB_STATUS = "Problem occured while retrieving latest job status.";
	private static final String MESSAGE_PROBLEM_OCCURED_WHILE_RETRIEVING_JOB_STATUS = "Problem occured while retrieving expenses job status.";

	private static final String PARAMETER_FROM_DATE = "fromDate";
	private static final String PARAMETER_TO_DATE = "toDate";

	/**
	 * Finds all expense analyses in a given period.
	 * 
	 * @param fromDate
	 *            the start date of the period.
	 * @param toDate
	 *            the end date of the period.
	 * @return list of all expenses analyses.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long fromDate = Long.parseLong(request.getParameter(PARAMETER_FROM_DATE));
		Long toDate = Long.parseLong(request.getParameter(PARAMETER_TO_DATE));

		ExpensesAnalysesDto result = new ExpensesAnalysesDto();
		addExpensesAnalyses(fromDate, toDate, result);
		addLastJobExecutionMessage(result);

		response.setContentType(HttpUtil.MIME_TYPE_APPLICATION_JSON);
		response.getWriter().println(new Gson().toJson(result));
	}

	private void addExpensesAnalyses(Long fromDate, Long toDate, ExpensesAnalysesDto expensesAnalysesDto) {
		try {
			ExpenseAnalysesDao expenseAnalysesDao = new ExpenseAnalysesDao();
			List<ExpensesAnalysisDto> expensesAnalyses = expenseAnalysesDao.findExpenseReportsAnalyses(fromDate,
					toDate);
			expensesAnalysesDto.setExpensesAnalyses(expensesAnalyses);
		} catch (SQLException e) {
			logger.error(
					MessageFormat.format(ERROR_PROBLEM_OCCURED_WHILE_RETRIEVING_EXPENSES_ANALYSES_FOR, e.getMessage()),
					e);
			expensesAnalysesDto.appendMessage(MESSAGE_PROBLEM_OCCURED_WHILE_RETRIEVING_EXPENSES_ANALYSES);
		}
	}

	private void addLastJobExecutionMessage(ExpensesAnalysesDto expensesAnalysesDto) {
		try {
			ExpenseJobDao expenseJobDao = new ExpenseJobDao();
			JobExecutionDto jobExecution = expenseJobDao.retrieveLatestJobExecution();
			expensesAnalysesDto.appendMessage(jobExecution.getMessage());
		} catch (SQLException e) {
			logger.error(ERROR_PROBLEM_OCCURED_WHILE_RETRIEVING_LATEST_JOB_STATUS, e);
			expensesAnalysesDto.appendMessage(MESSAGE_PROBLEM_OCCURED_WHILE_RETRIEVING_JOB_STATUS);
		}
	}
}

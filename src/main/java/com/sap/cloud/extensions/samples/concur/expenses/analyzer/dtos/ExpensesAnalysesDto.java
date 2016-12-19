package com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos;

import java.util.List;

/**
 * DTO for expenses analyses. Contains a list with expenses analyses and a
 * message related to them.
 *
 */
public class ExpensesAnalysesDto {

	private List<ExpensesAnalysisDto> expensesAnalyses;
	private StringBuilder message = new StringBuilder();

	/**
	 * Returns list of all expenses analyses.
	 * 
	 * @return list of all expenses anlyses.
	 */
	public List<ExpensesAnalysisDto> getExpensesAnalyses() {
		return expensesAnalyses;
	}

	/**
	 * Setter for all expenses analyses.
	 * 
	 * @param expensesAnalyses
	 *            the expenses analyses.
	 */
	public void setExpensesAnalyses(List<ExpensesAnalysisDto> expensesAnalyses) {
		this.expensesAnalyses = expensesAnalyses;
	}

	/**
	 * Returns the message.
	 * 
	 * @return the message.
	 */
	public String getMessage() {
		return message.toString();
	}

	/**
	 * Appends a message on a new line.
	 * 
	 * @param message
	 *            the message to be appended.
	 */
	public void appendMessage(String message) {
		if (message != null && !message.isEmpty()) {
			this.message.append(this.message.length() > 0 ? "\n" + message : message);
		}
	}
}

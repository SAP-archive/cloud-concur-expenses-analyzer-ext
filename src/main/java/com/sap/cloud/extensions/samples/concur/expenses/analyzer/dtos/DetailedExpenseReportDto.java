package com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * DTO for detailed expense report.
 * 
 * @see <a href=
 *      "https://developer.concur.com/api-reference-deprecated/version-two/expense-reports/expense-report-get.html#reportdetails-elements">Report
 *      Details Schema</a>
 *
 */
public class DetailedExpenseReportDto {

	@SerializedName("ReportID")
	private String reportID;

	@SerializedName("ExpenseEntriesList")
	private List<ExpenseEntryDto> expenseEntriesList;

	/**
	 * Returns the detailed expense report ID.
	 * 
	 * @return the detailed expense report ID.
	 */
	public String getReportID() {
		return reportID;
	}

	/**
	 * Setter for the detailed expense report ID.
	 * 
	 * @param reportID
	 *            ID of the detailed expense report.
	 */
	public void setReportID(String reportID) {
		this.reportID = reportID;
	}

	/**
	 * Returns all detailed expense report's expense entries.
	 * 
	 * @return list with all detailed expense report's expense entries.
	 */
	public List<ExpenseEntryDto> getExpenseEntriesList() {
		return expenseEntriesList;
	}

	/**
	 * Setter for the detailed expense report's expense entries.
	 * 
	 * @param expenseEntriesList
	 *            list with expense entries.
	 */
	public void setExpenseEntriesList(List<ExpenseEntryDto> expenseEntriesList) {
		this.expenseEntriesList = expenseEntriesList;
	}

}

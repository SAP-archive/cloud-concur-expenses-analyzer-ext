package com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos;

import com.google.gson.annotations.SerializedName;

/**
 * DTO for expense report.
 *
 * @see <a href=
 *      "https://developer.concur.com/api-reference/expense/expense-report/reports.html#report">Report
 *      Schema</a>
 */
public class ExpenseReportDto {

	@SerializedName("ID")
	private String id;

	/**
	 * Returns the expense report ID.
	 * 
	 * @return the expense report ID.
	 */
	public String getID() {
		return id;
	}

	/**
	 * Setter for the expense report ID.
	 * 
	 * @param id
	 *            the expense report ID.
	 */
	public void setID(String id) {
		this.id = id;
	}
}

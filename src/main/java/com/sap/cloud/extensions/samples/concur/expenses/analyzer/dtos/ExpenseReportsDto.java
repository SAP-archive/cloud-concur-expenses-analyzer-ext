package com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * DTO for expense reports.
 *
 * @see <a
 *      href="https://developer.concur.com/api-reference/expense/expense-report/reports.html#reports-1">Reports
 *      Schema</a>
 */
public class ExpenseReportsDto {

	@SerializedName("Items")
	private List<ExpenseReportDto> items;

	/**
	 * Returns expense report items.
	 * 
	 * @return the expense report items.
	 */
	public List<ExpenseReportDto> getItems() {
		return items;
	}

	/**
	 * Setter for the expense report items.
	 * 
	 * @param items
	 *            expense report items.
	 */
	public void setItems(List<ExpenseReportDto> items) {
		this.items = items;
	}
}

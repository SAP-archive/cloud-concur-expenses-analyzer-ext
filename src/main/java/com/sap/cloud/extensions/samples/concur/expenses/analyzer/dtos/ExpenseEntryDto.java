package com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * DTO for expense entry.
 * 
 * @see <a href=
 *      "https://developer.concur.com/api-reference-deprecated/version-two/expense-reports/expense-report-get.html#expenseentry-elements">
 *      Expense Entry Schema</a>
 *
 */
public class ExpenseEntryDto {

	@SerializedName("ReportEntryID")
	private String reportEntryID;

	@SerializedName("ExpenseTypeName")
	private String expenseTypeName;

	@SerializedName("TransactionDate")
	private Date transactionDate;

	@SerializedName("ApprovedAmount")
	private Double approvedAmount;

	@SerializedName("TransactionCurrencyName")
	private String transactionCurrencyName;

	/**
	 * Returns the report entry ID.
	 * 
	 * @return the report entry ID.
	 */
	public String getReportEntryID() {
		return reportEntryID;
	}

	/**
	 * Setter for the report entry ID.
	 * 
	 * @param reportEntryID
	 *            report entry ID.
	 */
	public void setReportEntryID(String reportEntryID) {
		this.reportEntryID = reportEntryID;
	}

	/**
	 * Returns the expense type name.
	 * 
	 * @return the expense type name.
	 */
	public String getExpenseTypeName() {
		return expenseTypeName;
	}

	/**
	 * Setter for the expense entry type name.
	 * 
	 * @param expenseTypeName
	 *            the expense entry type name.
	 */
	public void setExpenseTypeName(String expenseTypeName) {
		this.expenseTypeName = expenseTypeName;
	}

	/**
	 * Returns the expense entry transaction date.
	 * 
	 * @return the expense entry transaction date.
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}

	/**
	 * Setter for the expense entry transaction date.
	 * 
	 * @param transactionDate
	 *            expense entry transaction date.
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * Returns the expense entry approved amount.
	 * 
	 * @return the expense entry approved amount.
	 */
	public Double getApprovedAmount() {
		return approvedAmount;
	}

	/**
	 * Setter for the expense entry approved amount.
	 * 
	 * @param approvedAmount
	 *            expense entry approved amount.
	 */
	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	/**
	 * Returns the transaction currency name.
	 * 
	 * @return the transaction currency name.
	 */
	public String getTransactionCurrencyName() {
		return transactionCurrencyName;
	}

	/**
	 * Setter for the transaction currency name.
	 * 
	 * @param transactionCurrencyName
	 *            transaction currency name.
	 */
	public void setTransactionCurrencyName(String transactionCurrencyName) {
		this.transactionCurrencyName = transactionCurrencyName;
	}
}

package com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos;

import java.util.Date;

/**
 * DTO for expenses analysis.
 *
 */
public class ExpensesAnalysisDto {

	private Date transactionDate;
	private Double totalAmount;
	private String transactionCurrency;
	private String expenseType;

	/**
	 * Returns the transaction date.
	 * 
	 * @return the transaction date.
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}

	/**
	 * Setter for the transaction date.
	 * 
	 * @param transactionDate
	 *            transaction date.
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * Returns the total transaction amount.
	 * 
	 * @return the total transaction amount.
	 */
	public Double getTotalAmount() {
		return totalAmount;
	}

	/**
	 * Setter for the total transaction amount.
	 * 
	 * @param totalAmount
	 *            total transaction amount.
	 */
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * Returns the transaction currency.
	 * 
	 * @return the transaction currency.
	 */
	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	/**
	 * Setter for the transaction currency.
	 * 
	 * @param transactionCurrency
	 *            transaction currency.
	 */
	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	/**
	 * Returns the expense type.
	 * 
	 * @return the expense type.
	 */
	public String getExpenseType() {
		return expenseType;
	}

	/**
	 * Setter for the expense type.
	 * 
	 * @param expenseType
	 *            expense type.
	 */
	public void setExpenseType(String expenseType) {
		this.expenseType = expenseType;
	}
}

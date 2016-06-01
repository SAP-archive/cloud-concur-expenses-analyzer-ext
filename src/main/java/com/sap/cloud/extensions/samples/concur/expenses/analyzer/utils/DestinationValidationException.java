package com.sap.cloud.extensions.samples.concur.expenses.analyzer.utils;

/**
 * Exception for destination validation.
 *
 */
public class DestinationValidationException extends Exception {

	private static final long serialVersionUID = -611561142516744194L;

	public DestinationValidationException(String message) {
		super(message);
	}

	public DestinationValidationException(String message, Exception e) {
		super(message, e);
	}
}

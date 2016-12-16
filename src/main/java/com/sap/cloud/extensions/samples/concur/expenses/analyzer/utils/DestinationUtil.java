package com.sap.cloud.extensions.samples.concur.expenses.analyzer.utils;

import java.text.MessageFormat;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

/**
 * Destination util class.
 *
 */
public class DestinationUtil {

	private static final Logger logger = LoggerFactory.getLogger(DestinationUtil.class);

	private static final String ERROR_PROBLEM_OCCURED_WHILE_INITIALIZING_CONTEXT = "Problem occured while initializing context: {0}";
	private static final String ERROR_DESTINATION_IS_NOT_FOUND_HINT_MAKE_SURE_TO_HAVE_THE_DESTINATION_CONFIGURED = "Destination {0} is not found. Hint: Make sure to have the destination configured.";
	private static final String ERROR_WHILE_GETTING_DESTINATION_PROPERTIES_CONNECTIVITY_CONFIGURATION_IS_NULL = "Problem occured while getting destination properties: connectivity configuration is null.";

	private static final String JAVA_COMP_ENV_CONNECTIVITY_CONFIGURATION = "java:comp/env/connectivityConfiguration";

	private static ConnectivityConfiguration connectivityConfiguration;

	static {
		try {
			Context context = new InitialContext();
			DestinationUtil.connectivityConfiguration = (ConnectivityConfiguration) context
					.lookup(JAVA_COMP_ENV_CONNECTIVITY_CONFIGURATION);
		} catch (NamingException e) {
			String errorMessage = MessageFormat.format(ERROR_PROBLEM_OCCURED_WHILE_INITIALIZING_CONTEXT,
					e.getMessage());
			logger.error(errorMessage, e);
			throw new RuntimeException(errorMessage, e);
		}
	}

	/**
	 * Retrieves the destination with the given name and returns all destination
	 * properties.
	 * 
	 * @param destinationName
	 * @return map of all destination properties.
	 * @throws DestinationValidationException
	 *             when no configurations are found or destination with the
	 *             given name is not found.
	 */
	public static Map<String, String> retrieveDestinationProperties(String destinationName)
			throws DestinationValidationException {
		if (connectivityConfiguration == null) {
			logger.error(ERROR_WHILE_GETTING_DESTINATION_PROPERTIES_CONNECTIVITY_CONFIGURATION_IS_NULL);
			throw new RuntimeException(ERROR_WHILE_GETTING_DESTINATION_PROPERTIES_CONNECTIVITY_CONFIGURATION_IS_NULL);
		}

		DestinationConfiguration destinationConfiguration = connectivityConfiguration.getConfiguration(destinationName);
		if (destinationConfiguration == null) {
			String errorMessage = MessageFormat.format(
					ERROR_DESTINATION_IS_NOT_FOUND_HINT_MAKE_SURE_TO_HAVE_THE_DESTINATION_CONFIGURED, destinationName);
			logger.error(errorMessage);
			throw new DestinationValidationException(errorMessage);
		}
		return destinationConfiguration.getAllProperties();
	}
}

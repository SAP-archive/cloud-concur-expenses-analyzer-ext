package com.sap.cloud.extensions.samples.concur.expenses.analyzer.facades;

import java.text.MessageFormat;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.security.um.user.UserProvider;

/**
 * Facade class for user provider.
 *
 */
public class UserProviderFacade {

	private static final String JAVA_COMP_ENV_USER_PROVIDER = "java:comp/env/UserProvider";

	private static final String ERROR_PROBLEM_OCCURED_WHILE_INITIALIZING_USER_PROVIDER = "Problem occured while initializing user provider provider: {0}";

	private static final String DEBUG_INITIALIZING_USER_PROVIDER = "Initializing user provider...";
	private static final String DEBUG_INITIALIZED_USER_PROVIDER = "User provider initialized.";

	private static final Logger logger = LoggerFactory.getLogger(UserProviderFacade.class);

	private static UserProvider userProvider;

	/**
	 * Creates (if not already created) an {@link UserProvider} singleton
	 * instance and returns it.
	 * 
	 * @return the user provider instance.
	 */
	public static UserProvider getUserProvider() {
		if (UserProviderFacade.userProvider == null) {
			try {
				initUserProvider();
			} catch (NamingException e) {
				String errorMessage = MessageFormat.format(ERROR_PROBLEM_OCCURED_WHILE_INITIALIZING_USER_PROVIDER,
						e.getMessage());
				logger.error(errorMessage, e);
				throw new RuntimeException(errorMessage, e);
			}
		}
		return UserProviderFacade.userProvider;
	}

	private static void initUserProvider() throws NamingException {
		logger.debug(DEBUG_INITIALIZING_USER_PROVIDER);

		InitialContext ctx = new InitialContext();
		UserProviderFacade.userProvider = (UserProvider) ctx.lookup(JAVA_COMP_ENV_USER_PROVIDER);

		logger.debug(DEBUG_INITIALIZED_USER_PROVIDER);
	}

}

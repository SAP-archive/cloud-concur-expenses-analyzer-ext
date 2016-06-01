package com.sap.cloud.extensions.samples.concur.expenses.analyzer.facades;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A facade class for the persistence.
 *
 */
public class PersistenceFacade {

	private static final String JAVA_COMP_ENV_JDBC_DEFAULT_DB = "java:comp/env/jdbc/DefaultDB";

	private static final String ERROR_PROBLEM_OCCURED_WHILE_INITIALIZING_DATA_SOURCE = "Problem occured while initializing data source: {0}";

	private static final String DEBUG_INITIALIZING_DATA_SOURCE = "Initializing data source...";
	private static final String DEBUG_INITIALIZED_DATA_SOURCE = "Data source initialized.";

	private static final Logger logger = LoggerFactory
			.getLogger(PersistenceFacade.class);

	private static DataSource dataSource;

	/**
	 * Returns SQL connection.
	 * 
	 * @return the SQL connection.
	 * @throws SQLException
	 *             when the connection cannot be created.
	 */
	public static Connection getConnection() throws SQLException {
		return PersistenceFacade.getDataSource().getConnection();
	}

	private static DataSource getDataSource() {
		if (PersistenceFacade.dataSource == null) {
			try {
				initDataSource();
			} catch (NamingException e) {
				String errorMessage = MessageFormat.format(
						ERROR_PROBLEM_OCCURED_WHILE_INITIALIZING_DATA_SOURCE,
						e.getMessage());
				logger.error(errorMessage, e);
				throw new RuntimeException(errorMessage, e);
			}
		}

		return PersistenceFacade.dataSource;
	}

	private static void initDataSource() throws NamingException {
		logger.debug(DEBUG_INITIALIZING_DATA_SOURCE);

		InitialContext ctx = new InitialContext();
		PersistenceFacade.dataSource = (DataSource) ctx
				.lookup(JAVA_COMP_ENV_JDBC_DEFAULT_DB);

		logger.debug(DEBUG_INITIALIZED_DATA_SOURCE);
	}
}

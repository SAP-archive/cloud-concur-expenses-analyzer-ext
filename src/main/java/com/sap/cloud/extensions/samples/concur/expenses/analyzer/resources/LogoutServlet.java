package com.sap.cloud.extensions.samples.concur.expenses.analyzer.resources;

import java.io.IOException;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.security.auth.login.LoginContextFactory;

/**
 * Logs out the current user.
 *
 */
@WebServlet("/rest/logout")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 5388208376934914810L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getRemoteUser() != null) {
			try {
				LoginContext loginContext = LoginContextFactory
						.createLoginContext();
				loginContext.logout();
			} catch (LoginException e) {
				throw new ServletException(e);
			}
		}
	}
}
package com.sap.cloud.extensions.samples.concur.expenses.analyzer.resources;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.sap.cloud.extensions.samples.concur.expenses.analyzer.facades.UserProviderFacade;
import com.sap.security.um.user.PersistenceException;
import com.sap.security.um.user.UnsupportedUserAttributeException;
import com.sap.security.um.user.User;

/**
 * Returns information about the current user.
 *
 */
@WebServlet("/rest/user")
public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = -6498049206361806831L;

	private static final String PROPERTY_NAME = "name";
	private static final String FIRSTNAME_ATTRIBUTE = "firstname";
	private static final String LASTNAME_ATTRIBUTE = "lastname";

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getUserPrincipal() != null) {
			try {
				User user = UserProviderFacade.getUserProvider().getUser(
						request.getUserPrincipal().getName());
				String userName = user.getAttribute(FIRSTNAME_ATTRIBUTE) + " "
						+ user.getAttribute(LASTNAME_ATTRIBUTE);
				
				JsonObject nameJson = new JsonObject();
				nameJson.addProperty(PROPERTY_NAME, userName);
				
				response.getWriter().println(
						nameJson.toString());
			} catch (PersistenceException | UnsupportedUserAttributeException e) {
				throw new ServletException(e);
			}
		}
	}
}

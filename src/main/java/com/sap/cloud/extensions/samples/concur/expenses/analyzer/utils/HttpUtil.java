package com.sap.cloud.extensions.samples.concur.expenses.analyzer.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * HTTP util class.
 *
 */
public class HttpUtil {

	public static final String HTTP_HEADER_AUTHORIZATION = "Authorization";
	public static final String HTTP_HEADER_X_CONSUMER_KEY = "X-ConsumerKey";
	public static final String HTTP_HEADER_ACCEPT = "Accept";

	public static final String MIME_TYPE_APPLICATION_JSON = "application/json";

	public static final String AUTHENTICATION_BASIC = "basic";
	public static final String AUTHENTICATION_OAUTH = "OAuth";

	/**
	 * Creates and returns authorization field for Basic authentication.
	 * 
	 * Combines the given username and password with a single colon and then
	 * encodes the result using Base64.
	 * 
	 * @param user
	 * @param password
	 * @return Combined username and password with a single colon, encoded using
	 *         Base64.
	 */
	public static String encodeBase64(String user, String password) {
		return new String(Base64.encodeBase64((user + ":" + password).getBytes()));
	}

	/**
	 * Returns OAuth header with the given OAuth token.
	 * 
	 * @param token
	 *            OAuth token
	 * @return OAuth header with the given OAuth token.
	 */
	public static String getOAuthHeader(String token) {
		return AUTHENTICATION_OAUTH + " " + token;
	}
}

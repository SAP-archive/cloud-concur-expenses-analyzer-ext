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

	private static final String EMPTY_SPACE = " ";
	private static final String COLON = ":";

	public static String encodeBase64(String user, String password) {
		return new String(Base64.encodeBase64((user + COLON + password)
				.getBytes()));
	}

	public static String getOAuthHeader(String token) {
		return AUTHENTICATION_OAUTH + EMPTY_SPACE + token;
	}
}

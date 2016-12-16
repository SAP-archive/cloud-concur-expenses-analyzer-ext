package com.sap.cloud.extensions.samples.concur.expenses.analyzer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * Util class for calling Concur API endpoints.
 *
 */
public class ConcurAPIsUtil {

	private static final String API_DESTINATION = "concur-api";
	private static final String API_DESTINATION_URL = "URL";
	private static final String API_DESTINATION_PROXY_TYPE = "ProxyType";
	private static final String API_DESTINATION_ACCESS_TOKEN = "AccessToken";

	private static final String ERROR_DESTINATION_DOES_NOT_CONTAIN_PROPERTY = "Destination does not contain property [{0}].";
	private static final String DEBUG_CALLING_CONCUR_API_AT = "Calling Concur API at [{}]...";
	private static final String DEBUG_CALLED_CONCUR_API_AT = "Called Concur API at [{}].";
	private static final String DEBUG_OBTAINING_CONNECTION_TO_WITH_PROXY = "Obtaining connection to [{}] with proxy [{}]...";
	private static final String DEBUG_OBTAINED_CONNECTION_TO_WITH_PROXY = "Obtained connection to [{}] with proxy [{}].";

	private static final Logger logger = LoggerFactory.getLogger(ConcurAPIsUtil.class);

	/**
	 * Calls the Concur API endpoint defined by the given path. Requires the
	 * existence of "concur-api" HTTP destination on account level in the
	 * account. The "concur-api" destination must contain the following
	 * properties: 1) URL : the url of the Concur instance e.g.
	 * "https://www.concursolutions.com/". 2) AccessToken : an OAuth access
	 * token for the Concur company. 3) ProxyType : the proxy type e.g.
	 * "Internet".
	 * 
	 * @param path
	 *            the path that defines the endpoint to be called.
	 * @param cls
	 *            the class of the result entity.
	 * @param gson
	 *            gson object to be used for the parsing.
	 * @return the result of the API call.
	 * @throws IOException
	 *             if a problem occurs with the API call.
	 * @throws DestinationValidationException
	 *             if there is a problem with the Concur destination: the
	 *             destination cannot be found or the destination does not
	 *             contain one of the required properties:
	 */
	public static <T> T callConcurApi(String path, Class<T> cls, Gson gson)
			throws DestinationValidationException, IOException {
		String apiCallResult = ConcurAPIsUtil.callConcurApi(path);
		return gson.fromJson(apiCallResult, cls);
	}

	/**
	 * Calls the Concur API endpoint defined by the given path. Requires the
	 * existence of "concur-api" HTTP destination on account level in the
	 * account. The "concur-api" destination must contain the following
	 * properties: 1) URL : the url of the Concur instance e.g.
	 * "https://www.concursolutions.com/". 2) AccessToken : an OAuth access
	 * token for the Concur company. 3) ProxyType : the proxy type e.g.
	 * "Internet".
	 * 
	 * 
	 * @param path
	 *            the path that defines the endpoint to be called.
	 * @return String representation of the API call result.
	 * @throws IOException
	 *             if a problem occurs with the API call.
	 * @throws DestinationValidationException
	 *             if there is a problem with the Concur destination: the
	 *             destination cannot be found or the destination does not
	 *             contain one of the required properties:
	 */
	public static String callConcurApi(String path) throws IOException, DestinationValidationException {
		logger.debug(DEBUG_CALLING_CONCUR_API_AT, path);

		Map<String, String> destinationProperties = DestinationUtil.retrieveDestinationProperties(API_DESTINATION);
		validateDestinationProperties(destinationProperties, API_DESTINATION_URL, API_DESTINATION_PROXY_TYPE,
				API_DESTINATION_ACCESS_TOKEN);

		String concurApiUrl = removeTrailingSlash(destinationProperties.get(API_DESTINATION_URL)) + path;
		String proxyType = destinationProperties.get(API_DESTINATION_PROXY_TYPE);
		String accessToken = destinationProperties.get(API_DESTINATION_ACCESS_TOKEN);

		String result = readUrlConnection(createConnection(concurApiUrl, proxyType, accessToken));

		logger.debug(DEBUG_CALLED_CONCUR_API_AT, path);
		return result;
	}

	private static void validateDestinationProperties(Map<String, String> destinationProperties, String... properties)
			throws DestinationValidationException {
		for (String propery : properties) {
			if (!destinationProperties.containsKey(propery)) {
				String errorMessage = MessageFormat.format(ERROR_DESTINATION_DOES_NOT_CONTAIN_PROPERTY, propery);
				logger.error(errorMessage);
				throw new DestinationValidationException(errorMessage);
			}
		}
	}

	private static String readUrlConnection(HttpURLConnection connection) throws IOException {
		InputStream connectionInputStream = connection.getInputStream();
		String result = IOUtils.toString(connectionInputStream);
		IOUtils.closeQuietly(connectionInputStream);

		return result;
	}

	private static HttpURLConnection createConnection(String url, String proxyType, String oAuthToken)
			throws IOException {
		logger.debug(DEBUG_OBTAINING_CONNECTION_TO_WITH_PROXY, url, proxyType);
		URL url_ = new URL(url);
		Proxy proxy = ProxyUtil.createProxy(proxyType);
		HttpURLConnection concurConnection = (HttpURLConnection) url_.openConnection(proxy);

		concurConnection.setRequestProperty(HttpUtil.HTTP_HEADER_ACCEPT, HttpUtil.MIME_TYPE_APPLICATION_JSON);

		// set authorization header
		concurConnection.setRequestProperty(HttpUtil.HTTP_HEADER_AUTHORIZATION, HttpUtil.getOAuthHeader(oAuthToken));

		logger.debug(DEBUG_OBTAINED_CONNECTION_TO_WITH_PROXY, url, proxyType);
		return concurConnection;
	}

	private static String removeTrailingSlash(String s) {
		if (s.endsWith("/")) {
			s = s.substring(0, s.length() - 1);
		}

		return s;
	}
}
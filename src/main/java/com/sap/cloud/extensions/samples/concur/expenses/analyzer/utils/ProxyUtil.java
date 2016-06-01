package com.sap.cloud.extensions.samples.concur.expenses.analyzer.utils;

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proxy Util class.
 *
 */
public class ProxyUtil {

	private static final String ON_PREMISE_PROXY = "OnPremise";

	private static final String INTERNET_PROXY_HOST = "http.proxyHost";
	private static final String INTERNET_PROXY_PORT = "http.proxyPort";

	private static final String ON_PREMISE_PROXY_HOST = "HC_OP_HTTP_PROXY_HOST";
	private static final String ON_PREMISE_PROXY_PORT = "HC_OP_HTTP_PROXY_PORT";


	private static final String DEBUG_CREATING_PROXY_OF_TYPE = "Creating proxy of type: [{}]";
	private static final String DEBUG_CREATED_PROXY = "Created proxy [{}]:[{}]";

	private static final Logger logger = LoggerFactory
			.getLogger(ProxyUtil.class);

	/**
	 * Creates a proxy.
	 * 
	 * @param proxyType
	 *            the proxy type.
	 * @return the created proxy.
	 */
	public static Proxy getProxy(String proxyType) {
		logger.debug(DEBUG_CREATING_PROXY_OF_TYPE, proxyType);

		String proxyHost;
		int proxyPort;

		if (ON_PREMISE_PROXY.equals(proxyType)) {
			// Get proxy for On-Premise connectivity
			proxyHost = System.getenv(ON_PREMISE_PROXY_HOST);
			proxyPort = Integer.parseInt(System.getenv(ON_PREMISE_PROXY_PORT));
		} else {
			// Get proxy for Internet connectivity
			proxyHost = System.getProperty(INTERNET_PROXY_HOST);
			proxyPort = Integer.parseInt(System
					.getProperty(INTERNET_PROXY_PORT));
		}

		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
				proxyHost, proxyPort));
		logger.debug(DEBUG_CREATED_PROXY, proxyHost, proxyPort);
		return proxy;
	}
}

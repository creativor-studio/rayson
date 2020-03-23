/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.tools.profiler;

import java.net.UnknownHostException;

import org.rayson.api.client.RaysonServerAddress;
import org.rayson.api.http.HttpContentType;
import org.rayson.tools.console.ConsoleParameters;

/**
 *
 * @author creativor
 */
final class ProfilerParameters implements ConsoleParameters {
	private int callCount = 10;
	private boolean debug = false;
	private RaysonServerAddress serverAddress;
	private int threadCount = 5;
	private UrlArgument url;
	private boolean usingSsl = false;
	private boolean verify;
	private HttpContentType contentType = HttpContentType.RSON;

	/**
	 * @return the callCount
	 */
	public int getCallCount() {
		return callCount;
	}

	/**
	 * @return the contentType
	 */
	public HttpContentType getContentType() {
		return contentType;
	}

	/**
	 * @return the serverAddress
	 */
	public RaysonServerAddress getServerAddress() {
		return serverAddress;
	}

	/**
	 * @return the threadCount
	 */
	public int getThreadCount() {
		return threadCount;
	}

	/**
	 * @return the url
	 */
	public UrlArgument getUrl() {
		return url;
	}

	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @return the usingSsl
	 */
	public boolean isUsingSsl() {
		return usingSsl;
	}

	/**
	 * @return the verify
	 */
	public boolean isVerify() {
		return verify;
	}

	/**
	 * @param callCount the callCount to set
	 */
	void setCallCount(int callCount) {
		this.callCount = callCount;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {

		if (contentType != null && !contentType.isEmpty()) {
			HttpContentType ct = HttpContentType.typeOf(contentType);
			this.contentType = ct;
		}
	}

	/**
	 * 
	 */
	void setDebug() {
		debug = true;
	}

	void setServerAddress(RaysonServerAddress serverAddress) {
		this.serverAddress = serverAddress;
	}

	/**
	 * @param threadCount the threadCount to set
	 */
	void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	/**
	 * @param url the url to set
	 * @throws UnknownHostException
	 * @throws IllegalArgumentException
	 */
	public void setUrl(UrlArgument url) throws IllegalArgumentException, UnknownHostException {
		this.url = url;
		this.serverAddress = new RaysonServerAddress(url.getServerIp(), url.getPort(), usingSsl);
	}

	void setUsingSsl() {
		usingSsl = true;
	}

	public void setVerify() {
		this.verify = true;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		Object[] values = { threadCount, callCount, contentType, serverAddress, debug, usingSsl };
		String[] keys = { "threadCount", "callCount", "contentType", "serverAddress", "debug", "usingSsl" };
		int keyLen = keys.length;
		sb.append("{");
		for (int i = 0; i < keyLen; i++) {
			sb.append(keys[i]);
			sb.append(":");
			sb.append(values[i]);
			if (i != keyLen - 1)
				sb.append(", ");
		}
		sb.append("}");
		return sb.toString();
	}
}
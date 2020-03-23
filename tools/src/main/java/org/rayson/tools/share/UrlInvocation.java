/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.tools.share;

import java.util.Map;

import javax.json.JsonException;

import org.rayson.api.annotation.NotNull;
import org.rayson.api.http.HttpMessage;
import org.rayson.api.struct.KeyValue;
import org.rayson.share.exception.SerializationException;
import org.rayson.share.serial.FormContentCoder;

/**
 * An tool used to invoke Rayson RPC with full url path stands for the giving
 * service.
 * 
 * @author Nick Zhang
 *
 */
class UrlInvocation {
	/**
	 * 
	 * @param url Url string. Just look like :
	 *            "ip:port/service_full_name/method?param1=value1&param2=value2"
	 * @return
	 * @throws SerializationException If parse Rayson protocol information got
	 *             error.
	 */
	public static UrlInvocation parse(@NotNull String url) throws JsonException, SerializationException {
		if (url == null)
			throw new IllegalArgumentException("url should not be null");
		UrlInvocation invocation = new UrlInvocation();

		// 1. Parse parameters.
		int index = 0;
		int nextIndex = 0;
		nextIndex = url.indexOf(":");
		if (nextIndex == -1)
			throw new JsonException("no ip part in url");
		invocation.ip = url.substring(index, nextIndex);
		index = nextIndex;
		nextIndex = url.indexOf("/", index + 1);
		if (nextIndex == -1)
			throw new JsonException("no port part in url");

		invocation.port = url.substring(index + 1, nextIndex);
		index = nextIndex;
		// FIMXE: Make it support service name with domain name.
		nextIndex = url.indexOf("/", index + 1);
		if (nextIndex == -1)
			throw new JsonException("no service full name part in url");

		invocation.serviceName = url.substring(index + 1, nextIndex);
		index = nextIndex;

		nextIndex = url.indexOf("?", index + 1);
		if (nextIndex == -1)
			throw new JsonException("no method part in url");

		invocation.methodName = url.substring(index + 1, nextIndex);
		index = nextIndex;
		String parametersData = url.substring(index + 1);
		invocation.paramters = FormContentCoder.parseKeyValues(parametersData.getBytes(), HttpMessage.CHARSET);
		return invocation;
	}

	private String ip;
	private String port;
	private String serviceName;
	private String methodName;

	private Map<String, KeyValue> paramters;

	private UrlInvocation() {
		// Forbidden.
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @return the paramters
	 */
	public Map<String, KeyValue> getParamters() {
		return paramters;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	public String toString() {
		final StringBuffer sb = new StringBuffer();
		Object[] values = { ip, port, serviceName, methodName };
		String[] keys = { "ip", "port", "serviceName", "methodName" };
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
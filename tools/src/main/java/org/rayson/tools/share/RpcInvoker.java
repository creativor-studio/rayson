/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.tools.share;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;

import javax.json.JsonException;

import org.rayson.api.Protocol;
import org.rayson.api.client.RaysonServerAddress;
import org.rayson.api.exception.InvalidApiException;
import org.rayson.api.protocol.mirror.MethodMirror;
import org.rayson.api.protocol.mirror.ProtocolMirror;
import org.rayson.api.struct.RpcParameters;
import org.rayson.client.Rayson;
import org.rayson.share.exception.SerializationException;
import org.rayson.share.serial.rson.RsonUtil;
import org.rayson.share.util.MirrorTool;

/**
 * An tool class used to invoke Rayson RPC in JASON format.<br>
 * FIXME: Improve it.
 * 
 * @author creativor
 */
public class RpcInvoker {
	private UrlInvocation invocation;
	private final MethodMirror methodMirror;
	private final RpcParameters parameters;
	private final ProtocolMirror protocolMetaData;
	private final RaysonServerAddress serverAddr;
	final Method method;
	final Protocol proxy;

	/**
	 * Construct one new RPC invoker. All the RPC associated types which need by
	 * invoking a RPC - such as parameters , proxy and java method - will be
	 * loaded.
	 * 
	 * @param url Url string. Just look like :
	 *            "ip:port/service_full_name/method?param1=value1&param2=value2"
	 *            .
	 * 
	 * @throws JsonException If the url format is wrong.
	 * @throws SerializationException If parse Rayson protocol information got
	 *             error.
	 * @throws InvalidApiException
	 * @throws UnknownHostException
	 * @throws IllegalArgumentException
	 */
	public RpcInvoker(final String url) throws JsonException, SerializationException, InvalidApiException, IllegalArgumentException, UnknownHostException {
		// 1. Parse url invocation.

		invocation = UrlInvocation.parse(url);

		// 2. Set server address.
		final int serverPort = Integer.valueOf(invocation.getPort());
		serverAddr = new RaysonServerAddress(invocation.getIp(), serverPort);

		// 3. Parser the protocol meta data.
		final Class<? extends Protocol> protocol;
		try {
			protocol = (Class<? extends Protocol>) Class.forName(invocation.getServiceName(), false, this.getClass().getClassLoader());
		} catch (final Exception e) {
			throw new SerializationException("Load Rayson protocol error: " + e.getMessage());
		}

		if (!protocol.isInterface())
			throw new SerializationException("Rayson protocol " + protocol + " should be interface");

		protocolMetaData = MirrorTool.load(protocol);

		// 4. Parse method and it's arguments.

		String methodName = invocation.getMethodName();
		methodMirror = protocolMetaData.getMethod(methodName);
		if (methodMirror == null)
			throw new SerializationException("Method " + methodName + " not found in meta data");

		parameters = RsonUtil.paserRpcRequest(invocation.getParamters(), methodMirror);

		// Then initialize reflection method.

		proxy = getProxy();
		try {
			method = proxy.getClass().getMethod(methodMirror.getName(), methodMirror.getSource().getParameterTypes());
		} catch (final Exception e) {
			throw new SerializationException("Find method " + methodMirror.getName() + " error: " + e.getMessage());
		}
	}

	/**
	 * @return
	 * @throws IllegalArgumentException
	 * @throws InvalidApiException
	 */
	private Protocol getProxy() throws IllegalArgumentException, InvalidApiException {
		return Rayson.createProxy(serverAddr, protocolMetaData.getSource());
	}

	/**
	 * Invoke Rayson RPC one time.
	 * 
	 * @return Rpc invoking result.
	 * @throws IllegalArgumentException
	 * @throws SerializationException
	 */
	public Object invoke() throws SerializationException, InvocationTargetException {
		try {
			return method.invoke(proxy, parameters.toArguments());
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		final Object[] values = { invocation };
		final String[] keys = { "invocation" };
		final int keyLen = keys.length;
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
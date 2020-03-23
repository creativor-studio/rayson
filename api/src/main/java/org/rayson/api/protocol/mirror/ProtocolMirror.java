/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.protocol.mirror;

import java.util.Collections;
import java.util.Map;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.NotNull;
import org.rayson.api.protocol.info.ProtocolInfo;

/**
 * Mirror of {@link Protocol}.
 * 
 * @author Nick Zhang
 *
 */
public class ProtocolMirror implements TypeMirror<ProtocolInfo, Class<? extends Protocol>> {

	private ProtocolInfo info;

	private Map<String, MethodMirror> methods;

	private String name;

	private Class<? extends Protocol> source;

	/**
	 * @param name Name of the new protocol mirror.
	 * @param source Source java interface of the new protocol mirror.
	 * @param methods Method mirror list of the new protocol mirror.
	 * @param info Information of the new protocol mirror.
	 */
	public ProtocolMirror(String name, Class<? extends Protocol> source, Map<String, MethodMirror> methods, ProtocolInfo info) {
		super();
		this.name = name;
		this.source = source;
		this.methods = methods;
		this.info = info;
	}

	@Override
	public ProtocolInfo getInfo() {
		return info;
	}

	/**
	 * Get an {@link MethodMirror} of giving method name.
	 * 
	 * @param methodName Method name.
	 * @return Found method mirror. Or found none, returns <code>null</code>.
	 * @throws IllegalArgumentException If the methodName is <code>null</code>.
	 */
	public MethodMirror getMethod(@NotNull String methodName) {
		if (methodName == null)
			throw new IllegalArgumentException("methodName should not be null");
		return methods.get(methodName);
	}

	/**
	 * @return the methods map.Unmodified.
	 */
	public Map<String, MethodMirror> getMethods() {
		return Collections.unmodifiableMap(methods);
	}

	/**
	 * @return the name of this mirror.
	 */
	public String getName() {
		return name;
	}

	@Override
	public Class<? extends Protocol> getSource() {
		return source;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		final Object[] values = { info, source, methods };
		final String[] keys = { "info", "source", "methods" };
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
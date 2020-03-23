/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.struct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.rayson.api.annotation.NotNull;
import org.rayson.api.protocol.mirror.ParameterMirror;

/**
 * RPC parameters list.
 * 
 * @author Nick Zhang
 *
 */
public class RpcParameters {

	private Map<String, RpcParameter> parameters;

	/**
	 * Construct an default RPC parameters with empty values.
	 */
	public RpcParameters() {
		parameters = new HashMap<>();
	}

	/**
	 * @return Unmodifiable {@link RpcParameter} collection.
	 */
	public Collection<RpcParameter> collection() {
		return Collections.unmodifiableCollection(parameters.values());
	}

	/**
	 * @return Parameters count.
	 */
	public int count() {
		return parameters.size();
	}

	/**
	 * Get parameter of giving name.
	 * 
	 * @param name Parameter name. May with prefix
	 *            {@value ParameterMirror#VARIABLE_NAME_PREFIX} .
	 * @return {@link RpcParameter} matched the giving name. Or else,
	 *         <code>null</code>.
	 * @throws IllegalArgumentException if the name is <code>null</code>.
	 */
	public RpcParameter get(String name) {
		if (name == null)
			throw new IllegalArgumentException("name should not be null");

		RpcParameter p = parameters.get(name);

		if (p == null && name.startsWith(ParameterMirror.VARIABLE_NAME_PREFIX)) {
			try {
				String indexStr = name.substring(1);
				int index = Integer.parseInt(indexStr);
				for (RpcParameter curP : parameters.values()) {
					if (curP.getIndex() == index)
						return curP;
				}
			} catch (Exception e) {
				// Ignore it;
			}
		}

		return p;
	}

	/**
	 * Get parameter of giving name.
	 * 
	 * @param name Parameter name.
	 * @return Matched parameter.
	 * @throws NoSuchElementException If no such parameter found which match the
	 *             given name.
	 */
	public Object getValue(String name) throws NoSuchElementException {
		RpcParameter p = get(name);
		if (p == null)
			throw new NoSuchElementException(name);
		return p.getValue();
	}

	/**
	 * 
	 * @return <code>true</code> if Contains no parameters.
	 */
	public boolean isEmpty() {
		return parameters.isEmpty();
	}

	/**
	 * Put a parameter to this container.
	 * 
	 * @param param New parameter.
	 * @throws IllegalArgumentException If parameter is <code>null</code>.
	 */
	public void put(@NotNull RpcParameter param) throws IllegalArgumentException {
		if (param == null)
			throw new IllegalArgumentException("param should not be null");
		parameters.put(param.getName(), param);
	}

	/**
	 * Convert RPC parameters to arguments list.
	 * 
	 * @return Converted result.
	 */
	public Object[] toArguments() {
		List<RpcParameter> paramList = new ArrayList<RpcParameter>(parameters.values());
		Collections.sort(paramList, (o1, o2) -> {
			return o1.getIndex() - o2.getIndex();
		});

		Object[] result = new Object[paramList.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = paramList.get(i).getValue();
		}

		return result;
	}

	@Override
	public String toString() {
		return parameters.toString();
	}
}
/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.protocol.mirror;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.rayson.api.Protocol;
import org.rayson.api.protocol.info.MethodInfo;

/**
 * Mirror of one method in {@link Protocol}.
 * 
 * @author Nick Zhang
 *
 */
public final class MethodMirror implements TypeMirror<MethodInfo, Method> {

	private static final ParameterMirror[] DUMMY_ARRAY = new ParameterMirror[0];
	private MethodInfo info;
	private String name;

	private List<ParameterMirror> parameters;

	private Method source;

	/**
	 * @param name Name of the new method mirror.
	 * @param source Source java method of the new method mirror.
	 * @param parameters Parameter list of the new method mirror.
	 * @param info Information of the new method mirror.
	 */
	public MethodMirror(String name, Method source, List<ParameterMirror> parameters, MethodInfo info) {
		super();
		this.name = name;
		this.source = source;
		this.parameters = parameters;
		this.info = info;

		// Order the parameter list.
		this.parameters.sort(new Comparator<ParameterMirror>() {

			@Override
			public int compare(ParameterMirror o1, ParameterMirror o2) {
				return o1.getIndex() - o2.getIndex();
			}
		});
	}

	/**
	 * Get cloned parameter list of this method. It is for higher performance
	 * case.
	 * 
	 * @return Cloned parameter list .
	 */
	public ParameterMirror[] cloneParameters() {
		return this.parameters.toArray(DUMMY_ARRAY);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof MethodMirror))
			return false;
		final MethodMirror that = (MethodMirror) obj;
		if (!Objects.equals(this.name, that.name))
			return false;
		return true;
	}

	@Override
	public MethodInfo getInfo() {
		return info;
	}

	/**
	 * @return The associated method name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Parameter count of this method.
	 */
	public int getParameterCount() {
		return parameters.size();
	}

	/**
	 * @return The ordered parameters.Unmodifiable.<br>
	 *         It may be lower performance.
	 */
	public List<ParameterMirror> getParameters() {
		return Collections.unmodifiableList(parameters);
	}

	@Override
	public Method getSource() {
		return source;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}
}
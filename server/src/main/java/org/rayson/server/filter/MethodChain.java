/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.server.filter;

import java.lang.reflect.Method;

class MethodChain {
	private DefaultFilterChain chain;
	private Method method;

	MethodChain(Method method, DefaultFilterChain chain) {
		super();
		this.method = method;
		this.chain = chain;
	}

	@Override
	public String toString() {
		return chain.getProtocol().getName() + "#" + method.getName();
	}

	
	DefaultFilterChain getChain() {
		return chain;
	}

	/**
	 * @return The underling java method.
	 */
	Method getMethod() {
		return method;
	}
}
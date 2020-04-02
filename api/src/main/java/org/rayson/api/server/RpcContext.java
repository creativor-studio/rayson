
/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.server;

import java.util.HashMap;
import java.util.Map;

import org.rayson.api.annotation.ThreadSafe;

/**
 * Thread local RPC context. It is used to access RPC request on current thread
 * context.
 */
@ThreadSafe
public final class RpcContext {
	private static final ThreadLocal<RpcContext> LOCAL_CONTEXT = new ThreadLocal<RpcContext>() {
		@Override
		protected RpcContext initialValue() {
			return null;
		}
	};

	static void clearContext() {
		LOCAL_CONTEXT.remove();
	}

	/**
	 * @return Thread lcoal RPC context. Or <code>null</code> returns if invoke
	 *         this method out of RPC handling context.
	 */
	public static RpcContext getContext() {
		return LOCAL_CONTEXT.get();
	}

	static void setupContext() {
		final RpcContext context = new RpcContext();
		LOCAL_CONTEXT.set(context);
	}

	private ServerRequest request;
	/**
	 * The collection of user data attributes associated with this context.
	 */
	private Map<String, Object> attributes;

	private RpcContext() {
		attributes = new HashMap<>();
	}

	/**
	 * Returns the object bound with the specified name in this context, or
	 * <code>null</code> if no object is bound under the name.
	 *
	 * @param name a string specifying the name of the object.
	 * @return the object with the specified name.
	 * @throws IllegalArgumentException If the name is <code>null</code>.
	 */
	public Object getAttribute(String name) {
		if (name == null)
			throw new IllegalArgumentException("name should not be null");
		return attributes.get(name);
	}

	/**
	 * @return Current request on current thread context. May be
	 *         <code>null</code> if it is not available.
	 */
	public ServerRequest getRequest() {
		return this.request;
	}

	/**
	 * Removes the object bound with the specified name from this context. If
	 * the context does not have an object bound with the specified name, this
	 * method does nothing.
	 *
	 * @param name the name of the object to remove from this context.
	 * @throws IllegalArgumentException If the name is <code>null</code>.
	 * 
	 */
	public void removeAttribute(String name) {
		if (name == null)
			throw new IllegalArgumentException("name should not be null");
		attributes.remove(name);
	}

	/**
	 * Binds an object to this context, using the name specified. If an object
	 * of the same name is already bound to the context, the object is replaced.
	 * 
	 * If the value passed in is null, this has the same effect as calling
	 * <code>removeAttribute()</code>.
	 *
	 * @param name the name to which the object is bound; cannot be null
	 * @param value the object to be bound
	 * @throws IllegalArgumentException If the name is <code>null</code>.
	 */
	public void setAttribute(String name, Object value) {
		if (name == null)
			throw new IllegalArgumentException("name should not be null");
		attributes.put(name, value);
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		Object[] values = { request, attributes };
		String[] keys = { "request", "attributes" };
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
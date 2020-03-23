
package org.rayson.api.server;

/**
 * Thread local RPC context. It is used to access RPC request on current thread
 * context.
 * 
 * @author Nick Zhang
 *
 */
public final class RpcContext {
	private static final ThreadLocal<RpcContext> LOCAL_CONTEXT = new ThreadLocal<RpcContext>() {
		@Override
		protected RpcContext initialValue() {
			return new RpcContext(null);
		}
	};

	static void clearContext() {
		LOCAL_CONTEXT.remove();
	}

	/**
	 * @return Thread lcoal RPC context.
	 */
	public static RpcContext getContext() {
		return LOCAL_CONTEXT.get();
	}

	static void setupContext(final ServerRequest request) {
		final RpcContext context = new RpcContext(request);
		LOCAL_CONTEXT.set(context);
	}

	private final ServerRequest request;

	private RpcContext(final ServerRequest request) {
		this.request = request;
	}

	/**
	 * @return Current request on current thread context.
	 */
	public ServerRequest getRequest() {
		return this.request;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("request: " + this.request);
		return sb.toString();
	}
}
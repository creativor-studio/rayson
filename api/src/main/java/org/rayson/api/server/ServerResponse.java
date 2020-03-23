
package org.rayson.api.server;

import java.util.Collection;

import org.rayson.api.Response;
import org.rayson.api.annotation.NotNull;
import org.rayson.api.exception.RpcException;
import org.rayson.api.struct.KeyValues;

/**
 * RPC response on server side.
 * 
 * @author Nick Zhang
 *
 */
public interface ServerResponse extends Response {

	/**
	 * Add a header to this response.
	 * 
	 * @param name Header name.
	 * @param value Header value.
	 */
	public void addHeader(@NotNull String name, @NotNull String value);

	/**
	 * @return All headers of this response. Unmodifiable.
	 */
	public Collection<KeyValues> getAllHeaders();

	/**
	 * Get the exception occurred when processing the associated RPC request.
	 * 
	 * @return Occurred exception . Or <code>null</code> if no error occurred
	 *         when processing the associated RPC request.
	 */
	public abstract RpcException getException();

	/**
	 * Get the processing result of the associated RPC request.
	 * 
	 * @return Result of the associated RPC request.
	 * @throws RpcException If error occurred when processing the associated RPC
	 *             request.
	 */
	public abstract Object getResult() throws RpcException;

	/**
	 * Setup exception which occurred when processing the associated RPC
	 * request.
	 * 
	 * @param rpcException Error which occurred when processing the associated
	 *            RPC request.
	 */
	public abstract void setException(RpcException rpcException);

	/**
	 * Setup the processing result of the associated RPC request.
	 * 
	 * @param result The processing result of the associated RPC request.
	 */
	public abstract void setResult(Object result);
}

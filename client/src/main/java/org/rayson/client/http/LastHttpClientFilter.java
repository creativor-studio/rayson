/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client.http;

import java.io.IOException;

import org.rayson.api.client.HttpClientFilter;
import org.rayson.api.client.HttpClientRequest;
import org.rayson.api.client.HttpClientResponse;
import org.rayson.api.client.HttpFilterChain;
import org.rayson.api.client.Proxy;
import org.rayson.api.exception.ClientFilterException;
import org.rayson.client.ClientSystem;
import org.rayson.client.rpc.RpcProxy;
import org.rayson.client.transport.ClientConnection;

/**
 * Last {@link HttpClientFilter} of associated filter chain.
 * 
 * @author Nick Zhang
 *
 */
public class LastHttpClientFilter implements HttpClientFilter {
	private ClientConnection connection;
	private RpcProxy proxy;

	/**
	 * @param proxy The RPC proxy of the new filter.
	 */
	public LastHttpClientFilter(RpcProxy proxy) {
		super();
		this.proxy = proxy;
	}

	private ClientConnection fetchNewConn() throws IOException, InterruptedException {
		final ClientConnection conn = ClientSystem.getSystem().getConnectionPool().create(this.proxy.getServerAddr());

		conn.open();

		ClientSystem.getSystem().getConnectionPool().put(conn);
		return conn;
	}

	private synchronized ClientConnection getConnection() throws IOException, InterruptedException {
		if (connection == null) {
			connection = fetchNewConn();
			return connection;
		}
		// try reuse the connection.
		ClientConnection reused = ClientSystem.getSystem().getConnectionPool().reuse(connection);
		if (reused == null) {
			// Just abandon current connection.
			connection = null;
			connection = fetchNewConn();
		}
		return connection;
	}

	@Override
	public void doFilter(HttpClientRequest request, HttpClientResponse response, HttpFilterChain chain) throws ClientFilterException {

		HttpClientResponseImpl respImpl = (HttpClientResponseImpl) response;

		final HttpCallFuture future = new HttpCallFuture();
		final ResponseCallbackImpl callback = new ResponseCallbackImpl(future);
		try {

			// 1. Get connection of this proxy.
			final ClientConnection conn = getConnection();

			// 2. Add request to underling connection.
			conn.addRequest(request, callback);

			// 3. Wait for response.
			HttpClientResponse resultResp = future.waitFor();

			// 4. Setup HTTP response.
			resultResp.copyTo(respImpl);

		} catch (InterruptedException e) {
			throw new ClientFilterException(e);
		} catch (Throwable e) {
			respImpl.setError(e);
		}
	}

	@Override
	public void init(Proxy proxy) {
		// Do nothing.
	}
}
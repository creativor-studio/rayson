/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client.transport;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.StandardSocketOptions;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.rayson.api.client.HttpClientRequest;
import org.rayson.api.client.HttpClientResponse;
import org.rayson.api.client.RaysonServerAddress;
import org.rayson.api.exception.ConnectionClosedException;
import org.rayson.api.exception.ProtocolException;
import org.rayson.api.http.HttpConstants;
import org.rayson.api.http.HttpHeader;
import org.rayson.client.ClientSystem;
import org.rayson.client.http.ClientRequestWriter;
import org.rayson.client.http.ClientResponseReader;
import org.rayson.client.http.HttpClientResponseImpl;
import org.rayson.client.http.HttpResponseCallback;
import org.rayson.share.exception.ConnectionClosingException;
import org.rayson.share.exception.MalformedHttpMessageException;
import org.rayson.share.logger.RaysonLoggerFactory;
import org.rayson.share.transport.Connection;
import org.rayson.share.transport.NioChannel;
import org.slf4j.Logger;

/**
 * Client side transport connection.
 * 
 * @author creativor
 */
public class ClientConnection implements Connection {
	private static final Logger LOGGER = RaysonLoggerFactory.getLogger(ClientConnection.class);
	private static final long CONNECTION_TIMEOUT = 5 * 60 * 1000;
	/**
	 * Timeout for connecting a remote server socket. In milli-seconds.
	 */
	private static final long SOCKET_CONNECT_TIMEOUT = 3000;

	private static final HttpClientResponseImpl[] DUMMY_MESSAGE_ARRAY = new HttpClientResponseImpl[0];
	private long lastContact;
	private final ReentrantLock connectLock;

	private final Condition connectedCon;
	private final Map<Long, HttpResponseCallback> callbacks;
	private final long id;
	private final RaysonServerAddress serverAddr;
	private SocketChannel socketChannel;
	private NioChannel channel;
	private final ClientResponseReader reader;

	private boolean recycled = false;

	private Throwable connectException;
	private final ClientRequestWriter writer;
	private boolean connected;
	private boolean closed;
	private boolean keepAlive = true;

	private Throwable closingCause;

	/**
	 * Construct a new client connection with given id and server address.
	 * 
	 * @param id Unique id of the new connection.
	 * @param serverAddr Server address.
	 */
	public ClientConnection(final long id, final RaysonServerAddress serverAddr) {
		this.id = id;
		this.serverAddr = serverAddr;
		connectLock = new ReentrantLock();
		connectedCon = connectLock.newCondition();
		lastContact = System.currentTimeMillis();
		callbacks = Collections.synchronizedMap(new HashMap<Long, HttpResponseCallback>());
		this.reader = new ClientResponseReader(this);
		this.writer = new ClientRequestWriter(this);

	}

	/**
	 * Add request to this connection.
	 * 
	 * @param request HTTP request to be added to this connection.
	 * @param callback The call back which will be invoked when the response is
	 *            arrived.
	 * @throws ConnectionClosedException If this connection is closed.
	 */
	public synchronized void addRequest(final HttpClientRequest request, final HttpResponseCallback callback) throws ConnectionClosedException {
		if (closed)
			throw new ConnectionClosedException(this + " is closed");
		if (!request.isKeepAlive())
			keepAlive = false;
		callbacks.put(request.getId(), callback);
		this.writer.add(request);
	}

	/**
	 * Read data from underling channel asynchronously.
	 * 
	 * @throws EOFException Reached end of stream.
	 * @throws IOException If some other I/O error occurs.
	 * @throws ProtocolException If the response data is malformed according to
	 *             the communicating protocol.
	 * @throws ConnectionClosingException If the response telling the client to
	 *             closing the underling socket.
	 */
	public void asyncRead() throws IOException, EOFException, ProtocolException, ConnectionClosingException {
		final HttpClientResponseImpl[] responses;

		try {
			responses = reader.read(DUMMY_MESSAGE_ARRAY);

		} catch (MalformedHttpMessageException e) {
			throw new ProtocolException(e.getMessage());
		}

		if (responses != null) {
			touch();
			for (final HttpClientResponse response : responses) {
				if (!response.isKeepAlive())
					keepAlive = false;
				callbackResponse(response);
			}

			if (!keepAlive) {
				throw new ConnectionClosingException();
			}
		}
	}

	/**
	 * Write data to underling channel asynchronously.
	 * 
	 * @throws IOException If some other I/O error occurs.
	 */
	public void asyncWrite() throws IOException {
		if (writer.write() > 0)
			touch();
	}

	private synchronized void callbackResponse(final HttpClientResponse response) {

		final HttpHeader requestIdHeader = response.getHeader(HttpConstants.REQUEST_ID_HEADER_NAME);

		if (requestIdHeader == null) {
			// It means the client send wrong format request, so we try to
			// response all callback
			for (final Iterator<HttpResponseCallback> iterator = callbacks.values().iterator(); iterator.hasNext();) {
				final HttpResponseCallback callBack = iterator.next();
				callBack.onResponse(response);
				iterator.remove();
			}
			return;
		}
		final long requestId = requestIdHeader.getValueAsLong();
		final HttpResponseCallback callBack = callbacks.remove(requestId);
		callBack.onResponse(response);
	}

	@Override
	public synchronized void close() throws IOException {
		if (closed)
			return;
		channel.close();
		closed = true;
		// FIXME: handling all the error on pending un-finished call.
		// Response all callback(s).
		if (callbacks.isEmpty())
			return;
		for (final Iterator<HttpResponseCallback> iterator = callbacks.values().iterator(); iterator.hasNext();) {
			final HttpResponseCallback callBack = iterator.next();
			callBack.onFailed(new ConnectionClosedException("Connection is closed", closingCause));
			iterator.remove();
		}
	}

	@Override
	public NioChannel getChannel() {
		return channel;
	}

	@Override
	public long getId() {
		return id;
	}

	SocketChannel getSocketChannel() {
		return socketChannel;
	}

	@Override
	public boolean isKeepAlive() {
		return keepAlive;
	}

	/**
	 * @return True if this connection is recycled already.
	 */
	boolean isRecycled() {
		return recycled;
	}

	@Override
	public boolean isTimeout() {
		return System.currentTimeMillis() - this.lastContact > CONNECTION_TIMEOUT;
	}

	/**
	 * Notified that this connection will be closed later.
	 * 
	 * @param cause The cause error which cause this connection will be closed
	 *            later.
	 */
	void notifyClosing(Throwable cause) {
		this.closingCause = cause;
	}

	void notifyConnected() {
		notifyConnectFailed(null);
	}

	void notifyConnectFailed(final Throwable e) {
		try {
			connectLock.lock();
			this.connectException = e;
			connected = true;
			connectedCon.signalAll();
		} finally {
			connectLock.unlock();
		}
	}

	/**
	 * Open this connection.THis method will blocked until the underling socket
	 * is connected to remote server.
	 * 
	 * @throws ConnectException If connect to remote server got error.
	 * @throws IOException If any other I/O error occurred.
	 * @throws InterruptedException If current thread is interrupted.
	 */
	public void open() throws ConnectException, IOException, InterruptedException {
		socketChannel = SocketChannel.open();
		socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
		socketChannel.configureBlocking(false);
		this.channel = ClientSystem.getSystem().createChannel(socketChannel, serverAddr.isUsingSsl());
		ClientSystem.getSystem().getNioSelector().accept(this);
		socketChannel.connect(serverAddr.getSocketAddress());

		// Wait until the underling socket is connected to remote server.
		try {
			connectLock.lock();
			if (!connected && !connectedCon.await(SOCKET_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)) {

				// Try to close this connection first.
				try {
					this.close();
				} catch (Throwable e) {
					// Ignore it.
				}

				throw new SocketTimeoutException("Connection time out after " + SOCKET_CONNECT_TIMEOUT + " milli-seconds");
			}
		} finally {
			connectLock.unlock();
		}

		if (connectException != null) {
			if (connectException instanceof IOException)
				throw (IOException) connectException;
			else
				throw new ConnectException(connectException.getMessage());
		}

		// then return.
		return;
	}

	void recycle() {
		recycled = true;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		final Object[] values = { id, serverAddr, connected, closed, lastContact };
		final String[] keys = { "id", "serverAddr", "connected", "closed", "lastContact" };
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

	void touch() {
		lastContact = System.currentTimeMillis();
	}
}
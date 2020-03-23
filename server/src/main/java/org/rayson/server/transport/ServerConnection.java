
package org.rayson.server.transport;

import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.rayson.api.exception.ConnectionClosedException;
import org.rayson.api.http.HttpResponseStatus;
import org.rayson.api.server.HttpServerRequest;
import org.rayson.api.server.HttpServerResponse;
import org.rayson.server.Container;
import org.rayson.server.http.HttpServerRequestImpl;
import org.rayson.server.http.HttpServerResponseImpl;
import org.rayson.server.http.ServerRequestReader;
import org.rayson.server.http.ServerResponseWriter;
import org.rayson.share.exception.ConnectionClosingException;
import org.rayson.share.exception.MalformedHttpMessageException;
import org.rayson.share.transport.Connection;
import org.rayson.share.transport.NioChannel;

/**
 * Server side connection.
 *
 * @author creativor
 */
public class ServerConnection implements Connection {

	private static final long CONNECTION_TIMEOUT = 5 * 60 * 1000;

	private static final int MAX_PENDING_REQUEST_COUNT = 100;

	private static final HttpServerRequestImpl[] DUMMY_MESSAGE_ARRAY = new HttpServerRequestImpl[0];

	private final Container container;

	private final NioChannel channel;

	private final ServerRequestReader reader;

	private final ServerResponseWriter writer;

	private final long id;

	private long lastContact;

	private boolean closed;

	private boolean keepAlive = true;

	private final AtomicInteger pendingRequestCount;

	/**
	 * @param id Unique id of this new connection.
	 * @param channel Underling NIO socket channel of this new connection.
	 * @param container The server container which manage this connection.
	 */
	public ServerConnection(final long id, final NioChannel channel, final Container container) {
		super();
		this.container = container;
		this.channel = channel;
		lastContact = System.currentTimeMillis();
		this.reader = new ServerRequestReader(this);
		this.writer = new ServerResponseWriter(this);
		this.id = id;
		this.pendingRequestCount = new AtomicInteger(0);
	}

	/**
	 * Add a new response to this connection.
	 * 
	 * @param response A new arrived HTTP response ,need to be handled by this
	 *            Connection.
	 * @throws ConnectionClosedException If this connection is closed already.
	 */
	public synchronized void addResponse(final HttpServerResponse response) throws ConnectionClosedException {
		if (closed)
			throw new ConnectionClosedException(this + " is closed");
		if (!response.isKeepAlive())
			keepAlive = false;
		this.writer.add(response);
		pendingRequestCount.decrementAndGet();
	}

	/**
	 * Read data from underling channel asynchronously.
	 * 
	 * @throws EOFException Reached end of stream.
	 * @throws IOException If some other I/O error occurs.
	 */
	public void asyncRead() throws IOException, EOFException {

		// Check whether exceed pending request count.
		if (pendingRequestCount.get() >= MAX_PENDING_REQUEST_COUNT) {
			handleClientError(HttpResponseStatus.TOO_MANY_REQUESTS, null);
			return;
		}

		HttpServerRequest[] reqs;
		try {
			reqs = reader.read(DUMMY_MESSAGE_ARRAY);
		} catch (MalformedHttpMessageException e) {

			handleClientError(e.getStatus(), ((HttpServerRequest) e.getHttpMessage()).getRequestId());
			return;
		}

		if (reqs != null) {
			touch();
			for (final HttpServerRequest msg : reqs) {
				if (!msg.isKeepAlive())
					keepAlive = false;
				pendingRequestCount.incrementAndGet();
				container.getRequestManager().add(msg);
			}
		}

	}

	/**
	 * Write data to underling channel asynchronously.
	 * 
	 * @throws IOException If some other I/O error occurs.
	 * @throws ConnectionClosingException If current connection is closed
	 *             already.
	 */
	public void asyncWrite() throws IOException, ConnectionClosingException {
		if (writer.write() > 0)
			touch();
	}

	@Override
	public synchronized void close() throws IOException {
		if (closed)
			return;
		channel.close();
		closed = true;
	}

	@Override
	public NioChannel getChannel() {
		return channel;
	}

	@Override
	public long getId() {
		return id;
	}

	private void handleClientError(HttpResponseStatus errorStatus, String requestId) {

		// 1. Construct an response.
		HttpServerResponseImpl response = new HttpServerResponseImpl(errorStatus, requestId);

		// 2. Set keep-alive false.
		this.keepAlive = false;

		// 3. Cancel read event.

		this.channel.unInterestRead();

		// 4. Add response to writer.
		synchronized (this) {
			this.writer.add(response);
		}
	}

	@Override
	public boolean isKeepAlive() {
		return keepAlive;
	}

	@Override
	public boolean isTimeout() {
		return System.currentTimeMillis() - this.lastContact > CONNECTION_TIMEOUT;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		final Object[] values = { id, channel, closed, lastContact };
		final String[] keys = { "id", "channel", "closed", "lastContact" };
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

	private void touch() {
		lastContact = System.currentTimeMillis();
	}
}

package org.rayson.server.http;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.rayson.api.serial.ByteArrayOutputBuffer;
import org.rayson.api.server.HttpServerResponse;
import org.rayson.server.transport.ServerConnection;
import org.rayson.share.exception.ConnectionClosingException;
import org.rayson.share.serial.ByteArrayOutputBufferImpl;

/**
 * An tool used to write {@link HttpServerResponse} to target
 * {@link ServerConnection}.
 * 
 * @author Nick Zhang
 *
 */
public class ServerResponseWriter {

	private final ServerConnection connection;
	private ByteBuffer currentResponse;
	private final ConcurrentLinkedQueue<byte[]> queue;
	private static final ByteBuffer DUMMY_RESPONSE = ByteBuffer.allocate(0);

	/**
	 * 
	 * @param connection Underling socket connection of this writer.
	 */
	public ServerResponseWriter(final ServerConnection connection) {
		super();
		this.connection = connection;
		queue = new ConcurrentLinkedQueue<>();
		currentResponse = DUMMY_RESPONSE;
	}

	/**
	 * Add a new HTTP response to this writer.
	 * 
	 * @param response HTTP response to be added.
	 */
	public void add(final HttpServerResponse response) {

		final ByteArrayOutputBuffer bab = new ByteArrayOutputBufferImpl();
		response.writeTo(bab, response.getCharacterEncoding());
		queue.add(bab.toByteArray());
		if (currentResponse == DUMMY_RESPONSE) {
			currentResponse = ByteBuffer.wrap(queue.remove());
		}
		connection.getChannel().interestOps(connection.getChannel().getInterestOps() | SelectionKey.OP_WRITE);
		connection.getChannel().wakeupSelector();
	}

	/**
	 * Write response package(s) to the underling channel.
	 * 
	 * @return The number of bytes written, possibly zero.
	 * @throws IOException If some other I/O error occurs
	 * @throws ConnectionClosingException If the underling connection is already
	 *             closed.
	 */
	public int write() throws IOException, ConnectionClosingException {
		final int size = connection.getChannel().write(currentResponse);
		if (!currentResponse.hasRemaining()) {
			if (queue.isEmpty()) {
				// If all response is send out, and the connection is not an
				// keep-alive one, we should close the connection.
				if (!connection.isKeepAlive())
					throw new ConnectionClosingException();
				currentResponse = DUMMY_RESPONSE;
				// Notes , should not use
				// connection.getChannel().unInterestWrite(); , cause it will
				// cancel SSL supplement write events too.
				connection.getChannel().interestOps(connection.getChannel().getInterestOps() & ~SelectionKey.OP_WRITE);
			} else {
				currentResponse = ByteBuffer.wrap(queue.remove());
			}
		}
		return size;
	}
}
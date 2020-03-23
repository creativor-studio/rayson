
package org.rayson.client.http;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.rayson.api.client.HttpClientRequest;
import org.rayson.api.http.HttpMessage;
import org.rayson.api.serial.ByteArrayOutputBuffer;
import org.rayson.client.transport.ClientConnection;
import org.rayson.share.serial.ByteArrayOutputBufferImpl;

/**
 * An tool used to write HTTP request on client side.
 * 
 * @author Nick Zhang
 */
public class ClientRequestWriter {

	private final ClientConnection connection;
	private ByteBuffer currentRequest;
	private final ConcurrentLinkedQueue<byte[]> queue;
	private static final ByteBuffer DUMMY_REQUEST = ByteBuffer.allocate(0);

	/**
	 * Construct a new write with specified connection.
	 * 
	 * @param clientConnection Client connection of the new writer.
	 */
	public ClientRequestWriter(final ClientConnection clientConnection) {
		super();
		this.connection = clientConnection;
		queue = new ConcurrentLinkedQueue<>();
		currentRequest = DUMMY_REQUEST;
	}

	/**
	 * Add a new request instance to this writer.
	 * 
	 * @param request new HTTP request to be written by this writer.
	 */
	public void add(final HttpClientRequest request) {
		final ByteArrayOutputBuffer bab = new ByteArrayOutputBufferImpl();
		request.writeTo(bab, HttpMessage.CHARSET);
		queue.add(bab.toByteArray());
		if (currentRequest == DUMMY_REQUEST) {
			currentRequest = ByteBuffer.wrap(queue.remove());
		}
		connection.getChannel().interestOps(connection.getChannel().getInterestOps() | SelectionKey.OP_WRITE);
		connection.getChannel().wakeupSelector();
	}

	/**
	 * Write request package(s) to the underling channel.
	 * 
	 * @return The number of bytes written, possibly zero.
	 * @throws IOException If some other I/O error occurs
	 */
	public int write() throws IOException {
		final int size = connection.getChannel().write(currentRequest);
		if (!currentRequest.hasRemaining()) {
			if (queue.isEmpty()) {
				currentRequest = DUMMY_REQUEST;
				// Notes , should not use
				// connection.getChannel().unInterestWrite(); , cause it will
				// cancel SSL supplement write events too.
				connection.getChannel().interestOps(connection.getChannel().getInterestOps() & ~SelectionKey.OP_WRITE);
			} else {
				currentRequest = ByteBuffer.wrap(queue.remove());
			}
		}
		return size;
	}

}

package org.rayson.server.http;

import java.nio.ByteBuffer;

import org.rayson.api.annotation.ThreadSafe;
import org.rayson.api.http.HttpMessage;
import org.rayson.api.http.HttpMethod;
import org.rayson.api.http.HttpResponseStatus;
import org.rayson.api.http.HttpStartLine;
import org.rayson.api.server.HttpServerRequest;
import org.rayson.server.transport.ServerConnection;
import org.rayson.share.exception.MalformedHttpMessageException;
import org.rayson.share.http.HttpMessageReader;
import org.rayson.share.util.HttpMessageParser;

/**
 * An tool used to read {@link HttpServerRequest} out of target
 * {@link ServerConnection}.
 * 
 * @author Nick Zhang
 *
 */
@ThreadSafe(false)
public class ServerRequestReader extends HttpMessageReader<HttpServerRequestImpl> {

	/**
	 * @param connection Source underling connection.
	 */
	public ServerRequestReader(final ServerConnection connection) {
		super(connection);
	}

	// /**
	// * Take an line out of the underling buffer.
	// *
	// * @param count Bytes count, start from consumed position.
	// * @return Result line data.
	// */
	// private byte[] consumeLine(final int count) {
	// final byte[] dst = new byte[count];
	// int dstI = 0;
	// for (int i = consumedIndex; i < consumedIndex + count; i++) {
	// dst[dstI++] = bb.get(i);
	// }
	// consumedIndex += (count + 2);
	// return dst;
	// }

	@Override
	protected void checkMalformedStartLine(ByteBuffer buffer, int position, int limit, HttpMessage lastMessage) throws MalformedHttpMessageException {
		if (!HttpMethod.matchMethod(buffer, position, limit))
			throw new MalformedHttpMessageException(HttpResponseStatus.BAD_REQUEST, lastMessage);
	}

	@Override
	protected HttpServerRequestImpl createMessage() {
		return new HttpServerRequestImpl((ServerConnection) connection);
	}

	@Override
	protected HttpStartLine parseStartLine(ByteBuffer line, int startIndex, int endIndex) {
		return HttpMessageParser.parseRequestLine(line, startIndex, endIndex);

	}

	// /**
	// * Build request result list read on client error occurred.
	// *
	// * @param status Client error status code.
	// * @return
	// */
	// private HttpServerRequest[] buildOnClientError(HttpResponseStatus status)
	// {
	// // Remove read selection key first.
	// connection.getChannel().unInterestOps(SelectionKey.OP_READ);
	//
	// // Then build a client error request, and return all received requests.
	// lastMessage.setClientError(status);
	// receivedMsgs.add(lastMessage);
	// final HttpServerRequest[] result = receivedMsgs.toArray(new
	// HttpServerRequest[0]);
	// receivedMsgs.clear();
	// return result;
	// }
}
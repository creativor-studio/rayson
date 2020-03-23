
package org.rayson.client.http;

import java.nio.ByteBuffer;

import org.rayson.api.client.HttpClientResponse;
import org.rayson.api.http.HttpMessage;
import org.rayson.api.http.HttpStartLine;
import org.rayson.client.transport.ClientConnection;
import org.rayson.share.exception.MalformedHttpMessageException;
import org.rayson.share.http.HttpMessageReader;
import org.rayson.share.util.HttpMessageParser;

/**
 * An tool used to read {@link HttpClientResponse} out of target
 * {@link ClientConnection}.
 * 
 * @author Nick Zhang
 *
 */
public class ClientResponseReader extends HttpMessageReader<HttpClientResponseImpl> {

	/**
	 * Construct a new reader with specified connection.
	 * 
	 * @param connection Client connection of the new reader.
	 */
	public ClientResponseReader(final ClientConnection connection) {
		super(connection);
	}

	@Override
	protected HttpClientResponseImpl createMessage() {
		return new HttpClientResponseImpl();
	}

	@Override
	protected void checkMalformedStartLine(ByteBuffer buffer, int position, int limit, HttpMessage lastMessage) throws MalformedHttpMessageException {
		// TODO: do check.
		// No nothing.
	}

	@Override
	protected HttpStartLine parseStartLine(ByteBuffer line, int startIndex, int endIndex) {
		return HttpMessageParser.parseStatusLine(line, startIndex, endIndex);
	}

	// /**
	// * Build request result list read on server response error occurred.
	// *
	// * @param status Server response error status code.
	// * @return Http client response list result.
	// */
	// private HttpClientResponse[] buildOnServerError(HttpResponseStatus
	// status) {
	// // Remove read selection key first.
	// connection.getChannel().unInterestOps(SelectionKey.OP_READ);
	//
	// // Then build a client error request, and return all received requests.
	// lastMessage.setServerProtocolError(status);
	// receivedMsgs.add(lastMessage);
	// final HttpClientResponse[] result = receivedMsgs.toArray(new
	// HttpClientResponse[0]);
	// receivedMsgs.clear();
	// return result;
	// }
}
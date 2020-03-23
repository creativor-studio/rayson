/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.server.http;

import java.nio.charset.Charset;

import org.rayson.api.http.HttpConstants;
import org.rayson.api.http.HttpContentType;
import org.rayson.api.http.HttpHeader;
import org.rayson.api.http.HttpMessage;
import org.rayson.api.http.HttpResponseStatus;
import org.rayson.api.http.HttpStatusLine;
import org.rayson.api.server.HttpServerResponse;
import org.rayson.share.http.HttpAbstractMessage;
import org.rayson.share.http.HttpStatusLineImpl;
import org.rayson.share.serial.ContentCoderFactory;

/**
 * Implementation of {@link HttpServerResponse}.
 * 
 * @author Nick Zhang
 *
 */
public class HttpServerResponseImpl extends HttpAbstractMessage<HttpStatusLine> implements HttpServerResponse {
	private Charset charset = HttpMessage.CHARSET;
	private HttpContentType contentType = HttpContentType.JSON;
	private HttpServerRequestImpl request;

	/**
	 * @param request Request associated with this new response.
	 */
	public HttpServerResponseImpl(HttpServerRequestImpl request) {
		this.request = request;
		// Setup contentType and charset.
		setupFromReququest(request);
	}

	/**
	 * Construct an response instance on client side error occurred.
	 * 
	 * @param errorStatus Client side error caused response status.
	 * @param requestId Source request id, <code>null</code> means no specified
	 *            id.
	 */
	public HttpServerResponseImpl(HttpResponseStatus errorStatus, String requestId) {
		// Add connection close header
		addHeader(HttpConstants.CONNECTION_HEADER_NAME, HttpConstants.CONNECTION_CLOSE_TOKEN);
		if (requestId != null)
			addHeader(HttpConstants.REQUEST_ID_HEADER_NAME, requestId);
		setException(errorStatus, errorStatus.getReasonPhrase());
	}

	private void setupFromReququest(HttpServerRequestImpl request) {
		try {
			charset = Charset.forName(request.getCharacterEncoding());
		} catch (Exception e) {
			// Ignore it.
		}

		try {

			HttpHeader header = request.getHeader(HttpConstants.REQUEST_ID_HEADER_NAME);
			if (header != null) {
				// It is rayson client. so we override the content type from
				// request.
				this.contentType = HttpContentType.typeOf(request.getContentType());
			}

		} catch (Exception e) {
			// Ignore it.
		}
	}

	@Override
	public Charset getCharacterEncoding() {
		return charset;
	}

	@Override
	public HttpContentType getContentType() {
		return contentType;
	}

	@Override
	public void setBody(final byte[] body) {
		addHeader(HttpConstants.CONTENT_LENGTH_HEADER_NAME, String.valueOf(body.length));
		super.setBody(body);
	}

	@Override
	public void setCharacterEncoding(Charset charset) {
		if (charset == null)
			throw new IllegalArgumentException("charset should not be null");
		this.charset = charset;
	}

	@Override
	public void setContentType(HttpContentType type) {
		this.contentType = type;
	}

	@Override
	public void setException(HttpResponseStatus statusCode, String message) {
		setStartLine(new HttpStatusLineImpl(statusCode));
		setBody(ContentCoderFactory.get(getContentType().typeName()).encodeMessage(message, getCharacterEncoding()));
	}
}
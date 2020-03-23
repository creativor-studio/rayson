/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.server.http;

import java.io.IOException;

import org.rayson.api.http.HttpConstants;
import org.rayson.api.http.HttpContentType;
import org.rayson.api.http.HttpHeader;
import org.rayson.api.http.HttpMessage;
import org.rayson.api.http.HttpRequestLine;
import org.rayson.api.server.HttpServerRequest;
import org.rayson.server.transport.ServerConnection;
import org.rayson.share.http.HttpAbstractMessage;

/**
 * Implementation of {@link HttpServerRequest}.
 * 
 * @author Nick Zhang
 *
 */
public class HttpServerRequestImpl extends HttpAbstractMessage<HttpRequestLine> implements HttpServerRequest {
	private static final String CHARSET_PREFIX = "charset=";
	private static final char SEMICOLON_CHAR = ';';
	private final ServerConnection connection;

	private String url;
	private String requestId = null;
	private String characterEncoding;
	private String contentType;

	/**
	 * Constructor a new instance with associated connection.
	 * 
	 * @param connection Underling connection associated with the new instance.
	 */
	public HttpServerRequestImpl(ServerConnection connection) {
		super();
		this.connection = connection;
	}

	/**
	 * Setup http request parameters.
	 */
	public void setupParameters() {

		// Parse service url string.
		final String uri = this.getStartLine().getUri();
		// Parse the uri.
		int firstQuery = uri.indexOf(HttpConstants.QUERY_TOKEN);
		if (firstQuery < 0)
			firstQuery = uri.length();
		url = uri.substring(0, firstQuery);
		// methodName = (lastQuery == -1) ? uri.substring(lastSlash + 1) :
		// uri.substring(lastSlash + 1, lastQuery);
	}

	/**
	 * @return The associated server connection of this request.
	 */
	public ServerConnection getConnection() {
		return connection;
	}

	@Override
	public int getLocalPort() {
		try {
			return this.connection.getChannel().getLocalAddress().getPort();
		} catch (IOException e) {
			return -1;
		}
	}

	@Override
	public String getRemoteAddr() {
		try {
			return this.connection.getChannel().getRemoteAddress().getHostString();
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public int getRemotePort() {
		try {
			return this.connection.getChannel().getRemoteAddress().getPort();
		} catch (IOException e) {
			return -1;
		}
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String getRequestId() {
		// Lazy load it.
		if (requestId == null || requestId.isEmpty()) {
			final HttpHeader requestIdHeader = this.getHeader(HttpConstants.REQUEST_ID_HEADER_NAME);
			if (requestIdHeader != null)
				this.requestId = requestIdHeader.getValue();
		}
		return requestId;
	}

	@Override
	public String getCharacterEncoding() {
		if (characterEncoding == null)
			parseContentType();
		return characterEncoding;
	}

	private void parseContentType() {
		// Default value.
		characterEncoding = HttpMessage.CHARSET.name();
		contentType = HttpContentType.JSON.typeName();

		HttpHeader contentHeader = getHeader(HttpConstants.CONTENT_TYPE_HEADER_NAME);
		if (contentHeader != null) {

			String value = contentHeader.getValue();
			int semicolonIndex = value.indexOf(SEMICOLON_CHAR);
			if (semicolonIndex == -1) {
				contentType = value;
				return;
			}
			contentType = value.substring(0, semicolonIndex);
			String charsetString = value.substring(semicolonIndex + 1).trim();
			int charsetIndex = charsetString.indexOf(CHARSET_PREFIX);
			if (charsetIndex == -1)
				return;
			characterEncoding = charsetString.substring(charsetIndex + CHARSET_PREFIX.length());
		}

	}

	@Override
	public String getContentType() {
		if (contentType == null)
			parseContentType();
		return contentType;
	}
}
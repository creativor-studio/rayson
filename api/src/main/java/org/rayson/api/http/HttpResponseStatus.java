/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.http;

import org.rayson.api.annotation.NotNull;

/**
 * Commonly used response status codes defined by HTTP,@see
 * <a href= "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10">HTTP/
 * 1.1 documentation</a> for the complete list. <br>
 * 
 * @author creativor
 */
public enum HttpResponseStatus {

	/**
	 * 200 OK, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.1"> HTTP/
	 * 1.1 documentation</a> .
	 */
	OK(200, "OK"),
	/**
	 * 201 Created, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.2"> HTTP/
	 * 1.1 documentation</a>} .
	 */
	CREATED(201, "Created"),
	/**
	 * 202 Accepted, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.3"> HTTP/
	 * 1.1 documentation</a>} .
	 */
	ACCEPTED(202, "Accepted"),
	/**
	 * 204 No Content, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.5"> HTTP/
	 * 1.1 documentation</a>} .
	 */
	NO_CONTENT(204, "No Content"),
	/**
	 * 303 See Other, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.3.2"> HTTP/
	 * 1.1 documentation</a>} .
	 */
	MOVED_PERMANENTLY(301, "Moved Permanently"),
	/**
	 * 303 See Other, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.3.4"> HTTP/
	 * 1.1 documentation</a>} .
	 */
	SEE_OTHER(303, "See Other"),
	/**
	 * 304 Not Modified, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.3.5"> HTTP/
	 * 1.1 documentation</a>} .
	 */
	NOT_MODIFIED(304, "Not Modified"),
	/**
	 * 307 Temporary Redirect, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.3.8"> HTTP/
	 * 1.1 documentation</a>} .
	 */
	TEMPORARY_REDIRECT(307, "Temporary Redirect"),
	/**
	 * 400 Bad Request, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.1"> HTTP/
	 * 1.1 documentation</a>} .
	 */
	BAD_REQUEST(400, "Bad Request"),
	/**
	 * 401 Unauthorized, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.2"> HTTP/
	 * 1.1 documentation</a>} .
	 */
	UNAUTHORIZED(401, "Unauthorized"),
	/**
	 * 403 Forbidden, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.4"> HTTP/
	 * 1.1 documentation</a>} .
	 */
	FORBIDDEN(403, "Forbidden"),
	/**
	 * 404 Not Found, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.5"> HTTP/
	 * 1.1 documentation</a>} .
	 */
	NOT_FOUND(404, "Not Found"),
	/**
	 * 406 Not Acceptable, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.7"> HTTP/
	 * 1.1 documentation</a>} .
	 */
	NOT_ACCEPTABLE(406, "Not Acceptable"),
	/**
	 * 409 Conflict, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.10"> HTTP
	 * /1.1 documentation</a>} .
	 */
	CONFLICT(409, "Conflict"),
	/**
	 * 410 Gone, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.11"> HTTP
	 * /1.1 documentation</a>} .
	 */
	GONE(410, "Gone"),

	/**
	 * Status code (411) indicating that the request cannot be handled without a
	 * defined <code><em>Content-Length</em></code>.
	 */
	LENGTH_REQUIRED(411, "Length Header Required"),
	/**
	 * 412 Precondition Failed, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.13"> HTTP
	 * /1.1 documentation</a>} .
	 */
	PRECONDITION_FAILED(412, "Precondition Failed"),
	/**
	 * 413 Request Entity Too Large, s@see
	 * <a href= "https://tools.ietf.org/html/rfc2616#section-10.4.14"> HTTP/1.1
	 * documentation</a> .
	 */
	ENTITY_TOO_LARGE(413, "Request Entity Too Large"),
	/**
	 * Status code (414) indicating that the server is refusing to service the
	 * request because the <code><em>Request-URI</em></code> is longer than the
	 * server is willing to interpret.
	 */
	URI_TOO_LONG(414, "Request URI Too Long"),
	/**
	 * 415 Unsupported Media Type, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.16"> HTTP
	 * /1.1 documentation</a>} .
	 */
	UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
	/**
	 * The server is unwilling to process the request because its header fields
	 * are too large. The request MAY be resubmitted after reducing the size of
	 * the request header fields.
	 */
	HEADER_TOO_LARGE(413, "Request Header Fields Too Large"),
	/**
	 * The user has sent too many requests in a given amount of time ("rate
	 * limiting").
	 */
	TOO_MANY_REQUESTS(429, "Too Many Requests"),
	/**
	 * 40010 The client request format is wrong according to Rayson protocol.
	 */
	RAYSON_WRONG_PROTOCOL_FORMAT(40010, "Rayson Wrong Protocol Format"),

	/**
	 * 500 Internal Server Error, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.1"> HTTP/
	 * 1.1 documentation</a> .
	 */
	INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
	/**
	 * 503 Service Unavailable, @see <a href=
	 * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.4"> HTTP/
	 * 1.1 documentation</a> .
	 */
	SERVICE_UNAVAILABLE(503, "Service Unavailable"),
	/**
	 * For all other unknown errors.
	 */
	UNKNOWN_ERROR(9999, "Unknown Error");

	/**
	 * An enumeration representing the class of status code. Family is used here
	 * since class is overloaded in Java.
	 */
	public enum Family {
		/**
		 * Class for information.
		 */
		INFORMATIONAL,
		/**
		 * Class for success.
		 */
		SUCCESSFUL,
		/**
		 * Class for redirection.
		 */
		REDIRECTION,
		/**
		 * Class for client side error.
		 */
		CLIENT_ERROR,
		/**
		 * Class for server side error.
		 */
		SERVER_ERROR,
		/**
		 * Other classes.
		 */
		OTHER
	}

	/**
	 * Convert a numerical status code into the corresponding Status.
	 * 
	 * @param code the numerical status code
	 * @return the matching Status or null is no matching Status is defined
	 */
	public static HttpResponseStatus fromCode(final int code) {
		for (final HttpResponseStatus s : HttpResponseStatus.values()) {
			if (s.code == code) {
				return s;
			}
		}
		return null;
	}

	/**
	 * @return The reason phrase according to this response.
	 */
	public String getReasonPhrase() {
		return reasonPhrase;
	}

	/**
	 * Find the reason phrase of giving status code.
	 * 
	 * @param statusCode Response status code.
	 * @return Found reason phrase. Or {@link #DEFAULT_REASON_PHRASE}.
	 */
	@NotNull
	public static String getReasonPhrase(final int statusCode) {
		final HttpResponseStatus status = fromCode(statusCode);
		if (status != null)
			return status.reasonPhrase;
		return DEFAULT_REASON_PHRASE;
	}

	private final int code;

	private final String reasonPhrase;

	private Family family;
	/**
	 * Default reason phrase, when there is no reason phrase specified, we use
	 * this one.
	 */
	public static final String DEFAULT_REASON_PHRASE = INTERNAL_SERVER_ERROR.reasonPhrase;

	private HttpResponseStatus(final int code, final String reasonPhrase) {
		this.code = code;
		this.reasonPhrase = reasonPhrase;
		switch (code / 100) {
		case 1:
			this.family = Family.INFORMATIONAL;
			break;
		case 2:
			this.family = Family.SUCCESSFUL;
			break;
		case 3:
			this.family = Family.REDIRECTION;
			break;
		case 4:
			this.family = Family.CLIENT_ERROR;
			break;
		case 5:
			this.family = Family.SERVER_ERROR;
			break;
		default:
			this.family = Family.OTHER;
			break;
		}
	}

	/**
	 * Get the associated status code.
	 * 
	 * @return the status code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Get the class of status code.
	 * 
	 * @return the class of status code
	 */
	public Family getFamily() {
		return family;
	}

	/**
	 * Get the reason phrase.
	 * 
	 * @return the reason phrase
	 */
	@Override
	public String toString() {
		return reasonPhrase;
	}
}
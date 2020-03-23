/**
 * Copyright © 2020 Creativor Studio. About license information, please see
 * LICENSE.txt.
 */

package org.rayson.server.rpc;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.JsonException;

import org.rayson.api.exception.RpcException;
import org.rayson.api.http.HttpConstants;
import org.rayson.api.http.HttpResponseStatus;
import org.rayson.api.http.HttpStatusLine;
import org.rayson.api.protocol.mirror.MethodMirror;
import org.rayson.api.server.HttpServerResponse;
import org.rayson.api.server.ServerResponse;
import org.rayson.api.struct.KeyValues;
import org.rayson.rson.RsonElement;
import org.rayson.server.http.HttpServerResponseImpl;
import org.rayson.share.exception.SerializationException;
import org.rayson.share.http.HttpStatusLineImpl;
import org.rayson.share.serial.ContentCoder;
import org.rayson.share.serial.ContentCoderFactory;
import org.rayson.share.serial.rson.RsonUtil;

/**
 * Implementation of {@link ServerResponse}.
 * 
 * @author Nick Zhang
 *
 */
public class ServerResponseImpl implements ServerResponse {

	private static HttpStatusLine HTTP_STATUS_OK = new HttpStatusLineImpl(HttpResponseStatus.OK);

	private RpcException exception;

	private final Map<String, KeyValues> headers;

	private MethodMirror methodMirror;

	private final ServerRequestImpl request;

	private Object result;

	/**
	 * 
	 * @param request Request instance associated with this new response.
	 * @param method The underling java method which the request like to invoke.
	 */
	public ServerResponseImpl(final ServerRequestImpl request, MethodMirror method) {
		this.request = request;
		this.methodMirror = method;
		this.headers = new HashMap<>();
	}

	@Override
	public void addHeader(String name, String value) {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("name should not be empty");
		if (value == null || value.isEmpty())
			throw new IllegalArgumentException("value should not be empty");
		KeyValues item = headers.get(name);
		if (item == null) {
			item = new KeyValues(name);
			headers.put(name, item);
		}
		item.addValue(value);
	}

	@Override
	public Collection<KeyValues> getAllHeaders() {
		return Collections.unmodifiableCollection(headers.values());
	}

	@Override
	public RpcException getException() {
		return exception;
	}

	@Override
	public Object getResult() throws RpcException {
		if (gotException())
			throw exception;
		return result;
	}

	private boolean gotException() {
		return exception != null;
	}

	@Override
	public void setException(final RpcException rpcException) {
		this.exception = rpcException;
	}

	@Override
	public void setResult(final Object result) {
		this.result = result;
	}

	/**
	 * Convert this RPC server response to HTTP response.
	 * 
	 * @param httpRes HTTP request , which it create the HTTP response base on
	 *            it.
	 * @return Result HTTP response.
	 */
	public HttpServerResponse setupHttpResponse(HttpServerResponseImpl httpRes) {

		for (final Entry<String, KeyValues> header : headers.entrySet()) {
			for (String v : header.getValue().getValues()) {
				httpRes.addHeader(header.getKey(), v);
			}
		}

		// Setup default content type first.

		httpRes.setHeader(HttpConstants.CONTENT_TYPE_HEADER_NAME,
				httpRes.getContentType().typeName() + "; charset=" + httpRes.getCharacterEncoding().name());

		ContentCoder contentCoder = ContentCoderFactory.get(httpRes.getContentType().typeName());
		HttpStatusLine statusLine;
		byte[] body = null;
		if (gotException()) {
			statusLine = new HttpStatusLineImpl(exception.getCode(), HttpResponseStatus.getReasonPhrase(exception.getCode()));
			body = contentCoder.encodeMessage(exception.getMessage(), httpRes.getCharacterEncoding());

		} else {
			try {
				RsonElement rson = RsonUtil.toRson(this.result, methodMirror.getSource().getReturnType());
				body = contentCoder.encodeResponse(rson, httpRes.getCharacterEncoding(), httpRes);
				statusLine = HTTP_STATUS_OK.clone();
			} catch (final SerializationException | JsonException e) {
				statusLine = new HttpStatusLineImpl(HttpResponseStatus.RAYSON_WRONG_PROTOCOL_FORMAT);
				body = contentCoder.encodeMessage(e.getMessage(), httpRes.getCharacterEncoding());
			}
		}

		httpRes.setStartLine(statusLine);

		// Set body.
		httpRes.setBody(body);

		return httpRes;
	}
}
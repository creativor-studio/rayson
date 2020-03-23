/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client.rpc;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collection;

import org.rayson.api.annotation.NotNull;
import org.rayson.api.client.ClientResponse;
import org.rayson.api.exception.ProtocolException;
import org.rayson.api.exception.RpcException;
import org.rayson.api.http.HttpHeader;
import org.rayson.api.http.HttpResponseStatus;
import org.rayson.api.protocol.mirror.MethodMirror;
import org.rayson.client.http.HttpClientResponseImpl;
import org.rayson.rson.RsonElement;
import org.rayson.share.exception.SerializationException;
import org.rayson.share.serial.ContentCoder;
import org.rayson.share.serial.ContentCoderFactory;
import org.rayson.share.serial.rson.RsonUtil;

/**
 * ClientResponse implementation.
 * 
 * @author Nick Zhang
 *
 */
public class ClientResponseImpl extends AbstractClientMessage implements ClientResponse {
	private static final StackTraceElement[] DUMMPY_STACK_TRACE = new StackTraceElement[0];
	private ContentCoder contentCoder = ContentCoderFactory.RSON_SERIALIZER;

	private final MethodMirror methodMirror;

	private Throwable error;
	private Object result;

	ClientResponseImpl(final MethodMirror methodMirror) {
		super();
		this.methodMirror = methodMirror;

	}

	private void checkError() throws RpcException, IOException, ProtocolException {
		if (error != null) {
			error.setStackTrace(DUMMPY_STACK_TRACE.clone());
			if (error instanceof IOException)
				throw (IOException) error;
			if (error instanceof RpcException)
				throw (RpcException) error;
			if (error instanceof ProtocolException)
				throw (ProtocolException) error;

			throw new UndeclaredThrowableException(error);
		}
	}

	@Override
	public Object getResult() throws RpcException, IOException {
		checkError();
		return result;
	}

	/**
	 * Set error of this response.
	 * 
	 * @param error Error cause.
	 */
	public void setError(final Throwable error) {
		this.error = error;
	}

	@Override
	public void setException(final RpcException exception) {
		if (exception == null)
			throw new IllegalArgumentException("exception should not be null");
		setError(exception);
	}

	@Override
	public void setResult(final Object result) {
		this.result = result;
	}

	/**
	 * Setup client response from HTTP response.
	 * 
	 * @param httpResponse HTTP response.
	 */
	void setup(@NotNull final HttpClientResponseImpl httpResponse) {

		// setup headers first.
		final Collection<HttpHeader> headers = httpResponse.getHeaders();
		for (final HttpHeader httpHeader : headers) {
			this.addHeader(httpHeader.getName(), httpHeader.getValue());
		}

		// Stup exceptions.

		// Response error.
		final Throwable responseError = httpResponse.getError();
		if (responseError != null) {
			setError(responseError);
			return;
		}

		// Ok
		final int statusCode = httpResponse.getStartLine().getStatusCode();
		if (statusCode == HttpResponseStatus.OK.getCode()) {
			try {
				// Get content serializer.
				final String ct = httpResponse.getContentType().typeName();
				if (ct != null && !ct.isEmpty())
					contentCoder = ContentCoderFactory.get(ct);
				final RsonElement rson = contentCoder.decodeResponse(httpResponse.getBody(), httpResponse.getCharacterEncoding());
				Object result = RsonUtil.fromRson(rson, methodMirror.getSource().getReturnType());
				setResult(result);
				return;
			} catch (final SerializationException e) {
				setError(new ProtocolException(HttpResponseStatus.RAYSON_WRONG_PROTOCOL_FORMAT.getReasonPhrase(), e));
				return;
			} catch (final Throwable e) {
				setError(new ProtocolException(HttpResponseStatus.RAYSON_WRONG_PROTOCOL_FORMAT.getReasonPhrase(), e));
				return;
			}

		}
		// Not Ok.

		String msg;
		msg = contentCoder.decodeMessage(httpResponse.getBody(), httpResponse.getCharacterEncoding());
		setError(new RpcException(statusCode, msg));
	}
}
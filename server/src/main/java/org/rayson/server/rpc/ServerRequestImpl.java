
package org.rayson.server.rpc;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.NotNull;
import org.rayson.api.http.HttpHeader;
import org.rayson.api.http.HttpMessage;
import org.rayson.api.protocol.mirror.MethodMirror;
import org.rayson.api.protocol.mirror.ProtocolMirror;
import org.rayson.api.serial.JsonStringBuilder;
import org.rayson.api.server.HttpServerRequest;
import org.rayson.api.server.ServerRequest;
import org.rayson.api.struct.KeyValues;
import org.rayson.api.struct.RpcParameter;
import org.rayson.api.struct.RpcParameters;
import org.rayson.rson.element.ObjectElement;
import org.rayson.server.http.HttpServerRequestImpl;
import org.rayson.share.RaysonConstants;
import org.rayson.share.exception.SerializationException;
import org.rayson.share.serial.ContentCoderFactory;
import org.rayson.share.serial.rson.RsonUtil;

/**
 * An implementation of {@link ServerRequest}.
 * 
 * @author Nick Zhang
 *
 */
public class ServerRequestImpl implements ServerRequest {

	private final Map<String, KeyValues> headers;
	private final HttpServerRequestImpl httpRequst;
	private MethodMirror methodMirror;
	private RpcParameters parameters;
	private ProtocolMirror protocolMirror;

	private Protocol serivce;

	/**
	 * Construct a new {@link ServerRequest} from {@link HttpServerRequest}.
	 * 
	 * @param httpReq HTTP server request.
	 */
	public ServerRequestImpl(final HttpServerRequestImpl httpReq) {
		super();
		this.headers = new HashMap<String, KeyValues>();

		this.httpRequst = httpReq;
	}

	/**
	 * Add HTTP headers to this requeset.
	 * 
	 * @param headers HTTP headers to be added to this request.
	 */
	public void addHeaders(@NotNull final Collection<HttpHeader> headers) {
		KeyValues v;
		String key;
		for (final HttpHeader header : headers) {
			key = header.getName();
			v = this.headers.get(key);
			if (v == null) {
				v = new KeyValues(key);
				this.headers.put(key, v);
			}
			v.addValue(header.getValue());
		}
	}

	@Override
	public Collection<KeyValues> getAllHeaders() {
		return Collections.unmodifiableCollection(this.headers.values());
	}

	@Override
	public KeyValues getHeaders(String name) {

		if (name == null)
			throw new IllegalArgumentException("name should not be null");

		for (KeyValues h : headers.values()) {
			if (h.getKey().equals(name))
				return h;
		}
		return null;
	}

	@Override
	public HttpServerRequest getHttpRequest() {
		return httpRequst;
	}

	@Override
	public MethodMirror getMethodMirror() {
		return methodMirror;
	}

	@Override
	public RpcParameter getParameter(String name) {
		return parameters.get(name);
	}

	@Override
	public RpcParameters getParameters() {
		return parameters;
	}

	@Override
	public ProtocolMirror getProtocolMirror() {
		return protocolMirror;
	}

	/**
	 * @return the serivce
	 */
	public Protocol getSerivce() {
		return serivce;
	}

	/**
	 * Setup rpc parameters of this request.
	 * 
	 * @param service The RPC service instance.
	 * @param protocolMirror The associated protocol mirror of the target
	 *            method.
	 * @param methodMirror The target method mirror the the RPC invoking.
	 * @throws SerializationException If failed to setting up.
	 */
	public void setupRpcParams(final Protocol service, ProtocolMirror protocolMirror, final MethodMirror methodMirror) throws SerializationException {

		Charset charset = HttpMessage.CHARSET;
		try {
			charset = Charset.forName(httpRequst.getCharacterEncoding());
		} catch (Exception e) {
			// Ignore it.
		}
		// FIXME: version header is abandoned?
		final HttpHeader versionHeader = httpRequst.getHeader(RaysonConstants.PROTOCOL_VERSION_HEADER_NAME);
		this.addHeaders(httpRequst.getHeaders());
		this.serivce = service;
		this.methodMirror = methodMirror;
		this.protocolMirror = protocolMirror;

		ObjectElement rson = ContentCoderFactory.get(httpRequst.getContentType()).decodeRequest(httpRequst, charset,this.methodMirror);
		this.parameters = RsonUtil.paserRpcRequest(rson, methodMirror);
	}

	@Override
	public String toString() {
		JsonStringBuilder b = new JsonStringBuilder(this);
		b.append("headers", headers).append("parameters", parameters).append("serivce", serivce);
		return b.toJson();
	}
}
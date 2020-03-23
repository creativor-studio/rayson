
package org.rayson.server.filter;

import org.rayson.api.Protocol;
import org.rayson.api.exception.InvalidApiException;
import org.rayson.api.protocol.mirror.ProtocolMirror;
import org.rayson.api.server.ServerRequest;
import org.rayson.api.server.ServerResponse;
import org.rayson.api.server.ServiceFilter;
import org.rayson.api.server.ServiceFilterChain;
import org.rayson.api.server.exception.ServiceException;
import org.rayson.share.util.MirrorTool;

/**
 * Default implementation of {@link ServiceFilterChain}.
 * 
 * @author Nick Zhang
 *
 */
public class DefaultFilterChain implements ServiceFilterChain {
	private final ServiceFilter filter;
	private ServiceFilterChain next;
	private Class<? extends Protocol> protocol;
	private final Protocol service;
	private ProtocolMirror protocolMirror;

	DefaultFilterChain(Class<? extends Protocol> protocol, final Protocol service, final ServiceFilter filter) throws InvalidApiException {
		this.protocol = protocol;
		this.service = service;
		this.filter = filter;
		protocolMirror = MirrorTool.load(protocol);
	}

	/**
	 * 
	 * @param next The next chain of current one.
	 */
	void setNext(ServiceFilterChain next) {
		this.next = next;
	}

	/**
	 * 
	 * @return The protocol mirror of this chain.
	 */
	public ProtocolMirror getProtocolMirror() {
		return protocolMirror;
	}

	@Override
	public void doFilter(final ServerRequest request, final ServerResponse response) throws ServiceException {
		this.filter.doFilter(request, response, next);
	}

	/**
	 * 
	 * @return The service filter of this chain.
	 */
	protected ServiceFilter getFilter() {
		return filter;
	}

	/**
	 * @return Next item of this current chain.
	 */
	protected ServiceFilterChain getNext() {
		return this.next;
	}

	/**
	 * @return The underling RPC protocol of this chain.
	 */
	public Class<? extends Protocol> getProtocol() {
		return protocol;
	}

	/**
	 * @return The RPC service instance of this chain.
	 */
	public Protocol getService() {
		return this.service;
	}
}
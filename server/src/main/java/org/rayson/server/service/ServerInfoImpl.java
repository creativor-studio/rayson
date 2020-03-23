/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.server.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.UrlMapping;
import org.rayson.api.exception.RpcException;
import org.rayson.api.http.HttpResponseStatus;
import org.rayson.api.protocol.info.ProtocolInfo;
import org.rayson.api.protocol.info.ServerInfo;
import org.rayson.api.protocol.mirror.ProtocolMirror;
import org.rayson.api.server.ServerDiscovery;
import org.rayson.server.Container;

/**
 * Implementation of {@link ServerDiscovery}. <br>
 * 
 * @author creativor
 */
@UrlMapping("/")
final public class ServerInfoImpl implements ServerDiscovery {

	private Container container;

	/**
	 * @param container Rayson server container.
	 */
	public ServerInfoImpl(Container container) {
		this.container = container;
	}

	@Override
	public ProtocolInfo[] listProtocols() throws IOException, RpcException {
		List<ProtocolInfo> result = new ArrayList<>();

		for (ProtocolMirror mirror : this.container.getServiceManager().getProtocols()) {
			result.add(mirror.getInfo());
		}

		return result.toArray(new ProtocolInfo[0]);
	}

	@Override
	@UrlMapping("/")
	public ServerInfo show() throws IOException, RpcException {
		return this.container.getServer().getInfo();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ProtocolInfo showProtocol(final String namespace) throws IOException, RpcException {
		if (namespace == null)
			throw new IllegalArgumentException("namespace should not be null");

		Class<? extends Protocol> protocol;
		Class<?> klass;
		try {
			klass = Class.forName(namespace);

		} catch (Exception e) {
			throw new RpcException(HttpResponseStatus.SERVICE_UNAVAILABLE.getCode(), "No interface named " + namespace + " found");
		}

		if (!klass.isInterface() || !Protocol.class.isAssignableFrom(klass))
			throw new RpcException(HttpResponseStatus.SERVICE_UNAVAILABLE.getCode(),
					"Name space  " + namespace + " associated class is not an interface which extended from " + Protocol.class);

		protocol = (Class<? extends Protocol>) klass;
		ProtocolMirror mrrior = this.container.getServiceManager().getMirror(protocol);

		if (mrrior == null)
			throw new RpcException(HttpResponseStatus.NOT_FOUND.getCode(), "Name space  " + namespace + " information not found");

		return mrrior.getInfo();
	}

	@Override
	public void touch() throws IOException, RpcException {
		// Do nothing.
		return;
	}

}
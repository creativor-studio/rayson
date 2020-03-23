/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.server;

import java.io.IOException;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.SerializedName;
import org.rayson.api.annotation.Service;
import org.rayson.api.exception.RpcException;
import org.rayson.api.protocol.info.ProtocolInfo;
import org.rayson.api.protocol.info.ServerInfo;

/**
 * Show Rayson server information discovery.
 * 
 * @author creativor
 */
@Service
public interface ServerDiscovery extends Protocol {
	/**
	 * List all protocol informations.
	 * 
	 * @return All RPC protocol informations.
	 * @throws IOException If network error occurred.
	 * @throws RpcException If servicing this API got error.
	 */
	public ProtocolInfo[] listProtocols() throws IOException, RpcException;

	/**
	 * Show information of this server.
	 * 
	 * @return Current server information.
	 * @throws IOException If network error occurred.
	 * @throws RpcException If servicing this API got error.
	 */
	public ServerInfo show() throws IOException, RpcException;

	/**
	 * Touch this server.
	 * 
	 * @throws IOException If network error occurred.
	 * @throws RpcException If servicing this API got error.
	 */
	public void touch() throws IOException, RpcException;

	/**
	 * Show protocol information of given protocol name space.
	 * 
	 * @param namespace Name space of target protocol.
	 * @return Protocol information.
	 * @throws IOException If network error occurred.
	 * @throws RpcException If servicing this API got error.
	 */
	public ProtocolInfo showProtocol(@SerializedName("namespace") String namespace) throws IOException, RpcException;
}

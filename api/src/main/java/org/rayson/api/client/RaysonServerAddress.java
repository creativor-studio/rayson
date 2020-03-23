/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Stands for an Rayson server address.
 * 
 * @author creativor
 */
public class RaysonServerAddress {

	private final int port;

	private final InetSocketAddress socketAddress;
	private boolean usingSsl = false;

	/**
	 * 
	 * @param address Server address.
	 * @param port Port number of the server.
	 * @throws IllegalArgumentException If the arguments is illegal.
	 */
	public RaysonServerAddress(final InetAddress address, final int port) throws IllegalArgumentException {
		this(address, port, false);
	}

	/**
	 * 
	 * @param address Server address.
	 * @param port Port number of the server.
	 * @param usingSsl Whether using SSL protocol.
	 * @throws IllegalArgumentException If the arguments is illegal.
	 */
	public RaysonServerAddress(final InetAddress address, final int port, boolean usingSsl) throws IllegalArgumentException {
		if (address == null)
			throw new IllegalArgumentException("address should not be null");

		if (port <= 0)
			throw new IllegalArgumentException("Port number should be larger than 0");

		this.port = port;
		this.socketAddress = new InetSocketAddress(address, port);
		this.usingSsl = usingSsl;
	}

	/**
	 * @param address Server socket address.
	 * @throws IllegalArgumentException If the arguments is illegal.
	 */
	public RaysonServerAddress(final InetSocketAddress address) throws IllegalArgumentException {
		if (address == null)
			throw new IllegalArgumentException("address should not be null");

		this.socketAddress = address;
		this.port = address.getPort();
		this.usingSsl = false;
	}

	/**
	 * @param address Server socket address.
	 * @param usingSsl Whether using SSL protocol.
	 * @throws IllegalArgumentException If the arguments is illegal.
	 */
	public RaysonServerAddress(final InetSocketAddress address, boolean usingSsl) throws IllegalArgumentException {
		if (address == null)
			throw new IllegalArgumentException("address should not be null");

		this.socketAddress = address;
		this.port = address.getPort();
		this.usingSsl = usingSsl;
	}

	/**
	 * 
	 * @param ip Server IP address.
	 * @param port Port number of the server.
	 * @throws IllegalArgumentException If the arguments is illegal.
	 * @throws UnknownHostException If the ip address is not determined.
	 */
	public RaysonServerAddress(final String ip, final int port) throws IllegalArgumentException, UnknownHostException {
		this(ip, port, false);
	}

	/**
	 * 
	 * @param ip Server IP address.
	 * @param port Port number of the server.
	 * @param usingSsl Whether using SSL protocol.
	 * @throws IllegalArgumentException If the arguments is illegal.
	 * @throws UnknownHostException If the ip address is not determined.
	 */
	public RaysonServerAddress(final String ip, final int port, boolean usingSsl) throws IllegalArgumentException, UnknownHostException {
		this(InetAddress.getByName(ip), port, usingSsl);
	}

	/**
	 * @return Port number of this address.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return The IP socket address of this server address.
	 */
	public InetSocketAddress getSocketAddress() {
		return socketAddress;
	}

	/**
	 * @return Whether using SSL to communicating with server. By default,
	 *         <code>false</code>.
	 */
	public boolean isUsingSsl() {
		return usingSsl;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		final Object[] values = { socketAddress, usingSsl };
		final String[] keys = { "socketAddress", "usingSsl" };
		final int keyLen = keys.length;
		sb.append("{");
		for (int i = 0; i < keyLen; i++) {
			sb.append(keys[i]);
			sb.append(":");
			sb.append(values[i]);
			if (i != keyLen - 1)
				sb.append(", ");
		}
		sb.append("}");
		return sb.toString();
	}
}
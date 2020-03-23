/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.tools.profiler;

import java.net.UnknownHostException;

import org.rayson.tools.console.ConsoleArgument;
import org.rayson.tools.console.ConsoleException;

/**
 *
 * @author creativor
 */
final class UrlArgument implements ConsoleArgument<ProfilerParameters> {
	private int port;
	private String serverIp;

	public UrlArgument(String serverIp, int port) {
		super();
		this.serverIp = serverIp;
		this.port = port;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the serverIp
	 */
	public String getServerIp() {
		return serverIp;
	}

	@Override
	public void setupParameters(ProfilerParameters parameters) throws ConsoleException {
		try {
			parameters.setUrl(this);
		} catch (IllegalArgumentException | UnknownHostException e) {
			throw new ConsoleException("Failed to setup parameters", e);
		}
	}
}
/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.rayson.demo.mybuddy;

import java.io.IOException;

import org.rayson.api.annotation.UrlMapping;
import org.rayson.api.exception.RpcException;

/**
 * @author Nick Zhang
 *
 */
@UrlMapping("/")
class MyBuddyService implements MyBuddyProtocol {
	@UrlMapping("/")
	@Override
	public void action(String command) throws IOException, RpcException {
		if (command == null)
			throw new IllegalArgumentException("command should not be null");

		// get the i-Buddy
		IBuddy iBuddy = IBuddy.getIBuddy();
		try {
			iBuddy.sendCommand(command);
		} catch (IBuddyException e) {
			throw new RpcException(500, e.getMessage());
		}
	}

	@Override
	public String help() throws IOException, RpcException {
		StringBuffer sb = new StringBuffer();
		for (Action action : Action.values()) {
			sb.append(action.name());
			sb.append(":");
			sb.append(action.getDescription());
			sb.append("\n");
		}
		sb.append("--------------------------------\n");
		sb.append("Usage:\n");
		sb.append("COMMAND_NAME:1 means turn on the command\n");
		sb.append("COMMAND_NAME:0 means turn off the command\n");
		sb.append("COMMAND1;COMMAND2 means combind the two commands\n");
		sb.append("\n");
		return sb.toString();
	}

}
/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.rayson.demo.mybuddy;

import java.io.IOException;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.Service;
import org.rayson.api.exception.RpcException;

/**
 * @author Nick Zhang
 *
 */
@Service
public interface MyBuddyProtocol extends Protocol {
	public void action(String cmd) throws IOException, RpcException;

	public String help() throws IOException, RpcException;

}

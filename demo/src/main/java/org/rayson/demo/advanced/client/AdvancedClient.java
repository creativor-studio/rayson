/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.advanced.client;

import org.glassfish.json.JsonUtil;
import org.rayson.api.client.ProxyConfig;
import org.rayson.api.client.RaysonServerAddress;
import org.rayson.client.Rayson;
import org.rayson.demo.advanced.api.AdvancedProtocol;
import org.rayson.demo.advanced.api.SerialObject;
import org.rayson.rson.element.ObjectElement;
import org.rayson.share.serial.rson.RsonUtil;

/**
 *
 * @author creativor
 */
public class AdvancedClient {

	public static void main(final String[] args) throws Exception {

		final RaysonServerAddress serverAddr = new RaysonServerAddress("localhost", 8080);
		ProxyConfig config = new ProxyConfig();
		config.addFilter(ClientLogFilter.class);
		config.addHttpFilter(ClientAuthHttpFilter.class);

		AdvancedProtocol testProtocol;
		testProtocol = Rayson.createProxy(serverAddr, AdvancedProtocol.class, config);
		System.out.println(testProtocol.echo("Hello World"));

		SerialObject msg = new SerialObject();
		msg.setIntField(12345);
		msg.setStrField("12345");

		SerialObject response = testProtocol.echo2(msg);
		System.out.println(response);
		if (!response.equals(msg)) {
			System.err.println("Message " + msg + " not equals to the response " + response);
		}

		String jsonString = "{\"a\":\"b\",\"c\":3}";
		ObjectElement rsonMsg = (ObjectElement) RsonUtil.fromJson(JsonUtil.toJson(jsonString));
		ObjectElement rsonResp = testProtocol.echoRson(rsonMsg);
		System.out.println(rsonResp);
		if (!rsonResp.equals(rsonMsg)) {
			System.err.println("Message " + msg + " not equals to the response " + response);
		}
	}
}

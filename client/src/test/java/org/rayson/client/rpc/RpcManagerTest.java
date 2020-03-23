/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client.rpc;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.rayson.api.client.Proxy;
import org.rayson.api.client.ProxyConfig;
import org.rayson.api.client.RaysonServerAddress;
import org.rayson.api.server.ServerDiscovery;

/**
 * @author Nick Zhang
 *
 */
public class RpcManagerTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.rayson.client.rpc.RpcManager#nextCallId()}.
	 */
	@Test
	public void testNextCallId() {
		RpcManager m = new RpcManager();
		Assert.assertEquals(m.nextCallId(), m.nextCallId() - 1);
	}

	/**
	 * Test method for
	 * {@link org.rayson.client.rpc.RpcManager#createProxy(org.rayson.api.client.RaysonServerAddress, java.lang.Class, org.rayson.api.client.ProxyConfig)}
	 * .
	 * 
	 */
	@Test
	public void testCreateProxy() throws Exception {
		// Test proxy logic.
		RpcManager manager = new RpcManager();
		RaysonServerAddress serverAddr = new RaysonServerAddress("127.0.0.1", 3345);
		Class<ServerDiscovery> protocol = ServerDiscovery.class;
		ProxyConfig config = new ProxyConfig();
		ServerDiscovery proxy = manager.createProxy(serverAddr, protocol, config);
		assertTrue(proxy instanceof Proxy);
		assertSame(((Proxy) proxy).getConfig(), config);
	}
}
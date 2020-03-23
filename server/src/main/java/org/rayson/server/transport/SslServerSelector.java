/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.server.transport;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import org.rayson.server.Container;
import org.rayson.share.transport.SelSslSupportImpl;
import org.rayson.share.transport.SelectorSslSupport;
import org.rayson.share.transport.SslNioSelector;

/**
 * SSL version of {@link ServerSelector}.
 * 
 * @author creativor
 *
 */
public class SslServerSelector extends ServerSelector implements SslNioSelector {

	private SelectorSslSupport sslSupport;

	/**
	 * @param name Name of this new selector.
	 * @param selector Underling channel selector.
	 * @param socketChannel Underling socket channel.
	 * @param container Server container instance .
	 */
	public SslServerSelector(String name, Selector selector, ServerSocketChannel socketChannel, Container container) {
		super(name, selector, socketChannel, container);
		this.sslSupport = new SelSslSupportImpl(this);
	}

	@Override
	protected void preSelect() {
		super.preSelect();
		pollSupplementReads();
		pollSupplementWrites();
	}

	@Override
	public void addReadEvent(SelectionKey key) {

		sslSupport.addReadEvent(key);
	}

	@Override
	public void addWriteEvent(SelectionKey key) {

		sslSupport.addWriteEvent(key);
	}

	@Override
	public void clearReadEvents() {
		sslSupport.clearReadEvents();
	}

	@Override
	public void clearWriteEvents() {

		sslSupport.clearReadEvents();
	}

	@Override
	public void pollSupplementReads() {
		sslSupport.pollSupplementReads();
	}

	@Override
	public void pollSupplementWrites() {
		sslSupport.pollSupplementWrites();
	}
}

/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client.transport;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import org.rayson.share.transport.SelSslSupportImpl;
import org.rayson.share.transport.SelectorSslSupport;
import org.rayson.share.transport.SslNioSelector;

/**
 * SSL version of {@link ClientSelector}.
 * 
 * @author creativor
 *
 */
public class SslClientSelector extends ClientSelector implements SslNioSelector {

	/**
	 * @param selector Native underling {@link Selector}.
	 * @param name Name of this selector.
	 * 
	 */
	public SslClientSelector(Selector selector, String name) {
		super(selector, name);
		this.sslSupport = new SelSslSupportImpl(this);
	}

	private SelectorSslSupport sslSupport;

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
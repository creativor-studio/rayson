/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client.rpc;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.rayson.api.client.ClientMessage;
import org.rayson.api.struct.KeyValues;

/**
 * An abstract implementation of {@link ClientMessage}, with basic functions.
 * 
 * @author creativor
 */
abstract class AbstractClientMessage implements ClientMessage {
	private final Map<String, KeyValues> headers;

	protected AbstractClientMessage() {
		this.headers = new HashMap<>();
	}

	@Override
	public final void addHeader(String name, String value) {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("name should not be empty");
		if (value == null || value.isEmpty())
			throw new IllegalArgumentException("value should not be empty");
		KeyValues item = headers.get(name);
		if (item == null) {
			item = new KeyValues(name);
			headers.put(name, item);
		}
		item.addValue(value);
	}

	@Override
	public final Collection<KeyValues> getAllHeaders() {
		return Collections.unmodifiableCollection(headers.values());
	}

	@Override
	public final KeyValues getHeaders(String name) {
		if (name == null)
			throw new IllegalArgumentException("name should not be null");
		return headers.get(name);
	}
}
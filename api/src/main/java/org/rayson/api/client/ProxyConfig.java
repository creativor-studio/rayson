/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rayson.api.annotation.NotNull;

/**
 * Configuration setting of RPC proxy.
 * 
 * @see Proxy About RPC proxy.
 * @author creativor
 */
public class ProxyConfig {

	private List<Class<? extends ClientFilter>> filters;
	private List<Class<? extends HttpClientFilter>> httpFilters;

	/**
	 * Construct proxy configuration with default settings.
	 */
	public ProxyConfig() {
		filters = new ArrayList<>();
		httpFilters = new ArrayList<>();
	}

	/**
	 * Add {@link ClientFilter} definition to the target RPC proxy.
	 * 
	 * @param filter Client filter to be added to this configuration.
	 * @throws IllegalArgumentException if the filter is <code>null</code>.
	 */
	public void addFilter(@NotNull Class<? extends ClientFilter> filter) {
		if (filter == null)
			throw new IllegalArgumentException("filter should not be null");
		filters.add(filter);
	}

	/**
	 * Add {@link HttpClientFilter} definition to the target RPC proxy.
	 * 
	 * @param filter HTTP client filter to be added to this configuration.
	 * @throws IllegalArgumentException if the filter is <code>null</code>.
	 */
	public void addHttpFilter(@NotNull Class<? extends HttpClientFilter> filter) {
		if (filter == null)
			throw new IllegalArgumentException("filter should not be null");
		httpFilters.add(filter);
	}

	/**
	 * @return Client filter list. Unmodifiable.
	 */
	public List<Class<? extends ClientFilter>> getFilters() {
		return Collections.unmodifiableList(filters);
	}

	/**
	 * @return Client HTTP filter list. Unmodifiable.
	 */
	public List<Class<? extends HttpClientFilter>> getHttpFilters() {
		return Collections.unmodifiableList(httpFilters);
	}
}
/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client.http;

import org.rayson.api.client.HttpClientResponse;

/**
 * Implementation for {@link HttpResponseCallback}.
 * 
 * @author creativor
 */
class ResponseCallbackImpl implements HttpResponseCallback {

	private final HttpCallFuture future;

	ResponseCallbackImpl(final HttpCallFuture future) {
		this.future = future;
	}

	@Override
	public void onResponse(final HttpClientResponse response) {
		future.setResponse(response);
	}

	@Override
	public void onFailed(Throwable failCause) {
		future.setError(failCause);
	}
}
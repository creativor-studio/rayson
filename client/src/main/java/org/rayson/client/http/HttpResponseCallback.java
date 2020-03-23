/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client.http;

import org.rayson.api.client.HttpClientResponse;

/**
 * An tool used as callback function when the invoking result of an RPC request
 * is received.
 * 
 * @author creativor
 */
public interface HttpResponseCallback {
	/**
	 * Notify this call back function that an error which cause the response
	 * failed.
	 * 
	 * @param failCause Cause error make the response failed.
	 */
	public void onFailed(Throwable failCause);

	/**
	 * Notify this call back function that the response is ready.
	 * 
	 * @param response Received HTTP response from server.
	 */
	public void onResponse(HttpClientResponse response);
}
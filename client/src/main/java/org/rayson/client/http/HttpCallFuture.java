/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client.http;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.rayson.api.client.HttpClientResponse;
import org.rayson.api.exception.ProtocolException;

/**
 * {@link ClientRpcCall} future.
 * 
 * @author creativor
 */
final class HttpCallFuture {
	private HttpClientResponse response;
	private Throwable error;
	private boolean responsed = false;
	private ReentrantLock lock;
	private Condition responseCon;

	/**
	 * Construct an new future with default fields initialized.
	 */
	public HttpCallFuture() {
		lock = new ReentrantLock();
		responseCon = lock.newCondition();
	}

	/**
	 * Set the error that cause the call failed.
	 * 
	 * @param cause The exception cause the error.
	 */
	void setError(final Throwable cause) {
		lock.lock();
		try {
			this.error = cause;
			responsed = true;
			responseCon.signalAll();
		} finally {
			lock.unlock();
		}
	}

	void setResponse(final HttpClientResponse response) {
		lock.lock();
		try {
			this.response = response;
			responsed = true;
			responseCon.signalAll();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Causes the current thread to wait until it is signalled or interrupted.
	 * 
	 * @return HTTP response result.
	 * @throws InterruptedException if the current thread is interrupted (and
	 *             interruption of thread suspension is supported)
	 * @throws IOException If network error cause the call failed.
	 * @throws ProtocolException If wrong protocol format error cause the call
	 *             failed.
	 */
	public HttpClientResponse waitFor() throws InterruptedException, IOException, ProtocolException {
		lock.lock();
		try {
			while (!responsed) {
				responseCon.await();
			}
		} finally {
			lock.unlock();
		}

		if (error != null) {

			if (error instanceof IOException)
				throw (IOException) error;

			if (error instanceof ProtocolException)
				throw (ProtocolException) error;

			throw new UndeclaredThrowableException(error);

		}

		return response;
	}
}
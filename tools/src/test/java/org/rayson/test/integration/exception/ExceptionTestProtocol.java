
package org.rayson.test.integration.exception;

import java.io.IOException;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.Service;
import org.rayson.api.exception.RpcException;

@Service
public interface ExceptionTestProtocol extends Protocol {
	/**
	 * An test method just throw an {@link RpcException}.
	 * 
	 * @throws RpcException
	 * @throws IOException
	 */
	public void throwMethod() throws RpcException, IOException;

	public void voidMethod() throws RpcException, IOException;
}

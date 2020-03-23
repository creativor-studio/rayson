
package org.rayson.test.integration.exception;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.rayson.api.client.HttpClientFilter;
import org.rayson.api.client.HttpClientRequest;
import org.rayson.api.client.HttpClientResponse;
import org.rayson.api.client.HttpFilterChain;
import org.rayson.api.client.Proxy;
import org.rayson.api.exception.ClientFilterException;
import org.rayson.api.exception.RpcException;
import org.rayson.api.http.HttpHeader;
import org.rayson.api.http.HttpResponseStatus;
import org.rayson.share.http.HttpHeaderImpl;
import org.rayson.test.integration.BaseFlowTest;
import org.rayson.test.integration.TestStrategy;

public class ExceptionFlowTest extends BaseFlowTest<ExceptionTestProtocol> {

	private static class TestClientFilter implements HttpClientFilter {

		@Override
		public void doFilter(HttpClientRequest request, HttpClientResponse response, HttpFilterChain chain) throws ClientFilterException {
			HttpHeader header = new HttpHeaderImpl("TOO_LONG_HEADER",
					"XXXXXXXXXXX_XXXXXXXXXXXXXX_XXXXXXXXXXX_XXXXXXXXXXXXXX_XXXXXXXXXXX_XXXXXXXXXXXXXX_XXXXXXXXXXX_XXXXXXXXXXXXXX_"
							+ "XXXXXXXXXXX_XXXXXXXXXXXXXX_XXXXXXXXXXX_XXXXXXXXXXXXXX_XXXXXXXXXXX_XXXXXXXXXXXXXX_XXXXXXXXXXX_XXXXXXXXXXXXXX_"
							+ "XXXXXXXXXXX_XXXXXXXXXXXXXX_XXXXXXXXXXX_XXXXXXXXXXXXXX_XXXXXXXXXXX_XXXXXXXXXXXXXX_XXXXXXXXXXX_XXXXXXXXXXXXXX_"
							+ "XXXXXXXXXXX_XXXXXXXXXXXXXX_XXXXXXXXXXX_XXXXXXXXXXXXXX_XXXXXXXXXXX_XXXXXXXXXXXXXX_XXXXXXXXXXX_XXXXXXXXXXXXXX_"
							+ "XXXXXXXXXXX_XXXXXXXXXXXXXX_XXXXXXXXXXX_XXXXXXXXXXXXXX_XXXXXXXXXXX_XXXXXXXXXXXXXX_XXXXXXXXXXX_XXXXXXXXXXXXXX_"
							+ "XXXXXXXXXXX_XXXXXXXXXXXXXX_XXXXXXXXXXX_XXXXXXXXXXXXXX_XXXXXXXXXXX_XXXXXXXXXXXXXX_XXXXXXXXXXX_XXXXXXXXXXXXXX_" + "XXXXXXXXXXX_XXXXXXXXXXXXXX_");
			request.addHeader(header);
			chain.doFilter(request, response);
		}

		@Override
		public void init(Proxy proxy) {

		}
	}

	private static class ExceptionService implements ExceptionTestProtocol {

		@Override
		public void throwMethod() throws RpcException, IOException {
			throw new RpcException(HttpResponseStatus.FORBIDDEN);
		}

		@Override
		public void voidMethod() throws RpcException, IOException {

		}

	}

	@Override
	protected ExceptionTestProtocol createService() {
		return new ExceptionService();
	}

	@Test
	public void testBasicException() {
		try {
			getProxy().throwMethod();
			Assert.fail("should throw exception before come here");
		} catch (RpcException e) {
			Assert.assertEquals(e.getCode(), HttpResponseStatus.FORBIDDEN.getCode());

		} catch (IOException e) {

			Assert.fail("should not throw " + e);
		}
	}

	@Test
	@TestStrategy(clientFilter = TestClientFilter.class)
	public void testMalformedRequestException() {
		try {
			getProxy().voidMethod();
			Assert.fail("should throw exception before come here");
		} catch (RpcException e) {
			Assert.assertEquals(e.getCode(), HttpResponseStatus.HEADER_TOO_LARGE.getCode());

		} catch (Exception e) {

			Assert.fail("should not throw " + e);
		}
	}
}

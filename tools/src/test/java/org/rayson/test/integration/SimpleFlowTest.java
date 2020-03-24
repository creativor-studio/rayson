/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.test.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.rayson.api.client.HttpClientFilter;
import org.rayson.api.client.HttpClientRequest;
import org.rayson.api.client.HttpClientResponse;
import org.rayson.api.client.HttpFilterChain;
import org.rayson.api.client.Proxy;
import org.rayson.api.exception.ClientFilterException;
import org.rayson.api.exception.RpcException;
import org.rayson.api.http.HttpConstants;
import org.rayson.api.http.HttpContentType;
import org.rayson.api.http.HttpMessage;
import org.rayson.client.ClientSystem;
import org.rayson.share.http.HttpHeaderImpl;
import org.rayson.share.serial.ContentCoderFactory;
import org.rayson.test.integration.protocol.TestProtocol;
import org.rayson.test.integration.protocol.TestService;
import org.rayson.test.integration.protocol.TestStruct;

/**
 * An simple testing implements {@link BaseFlowTest}.
 * 
 * @author clouder
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ContentCoderFactory.class, ClientSystem.class })
@PowerMockIgnore({ "org.apache.http.conn.ssl.*", "javax.net.ssl.*", "javax.crypto.*" })
public class SimpleFlowTest extends BaseFlowTest<TestProtocol> {

	private static class ContentTypeFilter implements HttpClientFilter {

		@Override
		public void doFilter(HttpClientRequest request, HttpClientResponse response, HttpFilterChain chain) throws ClientFilterException {
			// HttpHeader header =
			// request.getHeader(HttpConstants.CONTENT_TYPE_HEADER_NAME);
			//
			// try {
			// MemberMatcher.field(HttpHeader.class, "value").set(header,
			// HttpContentType.FORM_URLENCODED.typeName());
			// } catch (IllegalArgumentException | IllegalAccessException e) {
			// Assert.fail(e.getMessage());
			// }
			chain.doFilter(request, response);
		}

		@Override
		public void init(Proxy proxy) {

		}

	}

	@Override
	protected TestProtocol createService() {
		return new TestService();
	}

	/**
	 * Do test all kinds of serialization.
	 */
	private void doTestSerialization() {
		try {
			// Test string.
			String str = "hello world";
			Object strr = getProxy().echo(str);
			assertEquals(str, strr);

			// Test byte array.
			byte[] sr2 = getProxy().echoBytes(str.getBytes());
			assertTrue(Arrays.equals(str.getBytes(), sr2));

			// Test byte.
			byte b = 3;
			byte br = getProxy().echoByte(b);
			assertEquals(b, br);

			// Test short.
			short s = 3;
			short sr = getProxy().echoShort(s);
			assertEquals(s, sr);

			// Test iterger.
			int i = 3;
			int ir = getProxy().echoInt(i);
			assertEquals(i, ir);

			// Test double.
			double d = 3.23;
			double dr = getProxy().echoDouble(d);
			assertTrue(d == dr);

			// Test long.
			long l = (long) 3.345;
			long lr = getProxy().echoLong(l);
			assertEquals(l, lr);

			// Test long.
			float f = 3.345f;
			float fr = getProxy().echoFloat(f);
			assertTrue(f == fr);

			// Test multiple parameters.
			String str1 = "one";
			int int2 = 22;
			String concatRes = getProxy().concat(str1, int2);
			assertEquals(concatRes, str1 + Integer.toString(int2));

			// Test structure.
			TestStruct testStruct = new TestStruct(12, false, "adfs", 345);
			TestStruct structR = getProxy().echoBean(testStruct);
			assertEquals(testStruct, structR);

		} catch (Exception e) {
			fail("doTestSerialization got error: " + e.getMessage());
		}
	}

	/**
	 * Test FORM_URLENCODED content type.
	 */
	@Test
	@TestStrategy(clientFilter = ContentTypeFilter.class)
	public void testFormContentType() {

		try {

			// Change default content coder
			ClientSystem client = ClientSystem.getSystem();

			ClientSystem mockClient = PowerMockito.mock(ClientSystem.class, new Answer() {
				@Override
				public Object answer(InvocationOnMock inv) throws Throwable {
					if (inv.getMethod().getName() == "getContentCoder") {
						return ContentCoderFactory.FORM_SERIALIZER;
					}
					if (inv.getMethod().getName() == "getContentTypeHeader") {
						return new HttpHeaderImpl(HttpConstants.CONTENT_TYPE_HEADER_NAME,
								HttpContentType.FORM_URLENCODED.typeName() + "; charset=" + HttpMessage.CHARSET.name());
					}
					return inv.getMethod().invoke(client, inv.getArguments());
				}
			});
			PowerMockito.mockStatic(ClientSystem.class);
			PowerMockito.when(ClientSystem.getSystem()).thenReturn(mockClient);

			doTestSerialization();

			Mockito.reset();

		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test JSON content type.
	 */
	@Test
	@TestStrategy(clientFilter = ContentTypeFilter.class)
	public void testJsonContentType() {

		try {

			// Change default content coder
			ClientSystem client = ClientSystem.getSystem();

			ClientSystem mockClient = PowerMockito.mock(ClientSystem.class, new Answer() {
				@Override
				public Object answer(InvocationOnMock inv) throws Throwable {
					if (inv.getMethod().getName() == "getContentCoder") {
						return ContentCoderFactory.JSON_SERIALIZER;
					}
					if (inv.getMethod().getName() == "getContentTypeHeader") {
						return new HttpHeaderImpl(HttpConstants.CONTENT_TYPE_HEADER_NAME,
								HttpContentType.JSON.typeName() + "; charset=" + HttpMessage.CHARSET.name());
					}
					return inv.getMethod().invoke(client, inv.getArguments());
				}
			});
			PowerMockito.mockStatic(ClientSystem.class);
			PowerMockito.when(ClientSystem.getSystem()).thenReturn(mockClient);

			doTestSerialization();

			Mockito.reset();

		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test RSON content type.
	 */
	@Test
	@TestStrategy(clientFilter = ContentTypeFilter.class)
	public void testRsonContentType() {

		try {

			// Change default content coder
			ClientSystem client = ClientSystem.getSystem();

			ClientSystem mockClient = PowerMockito.mock(ClientSystem.class, new Answer() {
				@Override
				public Object answer(InvocationOnMock inv) throws Throwable {
					if (inv.getMethod().getName() == "getContentSerializer") {
						return ContentCoderFactory.RSON_SERIALIZER;
					}
					if (inv.getMethod().getName() == "getContentTypeHeader") {
						return new HttpHeaderImpl(HttpConstants.CONTENT_TYPE_HEADER_NAME,
								HttpContentType.RSON.typeName() + "; charset=" + HttpMessage.CHARSET.name());
					}
					return inv.getMethod().invoke(client, inv.getArguments());
				}
			});
			PowerMockito.mockStatic(ClientSystem.class);
			PowerMockito.when(ClientSystem.getSystem()).thenReturn(mockClient);

			doTestSerialization();

			Mockito.reset();

		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testSerialization() throws IOException, RpcException {
		doTestSerialization();
	}

	@Test
	@TestStrategy(usingSsl = true)
	public void testSsl() {
		try {
			getProxy().touch();
			Assert.assertTrue(true);
		} catch (IOException | RpcException e) {
			Assert.fail("touch rpc server failed: " + e.getMessage());
		}
	}

	@Test
	@TestStrategy(usingSsl = true)
	public void testSslSerialization() throws IOException, RpcException {
		doTestSerialization();
	}

	@Test
	public void testTouch() {
		try {
			getProxy().touch();
			Assert.assertTrue(true);
		} catch (IOException | RpcException e) {
			Assert.fail("touch rpc server failed: " + e.getMessage());
		}
	}
}
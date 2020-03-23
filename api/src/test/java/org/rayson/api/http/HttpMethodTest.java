/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HttpMethodTest {

	/**
	 * (Description)
	 *
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * (Description)
	 *
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * (Description)
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * (Description)
	 *
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testLookup() {
		ByteBuffer testBytes = ByteBuffer.wrap("POST dddd 1.1".getBytes(HttpHeader.CHARSET));

		assertEquals(HttpMethod.lookAheadGet(testBytes, 0, testBytes.limit()), HttpMethod.POST);

		testBytes = ByteBuffer.wrap("GET".getBytes(HttpHeader.CHARSET));

		assertEquals(HttpMethod.lookAheadGet(testBytes, 0, testBytes.limit()), HttpMethod.GET);

		testBytes = ByteBuffer.wrap("POS dddd 1.1".getBytes(HttpHeader.CHARSET));

		assertEquals(HttpMethod.lookAheadGet(testBytes, 0, testBytes.limit()), HttpMethod.GET);

		testBytes = ByteBuffer.wrap("PO".getBytes(HttpHeader.CHARSET));

		assertEquals(HttpMethod.lookAheadGet(testBytes, 0, testBytes.limit()), HttpMethod.POST);

		testBytes = ByteBuffer.wrap("POSd dddd 1.1".getBytes(HttpHeader.CHARSET));

		assertEquals(HttpMethod.lookAheadGet(testBytes, 0, 2), HttpMethod.POST);
	}

	@Test
	public final void testMatchMethod() {
		ByteBuffer testBytes = ByteBuffer.wrap("POST dddd 1.1".getBytes(HttpHeader.CHARSET));

		assertTrue(HttpMethod.matchMethod(testBytes, 0, testBytes.limit()));

		testBytes = ByteBuffer.wrap("GET".getBytes(HttpHeader.CHARSET));

		assertTrue(HttpMethod.matchMethod(testBytes, 0, testBytes.limit()));

		testBytes = ByteBuffer.wrap("POS dddd 1.1".getBytes(HttpHeader.CHARSET));

		assertFalse(HttpMethod.matchMethod(testBytes, 0, testBytes.limit()));

		testBytes = ByteBuffer.wrap("PO".getBytes(HttpHeader.CHARSET));

		assertTrue(HttpMethod.matchMethod(testBytes, 0, testBytes.limit()));

		testBytes = ByteBuffer.wrap("POSd dddd 1.1".getBytes(HttpHeader.CHARSET));

		assertTrue(HttpMethod.matchMethod(testBytes, 0, 2));
	}

}

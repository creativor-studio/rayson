/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.jwt;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @author creativor
 *
 */
public class TestJwt {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {

		Algorithm algorithm = Algorithm.HMAC256("secret");
		Builder builder = JWT.create();
		builder.withIssuer("auth0");
		String token = builder.sign(algorithm);

		System.out.println(token);

		JWTVerifier verifier = JWT.require(algorithm).build();
		verifier.verify(token);

		DecodedJWT jwt = JWT.decode(token);

		System.out.println(jwt);

	}

}

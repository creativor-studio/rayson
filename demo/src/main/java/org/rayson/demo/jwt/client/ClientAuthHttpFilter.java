/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.jwt.client;

import java.io.UnsupportedEncodingException;

import org.rayson.api.client.HttpClientFilter;
import org.rayson.api.client.HttpClientRequest;
import org.rayson.api.client.HttpClientResponse;
import org.rayson.api.client.HttpFilterChain;
import org.rayson.api.client.Proxy;
import org.rayson.api.exception.ClientFilterException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

/**
 * @author Nick Zhang
 *
 */
public class ClientAuthHttpFilter implements HttpClientFilter {
	private static final String AUTH_HEADER_KEY = "Authorization";
	private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer ";

	@Override
	public void doFilter(HttpClientRequest request, HttpClientResponse response, HttpFilterChain chain) throws ClientFilterException {

		try {

			Algorithm algorithm;
			algorithm = Algorithm.HMAC256("secret");

			String token = JWT.create().withIssuer("auth0").sign(algorithm);

			request.addHeader(AUTH_HEADER_KEY, AUTH_HEADER_VALUE_PREFIX + token);

		} catch (IllegalArgumentException | UnsupportedEncodingException e) {

			throw new ClientFilterException("Failed to generate JWT token");
		}

		System.out.println("Sending HTTP request " + request + " ...");
		chain.doFilter(request, response);
	}

	@Override
	public void init(Proxy proxy) {
		// Do nothing.
	}
}
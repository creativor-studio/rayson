/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.jwt.server;

import java.io.UnsupportedEncodingException;

import org.rayson.api.http.HttpHeader;
import org.rayson.api.http.HttpResponseStatus;
import org.rayson.api.server.HttpFilterChain;
import org.rayson.api.server.HttpServerFilter;
import org.rayson.api.server.HttpServerRequest;
import org.rayson.api.server.HttpServerResponse;
import org.rayson.api.server.Server;
import org.rayson.share.logger.RaysonLoggerFactory;
import org.slf4j.Logger;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @author Nick Zhang
 *
 */
public class ServerAuthHttpFilter implements HttpServerFilter {
	private static final String AUTH_HEADER_KEY = "Authorization";
	private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer ";
	private static Logger LOG = RaysonLoggerFactory.getLogger(ServerAuthHttpFilter.class);

	@Override
	public void doFilter(HttpServerRequest request, HttpServerResponse response, HttpFilterChain chain) {

		String token = getBearerToken(request);

		DecodedJWT jwt;

		try {

			if (token == null)
				throw new JWTDecodeException("no token found");

			jwt = JWT.decode(token);

			// Then, find out the user info with the secret.

			Algorithm algorithm = Algorithm.HMAC256("secret");

			JWTVerifier verifier = JWT.require(algorithm).build();

			verifier.verify(token);

			chain.doFilter(request, response);

		} catch (JWTDecodeException e) {

			response.setException(HttpResponseStatus.UNAUTHORIZED, "No JWT provided");

		} catch (JWTVerificationException | IllegalArgumentException | UnsupportedEncodingException e) {

			response.setException(HttpResponseStatus.UNAUTHORIZED, "JWT verify failed");

		}

	}

	/**
	 * Get the bearer token from the HTTP request. The token is in the HTTP
	 * request "Authorization" header in the form of: "Bearer [token]"
	 */
	private String getBearerToken(HttpServerRequest request) {
		HttpHeader authHeader = request.getHeader(AUTH_HEADER_KEY);
		if (authHeader != null && authHeader.getValue().startsWith(AUTH_HEADER_VALUE_PREFIX)) {
			return authHeader.getValue().substring(AUTH_HEADER_VALUE_PREFIX.length());
		}
		return null;
	}

	@Override
	public void init(Server server) {
		// Do nothing.
	}
}
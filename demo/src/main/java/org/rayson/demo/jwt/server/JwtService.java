/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.jwt.server;

import java.io.IOException;

import org.rayson.api.annotation.UrlMapping;
import org.rayson.demo.jwt.api.JwtProtocol;

/**
 *
 * @author creativor
 */
public class JwtService implements JwtProtocol {

	@Override
	@UrlMapping("/")
	public String echo(final String msg) throws IOException {
		return msg;
	}

}
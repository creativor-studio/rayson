/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.tools.profiler;

import org.rayson.tools.console.ConsoleArgumentDef;
import org.rayson.tools.console.ConsoleException;

/**
 *
 * @author creativor
 */
final class UrlArgumentDef implements ConsoleArgumentDef<UrlArgument> {

	@Override
	public boolean match(String parameter, int index) {
		return true;
	}

	@Override
	public UrlArgument parse(final String parameter) throws ConsoleException {
		int splitorIndex = parameter.indexOf(':');
		String ip = parameter.substring(0, splitorIndex);
		int port = Integer.valueOf(parameter.substring(splitorIndex + 1));
		return new UrlArgument(ip, port);
	}
}

/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.tools.profiler;

import org.rayson.tools.console.ConsoleException;
import org.rayson.tools.console.ConsoleOptionDef;

/**
 * @author Nick Zhang
 *
 */
public class SslOptionDef extends ConsoleOptionDef<SslOption> {

	@Override
	public boolean hasParameter() {
		return false;
	}

	@Override
	public boolean match(String option) {
		return "-s".equalsIgnoreCase(option) || "--ssl".equalsIgnoreCase(option);
	}

	@Override
	public SslOption parse(String parameter) throws ConsoleException {
		return new SslOption();
	}
}
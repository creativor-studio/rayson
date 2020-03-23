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
class CallCountDef extends ConsoleOptionDef<CallCountOption> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rayson.tools.console.ConsoleOptionDef#hasParameter()
	 */
	@Override
	public boolean hasParameter() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rayson.tools.console.ConsoleOptionDef#match(java.lang.String)
	 */
	@Override
	public boolean match(String option) {
		return "-c".equalsIgnoreCase(option);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rayson.tools.console.ConsoleOptionDef#parse(java.lang.String)
	 */
	@Override
	public CallCountOption parse(String parameter) throws ConsoleException {
		return new CallCountOption(Integer.parseInt(parameter));
	}
}

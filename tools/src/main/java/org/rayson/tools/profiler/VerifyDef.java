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
class VerifyDef extends ConsoleOptionDef<VerifyOption> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rayson.tools.console.ConsoleOptionDef#hasParameter()
	 */
	@Override
	public boolean hasParameter() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rayson.tools.console.ConsoleOptionDef#match(java.lang.String)
	 */
	@Override
	public boolean match(String option) {
		return "-e".equalsIgnoreCase(option);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rayson.tools.console.ConsoleOptionDef#parse(java.lang.String)
	 */
	@Override
	public VerifyOption parse(String parameter) throws ConsoleException {
		return new VerifyOption();
	}
}
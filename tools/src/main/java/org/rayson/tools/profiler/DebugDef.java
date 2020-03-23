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
class DebugDef extends ConsoleOptionDef<DebugOption> {

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
		return "-v".equalsIgnoreCase(option);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rayson.tools.console.ConsoleOptionDef#parse(java.lang.String)
	 */
	@Override
	public DebugOption parse(String parameter) throws ConsoleException {
		return new DebugOption();
	}
}
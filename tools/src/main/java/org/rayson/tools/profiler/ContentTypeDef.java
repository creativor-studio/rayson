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
class ContentTypeDef extends ConsoleOptionDef<ContentTypeOption> {

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
		return "-ct".equalsIgnoreCase(option);
	}

	@Override
	public ContentTypeOption parse(String parameter) throws ConsoleException {
		return new ContentTypeOption(parameter);

	}
}

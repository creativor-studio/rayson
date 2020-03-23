/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.tools.console;

import org.rayson.api.annotation.Nullable;

/**
 * An option definition for {@link RConsole}.
 * 
 * @author creativor
 */
public abstract class ConsoleOptionDef<T extends ConsoleOption> {
	/**
	 * @return True if this option should has an parameter.Or else
	 *         <code>false</code>.
	 */
	public abstract boolean hasParameter();

	/**
	 * @return True if the option string match this option. Or else,
	 *         <code>false</code>.
	 */
	public abstract boolean match(String option);

	/**
	 * Parse out an option object.
	 * 
	 * @param parameter
	 *            Parameter string may be null.
	 * @return Parsed out option.
	 * @throws ConsoleException
	 *             If got any error.
	 */
	public abstract T parse(@Nullable String parameter) throws ConsoleException;
}
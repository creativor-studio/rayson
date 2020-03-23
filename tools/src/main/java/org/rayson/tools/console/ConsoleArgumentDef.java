/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.tools.console;

/**
 * An argument definition for {@link RConsole}. * @author creativor
 */
public interface ConsoleArgumentDef<T extends ConsoleArgument> {
	/**
	 * @param parameter
	 *            Parameter string.
	 * @param index
	 *            Paraeter index. Start with 0.
	 * @return True if the parameter string match the giving index. Or else,
	 *         <code>false</code>.
	 */
	public abstract boolean match(String parameter, int index);

	/**
	 * Parsed out an argument by giving a parameter string.
	 * 
	 * @param parameter
	 * @return
	 * @throws ConsoleException
	 *             If got any error.
	 */
	public T parse(String parameter) throws ConsoleException;
}

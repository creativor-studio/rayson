/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.tools.console;

/**
 * An option for {@link RConsole}.
 *
 * @author creativor
 */
public interface ConsoleArgument<P extends ConsoleParameters> {
	public void setupParameters(P parameters) throws ConsoleException;
}

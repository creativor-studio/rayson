/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.tools.console;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define the options and arguments definitions in {@link RConsole}
 * implementations.
 * 
 * @author creativor
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface ConsoleConfig {
	/**
	 * @return Arguments definition list.Ordered.
	 */
	public Class<? extends ConsoleArgumentDef<?>>[] arugments() default {};

	/**
	 * @return Optional definition list.Ordered.
	 */
	public Class<? extends ConsoleOptionDef<?>>[] options() default {};

}

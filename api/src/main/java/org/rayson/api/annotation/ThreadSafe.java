/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the target element is a thread-safe one or not. By default, it
 * means the target element is thread-safe.
 * 
 * @author creativor
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
public @interface ThreadSafe {
	/**
	 * @return The target element is thread-safe or not. By default, the value
	 *         is <code>true</code> .
	 */
	public boolean value() default true;
}

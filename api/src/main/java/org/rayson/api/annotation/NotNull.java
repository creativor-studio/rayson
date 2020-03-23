/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the target element should not be null object.
 * 
 * @author creativor
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.PARAMETER,
		java.lang.annotation.ElementType.LOCAL_VARIABLE })
public @interface NotNull {
	/**
	 * @return Reason about why should not be null.
	 */
	public String value() default "";
}

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
 * It is used to document an RSON format parameter on API.
 * 
 * @author creativor
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@DocumentedAnnotation
public @interface ApiParam {
	/**
	 * @return Description of target parameter.
	 */
	public String description();

	/**
	 * @return Name of target parameter.
	 */
	public String name();

	/**
	 * Indicate whether the target parameter is required.
	 * 
	 * @return True if the target parameter is required.
	 */
	public boolean required() default true;
}
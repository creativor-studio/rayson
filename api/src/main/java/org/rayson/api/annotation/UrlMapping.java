/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define the API path of the target method or class on specified Rayson
 * service. TODO: Add the mapping rule description.
 * 
 * @author creativor
 */
@Target(value = { ElementType.METHOD, ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface UrlMapping {
	/**
	 * @return Mapping rule.
	 */
	public String value();
}

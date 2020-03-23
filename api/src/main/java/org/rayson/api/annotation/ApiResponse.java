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
 * API response parameters description in Rayson API.
 * 
 * @author Nick Zhang
 *
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DocumentedAnnotation
public @interface ApiResponse {
	/**
	 * @return Parameters document list.
	 */
	ApiParam[] value();
}

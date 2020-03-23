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

import org.rayson.api.Protocol;
import org.rayson.api.server.ServiceFilter;

/**
 * Configuration of an {@link Protocol} service implementation in server.
 * 
 * @author creativor
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface ServiceConfig {

	/**
	 * @return Service filters of the target service. Ordered.
	 */
	public abstract Class<? extends ServiceFilter>[] filters() default {};

}
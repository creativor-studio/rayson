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

/**
 * If an {@link Protocol} interface annotated with this annotation, it means
 * that this {@link Protocol} can be used as a service of Rayson RPC.<br>
 * <br>
 * 
 * ### What is Service in Rayson Server<br>
 * <br>
 * - Provides a series of functions on some kind of resource managed by the
 * server.<br>
 * - Those functions defined in an interface as java method which implements
 * Protocol interface.<br>
 * - A class implements an service interface, called Service Implementation.
 * <br>
 * - An initialized service implementation object, and registered into the
 * server, called Service Instance.<br>
 * 
 * 
 * @author creativor
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface Service {
	/**
	 * @return Service description.
	 */
	public String value() default "";
}
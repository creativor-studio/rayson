/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.rayson.api.serial.RsonSerializable;

/**
 * An annotation that indicates this member should be serialized to JSON with
 * the provided name value as its field name.
 * 
 * @author Nick Zhang
 * @see RsonSerializable About how to serialize objects to JSON.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@DocumentedAnnotation
public @interface SerializedName {
	/**
	 * @return New field name used to serialize target field to Json string.
	 */
	public String value();
}

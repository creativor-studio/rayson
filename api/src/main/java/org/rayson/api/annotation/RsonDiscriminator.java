/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.rayson.api.annotation.RsonSubType.RsonSubTypes;
import org.rayson.api.serial.RsonSerializable;

/**
 * Specifies the discriminator column for strategies of mapping the RSON element
 * to {@link RsonSerializable} type .<br>
 * Example :
 * 
 * <pre>
 * <code>
class TestPolymorphicBean implements RsonSerializable {
	int intF = 9;
	&#64;RsonDiscriminator
	String type = "basic";
	TestPolymorphicBean() {
	}
}
</code>
 * </pre>
 *
 * @see RsonSubTypes
 *
 */
@Target({ ElementType.FIELD })
@Retention(RUNTIME)
@Documented
public @interface RsonDiscriminator {
}
/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.rayson.api.annotation.RsonSubType.RsonSubTypes;
import org.rayson.api.serial.RsonSerializable;

/**
 * Definition of a subtype, along with optional name. If name is missing, class
 * of the type will be checked for {@link RsonDiscriminator} annotation; and if
 * that is also missing or empty, a default name will be constructed by type id
 * mechanism. Default name is usually based on class name.
 */
@Repeatable(RsonSubTypes.class)
public @interface RsonSubType {
	/**
	 * @return Class of the sub type.
	 */
	public Class<? extends RsonSerializable> value();

	/**
	 * @return Logical type name used as the type identifier for the class.
	 */
	public String name() default "";

	/**
	 * Annotation used with {@link RsonDiscriminator} to indicate sub-types of
	 * serializable polymorphic types, and to associate logical names used
	 * within RSON content (which is more portable than using physical Java
	 * class names).
	 * <p>
	 * Note that just annotating a property or base type with this annotation
	 * does NOT enable polymorphic type handling: in addition,
	 * {@link RsonDiscriminator} or equivalent (such as enabling of so-called
	 * "default typing") annotation is needed, and only in such case is subtype
	 * information used.
	 */
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface RsonSubTypes {
		/**
		 * Subtypes of the annotated type (annotated class, or property value
		 * type associated with the annotated method). These will be checked
		 * recursively so that types can be defined by only including direct
		 * subtypes.
		 * 
		 * @return Sub type.
		 */
		public RsonSubType[] value();
	}
}
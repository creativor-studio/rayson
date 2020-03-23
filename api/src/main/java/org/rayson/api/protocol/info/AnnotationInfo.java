/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.protocol.info;

import javax.json.JsonString;
import javax.json.stream.JsonGenerator;

/**
 * Description an method annotation of {@link MethodInfo}.
 * 
 * @author Nick Zhang
 *
 */
public class AnnotationInfo implements MetadataInfo<JsonString> {
	private String text;

	AnnotationInfo() {

	}

	/**
	 * Construct a new annotation information with it's text.
	 * 
	 * @param text Annotation information text.
	 */
	public AnnotationInfo(String text) {
		super();
		this.text = text;
	}

	@Override
	public void fromJson(JsonString json) {
		this.text = json.toString();
	}

	/**
	 * @return Text presentation of this information.
	 */
	public String getText() {
		return text;
	}

	@Override
	public void toJson(JsonGenerator generator) {
		generator.write(text);
	}
}

/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.tools.profiler;

import org.rayson.tools.console.ConsoleOption;

/**
 * @author Nick Zhang
 *
 */
class ContentTypeOption implements ConsoleOption<ProfilerParameters> {
	private String contentType;

	ContentTypeOption(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}

	@Override
	public void setupParameters(ProfilerParameters parameters) {
		parameters.setContentType(contentType);
	}
}
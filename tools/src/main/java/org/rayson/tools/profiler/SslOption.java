/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.tools.profiler;

import org.rayson.tools.console.ConsoleOption;

class SslOption implements ConsoleOption<ProfilerParameters> {

	@Override
	public void setupParameters(ProfilerParameters parameters) {
		parameters.setUsingSsl();
	}

}

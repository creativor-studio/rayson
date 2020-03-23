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
class ThreadCountOption implements ConsoleOption<ProfilerParameters> {
	private int count;

	ThreadCountOption(int count) {
		this.count = count;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	@Override
	public void setupParameters(ProfilerParameters parameters) {
		parameters.setThreadCount(getCount());
	}
}
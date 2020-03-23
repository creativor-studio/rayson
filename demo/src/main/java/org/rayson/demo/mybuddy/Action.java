/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.rayson.demo.mybuddy;

/**
 * @author Nick Zhang
 *
 */
public enum Action {
	B("blue color"), BLUE("blue color"), C("set color, example C:1:0:1"), CLEAR("reset meassge"),
	EXEC("execute messages"), G("green color"), GREEN("green color"), HEART("set heart, example H:1"),
	L("reset meassge"), MR("flick right"), R("red color"), RED("red color"), RESET("reset message and execute them"),
	S("sleep"), SAPHIRE("saphire color"), SLEEP("sleep"), VIOLET("violet color"), WD("wing down"), WU("wing up"),
	X("execute messages"), YELLOW("yellow color"), Z("reset message and execute them");
	private String description;

	private Action(String cmd) {
		this.description = cmd;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
}

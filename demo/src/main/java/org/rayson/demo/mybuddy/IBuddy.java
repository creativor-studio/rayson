/*
This source is part of the
     _____  ___   ____
 __ / / _ \/ _ | / __/___  _______ _
/ // / , _/ __ |/ _/_/ _ \/ __/ _ `/
\___/_/|_/_/ |_/_/ (_)___/_/  \_, /
                             /___/
repository. It is in the public domain.
Contact BoD@JRAF.org for more information.

$Id: IBuddy.java 265 2008-09-07 14:37:02Z bod $
*/
package org.rayson.demo.mybuddy;

/**
 * Main entry point to i-Buddy commanding. Note: currently, one and only one
 * i-Buddy can be manipulated with this library. Therefore, this class should be
 * used as a singleton, even if this not enforced by the code.
 */
public class IBuddy {

	/**
	 * Different colors for the head (all possible combinations of the blue,
	 * green and red LEDs).
	 */
	public enum Color {
		RED, GREEN, BLUE, YELLOW, WHITE
	}

	/**
	 * Different wing positions.
	 */
	public enum Wings {
		AT_EASE, DOWN, UP
	}

	/**
	 * Different rotation states.
	 */
	public enum Rotate {
		AT_EASE, LEFT, RIGHT
	}

	/**
	 * Gets the i-Buddy.
	 * 
	 * @return the i-Buddy.
	 */
	public static IBuddy getIBuddy() {
		return new IBuddy();
	}

	private IBuddy() {
	}

	synchronized void sendCommand(String command) throws IBuddyException {
		StringBuffer bashCmd = new StringBuffer();
		bashCmd.append("echo -e \"");
		bashCmd.append(command.replaceAll(";", "\\\\n"));
		bashCmd.append("\" > /dev/udp/127.0.0.1/8888");
		System.out.println("Executing bash command\" " + bashCmd + " \"");
		try {
			Process process = Runtime.getRuntime().exec(new String[] { "bash", "-c", bashCmd.toString() });
			process.waitFor();
		} catch (Exception e) {
			throw new IBuddyException("Failed to send command to ibuddy", e);
		}
	}
}
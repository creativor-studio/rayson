/*
This source is part of the
     _____  ___   ____
 __ / / _ \/ _ | / __/___  _______ _
/ // / , _/ __ |/ _/_/ _ \/ __/ _ `/
\___/_/|_/_/ |_/_/ (_)___/_/  \_, /
                             /___/
repository. It is in the public domain.
Contact BoD@JRAF.org for more information.

$Id: IBuddyException.java 265 2008-09-07 14:37:02Z bod $
*/
package org.rayson.demo.mybuddy;

/**
 * General purpose exception for JLibIBuddy.
 */
public class IBuddyException extends Exception { 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IBuddyException(String message) {
        super(message);
    }

    public IBuddyException(String message, Throwable cause) {
        super(message, cause);
    }
}

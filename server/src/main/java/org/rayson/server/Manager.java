/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.server;

import org.rayson.server.exception.InternalServerError;

/**
 * An management role in Rayson server system to manage some kind of task.
 * 
 * @author Nick Zhang
 *
 */
public interface Manager {
	/**
	 * @return Container which hold this manager.
	 */
	public Container getContainer();

	/**
	 * @return Name of this manager.
	 */
	public String getName();

	/**
	 * Start this manager, make it ready to work.
	 * 
	 * @throws InternalServerError If failed to start this manager.
	 */
	abstract void start() throws InternalServerError;

	/**
	 * Stop this manager, make it offline.
	 * 
	 * @throws InternalServerError If failed to stop this manager.
	 */
	abstract void stop() throws InternalServerError;
}

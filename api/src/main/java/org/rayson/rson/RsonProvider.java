/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.ServiceLoader;

import org.rayson.api.serial.ByteArrayInputBuffer;

/**
 * Service provider for RSON processing objects.
 *
 * <p>
 * All the methods in this class are safe for use by multiple concurrent
 * threads.
 *
 * @see ServiceLoader
 */
public abstract class RsonProvider {

	/**
	 * A constant representing the name of the default {@code JsonProvider}
	 * implementation class.
	 */
	private static final String DEFAULT_PROVIDER = "org.rayson.share.serial.rson.RsonProviderImpl";

	/**
	 *
	 * Creates a JSON provider object. The provider is loaded using the
	 * {@link ServiceLoader#load(Class)} method. If there are no available
	 * service providers, this method returns the default service provider.
	 *
	 * @see ServiceLoader
	 * @return a JSON provider
	 */
	static RsonProvider provider() {
		ServiceLoader<RsonProvider> loader = ServiceLoader.load(RsonProvider.class);
		Iterator<RsonProvider> it = loader.iterator();
		if (it.hasNext()) {
			return it.next();
		}

		try {
			@SuppressWarnings("unchecked")
			Class<? extends RsonProvider> clazz = (Class<? extends RsonProvider>) Class.forName(DEFAULT_PROVIDER);
			return clazz.getDeclaredConstructor().newInstance();
		} catch (ClassNotFoundException x) {
			throw new RsonException("Provider " + DEFAULT_PROVIDER + " not found", x);
		} catch (Exception x) {
			throw new RsonException("Provider " + DEFAULT_PROVIDER + " could not be instantiated: " + x, x);
		}
	}

	/**
	 * Default constructor.
	 */
	protected RsonProvider() {
	}

	/**
	 * Creates a JSON parser from a byte array buffer.
	 * 
	 * @param buffer Json byte array source data in buffer.
	 * @param charset The character set which the target {@link RsonElement}
	 *            used.
	 * @return Result parser.
	 */
	public abstract RsonParser createParser(final ByteArrayInputBuffer buffer, Charset charset);

	/**
	 * Creates a RSON builder.
	 *
	 * @return a RSON builder.
	 */
	public abstract RsonBuilder createBuilder();

}
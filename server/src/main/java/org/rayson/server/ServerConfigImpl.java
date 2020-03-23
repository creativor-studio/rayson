
package org.rayson.server;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.rayson.api.annotation.NotNull;
import org.rayson.api.server.ServerConfig;
import org.rayson.share.config.ConfigBase;
import org.rayson.share.logger.RaysonLoggerFactory;
import org.slf4j.Logger;

/**
 * Implementation of {@link ConfigBase} in {@link RaysonServer}.
 * 
 * @author Nick Zhang
 *
 */
public class ServerConfigImpl extends ConfigBase<ServerConfigImpl> implements ServerConfig {
	/**
	 * Basic directory name of the server configuration.
	 */
	public static final String BASE_DIRECTORY = "server";

	private static final Logger LOGGER = RaysonLoggerFactory.getLogger(ServerConfigImpl.class);

	/**
	 * Load configuration. First, load the default configuration. Then load and
	 * merge the configuration from file.
	 * 
	 * @param portNumber Port number of the target server.
	 * @return Target server configuration setting.
	 */
	public static ServerConfigImpl load(int portNumber) {

		ServerConfigImpl defaultConfig = loadDefault();
		String relativePath = BASE_DIRECTORY + File.separatorChar + "server.cnf";
		try {
			defaultConfig.merge(relativePath);
		} catch (IOException e) {
			LOGGER.warn(e.getMessage());
			// Ignore it.
		}

		relativePath = BASE_DIRECTORY + File.separatorChar + "server." + portNumber + ".cnf";
		try {
			defaultConfig.merge(relativePath);
		} catch (IOException e) {
			LOGGER.warn(e.getMessage());
			// Ignore it.
		}

		return defaultConfig;
	}

	/**
	 * Load server default configuration.
	 * 
	 * @return Loaded server default configuration.
	 */
	public static ServerConfigImpl loadDefault() {
		return ConfigBase.newDefault(ServerConfigImpl.class);
	}

	private ServerConfigImpl() {
		super();
	}

	@Override
	protected void buildDefault() {
		for (ConfigKey key : ConfigKey.values()) {
			this.set(key.getToken(), key.getDefaultValue());
		}
	}

	@Override
	public boolean getAsBoolean(@NotNull ConfigKey key) {
		if (key == null)
			throw new IllegalArgumentException("key should not be null");
		String v = get(key.getToken());
		if (v == null)
			return false;
		return Boolean.valueOf(v);
	}

	@Override
	public Number getAsNumber(@NotNull ConfigKey key) throws NumberFormatException {
		if (key == null)
			throw new IllegalArgumentException("key should not be null");
		String v = get(key.getToken());
		if (v == null)
			return -1;
		return Double.valueOf(v);
	}

	@Override
	public String getValue(@NotNull ConfigKey key) {
		if (key == null)
			throw new IllegalArgumentException("key should not be null");
		String v = get(key.getToken());
		return v;
	}

	/**
	 * Set boolean value of giving key.
	 * 
	 * @param key Key which the value to be set.
	 * @param value Boolean value to set.
	 * @throws IllegalArgumentException If the key is <code>null</code>.
	 */
	public void setValue(@NotNull ConfigKey key, boolean value) {
		if (key == null)
			throw new IllegalArgumentException("key should not be null");
		set(key.getToken(), Objects.toString(value));
	}

	/**
	 * Set number value of giving key.
	 * 
	 * @param key Key which the value to be set.
	 * @param value Number value to set.
	 * @throws IllegalArgumentException If the key is <code>null</code>.
	 */
	public void setValue(@NotNull ConfigKey key, Number value) {
		if (key == null)
			throw new IllegalArgumentException("key should not be null");
		set(key.getToken(), Objects.toString(value));
	}

	/**
	 * Set string value of giving key.
	 * 
	 * @param key Key which the value to be set.
	 * @param value String value to set.
	 * @throws IllegalArgumentException If the key is <code>null</code>.
	 */
	public void setValue(@NotNull ConfigKey key, String value) {
		if (key == null)
			throw new IllegalArgumentException("key should not be null");
		set(key.getToken(), value);
	}
}

package org.rayson.client;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.rayson.api.annotation.NotNull;
import org.rayson.api.http.HttpContentType;
import org.rayson.share.config.ConfigBase;

/**
 * Client side configuration object.
 * 
 * @author creativor
 *
 */
public class ClientConfig extends ConfigBase<ClientConfig> {
	/**
	 * Basic directory name of the client configuration.
	 */
	public static final String BASE_DIRECTORY = "client";

	/**
	 * Supported key enum for {@link ClientConfig}.
	 */
	public static enum ConfigKey {
		/**
		 * Persistent network connection keepalive timeout setting, in seconds.
		 */
		NETWORK_KEEPALIVE_TIMEOUT("network.keepalive.timeout", "120", "Persistent network connection keepalive timeout setting, in seconds"),
		/**
		 * HTTP content type using in serialization.
		 */
		HTTP_CONTENT_TYPE("http.content.type", HttpContentType.RSON.typeName(), "HTTP content type using in serialization"),

		/**
		 * Ssl key store file password.
		 */
		SSL_KEY_PASSWORD("ssl.key.password", "", "Ssl key store file password"),
		/**
		 * Ssl strust store file name.
		 */
		SSL_TRUSTSTORE_FILE("ssl.truststore.file", "truststore.jks", "Ssl trust store file name"),
		/**
		 * Ssl key store file password.
		 */
		SSL_TRUSTSTORE_PASSWORD("ssl.truststore.password", "", "Ssl strust store file password");

		private String defaultValue;
		private String description;
		private String token;

		private ConfigKey(String token, String defaultValue, String description) {
			this.token = token;
			this.defaultValue = defaultValue;
			this.description = description;
		}

		/**
		 * @return Description of this configuration key.
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @return Token string of this configuration key.
		 */
		public String getToken() {
			return token;
		}
	}

	ClientConfig() {
		super();
	}

	/**
	 * Load configuration. First, load the default configuration. Then load and
	 * merge the configuration from file.
	 * 
	 * @return Loaded configuration.
	 */
	public static ClientConfig load() {

		ClientConfig defaultConfig = loadDefault();
		String relativePath = BASE_DIRECTORY + File.separatorChar + "client.cnf";
		try {
			defaultConfig.merge(relativePath);
		} catch (IOException e) {
			// Ignore it.
		}

		return defaultConfig;
	}

	/**
	 * @return Load default configuration instance.
	 */
	public static ClientConfig loadDefault() {
		return ConfigBase.newDefault(ClientConfig.class);
	}

	@Override
	protected void buildDefault() {
		for (ConfigKey key : ConfigKey.values()) {
			this.set(key.getToken(), key.defaultValue);
		}
	}

	/**
	 * Get boolean value of giving <code>key</code>.
	 * 
	 * @param key Configuration key.
	 * @return Boolean value of giving key. Or <code>false</code> if not found.
	 * @throws IllegalArgumentException If the key is <code>null</code>.
	 */
	public boolean getAsBoolean(@NotNull ConfigKey key) {
		if (key == null)
			throw new IllegalArgumentException("key should not be null");
		String v = get(key.getToken());
		if (v == null)
			return false;
		return Boolean.valueOf(v);
	}

	/**
	 * 
	 * @param key Configuration key.
	 * @return Number value matched with key. Or <code>-1</code> if no matched
	 *         found.
	 * @throws NumberFormatException If the string does not contain a parsable
	 *             number.
	 * @throws IllegalArgumentException If the key is <code>null</code>.
	 */
	public Number getAsNumber(@NotNull ConfigKey key) throws NumberFormatException {
		if (key == null)
			throw new IllegalArgumentException("key should not be null");
		String v = get(key.getToken());
		if (v == null)
			return -1;
		return Double.valueOf(v);
	}

	/**
	 * Get string value of giving <code>key</code>.
	 * 
	 * @param key Configuration key.
	 * @return String value of giving key. Or <code>null</code> if not found.
	 * @throws IllegalArgumentException If the key is <code>null</code>.
	 */
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
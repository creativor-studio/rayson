/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.server;

/**
 * Configuration setting of {@link Server}.
 * 
 * @author Nick Zhang
 *
 */
public interface ServerConfig {
	/**
	 * Supported key enum for {@link ServerConfig} implementation.
	 */
	public static enum ConfigKey {
		/**
		 * Persistent network connection keepalive timeout setting, in seconds.
		 */
		NETWORK_KEEPALIVE_TIMEOUT("network.keepalive.timeout", "15", "Persistent network connection keepalive timeout setting, in seconds"),
		/**
		 * Whether the server using ssl for http protocol. By default, false.
		 */
		SSL_ENABLED("ssl.enabled", "false", "Whether the server using ssl for http protocol"),
		/**
		 * Ssl key store file password.
		 */
		SSL_KEY_PASSWORD("ssl.key.password", "", "Ssl key store file password"),
		/**
		 * Ssl key store file name.
		 */
		SSL_KEYSTORE_FILE("ssl.keystore.file", "keystore.jks", "Ssl key store file name"),
		/**
		 * Ssl key store file password.
		 */
		SSL_KEYSTORE_PASSWORD("ssl.keystore.password", "", "Ssl key store file password");

		private String defaultValue;
		private String description;
		private String token;

		private ConfigKey(String token, String defaultValue, String description) {
			this.token = token;
			this.defaultValue = defaultValue;
			this.description = description;
		}

		/**
		 * @return The default value of this key.
		 */
		public String getDefaultValue() {
			return defaultValue;
		}

		/**
		 * @return Description of this key.
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @return The token of this key.
		 */
		public String getToken() {
			return token;
		}
	}

	/**
	 * Get an server configuration value of given key, as boolean.
	 * 
	 * @param key Configuration key.
	 * @return Boolean value matched with key. Or <code>false</code> if no
	 *         matched found.
	 */
	public boolean getAsBoolean(ConfigKey key);

	/**
	 * Get an server configuration value of given key.
	 * 
	 * @param key Configuration key.
	 * @return Number value matched with key. Or <code>-1</code> if no matched
	 *         found.
	 * @throws NumberFormatException If the string does not contain a parsable
	 *             number.
	 */
	public Number getAsNumber(ConfigKey key) throws NumberFormatException;

	/**
	 * Get an server configuration string value of given key.
	 * 
	 * @param key Configuration key.
	 * @return String value matched with key. Or <code>null</code> if no matched
	 *         found.
	 */
	public String getValue(ConfigKey key);
}
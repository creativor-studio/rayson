/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.protocol.info;

import javax.annotation.processing.Generated;

import org.rayson.api.serial.RsonSerializable;
import org.rayson.api.serial.JsonStringBuilder;

/**
 * Rayson Server information.
 * 
 * @author creativor
 */
public class ServerInfo implements RsonSerializable {
	/**
	 * Builder to build {@link ServerInfo}.
	 */
	public static final class Builder {
		private ServerInfo wrapped;

		private Builder() {
			this.wrapped = new ServerInfo();
		}

		/**
		 * Builder method of the builder.
		 * 
		 * @return built class
		 */
		public ServerInfo build() {
			return wrapped;
		}

		/**
		 * Builder method for portNumer parameter.
		 * 
		 * @param portNumber Port number to set.
		 * @return builder
		 */
		public Builder withPortNumber(int portNumber) {
			wrapped.portNumber = portNumber;
			return this;
		}

		/**
		 * Builder method for sslEnabled parameter.
		 * 
		 * @param sslEnabled SSL enable property to set.
		 * @return builder
		 */
		public Builder withSslEnabled(boolean sslEnabled) {
			wrapped.sslEnabled = sslEnabled;
			return this;
		}

		/**
		 * Builder method for version parameter.
		 * 
		 * @param version Version number to set.
		 * @return builder
		 */
		public Builder withVersion(int version) {
			wrapped.version = version;
			return this;
		}
	}

	/**
	 * Creates builder to build {@link ServerInfo}.
	 * 
	 * @return created builder
	 */
	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	private int portNumber;

	private boolean sslEnabled;

	private int version;

	@Generated("SparkTools")
	private ServerInfo() {
	}

	/**
	 * @return the version number of this server.
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @return the port number of this server.
	 */
	public int getPortNumber() {
		return portNumber;
	}

	/**
	 * @return the SSL enable property of this server.
	 */
	public boolean isSslEnabled() {
		return sslEnabled;
	}

	@Override
	public String toString() {
		JsonStringBuilder b = new JsonStringBuilder(this);
		b.append("sslEnabled", sslEnabled);
		b.append("portNumber", portNumber);
		b.append("version", version);
		return b.toJson();
	}
}
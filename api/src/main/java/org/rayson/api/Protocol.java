/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api;

import java.io.IOException;

import org.rayson.api.exception.ProtocolException;
import org.rayson.api.exception.RpcException;
import org.rayson.api.serial.RsonSerializable;

/**
 * An {@link Protocol} is an Rayson API definition. Typically, one type of
 * resource which managed by server has one protocol, which define the
 * manipulations of the resource . <br>
 * It obeys the following rules:<br>
 * <br>
 * # Method parameters type and return type:<br>
 * <br>
 * - Primitive types: boolean, short, int, long, float, double, String.<br>
 * - Simple enum.<br>
 * - {@link RsonSerializable} object.<br>
 * - rray list of upper types.<br>
 * - Byte array.<br>
 * <br>
 * # Each method should throws the following checked exceptions, which must be
 * defined in each method explicitly.<br>
 * <br>
 * - {@link IOException} to indicate the network error.<br>
 * - {@link RpcException} to indicate that the server handling the remote
 * error.<br>
 * - {@link ProtocolException} , an unchecked exception indicate that response
 * from server is malformed according to the communicating protocol.<br>
 * 
 */
public interface Protocol {

}
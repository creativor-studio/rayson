/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api;

import java.io.IOException;

import org.rayson.api.exception.ProtocolException;
import org.rayson.api.exception.RpcException;
import org.rayson.rson.RsonType;

/**
 * An {@link Protocol} is an Rayson API definition. Typically, one type of
 * resource which managed by server has one protocol, which define the
 * manipulations of the resource . <br>
 * It follows the rules:<br>
 * <br>
 * # Method parameters type and it's return type can be serialized. <br>
 * - Supported {@link org.rayson.rson serializable types}.<br>
 * <br>
 * * # Each method should throws the following checked exceptions, which must be
 * defined in each method explicitly.<br>
 * - {@link IOException} to indicate the network error.<br>
 * - {@link RpcException} to indicate that the server handling the remote
 * error.<br>
 * - {@link ProtocolException} An unchecked exception indicate that response
 * from server is malformed according to the communicating protocol.<br>
 * 
 * @see org.rayson.rson About RSON serializing structured data.
 * @see RsonType About supported serialization type.
 */
public interface Protocol {

}
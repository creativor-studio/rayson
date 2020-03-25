/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.rayson.api.exception.ProtocolException;
import org.rayson.api.exception.RpcException;
import org.rayson.api.serial.RsonSerializable;
import org.rayson.rson.RsonType;

/**
 * An {@link Protocol} is an Rayson API definition. Typically, one type of
 * resource which managed by server has one protocol, which define the
 * manipulations of the resource . <br>
 * It follows the rules:<br>
 * <br>
 * # Method parameters type and it's return type can be serialized. <br>
 * All serializable type listed in the following table:<br>
 * 
 * <pre>
<table>
    <tr>
        <td> Java Type</td>
        <td> {@link RsonType} </td>
        <td> Notes </td>
    </tr>
    <tr>
        <td> null </td>
        <td> {@link RsonType#NULL} </td>
        <td> </td>
    </tr>
    <tr>
        <td> {@link Boolean#TRUE} </td>
        <td> {@link RsonType#TRUE} </td>
        <td> </td>
    </tr>
    <tr>
        <td> {@link Boolean#FALSE} </td>
        <td> {@link RsonType#FALSE} </td>
        <td> </td>
    </tr>
    <tr>
        <td> {@link Byte} </td>
        <td> {@link RsonType#BYTE} </td>
        <td> </td>
    </tr>
    <tr>
        <td> {@link Short} </td>
        <td> {@link RsonType#SHORT} </td>
        <td> </td>
    </tr>
    <tr>
        <td> {@link Integer} </td>
        <td> {@link RsonType#INT} </td>
        <td> </td>
    </tr>
    <tr>
        <td> {@link Float} </td>
        <td> {@link RsonType#FLOAT} </td>
        <td> </td>
    </tr>
    <tr>
        <td> {@link Long} </td>
        <td> {@link RsonType#LONG} </td>
        <td> </td>
    </tr>
    <tr>
        <td>{@link String} or {@link Enum} </td>
        <td> {@link RsonType#STRING} </td>
        <td> Only simple {@link Enum} is supported </td>
    </tr>
    <tr>
        <td> array list </td>
        <td> {@link RsonType#ARRAY} or {@link RsonType#BIG_ARRAY} </td>
        <td> </td>
    </tr>
    <tr>
        <td> {@link RsonSerializable} </td>
        <td> {@link RsonType#OBJECT} or {@link RsonType#BIG_OBJECT}</td>
        <td> All fields can be serialized  </td>
    </tr>
    <tr>
        <td> byte array </td>
        <td> {@link RsonType#BYTE_ARRAY} </td>
        <td> </td>
    </tr>
    <tr>
        <td> {@link Map} </td>
        <td> {@link RsonType#OBJECT} </td>
        <td> With serializable parameterized type</td>
    </tr>
    <tr>
        <td> {@link List} </td>
        <td> {@link RsonType#ARRAY} </td>
        <td> With serializable parameterized type</td>
    </tr>
</table>
 * </pre>
 * 
 * # Each method should throws the following checked exceptions, which must be
 * defined in each method explicitly.<br>
 * <br>
 * - {@link IOException} to indicate the network error.<br>
 * - {@link RpcException} to indicate that the server handling the remote
 * error.<br>
 * - {@link ProtocolException} An unchecked exception indicate that response
 * from server is malformed according to the communicating protocol.<br>
 * 
 * @see RsonType About supported serialization type.
 */
public interface Protocol {

}
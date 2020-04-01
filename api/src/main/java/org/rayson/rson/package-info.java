/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

/**
 * RSON (Rayson Object Notation) is a lightweight data-interchange format. It's
 * language-neutral, platform-neutral, extensible mechanism for serializing
 * structured data.
 * 
 * <pre>
<table border="1">
	<caption>All supported serializable types</caption>
    <tr>
        <th>Java Type</th>
        <th> {@link RsonType} </th>
        <th> Notes </th>
    </tr>
    <tr>
        <td>null</td>
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
        <td> {@link Double} </td>
        <td> {@link RsonType#DOUBLE} </td>
        <td> </td>
    </tr>
    <tr>
        <td>{@link String} or {@link Enum} </td>
        <td> {@link RsonType#STRING} </td>
        <td> Only simple {@link Enum} is supported </td>
    </tr>
    <tr>
        <td>array list</td>
        <td> {@link RsonType#ARRAY}</td>
        <td> The array item type is serializable</td>
    </tr>
    <tr>
        <td>array list</td>
        <td>{@link RsonType#BIG_ARRAY} </td>
        <td> Items count larger than 127.</td>
    </tr>
    <tr>
        <td> {@link org.rayson.api.serial.RsonSerializable} </td>
        <td> {@link RsonType#OBJECT}</td>
        <td> All fields can be serialized  </td>
    </tr>
    <tr>
        <td> {@link org.rayson.api.serial.RsonSerializable} </td>
        <td> {@link RsonType#BIG_OBJECT}</td>
        <td> All fields can be serialized, and fields count larger than 127 </td>
    </tr>
    <tr>
        <td>byte array</td>
        <td> {@link RsonType#BYTE_ARRAY} </td>
        <td> </td>
    </tr>
    <tr>
        <td> {@link java.util.Map} </td>
        <td> {@link RsonType#OBJECT} </td>
        <td> With serializable parameterized type</td>
    </tr>
    <tr>
        <td> {@link java.util.List} </td>
        <td> {@link RsonType#ARRAY} </td>
        <td> With serializable parameterized type</td>
    </tr>
</table>
 * </pre>
 * 
 */

package org.rayson.rson;

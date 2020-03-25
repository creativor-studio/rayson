/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api;

/**
 * A FilterChain is an object provided by the client or server to the developer,
 * which giving a view into the invocation chain of a filtered request for a
 * service. Filters use the FilterChain to invoke the next filter in the chain,
 * or if the calling filter is the last filter in the chain, to invoke the
 * service at the end of the chain. <br>
 * Typically, a filter chain hash a method as following:
 * 
 * <pre>
 * <code>
*	public void doFilter(Request request, Response response);
 * </code>
 * </pre>
 * 
 * @author Nick Zhang
 *
 */
public interface FilterChain {

}
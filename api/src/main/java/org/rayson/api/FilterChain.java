/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api;

/**
 * A FilterChain is an object provided by the client and server to the developer
 * giving a view into the invocation chain of a filtered request for a service.
 * Filters use the FilterChain to invoke the next filter in the chain, or if the
 * calling filter is the last filter in the chain, to invoke the service at the
 * end of the chain. <br>
 * Typically, a filter chain hash a method as following:
 * 
 * <pre>
 * <code>
 * 	&#47;**
*	 * Causes the next filter in the chain to be invoked, or if the calling
*	 * filter is the last filter in the chain, causes the resource at the end of
*	 * the chain to be invoked.
*	 *
*	 *&#47;
*	public void doFilter(Request request, Response response);
 * </code>
 * </pre>
 * 
 * @author Nick Zhang
 *
 */
public interface FilterChain {

}
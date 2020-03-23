/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.rayson.demo.stock.api;

import java.io.IOException;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.Service;
import org.rayson.api.exception.RpcException;

/**
 * Stock service.
 * 
 * @author creativor
 */
@Service
public interface StockService extends Protocol {
	public Stock get(String symbol) throws IOException, RpcException;
}

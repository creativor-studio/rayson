/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.rayson.demo.stock.server;

import java.io.IOException;

import org.rayson.api.annotation.UrlMapping;
import org.rayson.api.exception.RpcException;
import org.rayson.api.http.HttpResponseStatus;
import org.rayson.demo.stock.api.Stock;
import org.rayson.demo.stock.api.StockDividend;
import org.rayson.demo.stock.api.StockQuote;
import org.rayson.demo.stock.api.StockService;
import org.rayson.demo.stock.api.StockStats;

import yahoofinance.YahooFinance;

/**
 *
 * @author creativor
 */

@UrlMapping("/")
public class StockServiceImpl implements StockService {

	@Override
	@UrlMapping("/")
	public Stock get(String symbol) throws IOException, RpcException {
		if (symbol == null || symbol.isEmpty())
			throw new IllegalArgumentException("symbol should not be empty");
		yahoofinance.Stock stock;
		try {
			stock = YahooFinance.get(symbol);

		} catch (Exception e) {

			throw new RpcException(
					HttpResponseStatus.INTERNAL_SERVER_ERROR.getCode(),
					"Can not search stock information from provider");

		}

		if (stock == null)
			throw new RpcException(5376, "No such stock " + symbol + " found");

		StockQuote quote = Utils.convert(stock.getQuote());
		StockStats stats = Utils.convert(stock.getStats());
		StockDividend dividend = Utils.convert(stock.getDividend());
		Stock result = new Stock(symbol, stock.getName(), stock.getCurrency(),
				stock.getStockExchange(), quote, stats, dividend);

		return result;
	}

}

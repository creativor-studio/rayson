/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.rayson.demo.stock.api;

import org.rayson.api.serial.RsonSerializable;

/**
 * Stock structure.
 * 
 * @author creativor
 */
public class Stock implements RsonSerializable {
	private String symbol;
	private String name;
	private String currency;
	private String stockExchange;

	private StockQuote quote;
	private StockStats stats;
	private StockDividend dividend;

	public Stock(String symbol, String name, String currency,
			String stockExchange, StockQuote quote, StockStats stats,
			StockDividend dividend) {
		super();
		this.symbol = symbol;
		this.name = name;
		this.currency = currency;
		this.stockExchange = stockExchange;
		this.quote = quote;
		this.stats = stats;
		this.dividend = dividend;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @return the stats
	 */
	public StockStats getStats() {
		return stats;
	}

	/**
	 * @return the stockExchange
	 */
	public String getStockExchange() {
		return stockExchange;
	}

	/**
	 * @return the dividend
	 */
	public StockDividend getDividend() {
		return dividend;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the quote
	 */
	public StockQuote getQuote() {
		return quote;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

}

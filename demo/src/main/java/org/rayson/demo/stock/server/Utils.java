/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.stock.server;

import java.text.DateFormat;
import java.util.Calendar;

import org.rayson.demo.stock.api.StockDividend;
import org.rayson.demo.stock.api.StockQuote;
import org.rayson.demo.stock.api.StockStats;

/**
 *
 * @author creativor
 */
public class Utils {

	private static final DateFormat DATA_FORMAT = DateFormat.getDateInstance();

	private static synchronized String format(Calendar calendar) {
		if (calendar == null)
			return null;
		return DATA_FORMAT.format(calendar.getTime());
	}

	/**
	 * @param dividend
	 * @return
	 */
	public static StockDividend convert(final yahoofinance.quotes.stock.StockDividend dividend) {

		final StockDividend result = new StockDividend(format(dividend.getPayDate()), format(dividend.getExDate()), dividend.getAnnualYield(),
				dividend.getAnnualYieldPercent());
		return result;
	}

	/**
	 * @param quote
	 * @return
	 */
	public static StockQuote convert(final yahoofinance.quotes.stock.StockQuote quote) {

		final StockQuote result = new StockQuote();
		result.setAsk(quote.getAsk());
		result.setAskSize(quote.getAskSize());
		result.setAvgVolume(quote.getAvgVolume());
		result.setBid(quote.getBid());
		result.setBidSize(quote.getBidSize());
		result.setDayHigh(quote.getDayHigh());
		result.setDayLow(quote.getDayLow());
		result.setLastTradeSize(quote.getLastTradeSize());
		result.setLastTradeDate(quote.getLastTradeDateStr());
		result.setLastTradeTime(quote.getLastTradeTimeStr());
		result.setOpen(quote.getOpen());
		result.setPreviousClose(quote.getPreviousClose());
		result.setPrice(quote.getPrice());
		result.setPriceAvg200(quote.getPriceAvg200());
		result.setPriceAvg50(quote.getPriceAvg50());
		result.setTimeZone(quote.getTimeZone().getDisplayName());
		result.setVolume(quote.getVolume());
		result.setYearHigh(quote.getYearHigh());
		result.setYearLow(quote.getYearLow());

		return result;
	}

	/**
	 * @param stats
	 * @param stockStats
	 * @return
	 */
	public static StockStats convert(final yahoofinance.quotes.stock.StockStats stats) {

		final StockStats result = new StockStats();
		result.setBookValuePerShare(stats.getBookValuePerShare());
		result.setEBITDA(stats.getEBITDA());
		result.setEps(stats.getEps());
		result.setEpsEstimateCurrentYear(stats.getEpsEstimateCurrentYear());
		result.setEpsEstimateNextQuarter(stats.getEpsEstimateNextQuarter());
		result.setEpsEstimateNextYear(stats.getEpsEstimateNextYear());
		result.setMarketCap(stats.getMarketCap());
		result.setOneYearTargetPrice(stats.getOneYearTargetPrice());
		result.setPe(stats.getPe());
		result.setPeg(stats.getPeg());
		result.setPriceBook(stats.getPriceBook());
		result.setPriceSales(stats.getPriceSales());
		result.setRevenue(stats.getRevenue());
		result.setSharesFloat(stats.getSharesFloat());
		result.setSharesOutstanding(stats.getSharesOutstanding());
		result.setSharesOwned(stats.getSharesOwned());
		result.setShortRatio(stats.getShortRatio());

		return result;
	}

	private Utils() {
		// Forbidden.
	}
}

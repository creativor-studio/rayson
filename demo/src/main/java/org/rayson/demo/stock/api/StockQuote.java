/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.rayson.demo.stock.api;

import java.math.BigDecimal;

import org.rayson.api.serial.RsonSerializable;

/**
 *
 * @author creativor
 */
public class StockQuote implements RsonSerializable {

	/**
	 * @param timeZone
	 *            the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * @param ask
	 *            the ask to set
	 */
	public void setAsk(BigDecimal ask) {
		this.ask = ask;
	}

	/**
	 * @param askSize
	 *            the askSize to set
	 */
	public void setAskSize(Long askSize) {
		this.askSize = askSize;
	}

	/**
	 * @param bid
	 *            the bid to set
	 */
	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}

	/**
	 * @param bidSize
	 *            the bidSize to set
	 */
	public void setBidSize(Long bidSize) {
		this.bidSize = bidSize;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @param lastTradeSize
	 *            the lastTradeSize to set
	 */
	public void setLastTradeSize(Long lastTradeSize) {
		this.lastTradeSize = lastTradeSize;
	}

	/**
	 * @return the lastTradeDate
	 */
	public String getLastTradeDate() {
		return lastTradeDate;
	}

	/**
	 * @param lastTradeDate
	 *            the lastTradeDate to set
	 */
	public void setLastTradeDate(String lastTradeDate) {
		this.lastTradeDate = lastTradeDate;
	}

	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * @return the ask
	 */
	public BigDecimal getAsk() {
		return ask;
	}

	/**
	 * @return the askSize
	 */
	public Long getAskSize() {
		return askSize;
	}

	/**
	 * @return the bid
	 */
	public BigDecimal getBid() {
		return bid;
	}

	/**
	 * @return the bidSize
	 */
	public Long getBidSize() {
		return bidSize;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @return the lastTradeSize
	 */
	public Long getLastTradeSize() {
		return lastTradeSize;
	}

	/**
	 * @return the lastTradeTime
	 */
	public String getLastTradeTime() {
		return lastTradeTime;
	}

	/**
	 * @return the open
	 */
	public BigDecimal getOpen() {
		return open;
	}

	/**
	 * @return the previousClose
	 */
	public BigDecimal getPreviousClose() {
		return previousClose;
	}

	/**
	 * @return the dayLow
	 */
	public BigDecimal getDayLow() {
		return dayLow;
	}

	/**
	 * @return the dayHigh
	 */
	public BigDecimal getDayHigh() {
		return dayHigh;
	}

	/**
	 * @return the yearLow
	 */
	public BigDecimal getYearLow() {
		return yearLow;
	}

	/**
	 * @return the yearHigh
	 */
	public BigDecimal getYearHigh() {
		return yearHigh;
	}

	/**
	 * @return the priceAvg50
	 */
	public BigDecimal getPriceAvg50() {
		return priceAvg50;
	}

	/**
	 * @return the priceAvg200
	 */
	public BigDecimal getPriceAvg200() {
		return priceAvg200;
	}

	/**
	 * @return the volume
	 */
	public Long getVolume() {
		return volume;
	}

	/**
	 * @return the avgVolume
	 */
	public Long getAvgVolume() {
		return avgVolume;
	}

	/**
	 * @param lastTradeTime
	 *            the lastTradeTime to set
	 */
	public void setLastTradeTime(String lastTradeTime) {
		this.lastTradeTime = lastTradeTime;
	}

	/**
	 * @param open
	 *            the open to set
	 */
	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	/**
	 * @param previousClose
	 *            the previousClose to set
	 */
	public void setPreviousClose(BigDecimal previousClose) {
		this.previousClose = previousClose;
	}

	/**
	 * @param dayLow
	 *            the dayLow to set
	 */
	public void setDayLow(BigDecimal dayLow) {
		this.dayLow = dayLow;
	}

	/**
	 * @param dayHigh
	 *            the dayHigh to set
	 */
	public void setDayHigh(BigDecimal dayHigh) {
		this.dayHigh = dayHigh;
	}

	/**
	 * @param yearLow
	 *            the yearLow to set
	 */
	public void setYearLow(BigDecimal yearLow) {
		this.yearLow = yearLow;
	}

	/**
	 * @param yearHigh
	 *            the yearHigh to set
	 */
	public void setYearHigh(BigDecimal yearHigh) {
		this.yearHigh = yearHigh;
	}

	/**
	 * @param priceAvg50
	 *            the priceAvg50 to set
	 */
	public void setPriceAvg50(BigDecimal priceAvg50) {
		this.priceAvg50 = priceAvg50;
	}

	/**
	 * @param priceAvg200
	 *            the priceAvg200 to set
	 */
	public void setPriceAvg200(BigDecimal priceAvg200) {
		this.priceAvg200 = priceAvg200;
	}

	/**
	 * @param volume
	 *            the volume to set
	 */
	public void setVolume(Long volume) {
		this.volume = volume;
	}

	/**
	 * @param avgVolume
	 *            the avgVolume to set
	 */
	public void setAvgVolume(Long avgVolume) {
		this.avgVolume = avgVolume;
	}

	private String timeZone;

	private BigDecimal ask;
	private Long askSize;
	private BigDecimal bid;
	private Long bidSize;
	private BigDecimal price;

	private Long lastTradeSize;
	private String lastTradeDate;
	private String lastTradeTime;

	private BigDecimal open;
	private BigDecimal previousClose;
	private BigDecimal dayLow;
	private BigDecimal dayHigh;

	private BigDecimal yearLow;
	private BigDecimal yearHigh;
	private BigDecimal priceAvg50;
	private BigDecimal priceAvg200;

	private Long volume;
	private Long avgVolume;
}
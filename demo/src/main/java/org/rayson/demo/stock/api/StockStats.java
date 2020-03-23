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
public class StockStats implements RsonSerializable {

	private BigDecimal marketCap;
	private Long sharesFloat;
	private Long sharesOutstanding;
	private Long sharesOwned;

	private BigDecimal eps;
	private BigDecimal pe;
	private BigDecimal peg;

	private BigDecimal epsEstimateCurrentYear;
	private BigDecimal epsEstimateNextQuarter;
	private BigDecimal epsEstimateNextYear;

	private BigDecimal priceBook;
	private BigDecimal priceSales;
	private BigDecimal bookValuePerShare;

	private BigDecimal revenue; // ttm
	private BigDecimal EBITDA; // ttm
	private BigDecimal oneYearTargetPrice;

	private BigDecimal shortRatio;

	/**
	 * @return the marketCap
	 */
	public BigDecimal getMarketCap() {
		return marketCap;
	}

	/**
	 * @return the sharesFloat
	 */
	public Long getSharesFloat() {
		return sharesFloat;
	}

	/**
	 * @return the sharesOutstanding
	 */
	public Long getSharesOutstanding() {
		return sharesOutstanding;
	}

	/**
	 * @return the sharesOwned
	 */
	public Long getSharesOwned() {
		return sharesOwned;
	}

	/**
	 * @param marketCap
	 *            the marketCap to set
	 */
	public void setMarketCap(BigDecimal marketCap) {
		this.marketCap = marketCap;
	}

	/**
	 * @param sharesFloat
	 *            the sharesFloat to set
	 */
	public void setSharesFloat(Long sharesFloat) {
		this.sharesFloat = sharesFloat;
	}

	/**
	 * @param sharesOutstanding
	 *            the sharesOutstanding to set
	 */
	public void setSharesOutstanding(Long sharesOutstanding) {
		this.sharesOutstanding = sharesOutstanding;
	}

	/**
	 * @param sharesOwned
	 *            the sharesOwned to set
	 */
	public void setSharesOwned(Long sharesOwned) {
		this.sharesOwned = sharesOwned;
	}

	/**
	 * @param eps
	 *            the eps to set
	 */
	public void setEps(BigDecimal eps) {
		this.eps = eps;
	}

	/**
	 * @param pe
	 *            the pe to set
	 */
	public void setPe(BigDecimal pe) {
		this.pe = pe;
	}

	/**
	 * @param peg
	 *            the peg to set
	 */
	public void setPeg(BigDecimal peg) {
		this.peg = peg;
	}

	/**
	 * @param epsEstimateCurrentYear
	 *            the epsEstimateCurrentYear to set
	 */
	public void setEpsEstimateCurrentYear(BigDecimal epsEstimateCurrentYear) {
		this.epsEstimateCurrentYear = epsEstimateCurrentYear;
	}

	/**
	 * @param epsEstimateNextQuarter
	 *            the epsEstimateNextQuarter to set
	 */
	public void setEpsEstimateNextQuarter(BigDecimal epsEstimateNextQuarter) {
		this.epsEstimateNextQuarter = epsEstimateNextQuarter;
	}

	/**
	 * @param epsEstimateNextYear
	 *            the epsEstimateNextYear to set
	 */
	public void setEpsEstimateNextYear(BigDecimal epsEstimateNextYear) {
		this.epsEstimateNextYear = epsEstimateNextYear;
	}

	/**
	 * @param priceBook
	 *            the priceBook to set
	 */
	public void setPriceBook(BigDecimal priceBook) {
		this.priceBook = priceBook;
	}

	/**
	 * @param priceSales
	 *            the priceSales to set
	 */
	public void setPriceSales(BigDecimal priceSales) {
		this.priceSales = priceSales;
	}

	/**
	 * @param bookValuePerShare
	 *            the bookValuePerShare to set
	 */
	public void setBookValuePerShare(BigDecimal bookValuePerShare) {
		this.bookValuePerShare = bookValuePerShare;
	}

	/**
	 * @param revenue
	 *            the revenue to set
	 */
	public void setRevenue(BigDecimal revenue) {
		this.revenue = revenue;
	}

	/**
	 * @param eBITDA
	 *            the eBITDA to set
	 */
	public void setEBITDA(BigDecimal eBITDA) {
		EBITDA = eBITDA;
	}

	/**
	 * @param oneYearTargetPrice
	 *            the oneYearTargetPrice to set
	 */
	public void setOneYearTargetPrice(BigDecimal oneYearTargetPrice) {
		this.oneYearTargetPrice = oneYearTargetPrice;
	}

	/**
	 * @param shortRatio
	 *            the shortRatio to set
	 */
	public void setShortRatio(BigDecimal shortRatio) {
		this.shortRatio = shortRatio;
	}

	/**
	 * @return the eps
	 */
	public BigDecimal getEps() {
		return eps;
	}

	/**
	 * @return the pe
	 */
	public BigDecimal getPe() {
		return pe;
	}

	/**
	 * @return the peg
	 */
	public BigDecimal getPeg() {
		return peg;
	}

	/**
	 * @return the epsEstimateCurrentYear
	 */
	public BigDecimal getEpsEstimateCurrentYear() {
		return epsEstimateCurrentYear;
	}

	/**
	 * @return the epsEstimateNextQuarter
	 */
	public BigDecimal getEpsEstimateNextQuarter() {
		return epsEstimateNextQuarter;
	}

	/**
	 * @return the epsEstimateNextYear
	 */
	public BigDecimal getEpsEstimateNextYear() {
		return epsEstimateNextYear;
	}

	/**
	 * @return the priceBook
	 */
	public BigDecimal getPriceBook() {
		return priceBook;
	}

	/**
	 * @return the priceSales
	 */
	public BigDecimal getPriceSales() {
		return priceSales;
	}

	/**
	 * @return the bookValuePerShare
	 */
	public BigDecimal getBookValuePerShare() {
		return bookValuePerShare;
	}

	/**
	 * @return the revenue
	 */
	public BigDecimal getRevenue() {
		return revenue;
	}

	/**
	 * @return the eBITDA
	 */
	public BigDecimal getEBITDA() {
		return EBITDA;
	}

	/**
	 * @return the oneYearTargetPrice
	 */
	public BigDecimal getOneYearTargetPrice() {
		return oneYearTargetPrice;
	}

	/**
	 * @return the shortRatio
	 */
	public BigDecimal getShortRatio() {
		return shortRatio;
	}

}

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
public class StockDividend implements RsonSerializable {
	private String payDate;
	private String exDate;
	private BigDecimal annualYield;
	private BigDecimal annualYieldPercent;

	/**
	 * @return the payDate
	 */
	public String getPayDate() {
		return payDate;
	}

	public StockDividend(String payDate, String exDate, BigDecimal annualYield,
			BigDecimal annualYieldPercent) {
		super();
		this.payDate = payDate;
		this.exDate = exDate;
		this.annualYield = annualYield;
		this.annualYieldPercent = annualYieldPercent;
	}

	/**
	 * @return the exDate
	 */
	public String getExDate() {
		return exDate;
	}

	/**
	 * @return the annualYield
	 */
	public BigDecimal getAnnualYield() {
		return annualYield;
	}

	/**
	 * @return the annualYieldPercent
	 */
	public BigDecimal getAnnualYieldPercent() {
		return annualYieldPercent;
	}

}

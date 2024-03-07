package com.backend.pennywise.entities;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class FXRates {
	@Id
	private String currencyCode;
	private BigDecimal rate;
	
	public FXRates() {
		super();
	}

	public FXRates(String currencyCode, BigDecimal rate) {
		super();
		this.currencyCode = currencyCode;
		this.rate = rate;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
}

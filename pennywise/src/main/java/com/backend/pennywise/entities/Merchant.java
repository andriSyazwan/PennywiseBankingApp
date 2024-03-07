package com.backend.pennywise.entities;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Component
@Entity
public class Merchant {
	// Self Attributes
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long merchantId;

	private String name;

	private String defaultCurrencyCode;
	
	// Mapped Attributes
	@ManyToOne
	@JoinColumn(name = "merchantCategoryCode")
	private MerchantCategory merchantCategory;
	
	// Constructor
	public Merchant() {
	}

	public Merchant(String name, String defaultCurrencyCode, MerchantCategory merchantCategory) {
		super();
		this.name = name;
		this.defaultCurrencyCode = defaultCurrencyCode;
		this.merchantCategory = merchantCategory;
	}

	// Getters and setters
	public long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultCurrencyCode() {
		return defaultCurrencyCode;
	}

	public void setDefaultCurrencyCode(String defaultCurrencyCode) {
		this.defaultCurrencyCode = defaultCurrencyCode;
	}
	
	public MerchantCategory getMerchantCategory() {
		return merchantCategory;
	}
	
	public void setMerchantCategory(MerchantCategory merchantCategory) {
		this.merchantCategory = merchantCategory;
	}
}

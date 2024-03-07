package com.backend.pennywise.entities;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Component
@Entity
public class MerchantCategory {
	// Self Attributes
	@Id
	private int merchantCategoryCode;

	private String categoryName;

	// Constructor
	public MerchantCategory() {
	}

	public MerchantCategory(int merchantCategoryCode, String categoryName) {
		super();
		this.merchantCategoryCode = merchantCategoryCode;
		this.categoryName = categoryName;
	}

	// Getters and setters
	public int getMerchantCategoryCode() {
		return merchantCategoryCode;
	}

	public void setMerchantCategoryCode(int merchantCategoryCode) {
		this.merchantCategoryCode = merchantCategoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}

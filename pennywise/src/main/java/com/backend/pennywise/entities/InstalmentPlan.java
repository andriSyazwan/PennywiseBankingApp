package com.backend.pennywise.entities;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class InstalmentPlan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long planId;
	
	private short noOfInstalments;
	
	private BigDecimal interestRate;

	public InstalmentPlan() {
		super();
	}

	public InstalmentPlan(short noOfInstalments, BigDecimal interestRate) {
		super();
		this.noOfInstalments = noOfInstalments;
		this.interestRate = interestRate;
	}

	public long getPlanId() {
		return planId;
	}

	public void setPlanId(long planId) {
		this.planId = planId;
	}

	public int getNoOfInstalments() {
		return noOfInstalments;
	}

	public void setNoOfInstalments(short noOfInstalments) {
		this.noOfInstalments = noOfInstalments;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	
}

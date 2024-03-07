package com.backend.pennywise.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Component
@Entity
public class InstalmentTransaction {
	// Self Attributes
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long instalmentId;

	private Date date;

	private BigDecimal totalAmount;

	private BigDecimal monthlyAmount;

	private Boolean isPaid = false;

	private short progressTracker;

	// Mapped Attributes
	@ManyToOne
	@JoinColumn(name = "cardId")
	private CreditCard creditCard;

	@ManyToOne
	@JoinColumn(name = "merchantId")
	private Merchant merchant;

	@ManyToOne
	@JoinColumn(name = "planId")
	private InstalmentPlan instalmentPlan;

	@ElementCollection
	private List<Transaction> transactionList = new ArrayList<>();

	// Constructors
	public InstalmentTransaction() {

	}

	public InstalmentTransaction(Date date, BigDecimal purchaseAmount, Boolean isPaid, short progressTracker,
			CreditCard creditCard, Merchant merchant, InstalmentPlan instalmentPlan) {
		super();
		this.date = date;
		this.totalAmount = purchaseAmount
				.multiply(instalmentPlan.getInterestRate().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).add(BigDecimal.ONE));
		this.monthlyAmount = this.totalAmount.divide(BigDecimal.valueOf(instalmentPlan.getNoOfInstalments()), 2, RoundingMode.HALF_UP);
		this.isPaid = isPaid;
		this.progressTracker = progressTracker;
		this.creditCard = creditCard;
		this.merchant = merchant;
		this.instalmentPlan = instalmentPlan;
	}

	public InstalmentTransaction(Date date, BigDecimal purchaseAmount, Boolean isPaid, short progressTracker,
			CreditCard creditCard, Merchant merchant, InstalmentPlan instalmentPlan, List<Transaction> transactionList) {
		super();
		this.date = date;
		this.totalAmount = purchaseAmount
				.multiply(instalmentPlan.getInterestRate().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).add(BigDecimal.ONE));
		this.monthlyAmount = this.totalAmount.divide(BigDecimal.valueOf(instalmentPlan.getNoOfInstalments()), 2, RoundingMode.HALF_UP);
		this.isPaid = isPaid;
		this.progressTracker = progressTracker;
		this.creditCard = creditCard;
		this.merchant = merchant;
		this.instalmentPlan = instalmentPlan;
		this.transactionList = transactionList;
	}

	public long getInstalmentId() {
		return instalmentId;
	}

	public void setInstalmentId(long instalmentId) {
		this.instalmentId = instalmentId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getMonthlyAmount() {
		return monthlyAmount;
	}

	public void setMonthlyAmount(BigDecimal monthlyAmount) {
		this.monthlyAmount = monthlyAmount;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	public short getProgressTracker() {
		return progressTracker;
	}

	public void setProgressTracker(short progressTracker) {
		this.progressTracker = progressTracker;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public InstalmentPlan getInstalmentPlan() {
		return instalmentPlan;
	}

	public void setInstalmentPlan(InstalmentPlan instalmentPlan) {
		this.instalmentPlan = instalmentPlan;
	}

	public List<Transaction> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(List<Transaction> transactionList) {
		this.transactionList = transactionList;
	}
}

package com.backend.pennywise.entities;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Component
@Entity
public class CreditCardBill {
	// Self attributes
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long billId;
	
	private BigDecimal billAmount;
	
	private Date billDate;
	
	private Date dueDate;
	
	private Date billingPeriodStart;
	
	private Date billingPeriodEnd;
	
	private boolean isPaid = false;
	
	private BigDecimal amountPaid = BigDecimal.ZERO;
	
	// Mapped Attributes
	@ManyToOne
	@JoinColumn(name = "cardNumber")
	private CreditCard creditCard;
	
	// Constructors
	public CreditCardBill() {
		
	}
	
	public CreditCardBill(BigDecimal billAmount, Date dueDate, Date billingPeriodStart, Date billingPeriodEnd,
			CreditCard creditCard) {
		super();
		this.billAmount = billAmount;
		this.dueDate = dueDate;
		this.billingPeriodStart = billingPeriodStart;
		this.billingPeriodEnd = billingPeriodEnd;
		this.creditCard = creditCard;
		billDate = new Date();
	}

	public CreditCardBill(BigDecimal billAmount, Date billDate, Date dueDate, Date billingPeriodStart,
			Date billingPeriodEnd, CreditCard creditCard) {
		super();
		this.billAmount = billAmount;
		this.billDate = billDate;
		this.dueDate = dueDate;
		this.billingPeriodStart = billingPeriodStart;
		this.billingPeriodEnd = billingPeriodEnd;
		this.creditCard = creditCard;
	}

	// Getters and Setters
	public long getBillId() {
		return billId;
	}

	public void setBillId(long billId) {
		this.billId = billId;
	}

	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getBillingPeriodStart() {
		return billingPeriodStart;
	}

	public void setBillingPeriodStart(Date billingPeriodStart) {
		this.billingPeriodStart = billingPeriodStart;
	}

	public Date getBillingPeriodEnd() {
		return billingPeriodEnd;
	}

	public void setBillingPeriodEnd(Date billingPeriodEnd) {
		this.billingPeriodEnd = billingPeriodEnd;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
}

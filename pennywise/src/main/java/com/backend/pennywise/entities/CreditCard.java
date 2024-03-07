package com.backend.pennywise.entities;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Component
@Entity
public class CreditCard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("cardId")
	private long cardId;
	
	// Self Attributes
	private long cardNumber;
	
	private BigDecimal creditLimit;
	
	private BigDecimal creditBalance;
	
	private Date expiryDate;
	
	private short cardVerificationValue;
	
	private String displayName;
	
	private final BigDecimal MINIMUM_PAYMENT_AMOUNT = BigDecimal.valueOf(50.00);
	
	private int pointsRedeemed;
	
	private int pointsBalance = 0;
	
	private BigDecimal rebatesRedeemed;
	
	private BigDecimal rebatesBalance = BigDecimal.ZERO;
	
	
	// Mapped Attributes
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;
	
	// Constructors
	public CreditCard() {
	}

	public CreditCard(long cardNumber, BigDecimal creditLimit, BigDecimal creditBalance, Date expiryDate, short cardVerificationValue,
			String displayName, User user) {
		super();
		this.cardNumber = cardNumber;
		this.creditLimit = creditLimit;
		this.creditBalance = creditBalance;
		this.expiryDate = expiryDate;
		this.cardVerificationValue = cardVerificationValue;
		this.displayName = displayName;
		this.user = user;
	}
	
	public CreditCard(long cardNumber, BigDecimal creditLimit, BigDecimal creditBalance, Date expiryDate, short cardVerificationValue,
					 String displayName, User user, int pointsBalance, BigDecimal rebatesBalance) {
		super();
		this.cardNumber = cardNumber;
		this.creditLimit = creditLimit;
		this.creditBalance = creditBalance;
		this.expiryDate = expiryDate;
		this.cardVerificationValue = cardVerificationValue;
		this.displayName = displayName;
		this.user = user;
		this.pointsBalance = pointsBalance;
		this.rebatesBalance = rebatesBalance;
	}

	// Getters and setters
	public long getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}

	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}

	public BigDecimal getCreditBalance() {
		return creditBalance;
	}

	public void setCreditBalance(BigDecimal creditBalance) {
		this.creditBalance = creditBalance;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public short getCardVerificationValue() {
		return cardVerificationValue;
	}

	public void setCardVerificationValue(short cardVerificationValue) {
		this.cardVerificationValue = cardVerificationValue;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public BigDecimal getMinimumPaymentAmount() {
		return MINIMUM_PAYMENT_AMOUNT;
	}

	public int getPointsRedeemed() {
		return pointsRedeemed;
	}

	public void setPointsRedeemed(int pointsRedeemed) {
		this.pointsRedeemed = pointsRedeemed;
	}

	public int getPointsBalance() {
		return pointsBalance;
	}

	public void setPointsBalance(int pointsBalance) {
		this.pointsBalance = pointsBalance;
	}

	public BigDecimal getRebatesRedeemed() {
		return rebatesRedeemed;
	}

	public void setRebatesRedeemed(BigDecimal rebatesRedeemed) {
		this.rebatesRedeemed = rebatesRedeemed;
	}

	public BigDecimal getRebatesBalance() {
		return rebatesBalance;
	}

	public void setRebatesBalance(BigDecimal rebatesBalance) {
		this.rebatesBalance = rebatesBalance;
	}

	public long getCardId() {
		return cardId;
	}

	public void setCardId(long cardId) {
		this.cardId = cardId;
	}
}

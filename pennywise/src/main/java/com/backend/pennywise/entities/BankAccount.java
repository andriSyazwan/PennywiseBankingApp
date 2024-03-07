package com.backend.pennywise.entities;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.backend.pennywise.enums.AccountType;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Component
@Entity
public class BankAccount {
	
	
	// Self Attributes
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("accountId")
	private long accountId;
	
	private long accountNumber;
	
	private BigDecimal balance;
	
	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	
	// Mapped Attributes
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;
	

	// Constructors
	public BankAccount() {
		
	}

	public BankAccount(BigDecimal balance, long accountNumber, AccountType accountType, User user) {
		super();
		this.balance = balance;
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.user = user;
	}

	// Getters and setters
	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	
	
	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}

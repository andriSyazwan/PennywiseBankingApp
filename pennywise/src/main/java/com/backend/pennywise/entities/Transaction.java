package com.backend.pennywise.entities;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.backend.pennywise.enums.TransactionType;
import com.backend.pennywise.enums.Type;

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
public class Transaction {
	// Self Attributes
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long transactionId;
	
	private Date date;
	
	private BigDecimal amount;
	
	@Enumerated(EnumType.STRING)
	private Type type;
	
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;
	
	// Mapped Attributes
	@ManyToOne
	@JoinColumn(name = "accountId")
	private BankAccount bankAccount;
	
	@ManyToOne
	@JoinColumn(name = "cardId")
	private CreditCard creditCard;
	
	@ManyToOne
	@JoinColumn(name = "merchantId")
	private Merchant merchant;
	
	// For transfers
	@ManyToOne
	@JoinColumn(name = "destinationAccount", nullable=true)
	private BankAccount destinationAccount;
	
	@ManyToOne
	@JoinColumn(name = "sourceAccount", nullable=true)
	private BankAccount sourceAccount;
	
	// Constructors
	public Transaction() {
		
	}
	
	public Transaction(Date date, BigDecimal amount, Type type, BankAccount bankAccount) {
		super();
		this.date = date;
		this.amount = amount;
		this.type = type;
		this.bankAccount = bankAccount;
	}
	
	public Transaction(Date date, BigDecimal amount, Type type, CreditCard creditCard,
			Merchant merchant) {
		super();
		this.date = date;
		this.amount = amount;
		this.type = type;
		this.merchant = merchant;
		this.creditCard = creditCard;
	}
	
	public Transaction(Date date, BigDecimal amount, Type type, TransactionType transactionType, CreditCard creditCard,
			Merchant merchant) {
		super();
		this.date = date;
		this.amount = amount;
		this.type = type;
		this.transactionType = transactionType;
		this.merchant = merchant;
		this.creditCard = creditCard;
	}
	
	public Transaction(Date date, BigDecimal amount, Type type, CreditCard creditCard) {
		super();
		this.date = date;
		this.amount = amount;
		this.type = type;
		this.creditCard = creditCard;
	}
	
	public Transaction(Date date, BigDecimal amount, Type type, BankAccount bankAccount, TransactionType transactionType) {
		super();
		this.date = date;
		this.amount = amount;
		this.type = type;
		this.bankAccount = bankAccount;
		this.transactionType = transactionType;
	}
	
	// For transfers
	public Transaction(long transactionId, Date date, BigDecimal amount, Type type, TransactionType transactionType, 
						BankAccount currentAccount, BankAccount destinationAccount,BankAccount sourceAccount) {
		super();
		this.transactionId = transactionId;
		this.date = date;
		this.amount = amount;
		this.type = type;
		this.transactionType = transactionType;
		this.bankAccount = currentAccount;
		this.destinationAccount = destinationAccount;
		this.sourceAccount = sourceAccount;
	}
	
	public Transaction(Date date, BigDecimal amount, Type type, TransactionType transactionType, CreditCard creditCard) {
		super();
		this.date = date;
		this.amount = amount;
		this.type = type;
		this.transactionType = transactionType;
		this.creditCard = creditCard;
	}
	
	
	public Transaction(Date date, BigDecimal amount, TransactionType transactionType, CreditCard creditCard,
			Merchant merchant) {
		super();
		this.date = date;
		this.amount = amount;
		this.transactionType = transactionType;
		this.creditCard = creditCard;
		this.merchant = merchant;
	}

	// Getters and setters
	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public BankAccount getDestinationAccount() {
		return destinationAccount;
	}

	public void setDestinationAccount(BankAccount destinationAccount) {
		this.destinationAccount = destinationAccount;
	}

	public BankAccount getSourceAccount() {
		return sourceAccount;
	}

	public void setSourceAccount(BankAccount sourceAccount) {
		this.sourceAccount = sourceAccount;
	}
	
}

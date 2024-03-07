package com.backend.pennywise.entities;

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
public class Point {
	// Self Attributes
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long pointId;
	
	private int amount;
	
	private Date date;
	
	// Mapped Attributes
	@ManyToOne
	@JoinColumn(name = "cardNumber")
	private CreditCard creditCard;
	
	@ManyToOne
	@JoinColumn(name = "transactionId")
	private Transaction transaction;
	
	// Constructors
	public Point() {
	}
	
	public Point(int amount, Date date, CreditCard creditCard) {
		super();
		this.amount = amount;
		this.date = date;
		this.creditCard = creditCard;
	}

	public Point(int amount, Date date, CreditCard creditCard, Transaction transaction) {
		super();
		this.amount = amount;
		this.date = date;
		this.creditCard = creditCard;
		this.transaction = transaction;
	}

	public long getPointId() {
		return pointId;
	}

	public void setPointId(long pointId) {
		this.pointId = pointId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	
	
}

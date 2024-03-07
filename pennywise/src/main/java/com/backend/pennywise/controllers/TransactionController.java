package com.backend.pennywise.controllers;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.backend.pennywise.entities.Transaction;
import com.backend.pennywise.services.TransactionService;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
	@Autowired
	private TransactionService transactionService;

	@GetMapping
	public List<Transaction> getAllTransactions() {
		return transactionService.getAllTransactons();
	}

	@GetMapping("/{id}")
	public Transaction getTransaction(@PathVariable(value = "id") long transactionId) {
		return transactionService.findTransactionById(transactionId);
	}

	@GetMapping("/merchant/{id}")
	public List<Transaction> getTransactionByMerchant(@PathVariable(value = "id") long merchantId) {
		return transactionService.findTransactionByMerchantId(merchantId);
	}

	@GetMapping("/account/{id}")
	public List<Transaction> getTransactionByAccount(@PathVariable(value = "id") long accountId) {
		return transactionService.findTransactionByAccount(accountId);
	}
	
	@GetMapping("/sourceAccount/{id}")
	public List<Transaction> getTransactionsBySourceAccount(@PathVariable( value= "id") long accountId){
		return transactionService.findTransactionsBySourceAccount(accountId);
	}
	
	@GetMapping("/destinationAccount/{id}")
	public List<Transaction> getTransactionsByDestinationAccount(@PathVariable( value= "id") long accountId){
		return transactionService.findTransactionsByDestinationAccount(accountId);
	}
	

	@GetMapping("/user/{id}")
	public List<Transaction> getTransactionByUser(@PathVariable(value = "id") long userId) {
		return transactionService.findTransactionByUser(userId);
	}

	@GetMapping("/user/5trans/{id}")
	public List<Transaction> getLatest5TransactionsByUser(@PathVariable(value = "id") long userId) {
		return transactionService.findLastFiveTransactionsByuser(userId);
	}


	@GetMapping("/creditCard/{id}")
	public List<Transaction> getTransactionByCreditCard(@PathVariable(value = "id") long creditCardId) {
		return transactionService.findTransactionByCreditCard(creditCardId);
	}

	@GetMapping("/month/bankAccount/{id}")
	List<Transaction> getBankAccountTransactionsByMonth(@PathVariable(value = "id") long bankAccountId,
			@RequestParam int month, @RequestParam int year) {
		return transactionService.findBankAccountTransactionByMonth(bankAccountId, month, year);
	}

	@GetMapping("/filterDate/bankAccount/{id}")
	List<Transaction> getBankAccoountTransactionsByDateRange(@PathVariable(value = "id") long bankAccountId,
			@RequestParam Date startDate, @RequestParam Date endDate) {
		return transactionService.findBankAccountTransactionsByTimePeriod(bankAccountId, startDate, endDate);
	}

	@GetMapping("/month/creditCard/{id}")
	List<Transaction> getCreditCardTransactionsByMonth(@PathVariable(value = "id") long creditCardId,
			@RequestParam int month, @RequestParam int year) {
		return transactionService.findCreditCardTransactionsByMonth(creditCardId, month, year);
	}

	@GetMapping("/filterDate/creditCard/{id}")
	List<Transaction> getCreditCardTransactionsByDateRange(@PathVariable(value = "id") long creditCardNum,
			@RequestParam Date startDate, @RequestParam Date endDate) {
		return transactionService.findCreditCardTransactionsByTimePeriod(creditCardNum, startDate, endDate);
	}

	@PostMapping("/add")
	public ResponseEntity<Transaction> addNewTransaction(@RequestBody Transaction transaction) {
		Transaction savedTransaction = transactionService.addTransaction(transaction);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedTransaction.getClass()).toUri();

		return ResponseEntity.created(location).body(savedTransaction);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Transaction> updateTransaction(@RequestBody Map<String, Object> updates,
			@PathVariable(value = "id") long transactionId) {
		Transaction updatedTransaction = transactionService.updateTransactionDetails(updates, transactionId);

		return ResponseEntity.ok(updatedTransaction);
	}

	@DeleteMapping("/{id}")
	public Map<String, Boolean> deleteTransaction(@PathVariable(value = "id") long transactionId) {
		return transactionService.deleteTransactionById(transactionId);
	}

	@GetMapping("/creditCard/5trans/{id}")
	public List<Transaction> getLatest5TransactionsByCreditCard(@PathVariable(value = "id") long creditCardId) {
		return transactionService.findLastFiveTransactionsByCreditCard(creditCardId);
	}

	@GetMapping("/bankAccount/5trans/{id}")
	public List<Transaction> getLatest5TransactionsByBankAccount(@PathVariable(value = "id") long accountId) {
		return transactionService.findLastFiveTransactionsByBankAccount(accountId);
	}
}


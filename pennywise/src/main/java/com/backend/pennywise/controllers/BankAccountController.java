package com.backend.pennywise.controllers;

import java.math.BigDecimal;
import java.net.URI;
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

import com.backend.pennywise.entities.BankAccount;
import com.backend.pennywise.entities.Transaction;
import com.backend.pennywise.services.BankAccountService;

@RestController
@RequestMapping("api/v1/bankAccounts")
public class BankAccountController {
	@Autowired
	private BankAccountService bankAccountService;
	
	@GetMapping
	public List<BankAccount> getAllBankAccounts(){
		return bankAccountService.getAllAccounts();
	}
	
	@GetMapping("/{id}")
	public BankAccount getBankAccount(@PathVariable("id") long bankAccountId) {
		return bankAccountService.findAccountById(bankAccountId);
	}
	
	@GetMapping("/user/{id}")
	public List<BankAccount> getBankAccountsByUserId(@PathVariable("id") long userId){
		return bankAccountService.findBankAccountsByUser(userId);
	}
	
	@PostMapping("/add")
	public ResponseEntity<BankAccount> addNewBankAccount(@RequestBody BankAccount bankAccount){
		BankAccount savedBankAccount = bankAccountService.addBankAccount(bankAccount);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				       .buildAndExpand(savedBankAccount.getAccountId()).toUri();

		return ResponseEntity.created(location).body(savedBankAccount);
	}
	

	@PatchMapping("/{id}")
	public ResponseEntity<BankAccount> updateBankAccount(@RequestBody Map<String, Object> updates,
														 @PathVariable(value= "id")long bankAccountId){
		BankAccount updatedAccount = bankAccountService.updateBankAccountDetails(updates, bankAccountId);
		
		return ResponseEntity.ok(updatedAccount);
	}
	
	@PostMapping("/withdraw/{id}")
	public ResponseEntity<Transaction> withdrawFunds(@PathVariable(value= "id")long bankAccountId,
												     @RequestParam BigDecimal amount){
		Transaction withdrawTransaction = bankAccountService.withdraw(bankAccountId, amount);
		
		return ResponseEntity.ok(withdrawTransaction);
	}
	
	@PostMapping("/deposit/{id}")
	public ResponseEntity<Transaction> depositFunds(@PathVariable(value= "id")long bankAccountId,
												     @RequestParam BigDecimal amount){
		Transaction depositTransaction = bankAccountService.deposit(bankAccountId, amount);
		
		return ResponseEntity.ok(depositTransaction);
	}
	
	@PostMapping("/transfer")
	public ResponseEntity<Transaction> transferFunds(@RequestParam long sourceAccountId,
			@RequestParam long receivingAccountId, @RequestParam BigDecimal amount) {
	

		Transaction transferTransaction = bankAccountService.transfer(sourceAccountId, receivingAccountId, amount);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(transferTransaction.getTransactionId()).toUri();

		return ResponseEntity.created(location).body(transferTransaction);
	}

	@DeleteMapping("/{id}")
	public Map<String, Boolean> deleteAccount (@PathVariable(value = "id") long bankAccountId){
		return bankAccountService.deleteAccountByAccountId(bankAccountId);
	}
	
	@DeleteMapping("/deleteByUserId/{id}")
	public Map<String, Boolean> deleteAccountByUserId (@PathVariable(value = "id") long userId){
		return bankAccountService.deleteAccountByUserId(userId);
	}
	

}

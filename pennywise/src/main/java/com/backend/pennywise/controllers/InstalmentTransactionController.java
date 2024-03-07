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

import com.backend.pennywise.entities.InstalmentTransaction;
import com.backend.pennywise.entities.Transaction;
import com.backend.pennywise.services.InstalmentTransactionService;

@RestController
@RequestMapping("/api/v1/instalmentTransactions")
public class InstalmentTransactionController {
	@Autowired
	private InstalmentTransactionService instalmentTransactionService;

	@GetMapping
	public List<InstalmentTransaction> getAllTransactions() {
		return instalmentTransactionService.getAllInstalmentTransaction();
	}

	@GetMapping("/{id}")
	public InstalmentTransaction getTransaction(@PathVariable(value = "id") long InstalmentTransactionId) {
		return instalmentTransactionService.findInstalmentTransactionById(InstalmentTransactionId);
	}
	

	@GetMapping("/user/{id}")
	public List<InstalmentTransaction> getInstalmentTransactionByUser(@PathVariable(value = "id") long userId) {
		return instalmentTransactionService.findInstalmentTransactionByuser(userId);
	}


	@GetMapping("/creditCard/{id}")
	public List<InstalmentTransaction> getInstalmentTransactionByCreditCard(@PathVariable(value = "id") long creditCardId) {
		return instalmentTransactionService.findInstalmentTransactionByCreditCard(creditCardId);
	}


	@PostMapping("/add")
	public ResponseEntity<InstalmentTransaction> addNewTransaction(@RequestBody InstalmentTransaction instalmentTransaction) {
		InstalmentTransaction savedInstalmentTransaction = instalmentTransactionService.addInstalmentTransaction(instalmentTransaction);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedInstalmentTransaction.getClass()).toUri();

		return ResponseEntity.created(location).body(savedInstalmentTransaction);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<InstalmentTransaction> updateInstalmentTransaction(@RequestBody Map<String, Object> updates,
			@PathVariable(value = "id") long instalmentTransactionId) {
		InstalmentTransaction updatedInstalmentTransaction = instalmentTransactionService.updateInstalmentTransaction(updates, instalmentTransactionId);

		return ResponseEntity.ok(updatedInstalmentTransaction);
	}

	@DeleteMapping("/{id}")
	public Map<String, Boolean> deleteInstalmentTransaction(@PathVariable(value = "id") long instalmentTransactionId) {
		return instalmentTransactionService.deleteInstalmentTransaction(instalmentTransactionId);
	}


}


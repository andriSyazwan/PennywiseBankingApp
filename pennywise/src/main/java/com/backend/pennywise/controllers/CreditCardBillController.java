package com.backend.pennywise.controllers;

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

import com.backend.pennywise.entities.CreditCardBill;
import com.backend.pennywise.entities.Transaction;
import com.backend.pennywise.repositories.CreditCardRepository;
import com.backend.pennywise.services.CreditCardBillService;

@RestController
@RequestMapping("/api/v1/creditCardBills")
public class CreditCardBillController {
	@Autowired
	private CreditCardBillService billService;
	
	@Autowired
	private CreditCardRepository creditCardRepo;
	
	@GetMapping
	public List<CreditCardBill> getAllCreditCardBill(){
		return billService.getAllBills();
	}
	
	@GetMapping("/{id}")
	public CreditCardBill getCreditCardBill(@PathVariable(value = "id") long creditCardBillId) {
		return billService.findBillById(creditCardBillId);
	}
	
	@GetMapping("/creditCard/{id}")
	public List<CreditCardBill> getCreditCardBillsByCreditCard(@PathVariable(value = "id") long creditCardId){
		return billService.findByCreditCard(creditCardId);
	}
	
	@GetMapping("/creditCard/{id}/latest")
	public CreditCardBill getLatestCreditCardBill (@PathVariable(value = "id") long creditCardNum){
		return billService.findLatestBillByCreditCard(creditCardNum);
	}
	
	@GetMapping("/creditCard/{id}/month")
	public CreditCardBill getBillByMonth(@PathVariable(value = "id") long creditCardNum,
										 @RequestParam int month, @RequestParam int year) {
		return billService.findBillByCreditCardByMonth(creditCardNum, month, year);
	}
	
	@GetMapping("/user/{id}")
	public List<CreditCardBill> getCreditCardBillsByUser(@PathVariable(value = "id") long userId){
		return billService.findByUser(userId);
	}
	
	@GetMapping("/generateBill/{id}")
	public CreditCardBill generateCreditCardBill(@PathVariable(value = "id") long creditCardId){
		return billService.generateBill(creditCardId);
	}
	
	@GetMapping("/billCharges/{id}")
	public List<Transaction> calculateCharges(@PathVariable(value = "id") long creditCardId){
		return billService.dueDateCharges(creditCardId);
	}
	
	@PostMapping("/add")
	public ResponseEntity<CreditCardBill> addNewCreditCardBill(@RequestBody CreditCardBill creditCardBill){
		CreditCardBill savedCreditCardBill = billService.addBill(creditCardBill);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				       .buildAndExpand(savedCreditCardBill.getClass()).toUri();

		return ResponseEntity.created(location).body(savedCreditCardBill);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<CreditCardBill> updateCreditCardBill(@RequestBody Map<String,Object> updates,
															   @PathVariable (value= "id") long billId){
		CreditCardBill updatedBill = billService.updateBillDetails(updates, billId);
		
		return ResponseEntity.ok(updatedBill);
	}

	@DeleteMapping("/{id}")
	public Map<String, Boolean> deleteCreditCardBill(@PathVariable(value = "id") long creditCardBillId){
		return billService.deleteBillById(creditCardBillId);
	}
}

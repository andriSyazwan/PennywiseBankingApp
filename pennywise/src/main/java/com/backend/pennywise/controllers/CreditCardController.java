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

import com.backend.pennywise.entities.CreditCard;
import com.backend.pennywise.entities.InstalmentTransaction;
import com.backend.pennywise.entities.Point;
import com.backend.pennywise.entities.Transaction;
import com.backend.pennywise.services.CreditCardService;

@RestController
@RequestMapping("/api/v1/creditCards")
public class CreditCardController {
	@Autowired
	private CreditCardService creditCardService;
	
	@GetMapping
	public List<CreditCard> getAllCreditCards(){
		return creditCardService.getAllCreditCard();
	}
	
	@GetMapping("/{id}")
	public CreditCard getCreditCard(@PathVariable("id") long creditCardNum) {
		return creditCardService.findCreditCardById(creditCardNum);
	}
	
	@GetMapping("/user/{id}")
	public List<CreditCard> getCreditCardsByUserId(@PathVariable("id") long userId){
		return creditCardService.findCreditCardByUser(userId);
	}
	
	@PostMapping("/add")
	public ResponseEntity<CreditCard> addNewCreditCard(@RequestBody CreditCard creditCard){
		CreditCard savedCreditCard = creditCardService.addCreditCard(creditCard);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				       .buildAndExpand(savedCreditCard.getClass()).toUri();

		return ResponseEntity.created(location).body(savedCreditCard);
	}
	
	@PostMapping("/{id}/redeemRebate")
	public Transaction redeemRebatesByCreditCard(@PathVariable(value = "id")long creditCardId,
													@RequestParam BigDecimal amount){
		return creditCardService.redeemRebates(creditCardId, amount);
	}
	
	@PostMapping("/{id}/redeemPoints")
	public ResponseEntity<Point> redeemPointsByCreditCard(@PathVariable(value = "id") long creditCardId, 
												@RequestParam int points) {
		return ResponseEntity.ok(creditCardService.redeemPoints(creditCardId, points));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<CreditCard> updateCreditCard(@PathVariable(value = "id") long creditCardId, 
													   @RequestBody Map<String, Object> updates){
		CreditCard updatedCreditCard = creditCardService.updateCreditCardDetails(creditCardId, updates);
		
		return ResponseEntity.ok(updatedCreditCard);
	}

	@DeleteMapping("/{id}")
	public Map<String, Boolean> deleteCreditCard(@PathVariable(value = "id") long creditCardId){
		return creditCardService.deleteCreditCardById(creditCardId);
	}
	
	//purchase item from merchant
	@PostMapping("/purchase")
	public ResponseEntity<Transaction> purchase(@RequestParam long creditCardId,
												     @RequestParam BigDecimal amount,
												     @RequestParam long merchantId){
		Transaction purchaseTransaction = creditCardService.makePurchaseWithCreditCard(creditCardId, amount, merchantId);
		
		return ResponseEntity.ok(purchaseTransaction);
	}
	
	// Installment
	@PostMapping("/instalmentPurchase")
	public ResponseEntity<InstalmentTransaction> purchaseByInstalment(@RequestParam long creditCardId,
		     														  @RequestParam BigDecimal amount,
		     														  @RequestParam long merchantId,
		     														  @RequestParam long instalmentPlanId){
		InstalmentTransaction purchaseTransaction = creditCardService.makePurchaseByInstalment(creditCardId, amount, merchantId, instalmentPlanId);

	return ResponseEntity.ok(purchaseTransaction);
	}
	
	@PostMapping("/ccBillPayment")
	public ResponseEntity<Transaction> makeCCBillPayment(@RequestParam long billId,
														 @RequestParam long bankAccountId,
														 @RequestParam BigDecimal amount){
		Transaction creditCardBillPayment = creditCardService.makeBillPayment(billId, bankAccountId, amount);
				
		return ResponseEntity.ok(creditCardBillPayment);
	}
}

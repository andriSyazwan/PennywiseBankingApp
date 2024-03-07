package com.backend.pennywise.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.pennywise.entities.CreditCard;
import com.backend.pennywise.entities.InstalmentTransaction;
import com.backend.pennywise.entities.Transaction;
import com.backend.pennywise.entities.User;
import com.backend.pennywise.exceptions.CreditCardNotFoundException;
import com.backend.pennywise.exceptions.InstalmentTransactionNotFoundException;
import com.backend.pennywise.exceptions.UserNotFoundException;
import com.backend.pennywise.repositories.CreditCardRepository;
import com.backend.pennywise.repositories.InstalmentPlanRepository;
import com.backend.pennywise.repositories.InstalmentTransactionRepository;
import com.backend.pennywise.repositories.UserRepository;

@Service
public class InstalmentTransactionService {
	
	@Autowired
	InstalmentTransactionRepository instalmentTransRepo;
	
	@Autowired
	InstalmentPlanRepository instalmentPlanRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	CreditCardRepository ccRepo;
	
	Logger logger = LogManager.getLogger(InstalmentTransactionService.class);
	
	public List<InstalmentTransaction> getAllInstalmentTransaction() {
		return instalmentTransRepo.findAll();
	}

	public InstalmentTransaction findInstalmentTransactionById(long instalmentId) {
		InstalmentTransaction instalmentTransaction = instalmentTransRepo.findById(instalmentId)
								 .orElseThrow(()->{
									 logger.error("instalment transaction id " + instalmentId + " not found");
								 	return new InstalmentTransactionNotFoundException("Instalment Transaction not found");
								 });		
		return instalmentTransaction;
	}

	public InstalmentTransaction addInstalmentTransaction(InstalmentTransaction InstalmentTransaction) {
		return instalmentTransRepo.save(InstalmentTransaction);
	}

	public InstalmentTransaction updateInstalmentTransaction(Map<String, Object> updates, long instalmentId) {
		InstalmentTransaction existingInstalmentTransaction = instalmentTransRepo.findById(instalmentId)
				 .orElseThrow(()->{
					 logger.error("instalment transaction id " + instalmentId + " not found");
				 	return new InstalmentTransactionNotFoundException("Instalment Transaction not found");
				 });	
		
		if (updates.containsKey("creditCard")) {
			existingInstalmentTransaction.setCreditCard((CreditCard)updates.get("creditCard"));
			logger.info("Updating credit card in instalment transaction id" + instalmentId);
		}
		
		if (updates.containsKey("transaction")) {
			existingInstalmentTransaction.setTransactionList((List<Transaction>)updates.get("transaction"));
			logger.info("Updating transaction in instalment transaction id" + instalmentId);
		}
		
		if (updates.containsKey("totalAmount")) {
			existingInstalmentTransaction.setTotalAmount((BigDecimal) updates.get("totalAmount"));
			logger.info("Updating total amount in instalment transaction id" + instalmentId);
		}
		
		if (updates.containsKey("monthlyAmount")) {
			existingInstalmentTransaction.setMonthlyAmount((BigDecimal) updates.get("monthlyAmount"));
			logger.info("Updating monthly in instalment transaction id" + instalmentId);
		}
		
		if (updates.containsKey("isPaid")) {
			existingInstalmentTransaction.setIsPaid((Boolean)updates.get("instalmentTransaction"));
			logger.info("Updating is paid in instalment transaction id" + instalmentId);
		}
		
		return instalmentTransRepo.save(existingInstalmentTransaction);
	}

	public Map<String, Boolean> deleteInstalmentTransaction(long instalmentId) {
		InstalmentTransaction existingInstalmentTransaction = instalmentTransRepo.findById(instalmentId)
				 .orElseThrow(()->{
					 logger.error("instalment transaction id " + instalmentId + " not found");
				 	return new InstalmentTransactionNotFoundException("Instalment Transaction not found");
				 });	
		
		instalmentTransRepo.delete(existingInstalmentTransaction);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		
		return response;
		
	}
	
	 public List<InstalmentTransaction> findInstalmentTransactionByCreditCard(long cardId){
		 CreditCard existingCC = ccRepo.findById(cardId).orElseThrow(() -> {
			logger.error("Credit card id " + cardId + " not found"); 					
			 return new CreditCardNotFoundException("Credit Card not found");
		 });

		 List<InstalmentTransaction> instalmentTransaction = new ArrayList<>();
		 instalmentTransaction.addAll(instalmentTransRepo.findByCreditCard(existingCC));
	     
	     return instalmentTransaction;
	}

	public List<InstalmentTransaction> findInstalmentTransactionByuser(long userId) {
		User existingUser = userRepo.findById(userId).orElseThrow(() -> {
			logger.error("User with userId " + userId + " not found");
			return new UserNotFoundException("User not found");
		});
		
		List<InstalmentTransaction> instalmentTransaction = new ArrayList<>();
		 instalmentTransaction.addAll(instalmentTransRepo.findByUser(existingUser));
	     
	     return instalmentTransaction;
	}

}

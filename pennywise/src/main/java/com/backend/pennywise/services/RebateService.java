package com.backend.pennywise.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.pennywise.entities.CreditCard;
import com.backend.pennywise.entities.Rebate;
import com.backend.pennywise.entities.Transaction;
import com.backend.pennywise.exceptions.CreditCardNotFoundException;
import com.backend.pennywise.exceptions.RebateNotFoundException;
import com.backend.pennywise.repositories.CreditCardRepository;
import com.backend.pennywise.repositories.RebateRepository;

@Service
public class RebateService {
	@Autowired
	private RebateRepository rebateRepo;
	
	@Autowired
	private CreditCardRepository ccRepo;
	
	Logger logger = LogManager.getLogger(RebateService.class);
	
	public List<Rebate> getAllRebates() {
		return rebateRepo.findAll();
	}

	public Rebate findRebateById(long rebateId) {
		Rebate Rebate = rebateRepo.findById(rebateId)
								 .orElseThrow(()->{
								 	logger.error("Rebate id " + rebateId + " not found");
								 	return new RebateNotFoundException("Cashback Rebate not found");
								 });
		
		return Rebate;
	}

	public Rebate addRebate(Rebate Rebate) {
		return rebateRepo.save(Rebate);
	}

	public Rebate updateRebatesDetails(Map<String, Object> updates, long rebateId) {
		Rebate existingRebate = rebateRepo.findById(rebateId)
								 .orElseThrow(()->{
								 	logger.error("Rebate id " + rebateId + " not found");
								 	return new RebateNotFoundException("Cashback Rebate not found");
								 });
		
		if (updates.containsKey("creditCard")) {
			existingRebate.setCreditCard((CreditCard)updates.get("creditCard"));
			logger.info("Updating credit card with rebate id " + rebateId);
		}
		
		if (updates.containsKey("transaction")) {
			existingRebate.setTransaction((Transaction)updates.get("transaction"));
			logger.info("Updating transaction with rebate id " + rebateId);
		}
		
		return rebateRepo.save(existingRebate);
	}

	public Map<String, Boolean> deleteRebatesById(long rebateId) {
		Rebate existingRebate = rebateRepo.findById(rebateId)
				 .orElseThrow(()->{
				 	logger.error("Rebate id " + rebateId + " not found");
				 	return new RebateNotFoundException("Cashback Rebate not found");
				 });
		
		rebateRepo.delete(existingRebate);
		logger.info("Successfully deleted rebate  id " + rebateId);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		
		return response;
		
	}
	
	 public List<Rebate> findRebatesByCCNumber (long cardNumber){
		 CreditCard existingCC = ccRepo.findById(cardNumber).orElseThrow(() -> new CreditCardNotFoundException("Credit Card not found"));

		 List<Rebate> Rebate = new ArrayList<>();
	     Rebate.addAll(rebateRepo.findByCreditCard(existingCC));

	     return Rebate;
	}

}

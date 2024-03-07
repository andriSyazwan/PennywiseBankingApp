package com.backend.pennywise.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.pennywise.entities.Merchant;
import com.backend.pennywise.entities.MerchantCategory;
import com.backend.pennywise.exceptions.MerchantNotFoundException;
import com.backend.pennywise.repositories.MerchantCategoryRepository;
import com.backend.pennywise.repositories.MerchantRepository;

@Service
public class MerchantService {
	@Autowired
	private MerchantRepository merchantRepo;
	
	@Autowired
	private MerchantCategoryRepository merchantCategoryRepo;
	
	Logger logger = LogManager.getLogger(MerchantService.class);
			
	public List<Merchant> getAllMerchants() {
		return merchantRepo.findAll();
	}
	
	public List<MerchantCategory> getAllMerchantCategories() {
		return merchantCategoryRepo.findAll();
	}

	public Merchant findMerchantById(long merchantId) {
		Merchant existingMerchant = merchantRepo.findById(merchantId)
									.orElseThrow(() -> {
										logger.error("Merchant with Id " + merchantId + " not found");
										return new MerchantNotFoundException("Merchant not found");
									});
		
		return existingMerchant;
	}

	public Merchant addMerchant(Merchant merchant) {
		return merchantRepo.save(merchant);
	}

	public Merchant updateMerchantDetails(Map<String, Object> updates, long merchantId) {
		Merchant existingMerchant = merchantRepo.findById(merchantId)
									.orElseThrow(() -> {
										logger.error("Merchant with Id " + merchantId + " not found");
										return new MerchantNotFoundException("Merchant not found");
									});
		
		if (updates.containsKey("name")) {
			existingMerchant.setName((String)updates.get("name"));
			logger.info("Updating name for merchant Id " + merchantId);
		}
		
		if (updates.containsKey("merchantCategory")) {
			existingMerchant.setMerchantCategory((MerchantCategory)updates.get("merchantCategory"));
			logger.info("Updating merchant category for merchant Id " + merchantId);
		}
		
		return merchantRepo.save(existingMerchant);
	}

	public Map<String, Boolean> deleteMerchantByMerchantId(long merchantId) {
		Merchant existingMerchant = merchantRepo.findById(merchantId)
									.orElseThrow(() -> {
										logger.error("Merchant with Id " + merchantId + " not found");
										return new MerchantNotFoundException("Merchant not found");
									});
		
		merchantRepo.delete(existingMerchant);
		logger.info("Successfully deleted merchant id " + merchantId);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		
		return response;
	}

	public List<Merchant> findByMCC(int merchantCategoryCode) {
		return merchantRepo.findByMerchantCategory_MerchantCategoryCode(merchantCategoryCode);
	}
}

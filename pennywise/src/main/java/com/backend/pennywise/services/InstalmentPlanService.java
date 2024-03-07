package com.backend.pennywise.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.pennywise.entities.InstalmentPlan;
import com.backend.pennywise.exceptions.InstalmentPlanNotFoundException;
import com.backend.pennywise.repositories.InstalmentPlanRepository;

@Service
public class InstalmentPlanService {
	@Autowired
	InstalmentPlanRepository instalmentPlanRepo;
	
	Logger logger = LogManager.getLogger(InstalmentPlanService.class);
	
	public List<InstalmentPlan> findAllPlans() {
		return instalmentPlanRepo.findAll();
	}
	
	public InstalmentPlan findById(long planId) {
		InstalmentPlan plan = instalmentPlanRepo.findById(planId)
				  		.orElseThrow(()-> {
						  	logger.error("Instalment plan with Id" + planId + "not found");
						  	return new InstalmentPlanNotFoundException("Instalment plan not found");
				  		});
		
		return plan;
	}

	public InstalmentPlan addInstalmentPlan(InstalmentPlan plan) {
		logger.info("New instalment plan saved");
		return instalmentPlanRepo.save(plan);
	}

	public InstalmentPlan updateInstalmentPlan(long planId, Map<String, Object> updates) {
		InstalmentPlan existingPlan = instalmentPlanRepo.findById(planId)
				  .orElseThrow(()-> {
					  	logger.error("Instalment plan with Id" + planId + "not found");
					  	return new InstalmentPlanNotFoundException("Instalment plan not found");
					  });
		
		if (updates.containsKey("noOfInstalments")) {
			existingPlan.setNoOfInstalments((short)updates.get("noOfInstalments"));
			logger.info("Updated no of instalments for instalment plan id " + planId);
		}
		
		if (updates.containsKey("interestRate")) {
			existingPlan.setInterestRate((BigDecimal)updates.get("insterestRate"));
			logger.info("Updated interest rate for instalment plan id " + planId);
		}
		
		return instalmentPlanRepo.save(existingPlan);
	}
}

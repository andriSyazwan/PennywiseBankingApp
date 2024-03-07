package com.backend.pennywise.controllers;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.backend.pennywise.entities.InstalmentPlan;
import com.backend.pennywise.services.InstalmentPlanService;

@RestController
@RequestMapping("/api/v1/instalmentPlans")
public class InstalmentPlanController {
	@Autowired
	InstalmentPlanService instalmentPlanService;
	
	@GetMapping
	public List<InstalmentPlan> getAllInstalmentPlans(){
		return instalmentPlanService.findAllPlans();
	}
	
	@PostMapping("/add")
	public ResponseEntity<InstalmentPlan> addNewInstalmentPlan(@RequestBody InstalmentPlan plan){
		InstalmentPlan newPlan = instalmentPlanService.addInstalmentPlan(plan);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
			       .buildAndExpand(newPlan.getClass()).toUri();

		return ResponseEntity.created(location).body(newPlan);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<InstalmentPlan> updateCreditCard(@PathVariable(value = "id") long planId, 
			   											@RequestBody Map<String, Object> updates){
		InstalmentPlan updatedPlan = instalmentPlanService.updateInstalmentPlan(planId, updates);

		return ResponseEntity.ok(updatedPlan);
}
}

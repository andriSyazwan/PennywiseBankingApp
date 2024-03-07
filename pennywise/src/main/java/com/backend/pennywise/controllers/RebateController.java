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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.backend.pennywise.entities.Rebate;
import com.backend.pennywise.services.RebateService;

@RestController
@RequestMapping("api/v1/rebates")
public class RebateController {
	@Autowired
	private RebateService RebateService;
	
	@GetMapping
	public List<Rebate> getAllRebate(){
		return RebateService.getAllRebates();
	}
	
	@GetMapping("/{id}")
	public Rebate getRebate(@PathVariable("id") long RebateId) {
		return RebateService.findRebateById(RebateId);
	}
	
	@PostMapping("/add")
	public ResponseEntity<Rebate> addNewRebate(@RequestBody Rebate Rebate){
		Rebate savedRebate = RebateService.addRebate(Rebate);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				       .buildAndExpand(savedRebate.getClass()).toUri();

		return ResponseEntity.created(location).body(savedRebate);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Rebate> updateRebate(@RequestBody Map<String, Object> updates,
																 @PathVariable(value = "id") long RebatesId){
		Rebate updatedRebate = RebateService.updateRebatesDetails(updates, RebatesId);
		
		return ResponseEntity.ok(updatedRebate);
	}

	@DeleteMapping("/{id}")
	public Map<String, Boolean> deleteRebate(@PathVariable(value = "id") long RebateId){
		return RebateService.deleteRebatesById(RebateId);
	}
	
	@GetMapping("/creditCard/{id}")
    public List<Rebate> getRebateByCCNo(@PathVariable("id") long cardNumber) {
        return RebateService.findRebatesByCCNumber(cardNumber);
    }
}

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

import com.backend.pennywise.entities.Merchant;
import com.backend.pennywise.entities.MerchantCategory;
import com.backend.pennywise.services.MerchantService;

@RestController
@RequestMapping("api/v1/merchants")
public class MerchantController {
	@Autowired
	private MerchantService merchantService;
	
	@GetMapping
	public List<Merchant> getAllMerchants(){
		return merchantService.getAllMerchants();
	}
	
	@GetMapping("/categories")
	public List<MerchantCategory> getAllMerchantCategories(){
		return merchantService.getAllMerchantCategories();
	}
	
	@GetMapping("/{id}")
	public Merchant getMerchant(@PathVariable("id") long merchantId) {
		return merchantService.findMerchantById(merchantId);
	}
	
	@GetMapping("/mcc/{code}")
	public List<Merchant> findMerchantByMCC(@PathVariable("code") int mcc){
		return merchantService.findByMCC(mcc);
	}
	
	@PostMapping("/add")
	public ResponseEntity<Merchant> addNewMerchant(@RequestBody Merchant merchant){
		Merchant savedMerchant = merchantService.addMerchant(merchant);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				       .buildAndExpand(savedMerchant.getClass()).toUri();

		return ResponseEntity.created(location).body(savedMerchant);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Merchant> updateMerchant(@RequestBody Map<String, Object> updates,
												   @PathVariable(value = "id") long merchantId){
		Merchant updatedMerchant = merchantService.updateMerchantDetails(updates, merchantId);
		
		return ResponseEntity.ok(updatedMerchant);
	}

	@DeleteMapping("/{id}")
	public Map<String, Boolean> deleteMerchant(@PathVariable(value = "id") long merchantId){
		return merchantService.deleteMerchantByMerchantId(merchantId);
	}
}

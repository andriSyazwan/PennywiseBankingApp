package com.backend.pennywise.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.pennywise.entities.FXRates;
import com.backend.pennywise.services.FXRatesService;

@RestController
@RequestMapping("/api/v1/fxRates")
public class FXRatesController {
	@Autowired
	FXRatesService fxRatesService;
	
	@GetMapping
	public List<FXRates> getAllFXRates(){
		return fxRatesService.findAllRates();
	}
	
	
	@GetMapping("/{id}")
	public FXRates getRateByCode(@PathVariable (value = "id") String currencyCode) {
		return fxRatesService.findByCurrencyCode(currencyCode);
	}
	
	@GetMapping("/convert")
	public BigDecimal convertToSGD(@RequestParam String currencyCode, @RequestParam BigDecimal amount) {
		return fxRatesService.convertCurrency(currencyCode, amount);
	}
							
}

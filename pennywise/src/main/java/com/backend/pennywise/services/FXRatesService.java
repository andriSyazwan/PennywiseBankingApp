package com.backend.pennywise.services;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.backend.pennywise.entities.FXRates;
import com.backend.pennywise.exceptions.FXRateNotFoundException;
import com.backend.pennywise.repositories.FXRatesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FXRatesService {
	@Autowired
	private FXRatesRepository fxRatesRepository;
	
	Logger logger = LogManager.getLogger(FXRatesService.class);
	
	public List<FXRates> findAllRates() {
		return fxRatesRepository.findAll();
	}

	public FXRates findByCurrencyCode(String currencyCode) {
		FXRates rate = fxRatesRepository.findByCurrencyCode(currencyCode)
				.orElseThrow(()-> {
					logger.error("Currency rate "+ currencyCode + " not found");
					return new FXRateNotFoundException("Invalid currency code");
				});
		
		return rate;
	}
	
	// Scheduled to run at 10am everyday
	// cron = second, minute, hour, day of month, month, day of week
	@Scheduled(cron = "0 0 10 * * *")
	public void storeConversionRates() {
		logger.info("Storing fx rates from external API");
		
		String url = "https://v6.exchangerate-api.com/v6/6a25d2568bcde93801f8c69c/latest/SGD";
		Map<String, Object> rates = null;
		
		try {
			// Open stream 
			InputStream stream = new URL(url).openStream(); 
			ObjectMapper objectMapper = new ObjectMapper();
			
			// parse to json 
			Map<String, Object>  conversion = objectMapper.readValue(stream, Map.class);
			rates = (Map<String, Object>) conversion.get("conversion_rates");
		}
		catch (IOException e) {
			logger.error("Error pulling information from external API");
            e.printStackTrace();
        }
		
		// Store in database
		rates.forEach((currencyCode, currencyRate) -> {
			BigDecimal rate = BigDecimal.valueOf(((Number)currencyRate).doubleValue());
			FXRates fxRate = new FXRates(currencyCode, rate);
			fxRatesRepository.save(fxRate);
		});
		logger.info("Successfully stored rates in database");
	}
	
	public BigDecimal convertCurrency(String currencyCode, BigDecimal amount) {
		FXRates fxRate = fxRatesRepository.findByCurrencyCode(currencyCode)
						.orElseThrow(()-> {
							logger.error("Currency rate "+ currencyCode + " not found");
							return new FXRateNotFoundException("Invalid currency code");
						});
		
		BigDecimal rate = fxRate.getRate();
		
		// Going to the "1" from other currency so divide 
		// Round to 2 decimal places
		BigDecimal convertedAmount = amount.divide(rate, 2, RoundingMode.HALF_UP);
	    
	    logger.info("Successfully converted " + currencyCode + amount + " to SGD" + convertedAmount);
	    return convertedAmount;			
	}
}

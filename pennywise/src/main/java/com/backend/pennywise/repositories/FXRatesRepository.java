package com.backend.pennywise.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.pennywise.entities.FXRates;

public interface FXRatesRepository extends JpaRepository <FXRates, String> {

	Optional<FXRates> findByCurrencyCode(String currencyCode);

}

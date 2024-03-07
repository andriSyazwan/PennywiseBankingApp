package com.backend.pennywise.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.pennywise.entities.CreditCard;
import com.backend.pennywise.entities.User;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long>{

	List<CreditCard> findByUser(User existingUser);
	
	Optional<CreditCard> findByCardNumber(long cardNumber);

}

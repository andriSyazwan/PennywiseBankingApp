package com.backend.pennywise.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.pennywise.entities.CreditCard;
import com.backend.pennywise.entities.Rebate;
import com.backend.pennywise.entities.Transaction;

@Repository
public interface RebateRepository extends JpaRepository<Rebate, Long>{

	void deleteByTransaction(Transaction transaction);
	
	List<Rebate> findByCreditCard(CreditCard creditCard);

	Collection<? extends Rebate> findByCreditCard_CardId(long cardId);

}

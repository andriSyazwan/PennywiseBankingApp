package com.backend.pennywise.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.pennywise.entities.CreditCard;
import com.backend.pennywise.entities.CreditCardBill;

@Repository
public interface CreditCardBillRepository extends JpaRepository<CreditCardBill, Long>{

	List<CreditCardBill> findByCreditCard(CreditCard existingCard);

	List<CreditCardBill> findByCreditCardIn(List<CreditCard> creditCards);

	Optional<CreditCardBill> findTopByCreditCardOrderByBillDateDesc(CreditCard existingCreditCard);

	Optional<CreditCardBill> findByCreditCardAndBillDateBetween(CreditCard existingCreditCard, Date startDate, Date endDate);
}

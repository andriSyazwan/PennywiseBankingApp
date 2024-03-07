package com.backend.pennywise.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.pennywise.entities.CreditCard;
import com.backend.pennywise.entities.Point;
import com.backend.pennywise.entities.Transaction;

@Repository
public interface PointRepository extends JpaRepository<Point, Long>{

	void deleteByTransaction(Transaction transaction);

	Collection<? extends Point> findByCreditCard_CardNumber(long cardNumber);

	Collection<? extends Point> findByCreditCard(CreditCard existingCC);

}

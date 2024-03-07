package com.backend.pennywise.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.pennywise.entities.CreditCard;
import com.backend.pennywise.entities.InstalmentTransaction;
import com.backend.pennywise.entities.User;



@Repository
public interface InstalmentTransactionRepository extends JpaRepository <InstalmentTransaction, Long > {

	Collection<? extends InstalmentTransaction> findByCreditCard(CreditCard existingCC);

	@Query("SELECT i FROM InstalmentTransaction i JOIN i.creditCard cc JOIN cc.user u WHERE u = :existingUser")
    Collection<? extends InstalmentTransaction> findByUser(@Param("existingUser") User existingUser);
	
	List<InstalmentTransaction> findByIsPaid(boolean b);

}

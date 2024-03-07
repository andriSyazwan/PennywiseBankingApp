package com.backend.pennywise.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.pennywise.entities.BankAccount;
import com.backend.pennywise.entities.User;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long>{

	List<BankAccount> findByUser(User existingUser);

	List<BankAccount> findByUser(BankAccount existingUser);

}

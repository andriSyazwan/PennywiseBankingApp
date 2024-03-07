package com.backend.pennywise.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.pennywise.entities.BankAccount;
import com.backend.pennywise.entities.CreditCard;
import com.backend.pennywise.entities.Merchant;
import com.backend.pennywise.entities.Transaction;
import com.backend.pennywise.enums.TransactionType;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	
	List<Transaction> findByMerchant(Merchant existingMerchant);

	List<Transaction> findByBankAccount(BankAccount existingAccount);

	List<Transaction> findByBankAccountIn(List<BankAccount> bankAccounts);

	List<Transaction> findByCreditCard(CreditCard existingCreditCard);
	
	List<Transaction> findByCreditCardIn(List<CreditCard> creditCards);	
	
	@Query("select t from Transaction t where t.creditCard in :existingCard order by t.date desc")
	List<Transaction> findLatest5TransCreditCardOrderedByDateDescending(@Param("existingCard")CreditCard existingCard, Pageable pageable);

	@Query("select t from Transaction t where t.bankAccount in :existingAccount order by t.date desc")
	List<Transaction> findLatest5TransBankAccOrderedByDateDescending(@Param("existingAccount")BankAccount existingBankAccount,
			Pageable pageable);
	
	List<Transaction> findByBankAccountAndDateBetween(BankAccount bankAccount, Date startDate, Date endDate);

	List<Transaction> findByCreditCardAndDateBetween(CreditCard existingCreditCard, Date startDate, Date endDate);

	@Query("select t from Transaction t where t.bankAccount in :bankAccounts order by t.date desc")
	List<Transaction> findLatest5TransactionsOrderedByDateDescending(List<BankAccount> bankAccounts, Pageable pageable);

	List<Transaction> findBySourceAccount(BankAccount existingAccount);

	List<Transaction> findByDestinationAccount(BankAccount existingAccount);

	List<Transaction> findByCreditCardAndTransactionType(CreditCard existingCreditCard,
			TransactionType creditCardBillPayment);

	@Query("select t from Transaction t where t.creditCard in :existingCreditCard "
			+ "and t.type not in ('CashBack', 'Points')"
			+ " and t.date between :startDate and :endDate")
    List<Transaction> findNonCashBackAndPointsTransactionsAndDateBetween(@Param("existingCreditCard") CreditCard existingCreditCard,
    																	 @Param("startDate") Date startDate,
    																	 @Param ("endDate") Date endDate);
}

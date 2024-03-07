package com.backend.pennywise.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.pennywise.DataLoader;
import com.backend.pennywise.entities.BankAccount;
import com.backend.pennywise.entities.Transaction;
import com.backend.pennywise.entities.User;
import com.backend.pennywise.enums.AccountType;
import com.backend.pennywise.enums.TransactionType;
import com.backend.pennywise.enums.Type;
import com.backend.pennywise.exceptions.BankAccountNotFoundException;
import com.backend.pennywise.exceptions.InsufficientBalanceException;
import com.backend.pennywise.exceptions.UserNotFoundException;
import com.backend.pennywise.repositories.BankAccountRepository;
import com.backend.pennywise.repositories.RebateRepository;
import com.backend.pennywise.repositories.TransactionRepository;
import com.backend.pennywise.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BankAccountService {
	@Autowired
	private BankAccountRepository bankAccountRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TransactionRepository transactionRepo;

	@Autowired
	private RebateRepository cashBackRewardsRepo;

	public List<BankAccount> getAllAccounts() {
		return bankAccountRepo.findAll();
	}

	public BankAccount findAccountById(long bankAccountId) {
		BankAccount existingBankAccount = bankAccountRepo.findById(bankAccountId)
										 .orElseThrow(() -> new BankAccountNotFoundException("Bank Account not found"));

		return existingBankAccount;
	}

	public BankAccount addBankAccount(BankAccount bankAccount) {
		long generatedAccountNumber = DataLoader.generateUniqueAccountId();
		
		bankAccount.setAccountNumber(generatedAccountNumber);
		
		return bankAccountRepo.save(bankAccount);
	}

	public BankAccount updateBankAccountDetails(Map<String, Object> updates, long bankAccountId) {
		BankAccount existingBankAccount = bankAccountRepo.findById(bankAccountId)
				                          .orElseThrow(() -> new BankAccountNotFoundException("Bank Account not found"));
		
		if (updates.containsKey("accountType")) {
			existingBankAccount.setAccountType((AccountType)updates.get("accountType"));
		}
		
		if (updates.containsKey("user")) {
			existingBankAccount.setUser((User)updates.get("user"));
		}
		
		if (updates.containsKey("balance")) {
		    Object balanceValue = updates.get("balance");
		    
		    if (balanceValue instanceof Number) {
		        existingBankAccount.setBalance((BigDecimal) balanceValue);
		    } else {
		        throw new IllegalArgumentException("Invalid type for balance. Expected Number, got " + balanceValue.getClass());
		    }
		}

		return bankAccountRepo.save(existingBankAccount);
	}

	@Transactional
	public Map<String, Boolean> deleteAccountByAccountId(Long bankAccountId) {
		BankAccount existingBankAccount = bankAccountRepo.findById(bankAccountId)
										  .orElseThrow(() -> new BankAccountNotFoundException("Bank Account not found"));

		List<Transaction> transactions = transactionRepo.findByBankAccount(existingBankAccount);

		// DEL ASSOCIATED TRANSACTION and cash back .
		for (Transaction transaction : transactions) {
			cashBackRewardsRepo.deleteByTransaction(transaction);
			transactionRepo.delete(transaction);
		}
		bankAccountRepo.delete(existingBankAccount);

		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);

		return response;
	}

	public List<BankAccount> findBankAccountsByUser(long userId) {
		User existingUser = userRepo.findById(userId)
                			.orElseThrow(()-> new UserNotFoundException("User not found"));
		
		List<BankAccount> userBankAccount = bankAccountRepo.findByUser(existingUser);
		
		return userBankAccount;

	}
	
	@Transactional
	public Map<String, Boolean> deleteAccountByUserId(long userId) {
		User existingUser = userRepo.findById(userId)
							.orElseThrow(() -> new UserNotFoundException("User not found"));

		List<BankAccount> bankAccounts = bankAccountRepo.findByUser(existingUser);

		// DEL ASSOCIATED TRANSACTION and cash back .
		for (BankAccount bankAccount : bankAccounts) {
			List<Transaction> transactions = transactionRepo.findByBankAccount(bankAccount);
			for (Transaction transaction : transactions) {
				cashBackRewardsRepo.deleteByTransaction(transaction);
				transactionRepo.delete(transaction);
			}
			bankAccountRepo.delete(bankAccount);
		}

		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);

		return response;
	}

	public Transaction withdraw(long bankAccountId, BigDecimal amount) {
		BankAccount existingBankAccount = bankAccountRepo.findById(bankAccountId)
										  .orElseThrow(() -> new BankAccountNotFoundException("Bank Account not found"));
		
		if ((existingBankAccount.getBalance()).compareTo(amount) < 0) {
			throw new InsufficientBalanceException("Insufficient balance.");
		}
		
		existingBankAccount.setBalance(existingBankAccount.getBalance().subtract(amount));
		bankAccountRepo.save(existingBankAccount);
		
		Transaction newTransaction = new Transaction(new Date(), amount.negate(), Type.Local, existingBankAccount);
		newTransaction.setTransactionType(TransactionType.Withdraw);
		return transactionRepo.save(newTransaction);
	}

	public Transaction deposit(long bankAccountId, BigDecimal amount) {
		BankAccount existingBankAccount = bankAccountRepo.findById(bankAccountId)
										  .orElseThrow(() -> new BankAccountNotFoundException("Bank Account not found"));
		
		existingBankAccount.setBalance(existingBankAccount.getBalance().add(amount));
		bankAccountRepo.save(existingBankAccount);
		
		Transaction newTransaction = new Transaction(new Date(), amount, Type.Local, existingBankAccount);
		newTransaction.setTransactionType(TransactionType.Deposit);
		return transactionRepo.save(newTransaction);
	}
	

	
	public Transaction transfer(long sourceAccountId, long receivingAccountId, BigDecimal amount) {
		
		BankAccount sourceAccount = bankAccountRepo.findById(sourceAccountId)
				.orElseThrow(() -> new BankAccountNotFoundException("Source Account not found"));

		BankAccount receivingAccount = bankAccountRepo.findById(receivingAccountId)
				.orElseThrow(() -> new BankAccountNotFoundException("Receiving Account not found"));

		  // Withdraw from the source account
	    Transaction sendMoneyTransaction = withdraw(sourceAccountId, amount);
	    sendMoneyTransaction.setTransactionType(TransactionType.Transfer);
	    sendMoneyTransaction.setSourceAccount(sourceAccount);
	    sendMoneyTransaction.setDestinationAccount(receivingAccount);
	    
	    
	    // Deposit into the receiving account
	    Transaction receiveMoneyTransaction = deposit(receivingAccountId, amount);
	    receiveMoneyTransaction.setTransactionType(TransactionType.Transfer);
	    receiveMoneyTransaction.setDestinationAccount(receivingAccount);
	    receiveMoneyTransaction.setSourceAccount(sourceAccount);
		
		transactionRepo.save(sendMoneyTransaction);
		transactionRepo.save(receiveMoneyTransaction);	

		return sendMoneyTransaction; 
	}
	

	
	
}

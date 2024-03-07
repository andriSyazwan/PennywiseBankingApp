package com.backend.pennywise.services;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.pennywise.entities.BankAccount;
import com.backend.pennywise.entities.CreditCard;
import com.backend.pennywise.entities.Merchant;
import com.backend.pennywise.entities.Transaction;
import com.backend.pennywise.entities.User;
import com.backend.pennywise.enums.TransactionType;
import com.backend.pennywise.enums.Type;
import com.backend.pennywise.exceptions.BankAccountNotFoundException;
import com.backend.pennywise.exceptions.CreditCardNotFoundException;
import com.backend.pennywise.exceptions.MerchantNotFoundException;
import com.backend.pennywise.exceptions.TransactionNotFoundException;
import com.backend.pennywise.exceptions.UserNotFoundException;
import com.backend.pennywise.repositories.BankAccountRepository;
import com.backend.pennywise.repositories.CreditCardRepository;
import com.backend.pennywise.repositories.MerchantRepository;
import com.backend.pennywise.repositories.TransactionRepository;
import com.backend.pennywise.repositories.UserRepository;

@Service
public class TransactionService {
	@Autowired
	private TransactionRepository transactionRepo;

	@Autowired
	private MerchantRepository merchantRepo;

	@Autowired
	private BankAccountRepository bankAccountRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CreditCardRepository creditCardRepo;
	
	Logger logger = LogManager.getLogger(TransactionService.class);

	public List<Transaction> getAllTransactons() {
		return transactionRepo.findAll();
	}

	public Transaction findTransactionById(long transactionId) {
		Transaction existingTransaction = transactionRepo.findById(transactionId)
				.orElseThrow(() -> {
					logger.error("Transaction id " + transactionId + " not found");
					return new TransactionNotFoundException("Transaction not found");
				});

		return existingTransaction;
	}

	public Transaction addTransaction(Transaction transaction) {
		return transactionRepo.save(transaction);
	}

	public Transaction updateTransactionDetails(Map<String, Object> updates, long transactionId) {
		Transaction existingTransaction = transactionRepo.findById(transactionId)
				.orElseThrow(() -> {
					logger.error("Transaction id " + transactionId + " not found");
					return new TransactionNotFoundException("Transaction not found");
				});

		if (updates.containsKey("merchant")) {
			existingTransaction.setMerchant((Merchant) updates.get("merchant"));
			logger.info("Updating merchant for transaction id " + transactionId);
		}

		if (updates.containsKey("bankAccount")) {
			existingTransaction.setBankAccount((BankAccount) updates.get("bankAccount"));
			logger.info("Updating bank account for transaction id " + transactionId);
		}

		if (updates.containsKey("date")) {
			existingTransaction.setDate((Date) updates.get("date"));
			logger.info("Updating date for transaction id " + transactionId);
		}

		if (updates.containsKey("amount")) {
			existingTransaction.setAmount((BigDecimal) updates.get("amount"));
			logger.info("Updating amount for transaction id " + transactionId);
		}

		if (updates.containsKey("type")) {
			existingTransaction.setType((Type) updates.get("type"));
			logger.info("Updating type for transaction id " + transactionId);
		}
		
		if (updates.containsKey("transactionType")) {
			existingTransaction.setTransactionType((TransactionType) updates.get("transactionType"));
			logger.info("Updating transcation type for transaction id " + transactionId);
		}

		return transactionRepo.save(existingTransaction);
	}

	public Map<String, Boolean> deleteTransactionById(long transactionId) {
		Transaction existingTransaction = transactionRepo.findById(transactionId)
				.orElseThrow(() -> {
					logger.error("Transaction id " + transactionId + " not found");
					return new TransactionNotFoundException("Transaction not found");
				});

		transactionRepo.delete(existingTransaction);
		logger.info("Transaction id " + transactionId + " successfully deleted");

		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);

		return response;
	}

	public List<Transaction> findTransactionByMerchantId(long merchantId) {
		Merchant existingMerchant = merchantRepo.findById(merchantId)
				.orElseThrow(() -> {
					logger.error("Merchant id " + merchantId + " not found");
					return new MerchantNotFoundException("Merchant not found");
				});

		return transactionRepo.findByMerchant(existingMerchant);
	}

	public List<Transaction> findTransactionByAccount(long accountId) {
		BankAccount existingAccount = bankAccountRepo.findById(accountId)
				.orElseThrow(() -> {
					logger.error("Bank account id " + accountId + " not found");
					return new BankAccountNotFoundException("Bank account not found");
				});

		return transactionRepo.findByBankAccount(existingAccount);
	}

	public List<Transaction> findTransactionByUser(long userId) {
		User existingUser = userRepo.findById(userId).orElseThrow(() -> {
			logger.error("User with userId " + userId + " not found");
			return new UserNotFoundException("User not found");
		});
		
		Set<Transaction> userTransactionsSet = new HashSet<>();
		
		List<BankAccount> bankAccounts = bankAccountRepo.findByUser(existingUser);
		List<CreditCard> creditCards = creditCardRepo.findByUser(existingUser);
		
		userTransactionsSet.addAll(transactionRepo.findByBankAccountIn(bankAccounts));
		userTransactionsSet.addAll(transactionRepo.findByCreditCardIn(creditCards));
		
		return userTransactionsSet.stream().collect(Collectors.toList());
	}

	public List<Transaction> findTransactionByCreditCard(long creditCardId) {
		CreditCard existingCreditCard = creditCardRepo.findById(creditCardId)				
									.orElseThrow(() -> {
										logger.error("Credit card with creditCardId " + creditCardId + "not found");
										return new CreditCardNotFoundException("Credit card not found");
									});

		List <Transaction> transactions = transactionRepo.findByCreditCardAndTransactionType(existingCreditCard, TransactionType.Credit_Card_Bill_Payment);
		transactions.addAll(transactionRepo.findByCreditCardAndTransactionType(existingCreditCard, TransactionType.Purchase));
		transactions.addAll(transactionRepo.findByCreditCardAndTransactionType(existingCreditCard, TransactionType.Purchase_With_Instalment));
		
		return transactions;
	}
	//find last 5 trasnasction by USER
	public List<Transaction> findLastFiveTransactionsByuser(long userId) {
		User existingUser = userRepo.findById(userId).orElseThrow(() -> {
			logger.error("User with userId " + userId + " not found");
			return new UserNotFoundException("User not found");
		});

		List<BankAccount> bankAccounts = bankAccountRepo.findByUser(existingUser);

	    // request the latest 5 transactions
	    Pageable pageable = PageRequest.of(0, 5); // Page number 0, page size 5

	    return transactionRepo.findLatest5TransactionsOrderedByDateDescending(bankAccounts, pageable);
	}

	
	//find last 5 transaction by CREDIT CARD
	public List<Transaction> findLastFiveTransactionsByCreditCard(long cardId) {
		CreditCard existingCard = creditCardRepo.findById(cardId)				
				.orElseThrow(() -> {
					logger.error("Credit card with creditCardId " + cardId + "not found");
					return new CreditCardNotFoundException("Credit card not found");
					});
	
	    // request the latest 5 transactions
	    Pageable pageable = PageRequest.of(0, 5); // Page number 0, page size 5

	    return transactionRepo.findLatest5TransCreditCardOrderedByDateDescending(existingCard, pageable);
	}
	//find last 5 transaction by BANK ACC
	public List<Transaction> findLastFiveTransactionsByBankAccount(long bankAccountId) {
		BankAccount existingBankAccount = bankAccountRepo.findById(bankAccountId)
				  .orElseThrow(()-> {
					  logger.error("Bank Account id" + bankAccountId + " not found");
					  return new BankAccountNotFoundException("Invalid bank account");
				  });
		
	    // request the latest 5 transactions
	    Pageable pageable = PageRequest.of(0, 5); // Page number 0, page size 5

	    return transactionRepo.findLatest5TransBankAccOrderedByDateDescending(existingBankAccount, pageable);
	}

	
	public List<Transaction> findBankAccountTransactionByMonth(long accountId, int month, int year) {
		BankAccount existingBankAccount = bankAccountRepo.findById(accountId)
				  .orElseThrow(()-> {
					  logger.error("Bank Account id" + accountId + " not found");
					  return new BankAccountNotFoundException("Invalid bank account");
				  });
		
		
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        // Set to specified month and first day of the month
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        // Set end date to last day of the current month at 23:59
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endDate = calendar.getTime();
        
        logger.info("Retrieve transactions by bank account between " + startDate + " and " + endDate);
        return transactionRepo.findByBankAccountAndDateBetween(existingBankAccount, startDate, endDate);
    }
	
	public List<Transaction> findBankAccountTransactionsByTimePeriod(long bankAccountId, Date startDate, Date endDate) {
		BankAccount existingBankAccount = bankAccountRepo.findById(bankAccountId)
				  .orElseThrow(()-> {
					  logger.error("Bank Account id" + bankAccountId + " not found");
					  return new BankAccountNotFoundException("Invalid bank account");
				  });
		
		return transactionRepo.findByBankAccountAndDateBetween(existingBankAccount, startDate, endDate);
	}
	
	public List<Transaction> findCreditCardTransactionsByMonth(long creditCardId, int month, int year) {
		logger.info("Finding credit card transactions for creditCardId: " + creditCardId + ", month: " + month + ", year: " + year);
		
		CreditCard existingCreditCard = creditCardRepo.findById(creditCardId)				
				.orElseThrow(() -> {
					logger.error("Credit card with creditCardId " + creditCardId + "not found");
					return new CreditCardNotFoundException("Credit card not found");
					});
		
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        // Set to specified month and first day of the month
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        // Set end date to last day of the specified month at 23:59:59
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endDate = calendar.getTime();

        List<Transaction> transactions = transactionRepo.findByCreditCardAndDateBetween(existingCreditCard, startDate, endDate);
        System.out.println(startDate);
        System.out.println(endDate);
        System.out.println("Found " + transactions.size() + " transactions");
        
        for (Transaction transaction : transactions) {
            System.out.println("Transaction ID: " + transaction.getTransactionId());
            System.out.println("Date: " + transaction.getDate());
            System.out.println("Amount: " + transaction.getAmount());
            System.out.println("Type: " + transaction.getType());
            // Add more details as needed
            System.out.println("-----------------------------");
        }

        return transactions;
    }
	
	public List<Transaction> findCreditCardTransactionsByTimePeriod(long creditCardId, Date startDate, Date endDate) {
		CreditCard existingCreditCard = creditCardRepo.findById(creditCardId)				
										.orElseThrow(() -> {
											logger.error("Credit card with creditCardId " + creditCardId + "not found");
											return new CreditCardNotFoundException("Credit card not found");
										});
		
		return transactionRepo.findByCreditCardAndDateBetween(existingCreditCard, startDate, endDate);
	}

	public List<Transaction> findTransactionsBySourceAccount(long accountId) {
		BankAccount existingAccount = bankAccountRepo.findById(accountId)
				  .orElseThrow(()-> {
					  logger.error("Bank Account id" + accountId + " not found");
					  return new BankAccountNotFoundException("Invalid bank account");
				  });

		return transactionRepo.findBySourceAccount(existingAccount);
	}

	public List<Transaction> findTransactionsByDestinationAccount(long accountId) {
		BankAccount existingAccount = bankAccountRepo.findById(accountId)
				  .orElseThrow(()-> {
					  logger.error("Bank Account id" + accountId + " not found");
					  return new BankAccountNotFoundException("Invalid bank account");
				  });

		return transactionRepo.findByDestinationAccount(existingAccount);
	}
}

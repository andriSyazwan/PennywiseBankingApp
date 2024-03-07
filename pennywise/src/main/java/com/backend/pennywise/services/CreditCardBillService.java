package com.backend.pennywise.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.backend.pennywise.entities.CreditCard;
import com.backend.pennywise.entities.CreditCardBill;
import com.backend.pennywise.entities.Transaction;
import com.backend.pennywise.entities.User;
import com.backend.pennywise.enums.TransactionType;
import com.backend.pennywise.enums.Type;
import com.backend.pennywise.exceptions.CreditCardBillNotFoundException;
import com.backend.pennywise.exceptions.CreditCardNotFoundException;
import com.backend.pennywise.exceptions.UserNotFoundException;
import com.backend.pennywise.repositories.CreditCardBillRepository;
import com.backend.pennywise.repositories.CreditCardRepository;
import com.backend.pennywise.repositories.TransactionRepository;
import com.backend.pennywise.repositories.UserRepository;

@Service
public class CreditCardBillService {

	@Autowired
	private CreditCardBillRepository billRepo;
	
	@Autowired
	private CreditCardRepository creditCardRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private TransactionRepository transactionRepo;
	
	Logger logger = LogManager.getLogger(CreditCardBillService.class);
	
	public List<CreditCardBill> getAllBills() {
		return billRepo.findAll();
	}

	public CreditCardBill findBillById(long creditCardBillId) {
		CreditCardBill bill = billRepo.findById(creditCardBillId)
						 .orElseThrow(()-> {
							 logger.error("Credit card bill id" + creditCardBillId + " not found");
							 return new CreditCardBillNotFoundException("Bill not found");
						 });
		
		logger.info("Found credit card bill with Id " + creditCardBillId);
		return bill;
	}

	public CreditCardBill addBill(CreditCardBill creditCardBill) {
		logger.info("Saved new bill");
		return billRepo.save(creditCardBill);
	}

	public CreditCardBill updateBillDetails(Map<String, Object> updates, long billId) {
		CreditCardBill existingBill = billRepo.findById(billId)
												 .orElseThrow(()-> {
													 logger.error("Credit card bill id" + billId + " not found");
													 return new CreditCardBillNotFoundException("Bill not found");
												 });
		
		if (updates.containsKey("creditCard")) {
			existingBill.setCreditCard((CreditCard)updates.get("creditCard"));
			logger.info("Updated credit card for credit card bill id " + billId);
		}
		
		if (updates.containsKey("billAmount")){
			existingBill.setBillAmount(BigDecimal.valueOf(((Number)updates.get("billAmount")).doubleValue()));
			logger.info("Updated bill amount for credit card bill id " + billId);
		}
		
		if (updates.containsKey("dueDate")){
			existingBill.setDueDate((Date)updates.get("dueDate"));
			logger.info("Updated due date for credit card bill id " + billId);
		}
		
		if (updates.containsKey("billingPeriodStart")) {
			existingBill.setBillingPeriodStart((Date)updates.get("billingPeriodStart"));
			logger.info("Updated biling period start for credit card bill id " + billId);
		}
		
		if (updates.containsKey("billingPeriodEnd")) {
			existingBill.setBillingPeriodEnd((Date)updates.get("billingPeriodEnd"));
			logger.info("Updated biling period end for credit card bill id " + billId);
		}
		
		return billRepo.save(existingBill);
	}

	public Map<String, Boolean> deleteBillById(long creditCardBillId) {
		CreditCardBill existingBill = billRepo.findById(creditCardBillId)
							 .orElseThrow(()-> {
								 logger.error("Credit card bill id" + creditCardBillId + " not found");
								 return new CreditCardBillNotFoundException("Bill not found");
							 });
		
		billRepo.delete(existingBill);
		logger.info("Bill Id " + creditCardBillId + " deleted successfully");
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		
		return response;
	}

	public List<CreditCardBill> findByCreditCard(long creditCardId) {
		CreditCard existingCard = creditCardRepo.findById(creditCardId)				
									.orElseThrow(() -> {
											logger.error("Credit card with creditCardId " + creditCardId + "not found");
											return new CreditCardNotFoundException("Credit card not found");
										});
		
		logger.info("Found bills by credit card id " + creditCardId);
		return billRepo.findByCreditCard(existingCard);
	}

	public List<CreditCardBill> findByUser(long userId) {
		User existingUser = userRepo.findById(userId).orElseThrow(() -> {
							logger.error("User with userId " + userId + " not found");
							return new UserNotFoundException("User not found");
						});
		
		List <CreditCard> creditCards = creditCardRepo.findByUser(existingUser);
		
		logger.info("Found bills by credit cards from user id " + userId);
		return billRepo.findByCreditCardIn(creditCards);
	}
	
	public CreditCardBill findLatestBillByCreditCard(long creditCardId) {
		CreditCard existingCreditCard = creditCardRepo.findById(creditCardId)				
										.orElseThrow(() -> {
												logger.error("Credit card with creditCardId " + creditCardId + "not found");
												return new CreditCardNotFoundException("Credit card not found");
											});
		
		CreditCardBill bill = billRepo.findTopByCreditCardOrderByBillDateDesc(existingCreditCard)
							  .orElseThrow(()-> {
								  		logger.error("No latest bill found for card id " + creditCardId);
								  		return new CreditCardBillNotFoundException("Bill not found");}
									  );
		logger.info("Retrieved latest bill for credit card id " + creditCardId);
		return bill;
	}
	
	public CreditCardBill findBillByCreditCardByMonth(long creditCardId, int month, int year) {
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
		Date startDate = calendar.getTime();
		
	    // Set end date to last day of the current month at 23:59
	    calendar.set(Calendar.MONTH, month);
	    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    Date endDate = calendar.getTime();
		
		CreditCardBill bill = billRepo.findByCreditCardAndBillDateBetween(existingCreditCard, startDate, endDate)
							  .orElseThrow(()-> {
								  logger.error("No bill found for credit card Id" + creditCardId + 
										  	" between " + startDate + " to " + endDate);
								  return new CreditCardBillNotFoundException("No credit card bill found");
							  });
		return bill;
	}
	
	// Set billing period to previous month
	// Get transactions from previous month and add up amount
	// Set due date
	public CreditCardBill generateBill(long cardId) {
		CreditCard existingCreditCard = creditCardRepo.findById(cardId)				
				.orElseThrow(() -> {
						logger.error("Credit card with creditCardId " + cardId + "not found");
						return new CreditCardNotFoundException("Credit card not found");
					});
		
		logger.info("Generating bill for card id " + cardId);
		
		CreditCardBill newBill = new CreditCardBill();
		newBill.setCreditCard(existingCreditCard);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		System.out.println(calendar.getTime());
		newBill.setBillDate(calendar.getTime());
		
		// Set due date 21 days later
		calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 21);
		Date dueDate = calendar.getTime();
		newBill.setDueDate(dueDate);
		
		// Billing period
		// Set to first day of previous month
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		Date billingPeriodStart = calendar.getTime();
		newBill.setBillingPeriodStart(billingPeriodStart);
		
		// Set to last day of previous month
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		Date billingPeriodEnd = calendar.getTime();
		newBill.setBillingPeriodEnd(billingPeriodEnd);
		
		// Calculate bill amount
		// Get all transactions from previous month, add up amount
		List <Transaction> pastMonthTransactions = transactionRepo.findNonCashBackAndPointsTransactionsAndDateBetween(existingCreditCard, 
												   billingPeriodStart, billingPeriodEnd);
		
		BigDecimal billAmount = BigDecimal.valueOf(0.0);
		for (Transaction transaction : pastMonthTransactions) {
			billAmount = billAmount.add((transaction.getAmount()).abs());
		}
		billAmount.setScale(2, RoundingMode.HALF_UP);
		
		newBill.setBillAmount(billAmount);
		
		logger.info("Bill id " + newBill.getBillId() + " generated for credit card id " + cardId);
		return billRepo.save(newBill);
	}
	
	// Generate bill at first day of every month
	@Scheduled(cron = "0 0 0 1 * *")
	public void scheduledBillGenerator() {
		logger.info("Generating bills for all credit cards");
		List<CreditCard> allCreditCards = creditCardRepo.findAll();
		
		for (CreditCard card : allCreditCards) {
			generateBill(card.getCardId());
		}
	}
	
	// Due date
	// Add interest for unpaid amount
	// Check if bill isPaid, else add late payment fee
	// Late payment $200 to cc creditBalance, create transaction for card
	// (billAmount - paymentAmount) * interest rate
	public List<Transaction> dueDateCharges(long cardId) {
		CreditCard existingCard = creditCardRepo.findById(cardId)				
				.orElseThrow(() -> {
					logger.error("Credit card with creditCardId " + cardId + "not found");
					return new CreditCardNotFoundException("Credit card not found");
					});
		
		CreditCardBill latestBill = billRepo.findTopByCreditCardOrderByBillDateDesc(existingCard)
									 .orElseThrow(()-> {
										 logger.info("Latest bill not found for credit card Id " + cardId);
										 return new CreditCardBillNotFoundException("Credit card bill not found");
									 });
		
		logger.info("Generating charges for credit card id " + cardId + ", bill id " + latestBill.getBillId());
		
		List<Transaction> extraCharges = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.HOUR_OF_DAY, 12);
		Date newDate = calendar.getTime();
		System.out.println(newDate);
		
		// Add late fee to cc credit balance if not paid
		if (!latestBill.getIsPaid()) {
			BigDecimal lateFee = BigDecimal.valueOf(200.00);
			
			existingCard.setCreditBalance(existingCard.getCreditBalance().add(lateFee));
			
			Transaction lateFeeTransaction = new Transaction(newDate, lateFee, Type.Local,
															 TransactionType.Credit_Card_Late_Fee,
															 existingCard);
			creditCardRepo.save(existingCard);
			transactionRepo.save(lateFeeTransaction);
			extraCharges.add(lateFeeTransaction);
			
			logger.info("Late fee of $200 added to credit card id " + cardId);
		}
		
		// Add interest rate for remaining amount
		// Bring balance forward to next upcoming bill
		if (latestBill.getBillAmount() != latestBill.getAmountPaid() ) {
			BigDecimal interestRate = BigDecimal.valueOf(0.28);
			
			BigDecimal unpaidAmount = (latestBill.getBillAmount()).subtract(latestBill.getAmountPaid());
			
			BigDecimal interestAmount = unpaidAmount.multiply(interestRate);
			interestAmount.setScale(2, RoundingMode.HALF_UP);
			
			existingCard.setCreditBalance((existingCard.getCreditBalance()).add(interestAmount));
		
			Transaction interestRateTransaction = new Transaction(newDate, interestAmount,
																  Type.Local, TransactionType.Credit_Card_Interest,
																  existingCard);
			creditCardRepo.save(existingCard);
			transactionRepo.save(interestRateTransaction);
			
			Transaction outstandingAmountTransaction = new Transaction(newDate, unpaidAmount,Type.Local, 
																	   TransactionType.Credit_Card_Balance_Brought_Forward,
																	   existingCard);
			transactionRepo.save(outstandingAmountTransaction);
			
			extraCharges.add(interestRateTransaction);
			extraCharges.add(outstandingAmountTransaction);
			
			logger.info("Brought forward balance + interest rate $" + unpaidAmount + "credit card id " + cardId);
		}
		
		return extraCharges;
	}
	
	// Scheduled to run on 21st every month
	@Scheduled(cron = "0 0 0 21 * *")
	public void scheduledBillDueDate(){
		logger.info("Generating bill charges");
		List<CreditCard> allCreditCards = creditCardRepo.findAll();
		
		for (CreditCard card : allCreditCards) {
			dueDateCharges(card.getCardId());
		}
	}
}

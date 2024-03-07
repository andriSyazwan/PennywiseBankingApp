package com.backend.pennywise.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.pennywise.entities.BankAccount;
import com.backend.pennywise.entities.CreditCard;
import com.backend.pennywise.entities.CreditCardBill;
import com.backend.pennywise.entities.FXRates;
import com.backend.pennywise.entities.InstalmentPlan;
import com.backend.pennywise.entities.InstalmentTransaction;
import com.backend.pennywise.entities.Merchant;
import com.backend.pennywise.entities.Point;
import com.backend.pennywise.entities.Rebate;
import com.backend.pennywise.entities.Transaction;
import com.backend.pennywise.entities.User;
import com.backend.pennywise.enums.TransactionType;
import com.backend.pennywise.enums.Type;
import com.backend.pennywise.exceptions.BankAccountNotFoundException;
import com.backend.pennywise.exceptions.CreditCardBillNotFoundException;
import com.backend.pennywise.exceptions.CreditCardNotFoundException;
import com.backend.pennywise.exceptions.CreditLimitReachedException;
import com.backend.pennywise.exceptions.FXRateNotFoundException;
import com.backend.pennywise.exceptions.InstalmentPlanNotFoundException;
import com.backend.pennywise.exceptions.InsufficientBalanceException;
import com.backend.pennywise.exceptions.InsufficientPaymentException;
import com.backend.pennywise.exceptions.InvalidPaymentAmountException;
import com.backend.pennywise.exceptions.MerchantNotFoundException;
import com.backend.pennywise.exceptions.UserNotFoundException;
import com.backend.pennywise.repositories.BankAccountRepository;
import com.backend.pennywise.repositories.CreditCardBillRepository;
import com.backend.pennywise.repositories.CreditCardRepository;
import com.backend.pennywise.repositories.FXRatesRepository;
import com.backend.pennywise.repositories.InstalmentPlanRepository;
import com.backend.pennywise.repositories.InstalmentTransactionRepository;
import com.backend.pennywise.repositories.MerchantRepository;
import com.backend.pennywise.repositories.PointRepository;
import com.backend.pennywise.repositories.RebateRepository;
import com.backend.pennywise.repositories.TransactionRepository;
import com.backend.pennywise.repositories.UserRepository;

@Service
public class CreditCardService {
	@Autowired
	private CreditCardRepository creditCardRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TransactionRepository transactionRepo;

	@Autowired
	private MerchantRepository merchantRepo;

	@Autowired
	private FXRatesRepository fxRepo;
	
	@Autowired
	private CreditCardBillRepository ccBillRepo;
	
	@Autowired
	private BankAccountRepository bankAccountRepo;
	
	@Autowired
	private RebateRepository rebateRepo;
	
	@Autowired
	private PointRepository pointRepo;
	
	@Autowired
	private InstalmentPlanRepository instalmentPlanRepo;
	
	@Autowired
	private InstalmentTransactionRepository instalmentTransRepo;
	
	// Logger object to log messages
	Logger logger = LogManager.getLogger(CreditCardService.class);

	public List<CreditCard> getAllCreditCard() {
		return creditCardRepo.findAll();
	}

	public CreditCard findCreditCardById(long creditCardId) {
		CreditCard card = creditCardRepo.findById(creditCardId)				
				.orElseThrow(() -> {
					logger.error("Credit card with creditCardId " + creditCardId + "not found");
					return new CreditCardNotFoundException("Credit card not found");
					});
		
		logger.info("Found credit card with creditCardId: " + creditCardId);
		
		return card;
	}

	public CreditCard addCreditCard(CreditCard creditCard) {
		logger.info ("Saved new credit card: " + creditCard.getCardId());
		
		return creditCardRepo.save(creditCard);
	}

	public CreditCard updateCreditCardDetails(long creditCardId, Map<String, Object> updates) {
		CreditCard existingCard = creditCardRepo.findById(creditCardId)
				.orElseThrow(() -> {
					logger.error("Credit card with Id: " + creditCardId + " not found");
					return new CreditCardNotFoundException("Credit card not found");
				});

		if (updates.containsKey("creditLimit")) {
			existingCard.setCreditLimit((BigDecimal) updates.get("creditLimit"));
			logger.info("Updating credit limit of credit card id " + creditCardId);
		}

		if (updates.containsKey("creditBalance")) {
			existingCard.setCreditBalance(((BigDecimal) updates.get("creditBalance")));
			logger.info("Updating credit balance of credit card id " + creditCardId);
		}

		if (updates.containsKey("expiryDate")) {
			existingCard.setExpiryDate((Date) updates.get("expiryDate"));
			logger.info("Updating credit card expiry date of credit card id " + creditCardId);
		}

		if (updates.containsKey("cardVerificationValue")) {
			existingCard.setCardVerificationValue((Short) updates.get("cardVerificationValue"));
			logger.info("Updating credit card verification value of credit card id " + creditCardId);
		}

		if (updates.containsKey("displayName")) {
			existingCard.setDisplayName((String) updates.get("displayName"));
			logger.info("Updating display name of credit card id " + creditCardId);
		}
		
		if (updates.containsKey("pointsRedeemed")) {
			existingCard.setPointsRedeemed((int) updates.get("pointsRedeemed"));
			logger.info("Updating value of points redeemed of credit card id " + creditCardId);
		}
		
		if (updates.containsKey("pointsBalance")) {
			existingCard.setPointsBalance((int) updates.get("pointsBalance"));
			logger.info("Updating valueof points balance of credit card id " + creditCardId);
		}
		
		if (updates.containsKey("rebatesRedeemed")) {
			existingCard.setRebatesRedeemed((BigDecimal) updates.get("rebatesRedeemed"));
			logger.info("Updating value of rebates redeemed of credit card id " + creditCardId);
		}
		
		if (updates.containsKey("rebatesBalance")) {
			existingCard.setRebatesBalance((BigDecimal) updates.get("rebatesBalance"));
			logger.info("Updating value of rebates balance of credit card id " + creditCardId);
		}
		
		return creditCardRepo.save(existingCard);
	}

	public Map<String, Boolean> deleteCreditCardById(long creditCardId) {
		CreditCard existingCard = creditCardRepo.findById(creditCardId)				
				.orElseThrow(() -> {
					logger.error("Credit card with creditCardId " + creditCardId + "not found");
					return new CreditCardNotFoundException("Credit card not found");
					});
		
		creditCardRepo.delete(existingCard);
		logger.info("Credit card with Id " + creditCardId + " deleted");

		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);

		return response;
	}

	public List<CreditCard> findCreditCardByUser(long userId) {
		User existingUser = userRepo.findById(userId).orElseThrow(() -> {
			logger.error("User with userId " + userId + " not found");
			return new UserNotFoundException("User not found");
		});
		
		logger.info("User with userId " + userId + " found");
		return creditCardRepo.findByUser(existingUser);
	}

	public Transaction makePurchaseWithCreditCard(long creditCardId, BigDecimal amount, long merchantId) {
		
		CreditCard sourceAccount = creditCardRepo.findById(creditCardId)
				.orElseThrow(() -> {
							logger.error("Credit card with Id " + creditCardId + " not found");
							return new CreditCardNotFoundException("Source CC Account not found");
						});

		Merchant merchant = merchantRepo.findById(merchantId)
				.orElseThrow(() -> {
					logger.error("Merchant with Id " + merchantId + " not found");
					return new MerchantNotFoundException("Merchant not found");
				});
		
		String merchantCurrencyCode = merchant.getDefaultCurrencyCode();
		BigDecimal purchaseInSGD = BigDecimal.ZERO;
		
		// make a purchase
		Transaction purchaseTransaction = new Transaction(new Date(), purchaseInSGD, Type.Local, sourceAccount);
		purchaseTransaction.setTransactionType(TransactionType.Purchase);
		purchaseTransaction.setMerchant(merchant);
		
		//FOREIGN PURCHASES
		if (!merchantCurrencyCode.equals("SGD")) {
			
			FXRates fx = fxRepo.findById(merchantCurrencyCode)
					.orElseThrow(() -> {
						logger.error("FXRate " + merchantCurrencyCode + " not found");
						return new FXRateNotFoundException("Currency not found");
					});
			
			BigDecimal fxRate = fx.getRate();
			purchaseInSGD = amount.divide(fxRate, 2, RoundingMode.HALF_UP);
			logger.info("Successfully converted " + amount + merchantCurrencyCode + " to " 
						 + purchaseInSGD + " at rate " + fxRate);
			
			// Check for sufficient balance
			if (((sourceAccount.getCreditBalance()).add(purchaseInSGD)).compareTo(sourceAccount.getCreditLimit()) < 0) {
				purchaseTransaction.setType(Type.Foreign);
				purchaseTransaction.setAmount(purchaseInSGD);
				
				sourceAccount.setCreditBalance((sourceAccount.getCreditBalance()).add(purchaseInSGD));
				logger.info("Credit balance updated for credit card id" + creditCardId);
			}
			
			else {
				logger.error("Credit limit reach for credit card id" + creditCardId + ". Transaction aborted.");
				throw new CreditLimitReachedException("Credit Limit Reached! Transaction Aborted.");
			}	
			
		}
		
		//LOCAL PURCHASES
		if (merchantCurrencyCode.equals("SGD")) {
			purchaseInSGD = amount.setScale(2, RoundingMode.HALF_UP);
			
			// check for sufficient balance
			if ((sourceAccount.getCreditBalance().add(purchaseInSGD)).compareTo(sourceAccount.getCreditLimit()) < 0) {
				purchaseTransaction.setType(Type.Local);
				purchaseTransaction.setAmount(purchaseInSGD);
				
				sourceAccount.setCreditBalance((sourceAccount.getCreditBalance()).add(purchaseInSGD));
				logger.info("Credit balance updated for credit card id" + creditCardId);
			}
			else {
				logger.error("Credit limit reach for credit card id" + creditCardId + ". Transaction aborted.");
				throw new CreditLimitReachedException("Credit Limit Reached! Transaction Aborted.");
			}
			
			// Add rebates to credit card cashback balance
			// 1% for groceries(MCC 5411), 1.5% for dining (MC 5814)
			// Groceries
			if (merchant.getMerchantCategory().getMerchantCategoryCode() == 5411) {
				BigDecimal rebateRate = BigDecimal.valueOf(0.01);
				BigDecimal rebateAmount = purchaseInSGD.multiply(rebateRate).setScale(2, RoundingMode.HALF_UP);
							
				Rebate newRebate = new Rebate(rebateAmount, new Date(), sourceAccount);
				
				sourceAccount.setRebatesBalance((sourceAccount.getRebatesBalance()).add(rebateAmount));
				
				creditCardRepo.save(sourceAccount);
				rebateRepo.save(newRebate);
				logger.info("Rebate " + newRebate.getRebateId() + " calculated for credit card id " + creditCardId);
			}
			// Dining
			if (merchant.getMerchantCategory().getMerchantCategoryCode() == 5814) {
				BigDecimal rebateRate = BigDecimal.valueOf(0.015);
				BigDecimal rebateAmount = purchaseInSGD.multiply(rebateRate).setScale(2, RoundingMode.HALF_UP);
				
				sourceAccount.setRebatesBalance((sourceAccount.getRebatesBalance()).add(rebateAmount));
				
				creditCardRepo.save(sourceAccount);
				
				Rebate newRebate = new Rebate(rebateAmount, new Date(), sourceAccount);
				rebateRepo.save(newRebate);
				logger.info("Rebate " + newRebate.getRebateId() + " calculated for credit card id " + creditCardId);
			}
		}
		
		// Add points for purchases
		// 1 point per $1 spend
		BigDecimal points = purchaseInSGD.setScale(0, RoundingMode.DOWN);
			
		Point newPoint = new Point(points.intValue(), new Date(), sourceAccount);		
		sourceAccount.setPointsBalance(sourceAccount.getPointsBalance() + points.intValue());
		
		pointRepo.save(newPoint);
		creditCardRepo.save(sourceAccount);
		logger.info("Point id " + newPoint.getPointId() + " calculated for credit card id " + creditCardId);
		
		logger.info("Purchase completed for credit card id" + creditCardId + " with merchant id " + merchantId);
		return transactionRepo.save(purchaseTransaction);
	}

	public Transaction makeBillPayment(long billId, long bankAccountId, BigDecimal amount) {
		CreditCardBill existingCreditCardBill = ccBillRepo.findById(billId)
												 .orElseThrow(()-> {
													 logger.error("Credit card bill id " + billId + " not found");
													 return new CreditCardBillNotFoundException("Bill not found");
												 });
		
		BankAccount existingBankAccount = bankAccountRepo.findById(bankAccountId)
										  .orElseThrow(()-> {
											  logger.error("Bank Account id" + bankAccountId + " not found");
											  return new BankAccountNotFoundException("Invalid bank account");
										  });
		
		CreditCard creditCard = existingCreditCardBill.getCreditCard();
		
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			logger.error("Payment amount less than 0");
			throw new IllegalArgumentException("Amount must be greater than zero");
		}
		
		// if bill amount is more than min. payment amount, amount paid less than min.payment amount
		if ((existingCreditCardBill.getBillAmount().compareTo(creditCard.getMinimumPaymentAmount()) > 0)
				&& (amount.compareTo(creditCard.getMinimumPaymentAmount()) < 0 )){
			logger.error("Payment amount less than minimum payment amount for bank account id" + bankAccountId);
			throw new InsufficientPaymentException("Payment amount is less than minimum payment amount");
		}
		
		if (amount.compareTo(existingBankAccount.getBalance()) > 0) {
			logger.error("Insufficient balance in bank account id" + bankAccountId);
			throw new InsufficientBalanceException("Insufficient balance in bank account");
		}
		
		if (amount.compareTo(existingCreditCardBill.getBillAmount()) > 0) {
			logger.error("Payment amount greater than bill amount");
			throw new InvalidPaymentAmountException("Amount greater than bill amount");
		}
		
		// Deduct amount from bank account and credit balance
		existingBankAccount.setBalance((existingBankAccount.getBalance()).subtract(amount));
		logger.info("Balance updated for bank account" + bankAccountId);
		
		creditCard.setCreditBalance((creditCard.getCreditBalance()).subtract(amount));
		logger.info("Credit balance updated for credit card" + creditCard.getCardId());
		
		// Update credit card bill
		existingCreditCardBill.setIsPaid(true);
		existingCreditCardBill.setAmountPaid(existingCreditCardBill.getAmountPaid().add(amount));
		logger.info("Credit card bill id" + billId + " updated");
		
		// Set transaction
		Transaction billPayment = new Transaction(new Date(), amount.negate(), Type.Local, existingBankAccount);
		billPayment.setTransactionType(TransactionType.Credit_Card_Bill_Payment);
		billPayment.setSourceAccount(existingBankAccount);
		billPayment.setCreditCard(creditCard);
		logger.info("Transaction id" + billPayment.getTransactionId() + " created for bill payment");
		
		creditCardRepo.save(creditCard);
		bankAccountRepo.save(existingBankAccount);
		
		return transactionRepo.save(billPayment);
	}
	
	// Redeem amount, deduct from latest bill
	public Transaction redeemRebates(long creditCardId, BigDecimal redeemAmount) {
		CreditCard existingCard = creditCardRepo.findById(creditCardId)				
					.orElseThrow(() -> {
						logger.error("Credit card with creditCardId " + creditCardId + "not found");
						return new CreditCardNotFoundException("Credit card not found");
					});
		
		CreditCardBill latestBill = ccBillRepo.findTopByCreditCardOrderByBillDateDesc(existingCard)
				 		.orElseThrow(()-> {
				 			logger.error("No existing bill for credit card id " + creditCardId);
				 			return new CreditCardBillNotFoundException("Bill not found");
				 		});
		
		// Check if amount is negative or more than current balance
		if (redeemAmount.compareTo(BigDecimal.ZERO) <= 0) {
			logger.error("Redemption amount less than 0");
			throw new InvalidPaymentAmountException("Value must be greate than 0");
		}
		
		if (redeemAmount.compareTo(existingCard.getRebatesBalance()) > 0) {
			logger.error("Insufficient rebate balance");
			throw new InsufficientBalanceException("Insufficient balance");
		}
		
		// Deduct from latest bill
		latestBill.setBillAmount((latestBill.getBillAmount()).subtract(redeemAmount));
		
		// Deduct balance from cc and add to redeemed
		// round to 2 decimal places
		BigDecimal rebatesBalance = (existingCard.getRebatesBalance()).subtract(redeemAmount);
		rebatesBalance.setScale(2, RoundingMode.HALF_UP);
		existingCard.setRebatesBalance(rebatesBalance);
		
		BigDecimal rebatesRedeemed = (existingCard.getRebatesRedeemed().add(redeemAmount));
		rebatesRedeemed.setScale(2, RoundingMode.HALF_UP);
		existingCard.setRebatesRedeemed(rebatesRedeemed);
		
		Transaction redeemTransaction = new Transaction(new Date(), redeemAmount.negate(), Type.Local, TransactionType.Cashback,
														existingCard);
		Transaction savedTransaction=transactionRepo.save(redeemTransaction);
		Rebate redeemedRebate = new Rebate(redeemAmount.negate() , new Date(), existingCard, redeemTransaction);
		
		rebateRepo.save(redeemedRebate);
		
		logger.info("Rebates redeemed successfully for credit card id " + creditCardId);
		return savedTransaction;
	}

	public Point redeemPoints(long creditCardId, int points) {
		CreditCard existingCard = creditCardRepo.findById(creditCardId)				
				.orElseThrow(() -> {
					logger.error("Credit card with creditCardId " + creditCardId + "not found");
					return new CreditCardNotFoundException("Credit card not found");
					});
		
		// Check if point exceeds current balance
		if (existingCard.getPointsBalance() < points) {
			logger.error("Insufficient points available for credit card id " + creditCardId);
			throw new InsufficientBalanceException("Insufficient points available");
		}
		
		// Deduct points, add to redeemed
		existingCard.setPointsBalance(existingCard.getPointsBalance() - points);
		existingCard.setPointsRedeemed(existingCard.getPointsRedeemed() + points);
		creditCardRepo.save(existingCard);
		
		Point redeemedPoints = new Point(points*-1, new Date(), existingCard);
		pointRepo.save(redeemedPoints);
		
		logger.info("Points balance and redeemed updated for credit card id " + creditCardId);
		return redeemedPoints;
	}

	public InstalmentTransaction makePurchaseByInstalment(long creditCardId, BigDecimal amount, long merchantId,
												long instalmentPlanId) {
		CreditCard existingCard = creditCardRepo.findById(creditCardId)				
				.orElseThrow(() -> {
					logger.error("Credit card with creditCardId " + creditCardId + "not found");
					return new CreditCardNotFoundException("Credit card not found");
					});
		
		Merchant existingMerchant = merchantRepo.findById(merchantId)
									.orElseThrow(() -> {
											logger.error("Merchant with Id " + merchantId + " not found");
											return new MerchantNotFoundException("Merchant not found");
									});
		
		InstalmentPlan existingPlan = instalmentPlanRepo.findById(instalmentPlanId)
									  .orElseThrow(()-> {
										  	logger.error("Instalment plan with Id " + instalmentPlanId + "not found");
										  	return new InstalmentPlanNotFoundException("Instalment plan not found");
										  });
		
		// Calculate amount to pay per month
		// Divide by no. of instalment
		// Add interest, amount * interest/100
		BigDecimal amountPerMonth = amount.divide(BigDecimal.valueOf(existingPlan.getNoOfInstalments()), 2, RoundingMode.HALF_UP);
			
		BigDecimal interestPerMonth = amountPerMonth.multiply(existingPlan.getInterestRate().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
		
		amountPerMonth = amountPerMonth.add(interestPerMonth);
		logger.info("Amount payable per month: " + amountPerMonth);
		
		InstalmentTransaction newInstalment = new InstalmentTransaction(new Date(), amount, false, (short)(existingPlan.getNoOfInstalments() -1),
																		existingCard, existingMerchant, existingPlan);
		
		Type type = existingMerchant.getDefaultCurrencyCode().equals("SGD") ? Type.Local : Type.Foreign;
		
		Transaction firstInstalment = new Transaction(new Date(), amountPerMonth, type, TransactionType.Purchase_With_Instalment, 
														existingCard, existingMerchant);
		transactionRepo.save(firstInstalment);
		newInstalment.getTransactionList().add(firstInstalment);
		
		// Add to credit balance
		existingCard.setCreditBalance(existingCard.getCreditBalance().add(amountPerMonth));
		creditCardRepo.save(existingCard);
		
		logger.info("Instalment purchase completed successfully");
		return instalmentTransRepo.save(newInstalment);
	}
}

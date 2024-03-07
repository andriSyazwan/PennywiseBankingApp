package com.backend.pennywise;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.backend.pennywise.entities.BankAccount;
import com.backend.pennywise.entities.CreditCard;
import com.backend.pennywise.entities.CreditCardBill;
import com.backend.pennywise.entities.InstalmentPlan;
import com.backend.pennywise.entities.InstalmentTransaction;
import com.backend.pennywise.entities.Merchant;
import com.backend.pennywise.entities.MerchantCategory;
import com.backend.pennywise.entities.Point;
import com.backend.pennywise.entities.Rebate;
import com.backend.pennywise.entities.Transaction;
import com.backend.pennywise.entities.User;
import com.backend.pennywise.enums.AccountType;
import com.backend.pennywise.enums.TransactionType;
import com.backend.pennywise.enums.Type;
import com.backend.pennywise.repositories.BankAccountRepository;
import com.backend.pennywise.repositories.CreditCardBillRepository;
import com.backend.pennywise.repositories.CreditCardRepository;
import com.backend.pennywise.repositories.InstalmentPlanRepository;
import com.backend.pennywise.repositories.InstalmentTransactionRepository;
import com.backend.pennywise.repositories.MerchantCategoryRepository;
import com.backend.pennywise.repositories.MerchantRepository;
import com.backend.pennywise.repositories.PointRepository;
import com.backend.pennywise.repositories.RebateRepository;
import com.backend.pennywise.repositories.TransactionRepository;
import com.backend.pennywise.repositories.UserRepository;
//import com.backend.pennywise.services.CurrencyService;
import com.backend.pennywise.services.FXRatesService;

@Component
public class DataLoader implements ApplicationRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BankAccountRepository bankAccountRepository;

	@Autowired
	private MerchantRepository merchantRepository;

	@Autowired
	private MerchantCategoryRepository merchantCategoryRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private RebateRepository rebateRepository;

	@Autowired
	private PointRepository pointRepository;

	@Autowired
	private CreditCardRepository creditCardRepository;

	@Autowired
	private CreditCardBillRepository creditCardBillRepository;

	@Autowired
	private InstalmentPlanRepository instalmentPlanRepository;

	@Autowired
	private InstalmentTransactionRepository instalmentTransactionRepository;

	@Autowired
	private FXRatesService fxRatesService;

	private static final int CARD_NUMBER_LENGTH = 16;
	private static final int accountIdNumberLength = 10;
	public static final Set<Long> usedCardNumbers = new HashSet<>();
	public static final Set<Long> usedAccountNumbers = new HashSet<>(); // To track used card numbers

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// Load up all the tables with entries
		Random random = new Random();

		// User entries
		User user1 = new User("username1", "password1", "email1@gmail.com", "Giannis", "Antetokounmpo", "address1");
		userRepository.save(user1);

		User user2 = new User("username2", "password2", "email2@gmail.com", "Kar-Wai", "Wong", "address2");
		userRepository.save(user2);

		// BankAccount entries
		List<BankAccount> bankAccounts = new ArrayList<>();

		BankAccount bankAccount1 = new BankAccount(BigDecimal.valueOf(1000.0), generateUniqueAccountId(),
				AccountType.Checking_Account, user1);
		bankAccounts.add(bankAccount1);

		BankAccount bankAccount2 = new BankAccount(BigDecimal.valueOf(5000.0), generateUniqueAccountId(),
				AccountType.Savings_Account, user2);
		bankAccounts.add(bankAccount2);

		BankAccount bankAccount3 = new BankAccount(BigDecimal.valueOf(6000.0), generateUniqueAccountId(),
				AccountType.Checking_Account, user1);
		bankAccounts.add(bankAccount3);

		BankAccount bankAccount4 = new BankAccount(BigDecimal.valueOf(4000.0), generateUniqueAccountId(),
				AccountType.Savings_Account, user2);
		bankAccounts.add(bankAccount4);

		BankAccount bankAccount5 = new BankAccount(BigDecimal.valueOf(8000.0), generateUniqueAccountId(),
				AccountType.Savings_Account, user1);
		bankAccounts.add(bankAccount5);

		bankAccountRepository.saveAll(bankAccounts);

		// Merchant entries
		List<MerchantCategory> merchantCategories = new ArrayList<>();

		MerchantCategory groceryStoresSupermarkets = new MerchantCategory(5411, "Grocery Stores, Supermarkets");
		merchantCategories.add(groceryStoresSupermarkets);

		MerchantCategory drugStoresPharmacies = new MerchantCategory(5912, "Drug Stores and Pharmacies");
		merchantCategories.add(drugStoresPharmacies);

		MerchantCategory electronicsStores = new MerchantCategory(5732, "Electronics Stores");
		merchantCategories.add(electronicsStores);

		MerchantCategory dining = new MerchantCategory(5814, "Dining");
		merchantCategories.add(dining);

		MerchantCategory departmentStores = new MerchantCategory(5311, "Department Stores");
		merchantCategories.add(departmentStores);

		merchantCategoryRepository.saveAll(merchantCategories);

		// Merchant entries
		List<Merchant> merchants = new ArrayList<>();

		String[] retailers = { "Shopee", "Lazada", "NTUC FairPrice", "Giant", "Cold Storage", "Guardian", "Watsons",
				"Courts", "Challenger", "Starbucks", "Sainsbury's", "Daiso", "Bunnpris", "Walmart" };

		Map<String, String> retailerToCurrencyCodeMap = new HashMap<>();
		retailerToCurrencyCodeMap.put("Shopee", "SGD");
		retailerToCurrencyCodeMap.put("Lazada", "SGD");
		retailerToCurrencyCodeMap.put("NTUC FairPrice", "SGD");
		retailerToCurrencyCodeMap.put("Giant", "SGD");
		retailerToCurrencyCodeMap.put("Cold Storage", "SGD");
		retailerToCurrencyCodeMap.put("Guardian", "SGD");
		retailerToCurrencyCodeMap.put("Watsons", "SGD");
		retailerToCurrencyCodeMap.put("Courts", "SGD");
		retailerToCurrencyCodeMap.put("Challenger", "SGD");
		retailerToCurrencyCodeMap.put("Starbucks", "SGD");
		retailerToCurrencyCodeMap.put("Sainsbury's", "GBP");
		retailerToCurrencyCodeMap.put("Daiso", "JPY");
		retailerToCurrencyCodeMap.put("Bunnpris", "NOK");
		retailerToCurrencyCodeMap.put("Walmart", "USD");

		Map<String, MerchantCategory> retailerToCategoryMap = new HashMap<>();
		retailerToCategoryMap.put("Shopee", departmentStores);
		retailerToCategoryMap.put("Lazada", departmentStores);
		retailerToCategoryMap.put("NTUC FairPrice", groceryStoresSupermarkets);
		retailerToCategoryMap.put("Giant", groceryStoresSupermarkets);
		retailerToCategoryMap.put("Cold Storage", groceryStoresSupermarkets);
		retailerToCategoryMap.put("Guardian", drugStoresPharmacies);
		retailerToCategoryMap.put("Watsons", drugStoresPharmacies);
		retailerToCategoryMap.put("Courts", electronicsStores);
		retailerToCategoryMap.put("Challenger", electronicsStores);
		retailerToCategoryMap.put("Starbucks", dining);
		retailerToCategoryMap.put("Sainsbury's", groceryStoresSupermarkets);
		retailerToCategoryMap.put("Daiso", groceryStoresSupermarkets);
		retailerToCategoryMap.put("Bunnpris", groceryStoresSupermarkets);
		retailerToCategoryMap.put("Walmart", departmentStores);

		for (int i = 0; i < retailers.length; i++) {
			String randomRetailer = retailers[i];
			String defaultCurrencyCode = retailerToCurrencyCodeMap.get(randomRetailer);
			MerchantCategory merchantCategory = retailerToCategoryMap.get(randomRetailer);

			Merchant merchant = new Merchant(randomRetailer, defaultCurrencyCode, merchantCategory);
			merchants.add(merchant);
		}
		merchantRepository.saveAll(merchants);

		// CreditCard entries
		List<CreditCard> creditCards = new ArrayList<>();

		String[] displayNames = { "Giannis Antetokounmpo", "Giannis Antetokounmpo", "Wong Kar-Wai", "Wong Kar-Wai" };

		for (int i = 0; i < displayNames.length; i++) {
			long generatedCardNumber = generateUniqueRandomCardNumber(); // Generate a unique 16-digit card number

			// Other attributes
			short cvv = (short) (100 + random.nextInt(900));
			BigDecimal creditLimit = BigDecimal.valueOf(((random.nextInt(10) + 1) * 1000) + 1000);
			BigDecimal creditBalance = BigDecimal.valueOf(random.nextDouble(2000));

			creditBalance.setScale(2, RoundingMode.HALF_UP);

			User selectedUser = i >= 2 ? user1 : user2;

			// Create CreditCard instances and save them
			CreditCard creditCard = new CreditCard(generatedCardNumber, creditLimit, creditBalance,
					randomDateForYearRange(2026, 2028), cvv, displayNames[i], selectedUser);

			creditCards.add(creditCard);
		}
		creditCardRepository.saveAll(creditCards);

		// InstalmentPlan entries
		InstalmentPlan plan1 = new InstalmentPlan((short) 3, BigDecimal.valueOf(11.0));
		InstalmentPlan plan2 = new InstalmentPlan((short) 6, BigDecimal.valueOf(20.0));
		InstalmentPlan plan3 = new InstalmentPlan((short) 9, BigDecimal.valueOf(27.0));

		instalmentPlanRepository.save(plan1);
		instalmentPlanRepository.save(plan2);
		instalmentPlanRepository.save(plan3);
		// Transaction entries
		
		//List<Transaction> transactionsForCards = transactionGeneratorForCards(10, creditCards, merchants);
		List<Transaction> transactionsForAccounts = transactionGeneratorForAccounts(5, bankAccounts);
		//transactionRepository.saveAll(transactionsForCards);
		transactionRepository.saveAll(transactionsForAccounts);
		
		List<CreditCardBill> octBills = new ArrayList<>();		
		for (int i = 0; i < 4; i++) {
			CreditCardBill octBill = new CreditCardBill();
			octBill.setCreditCard(creditCards.get(i));
			octBill.setBillDate(Date.valueOf(LocalDate.of(2023, 10, 1)));
			octBill.setDueDate(Date.valueOf(LocalDate.of(2023, 10, 21)));
			octBill.setBillingPeriodStart(Date.valueOf(LocalDate.of(2023, 9, 1)));
			octBill.setBillingPeriodEnd(Date.valueOf(LocalDate.of(2023, 9, 30)));
			octBill.setBillAmount(BigDecimal.ZERO);
			creditCardBillRepository.save(octBill);
			octBills.add(octBill);
		}
		
		List<CreditCardBill> novBills = new ArrayList<>();		
		for (int i = 0; i < 4; i++) {
			CreditCardBill bill = new CreditCardBill();
			bill.setCreditCard(creditCards.get(i));
			bill.setBillDate(Date.valueOf(LocalDate.of(2023, 11, 1)));
			bill.setDueDate(Date.valueOf(LocalDate.of(2023, 11, 21)));
			bill.setBillingPeriodStart(Date.valueOf(LocalDate.of(2023, 10, 1)));
			bill.setBillingPeriodEnd(Date.valueOf(LocalDate.of(2023, 10, 31)));
			bill.setBillAmount(BigDecimal.ZERO);
			creditCardBillRepository.save(bill);
			novBills.add(bill);
		}
		
		List<CreditCardBill> decBills = new ArrayList<>();		
		for (int i = 0; i < 4; i++) {
			CreditCardBill bill = new CreditCardBill();
			bill.setCreditCard(creditCards.get(i));
			bill.setBillDate(Date.valueOf(LocalDate.of(2023, 12, 1)));
			bill.setDueDate(Date.valueOf(LocalDate.of(2023, 12, 21)));
			bill.setBillingPeriodStart(Date.valueOf(LocalDate.of(2023, 11, 1)));
			bill.setBillingPeriodEnd(Date.valueOf(LocalDate.of(2023, 11, 30)));
			bill.setBillAmount(BigDecimal.ZERO);
			creditCardBillRepository.save(bill);
			decBills.add(bill);
		}	
		
		// Transaction, save transaction, set to monthly billing
		// rebates for merchants 2, 3, 4 0.01%, merchant 9 0.014%
		// points for each transaction
		// Local purchase
		// 1
		Transaction transaction1 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 9, 2)), 
				BigDecimal.valueOf(19.90), Type.Local, TransactionType.Purchase, creditCards.get(0), merchants.get(0));
		transactionRepository.save(transaction1);
		octBills.get(0).setBillAmount(octBills.get(0).getBillAmount().add(BigDecimal.valueOf(19.90)));
		
		int points = BigDecimal.valueOf(19.90).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p1 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 9, 1)), creditCards.get(0));
		creditCards.get(0).setPointsBalance(creditCards.get(0).getPointsBalance() + points);
		
		pointRepository.save(p1);
		creditCardRepository.save(creditCards.get(0));
		
		// 2
		Transaction transaction2 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 9, 2)), 
				BigDecimal.valueOf(1.90), Type.Local, TransactionType.Purchase, creditCards.get(1), merchants.get(1));
		transactionRepository.save(transaction2);
		octBills.get(1).setBillAmount(octBills.get(1).getBillAmount().add(BigDecimal.valueOf(1.90)));
		
		points = BigDecimal.valueOf(1.90).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p2 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 9, 2)), creditCards.get(1));
		creditCards.get(1).setPointsBalance(creditCards.get(1).getPointsBalance() + points);
		
		pointRepository.save(p2);
		creditCardRepository.save(creditCards.get(1));
		
		// 3
		Transaction transaction3 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 9, 3)), 
				BigDecimal.valueOf(20.95), Type.Local, TransactionType.Purchase, creditCards.get(2), merchants.get(2));
		transactionRepository.save(transaction3);
		octBills.get(2).setBillAmount(octBills.get(2).getBillAmount().add(BigDecimal.valueOf(20.95)));
		points = BigDecimal.valueOf(20.95).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p3 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 9, 3)), creditCards.get(2));
		creditCards.get(2).setPointsBalance(creditCards.get(2).getPointsBalance() + points);
		
		pointRepository.save(p3);
		creditCardRepository.save(creditCards.get(2));
		
		// 4
		Transaction transaction4 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 9, 23)), 
				BigDecimal.valueOf(199.13), Type.Local, TransactionType.Purchase, creditCards.get(3), merchants.get(5));
		transactionRepository.save(transaction4);
		
		octBills.get(3).setBillAmount(octBills.get(3).getBillAmount().add(BigDecimal.valueOf(199.13)));
		
		BigDecimal rebateAmount = BigDecimal.valueOf(199.13).multiply(BigDecimal.valueOf(0.01).setScale(2, RoundingMode.HALF_UP));
		Rebate r1 = new Rebate(rebateAmount, java.sql.Date.valueOf(LocalDate.of(2023, 9, 23)), creditCards.get(3));
		
		creditCards.get(3).setRebatesBalance((creditCards.get(3).getRebatesBalance()).add(rebateAmount));
		
		rebateRepository.save(r1);
		creditCardRepository.save(creditCards.get(3));
		
		points = BigDecimal.valueOf(199.13).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p4 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 9, 23)), creditCards.get(3));
		creditCards.get(3).setPointsBalance(creditCards.get(3).getPointsBalance() + points);
		
		pointRepository.save(p4);
		creditCardRepository.save(creditCards.get(3));
		
		// 5
		Transaction transaction5 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 9, 30)), 
				BigDecimal.valueOf(19.90), Type.Local, TransactionType.Purchase, creditCards.get(1), merchants.get(5));
		transactionRepository.save(transaction5);
		
		octBills.get(1).setBillAmount(octBills.get(1).getBillAmount().add(BigDecimal.valueOf(19.90)));
		
		rebateAmount = BigDecimal.valueOf(19.90).multiply(BigDecimal.valueOf(0.01).setScale(2, RoundingMode.HALF_UP));
		Rebate r2 = new Rebate(rebateAmount, java.sql.Date.valueOf(LocalDate.of(2023, 9, 30)), creditCards.get(1));
		
		creditCards.get(1).setRebatesBalance((creditCards.get(1).getRebatesBalance()).add(rebateAmount));
		
		rebateRepository.save(r2);
		creditCardRepository.save(creditCards.get(1));
		
		points = BigDecimal.valueOf(19.90).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p5 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 9, 30)), creditCards.get(1));
		creditCards.get(1).setPointsBalance(creditCards.get(0).getPointsBalance() + points);
		
		pointRepository.save(p5);
		creditCardRepository.save(creditCards.get(1));
		
		// 6
		Transaction transaction6 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 9, 12)), 
				BigDecimal.valueOf(9.95), Type.Local, TransactionType.Purchase, creditCards.get(2), merchants.get(9));
		transactionRepository.save(transaction6);
		octBills.get(2).setBillAmount(octBills.get(2).getBillAmount().add(BigDecimal.valueOf(9.95)));
		
		rebateAmount = BigDecimal.valueOf(9.95).multiply(BigDecimal.valueOf(0.015).setScale(2, RoundingMode.HALF_UP));
		
		Rebate r5 = new Rebate(rebateAmount, java.sql.Date.valueOf(LocalDate.of(2023, 9, 12)), creditCards.get(2));
		creditCards.get(2).setRebatesBalance((creditCards.get(2).getRebatesBalance()).add(rebateAmount));
		
		rebateRepository.save(r5);
		creditCardRepository.save(creditCards.get(2));
		
		points = BigDecimal.valueOf(9.95).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p6 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 9, 12)), creditCards.get(2));
		creditCards.get(2).setPointsBalance(creditCards.get(2).getPointsBalance() + points);
		
		pointRepository.save(p6);
		creditCardRepository.save(creditCards.get(2));
		
		// 7
		Transaction transaction7 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 10, 11)), 
				BigDecimal.valueOf(8.90), Type.Local, TransactionType.Purchase, creditCards.get(3), merchants.get(8));
		transactionRepository.save(transaction7);
		novBills.get(3).setBillAmount(novBills.get(3).getBillAmount().add(BigDecimal.valueOf(8.90)));
		
		points = BigDecimal.valueOf(19.90).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p7 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 10, 11)), creditCards.get(3));
		creditCards.get(3).setPointsBalance(creditCards.get(3).getPointsBalance() + points);
		
		pointRepository.save(p7);
		creditCardRepository.save(creditCards.get(3));
		
		// 8
		Transaction transaction8 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 10, 12)), 
				BigDecimal.valueOf(21.50), Type.Local, TransactionType.Purchase, creditCards.get(0), merchants.get(9));
		transactionRepository.save(transaction8);
		novBills.get(0).setBillAmount(novBills.get(0).getBillAmount().add(BigDecimal.valueOf(21.50)));
		
		rebateAmount = BigDecimal.valueOf(21.50).multiply(BigDecimal.valueOf(0.015).setScale(2, RoundingMode.HALF_UP));
		
		Rebate r6 = new Rebate(rebateAmount, java.sql.Date.valueOf(LocalDate.of(2023, 10, 12)), creditCards.get(0));
		creditCards.get(0).setRebatesBalance((creditCards.get(0).getRebatesBalance()).add(rebateAmount));
		
		rebateRepository.save(r6);
		creditCardRepository.save(creditCards.get(0));
		
		points = BigDecimal.valueOf(21.50).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p8 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 10, 12)), creditCards.get(0));
		creditCards.get(0).setPointsBalance(creditCards.get(0).getPointsBalance() + points);
		
		pointRepository.save(p8);
		creditCardRepository.save(creditCards.get(0));
		
		// 9
		Transaction transaction9 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 10, 20)), 
				BigDecimal.valueOf(200.93), Type.Local, TransactionType.Purchase, creditCards.get(2), merchants.get(6));
		transactionRepository.save(transaction9);
		novBills.get(2).setBillAmount(novBills.get(2).getBillAmount().add(BigDecimal.valueOf(200.93)));
		// hardcode to add in late fees/balance brought forward/interest for demo
		novBills.get(2).setBillAmount(novBills.get(2).getBillAmount().add(BigDecimal.valueOf(239.55)));
		
		points = BigDecimal.valueOf(200.93).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p9 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 10, 20)), creditCards.get(2));
		creditCards.get(2).setPointsBalance(creditCards.get(2).getPointsBalance() + points);
		
		pointRepository.save(p9);
		creditCardRepository.save(creditCards.get(2));
		
		// 10
		Transaction transaction10 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 10, 30)), 
				BigDecimal.valueOf(56.90), Type.Local, TransactionType.Purchase, creditCards.get(1), merchants.get(0));
		transactionRepository.save(transaction10);
		novBills.get(1).setBillAmount(novBills.get(1).getBillAmount().add(BigDecimal.valueOf(56.90)));
		
		points = BigDecimal.valueOf(56.90).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p10 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 10, 30)), creditCards.get(1));
		creditCards.get(1).setPointsBalance(creditCards.get(1).getPointsBalance() + points);
		
		pointRepository.save(p10);
		creditCardRepository.save(creditCards.get(1));
		
		// 11
		Transaction transaction11 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 11, 11)), 
				BigDecimal.valueOf(199.10), Type.Local, TransactionType.Purchase, creditCards.get(0), merchants.get(2));
		transactionRepository.save(transaction11);
		decBills.get(0).setBillAmount(decBills.get(0).getBillAmount().add(BigDecimal.valueOf(199.10)));
		
		points = BigDecimal.valueOf(199.10).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p11 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 11, 11)), creditCards.get(0));
		creditCards.get(0).setPointsBalance(creditCards.get(0).getPointsBalance() + points);
		
		pointRepository.save(p11);
		creditCardRepository.save(creditCards.get(0));
		
		// 12
		Transaction transaction12 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 11, 16)), 
				BigDecimal.valueOf(50.00), Type.Local, TransactionType.Purchase, creditCards.get(2), merchants.get(3));
		transactionRepository.save(transaction12);
		decBills.get(2).setBillAmount(decBills.get(2).getBillAmount().add(BigDecimal.valueOf(50.00)));
		
		//HardCode to bring forward CreditCard fees/balance/interest for demo
		decBills.get(2).setBillAmount(decBills.get(2).getBillAmount().add(BigDecimal.valueOf(1098.61)));
		
		rebateAmount = BigDecimal.valueOf(50.00).multiply(BigDecimal.valueOf(0.01).setScale(2, RoundingMode.HALF_UP));
		
		Rebate r3 = new Rebate(rebateAmount, java.sql.Date.valueOf(LocalDate.of(2023, 11, 16)), creditCards.get(2));
		creditCards.get(2).setRebatesBalance((creditCards.get(2).getRebatesBalance()).add(rebateAmount));
		rebateRepository.save(r3);
		creditCardRepository.save(creditCards.get(2));
		
		points = BigDecimal.valueOf(50.00).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p12 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 9, 1)), creditCards.get(2));
		creditCards.get(2).setPointsBalance(creditCards.get(2).getPointsBalance() + points);
		
		pointRepository.save(p12);
		creditCardRepository.save(creditCards.get(2));
		
		// 13
		Transaction transaction13 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 11, 20)), 
				BigDecimal.valueOf(19.90), Type.Local, TransactionType.Purchase, creditCards.get(1), merchants.get(8));
		transactionRepository.save(transaction13);
		decBills.get(1).setBillAmount(decBills.get(1).getBillAmount().add(BigDecimal.valueOf(19.90)));
		
		points = BigDecimal.valueOf(19.90).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p13 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 11, 20)), creditCards.get(1));
		creditCards.get(1).setPointsBalance(creditCards.get(1).getPointsBalance() + points);
		
		pointRepository.save(p13);
		creditCardRepository.save(creditCards.get(1));
		
		// 14
		Transaction transaction14 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 11, 21)), 
				BigDecimal.valueOf(19.90), Type.Local, TransactionType.Purchase, creditCards.get(3), merchants.get(7));
		transactionRepository.save(transaction14);
		decBills.get(3).setBillAmount(decBills.get(3).getBillAmount().add(BigDecimal.valueOf(19.90)));
		
		points = BigDecimal.valueOf(19.90).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p14 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 10, 21)), creditCards.get(3));
		creditCards.get(3).setPointsBalance(creditCards.get(3).getPointsBalance() + points);
		
		pointRepository.save(p14);
		creditCardRepository.save(creditCards.get(3));
		
		// 15
		Transaction transaction15 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 12, 11)), 
				BigDecimal.valueOf(14.90), Type.Local, TransactionType.Purchase, creditCards.get(0), merchants.get(9));
		transactionRepository.save(transaction15);
		decBills.get(0).setBillAmount(decBills.get(0).getBillAmount().add(BigDecimal.valueOf(14.90)));
		
		rebateAmount = BigDecimal.valueOf(14.90).multiply(BigDecimal.valueOf(0.015).setScale(2, RoundingMode.HALF_UP));
		
		Rebate r4 = new Rebate(rebateAmount, java.sql.Date.valueOf(LocalDate.of(2023, 12, 11)), creditCards.get(0));
		creditCards.get(0).setRebatesBalance((creditCards.get(0).getRebatesBalance()).add(rebateAmount));
		
		rebateRepository.save(r4);
		creditCardRepository.save(creditCards.get(0));
		
		points = BigDecimal.valueOf(14.90).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p15 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 12, 11)), creditCards.get(0));
		creditCards.get(0).setPointsBalance(creditCards.get(0).getPointsBalance() + points);
		
		pointRepository.save(p15);
		creditCardRepository.save(creditCards.get(0));
		
		// Foreign transactions
		// 17
		Transaction transaction17 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 9, 11)), 
				BigDecimal.valueOf(133.33), Type.Foreign, TransactionType.Purchase, creditCards.get(0), merchants.get(10));
		transactionRepository.save(transaction17);
		octBills.get(0).setBillAmount(octBills.get(0).getBillAmount().add(BigDecimal.valueOf(133.33)));
		
		points = BigDecimal.valueOf(133.33).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p17 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 9, 11)), creditCards.get(0));
		creditCards.get(0).setPointsBalance(creditCards.get(0).getPointsBalance() + points);
		
		pointRepository.save(p17);
		creditCardRepository.save(creditCards.get(0));
		
		// 18
		Transaction transaction18 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 9, 4)), 
				BigDecimal.valueOf(80.00), Type.Foreign, TransactionType.Purchase, creditCards.get(1), merchants.get(11));
		transactionRepository.save(transaction18);
		octBills.get(1).setBillAmount(octBills.get(1).getBillAmount().add(BigDecimal.valueOf(80.00)));
		
		points = BigDecimal.valueOf(80.00).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p18 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 9, 4)), creditCards.get(1));
		creditCards.get(1).setPointsBalance(creditCards.get(1).getPointsBalance() + points);
		
		pointRepository.save(p18);
		creditCardRepository.save(creditCards.get(1));
		
		
		// 21
		Transaction transaction21 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 10, 2)), 
				BigDecimal.valueOf(501.11), Type.Foreign, TransactionType.Purchase, creditCards.get(2), merchants.get(13));
		transactionRepository.save(transaction21);
		novBills.get(2).setBillAmount(novBills.get(2).getBillAmount().add(BigDecimal.valueOf(501.11)));
		
		points = BigDecimal.valueOf(500.11).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p21 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 10, 1)), creditCards.get(2));
		creditCards.get(2).setPointsBalance(creditCards.get(2).getPointsBalance() + points);
		
		pointRepository.save(p21);
		creditCardRepository.save(creditCards.get(2));
		
		// 22
		Transaction transaction22 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 11, 11)), 
				BigDecimal.valueOf(40.33), Type.Foreign, TransactionType.Purchase, creditCards.get(1), merchants.get(12));
		transactionRepository.save(transaction22);
		novBills.get(1).setBillAmount(novBills.get(1).getBillAmount().add(BigDecimal.valueOf(40.33)));
		
		points = BigDecimal.valueOf(40.33).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p22 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 11, 1)), creditCards.get(1));
		creditCards.get(1).setPointsBalance(creditCards.get(1).getPointsBalance() + points);
		
		pointRepository.save(p22);
		creditCardRepository.save(creditCards.get(1));
		
		// 23
		Transaction transaction23 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 12, 10)), 
				BigDecimal.valueOf(100.41), Type.Foreign, TransactionType.Purchase, creditCards.get(3), merchants.get(13));
		transactionRepository.save(transaction23);
		decBills.get(3).setBillAmount(decBills.get(3).getBillAmount().add(BigDecimal.valueOf(100.41)));
		
		points = BigDecimal.valueOf(100.41).setScale(0, RoundingMode.DOWN).intValue();
		
		Point p23 = new Point(points, java.sql.Date.valueOf(LocalDate.of(2023, 9, 1)), creditCards.get(3));
		creditCards.get(3).setPointsBalance(creditCards.get(3).getPointsBalance() + points);
		
		pointRepository.save(p23);
		creditCardRepository.save(creditCards.get(3));
		
		// Instalment purchase
		// InstalmentTransaction1. Plan1, 3 months. Sept, Oct, Nov
		InstalmentTransaction instalmentTransaction1 = new InstalmentTransaction (java.sql.Date.valueOf(LocalDate.of(2023, 9, 2)),
				BigDecimal.valueOf(800.00), false, (short)3, creditCards.get(0), merchants.get(0), plan1);
		instalmentTransactionRepository.save(instalmentTransaction1);
		
		Transaction instalment1 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 9, 2)), 
				instalmentTransaction1.getMonthlyAmount(), Type.Local, TransactionType.Purchase_With_Instalment, creditCards.get(0), merchants.get(0));
		transactionRepository.save(instalment1);
		octBills.get(0).setBillAmount(octBills.get(0).getBillAmount().add(instalmentTransaction1.getMonthlyAmount()));
		
		// Set date to 1st day of month, time to 12pm
		LocalDateTime dateTime = LocalDateTime.of(2023, 10, 1, 12, 0);
		Timestamp timestamp = Timestamp.valueOf(dateTime);
		Transaction instalment2 = new Transaction(new Date(timestamp.getTime()), 
				instalmentTransaction1.getMonthlyAmount(), Type.Local, TransactionType.Purchase_With_Instalment, creditCards.get(0), merchants.get(0));
		transactionRepository.save(instalment2);
		novBills.get(0).setBillAmount(novBills.get(0).getBillAmount().add(instalmentTransaction1.getMonthlyAmount()));
		
		// Set date to 1st day of month, time to 12pm
		dateTime = LocalDateTime.of(2023, 11, 1, 12, 0);
		timestamp = Timestamp.valueOf(dateTime);
		Transaction instalment3 = new Transaction(new Date(timestamp.getTime()), 
				instalmentTransaction1.getMonthlyAmount(), Type.Local, TransactionType.Purchase_With_Instalment, creditCards.get(0), merchants.get(0));
		transactionRepository.save(instalment3);
		decBills.get(0).setBillAmount(decBills.get(0).getBillAmount().add(instalmentTransaction1.getMonthlyAmount()));
		
		instalmentTransaction1.setProgressTracker((short)0);
		instalmentTransaction1.setIsPaid(true);
		instalmentTransactionRepository.save(instalmentTransaction1);
		
		// InstalmentTransaction2. Plan2, 6 months. Sept, Oct, Nov, Dec
		InstalmentTransaction instalmentTransaction2 = new InstalmentTransaction (java.sql.Date.valueOf(LocalDate.of(2023, 9, 10)),
				BigDecimal.valueOf(3599.00), false, (short)2, creditCards.get(1), merchants.get(7), plan2);
		instalmentTransactionRepository.save(instalmentTransaction2);
	
		Transaction instalment4 = new Transaction(java.sql.Date.valueOf(LocalDate.of(2023, 9, 10)), 
				instalmentTransaction2.getMonthlyAmount(), Type.Local, TransactionType.Purchase_With_Instalment, creditCards.get(1), merchants.get(7));
		transactionRepository.save(instalment4);
		octBills.get(1).setBillAmount(octBills.get(1).getBillAmount().add(instalmentTransaction2.getMonthlyAmount()));
		
		// Set date to 1st day of month, time to 12pm
		dateTime = LocalDateTime.of(2023, 10, 1, 12, 0);
		timestamp = Timestamp.valueOf(dateTime);
		Transaction instalment5 = new Transaction(new Date(timestamp.getTime()), 
				instalmentTransaction2.getMonthlyAmount(), Type.Local, TransactionType.Purchase_With_Instalment, creditCards.get(1), merchants.get(7));
		transactionRepository.save(instalment5);
		novBills.get(1).setBillAmount(novBills.get(1).getBillAmount().add(instalmentTransaction2.getMonthlyAmount()));
		
		// Set date to 1st day of month, time to 12pm
		dateTime = LocalDateTime.of(2023, 11, 1, 12, 0);
		timestamp = Timestamp.valueOf(dateTime);
		Transaction instalment6 = new Transaction(new Date(timestamp.getTime()), 
				instalmentTransaction2.getMonthlyAmount(), Type.Local, TransactionType.Purchase_With_Instalment, creditCards.get(1), merchants.get(7));
		transactionRepository.save(instalment6);
		decBills.get(1).setBillAmount(decBills.get(1).getBillAmount().add(instalmentTransaction2.getMonthlyAmount()));
		
		// Set date to 1st day of month, time to 12pm
		dateTime = LocalDateTime.of(2023, 12, 1, 12, 0);
		timestamp = Timestamp.valueOf(dateTime);
		Transaction instalment7 = new Transaction(new Date(timestamp.getTime()), 
				instalmentTransaction2.getMonthlyAmount(), Type.Local, TransactionType.Purchase_With_Instalment, creditCards.get(1), merchants.get(7));
		transactionRepository.save(instalment7);
		
		instalmentTransaction2.setProgressTracker((short)2);
		instalmentTransactionRepository.save(instalmentTransaction2);
		
		// InstalmentTransaction3. Plan 3, 9 months. Nov, Dec
		InstalmentTransaction instalmentTransaction3 = new InstalmentTransaction (java.sql.Date.valueOf(LocalDate.of(2023, 11, 10)),
				BigDecimal.valueOf(5849.00), false, (short)2, creditCards.get(1), merchants.get(8), plan3);
		instalmentTransactionRepository.save(instalmentTransaction1);
		
		Transaction instalment8 = new Transaction(Date.valueOf(LocalDate.of(2023, 11, 10)), 
				instalmentTransaction3.getMonthlyAmount(), Type.Local, TransactionType.Purchase_With_Instalment, creditCards.get(1), merchants.get(8));
		transactionRepository.save(instalment8);
		decBills.get(1).setBillAmount(decBills.get(1).getBillAmount().add(instalmentTransaction3.getMonthlyAmount()));
		
		// Set date to 1st day of month, time to 12pm
		dateTime = LocalDateTime.of(2023, 12, 1, 12, 0);
		timestamp = Timestamp.valueOf(dateTime);
		Transaction instalment9 = new Transaction(new Date(timestamp.getTime()), 
				instalmentTransaction3.getMonthlyAmount(), Type.Local, TransactionType.Purchase_With_Instalment, creditCards.get(1), merchants.get(8));
		transactionRepository.save(instalment9);
		
		instalmentTransaction3.setProgressTracker((short)7);
		instalmentTransactionRepository.save(instalmentTransaction3);
		
		for (int i = 0; i < 4; i++) {
			octBills.get(i).setIsPaid(true);
			novBills.get(i).setIsPaid(true);
		}
		
		creditCardBillRepository.saveAll(octBills);
		creditCardBillRepository.saveAll(novBills);
		creditCardBillRepository.saveAll(decBills);

		// 21 Oct
		for (int i = 0; i < 4; i++) {
			LocalDate chargesDate = LocalDate.of(2023, 10, 21);
			Date date = Date.valueOf(chargesDate);
			Transaction lateFee = new Transaction(date, BigDecimal.valueOf(200), Type.Local,
					TransactionType.Credit_Card_Late_Fee, creditCards.get(i));
			
			Transaction outstandingAmount = new Transaction(date, octBills.get(i).getBillAmount(), Type.Local,
					TransactionType.Credit_Card_Balance_Brought_Forward, creditCards.get(i));
			
			BigDecimal interestRate = BigDecimal.valueOf(0.28);
			BigDecimal interestAmount = octBills.get(i).getBillAmount().multiply(interestRate).setScale(2, RoundingMode.HALF_UP);
			Transaction interest = new Transaction(date, interestAmount, Type.Local,
					TransactionType.Credit_Card_Interest, creditCards.get(i));
			
			creditCards.get(i).setCreditBalance(creditCards.get(i).getCreditBalance().add(interestAmount));
			creditCards.get(i).setCreditBalance(creditCards.get(i).getCreditBalance().add(BigDecimal.valueOf(200)));
			creditCards.get(i).setCreditBalance(creditCards.get(i).getCreditBalance().add(octBills.get(i).getBillAmount()));
			
			creditCardRepository.save(creditCards.get(i));
			transactionRepository.save(lateFee);
			transactionRepository.save(outstandingAmount);
			transactionRepository.save(interest);
		}
		
		// 21 Nov
		for (int i = 0; i < 4; i++) {
			LocalDate chargesDate = LocalDate.of(2023, 11, 21);
			Date date = Date.valueOf(chargesDate);
			Transaction lateFee = new Transaction(date, BigDecimal.valueOf(200), Type.Local,
					TransactionType.Credit_Card_Late_Fee, creditCards.get(i));
			
			Transaction outstandingAmount = new Transaction(date, novBills.get(i).getBillAmount(), Type.Local,
					TransactionType.Credit_Card_Balance_Brought_Forward, creditCards.get(i));
			
			BigDecimal interestRate = BigDecimal.valueOf(0.28);
			BigDecimal interestAmount = novBills.get(i).getBillAmount().multiply(interestRate).setScale(2, RoundingMode.HALF_UP);
			Transaction interest = new Transaction(date, interestAmount, Type.Local,
					TransactionType.Credit_Card_Interest, creditCards.get(i));
			
			creditCards.get(i).setCreditBalance(creditCards.get(i).getCreditBalance().add(interestAmount));
			creditCards.get(i).setCreditBalance(creditCards.get(i).getCreditBalance().add(BigDecimal.valueOf(200)));
			creditCards.get(i).setCreditBalance(creditCards.get(i).getCreditBalance().add(novBills.get(i).getBillAmount()));
			
			creditCardRepository.save(creditCards.get(i));
			transactionRepository.save(lateFee);
			transactionRepository.save(outstandingAmount);
			transactionRepository.save(interest);
		}

		
		// Rebate entries
//		Rebate rebate1 = new Rebate(BigDecimal.valueOf(123.4), randomDate(), creditCards.get(0),
//				transactionsForCards.get(0));
//		rebateRepository.save(rebate1);
//		Rebate rebate2 = new Rebate(BigDecimal.valueOf(123.4), randomDate(), creditCards.get(1),
//				transactionsForCards.get(1));
//		rebateRepository.save(rebate2);
//		Rebate rebate3 = new Rebate(BigDecimal.valueOf(123.4), randomDate(), creditCards.get(2),
//				transactionsForCards.get(2));
//		rebateRepository.save(rebate3);
//
//		// Point entries
//		Point point1 = new Point(123, randomDate(), creditCards.get(0), transactionsForCards.get(0));
//		pointRepository.save(point1);
//		Point point2 = new Point(456, randomDate(), creditCards.get(1), transactionsForCards.get(1));
//		pointRepository.save(point2);
//		Point point3 = new Point(789, randomDate(), creditCards.get(2), transactionsForCards.get(2));
//		pointRepository.save(point3);

		// Fetch and store fxRates from API into database
		fxRatesService.storeConversionRates();
	}

	public Date randomDate() {
		Date startDate = Date.valueOf("2020-01-01");
		Date endDate = Date.valueOf("2023-12-12");

		return randomDateGenerator(startDate, endDate);
	}

	public Date randomDateForYearRange(int startYear, int endYear) {
		Date startDate = Date.valueOf(startYear + "-01-01");
		Date endDate = Date.valueOf(endYear + "-12-31");

		return randomDateGenerator(startDate, endDate);
	}

	public Date randomDateForMonth(int year, int month) {
		if (month < 1 || month > 12) {
			throw new IllegalArgumentException("Invalid month. Month should be between 1 and 12.");
		}

		LocalDate currentDate = LocalDate.now();
		LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
		LocalDate lastDayOfMonth = LocalDate.of(year, month, YearMonth.of(year, month).lengthOfMonth());

		Date startDate = Date.valueOf(firstDayOfMonth);
		Date endDate = currentDate.isBefore(lastDayOfMonth) ? Date.valueOf(currentDate) : Date.valueOf(lastDayOfMonth);

		return randomDateGenerator(startDate, endDate);
	}

	private Date randomDateGenerator(Date startDate, Date endDate) {
		long startTime = startDate.getTime();
		long endTime = endDate.getTime() + 24 * 60 * 60 * 1000 - 1; // Account for time between 00:00:00hrs and 23:59:59hrs

		long randomTime = (long) (startTime + Math.random() * Math.subtractExact(endTime, startTime));

		return new Date(randomTime);
	}

	public List<Transaction> transactionGeneratorForCards(int numOfTransactions, List<CreditCard> creditCards,
			List<Merchant> merchants) {
		List<Transaction> transactions = new ArrayList<Transaction>();

		for (int i = 0; i < numOfTransactions; i++) {
			Random random = new Random();

			// Generate random transactions for 3 consecutive months
			for (int j = 0; j < 3; j++) {
				Merchant merchant = merchants.get(random.nextInt(merchants.size()));
				BigDecimal randomAmount = generateRandomAmountForMerchant(merchant);
				randomAmount.setScale(2, RoundingMode.HALF_UP);

				transactions.add(new Transaction(randomDateForMonth(2023, 10 + j), randomAmount, Type.Local,
						TransactionType.Purchase, creditCards.get(random.nextInt(creditCards.size())), merchant));
			}
		}

		return transactions;
	}

	public List<Transaction> transactionGeneratorForAccounts(int numOfTransactions, List<BankAccount> bankAccounts) {
		List<Transaction> transactions = new ArrayList<Transaction>();

		for (int i = 0; i < numOfTransactions; i++) {
			Random random = new Random();

			// Generate random transactions for 3 consecutive months
			for (int j = 0; j < 3; j++) {
				// Generate a random integer in the range [2, 100)
				int randomValueInRangeDeposit = 2 + random.nextInt(100 - 2 + 1);
				int randomValueInRangeWithdraw = 2 + random.nextInt(100 - 2 + 1);

				// Scale the value to the desired range [20, 1000)
				// Simulates deposit and withdrawal amounts in the 10s with minimum amount of 20
				BigDecimal randomAmountDeposit = new BigDecimal(randomValueInRangeDeposit * 10);
				BigDecimal randomAmountWithdraw = new BigDecimal(randomValueInRangeWithdraw * -10);
				randomAmountDeposit.setScale(2, RoundingMode.HALF_UP);
				randomAmountWithdraw.setScale(2, RoundingMode.HALF_UP);

				transactions.add(new Transaction(randomDateForMonth(2023, 10 + j), randomAmountDeposit, Type.Local,
						bankAccounts.get(random.nextInt(bankAccounts.size())), TransactionType.Deposit));
				transactions.add(new Transaction(randomDateForMonth(2023, 10 + j), randomAmountWithdraw, Type.Local,
						bankAccounts.get(random.nextInt(bankAccounts.size())), TransactionType.Withdraw));
			}
		}

		return transactions;
	}

	private BigDecimal generateRandomAmountForMerchant(Merchant merchant) {
		MerchantCategory merchantCategory = merchant.getMerchantCategory();
		Random random = new Random();
		double randomAmount = 0;

		// Generate random amount based on merchant category
		// Multiple random values added together to increase variance
		if (merchantCategory != null) {
			switch (merchantCategory.getMerchantCategoryCode()) {
			case 5814: // Dining
				randomAmount = (random.nextDouble(25) + random.nextDouble(25));
				break;
			case 5411: // Grocery Stores, Supermarkets
			case 5912: // Drug Stores and Pharmacies
				randomAmount = (random.nextDouble(50) + random.nextDouble(50));
				break;
			case 5311: // Department Stores
				randomAmount = (random.nextDouble(100) + random.nextDouble(100));
				break;
			case 5732: // Electronics Stores
				randomAmount = (random.nextDouble(100) + random.nextDouble(200) + random.nextDouble(200));
				break;
			default:
				break;
			}
		}

		return BigDecimal.valueOf(randomAmount);
	}

	private List<Transaction> generateInstalmentPaymentTransactions(InstalmentPlan plan, LocalDate startDate,
			CreditCard creditCard, Merchant merchant, BigDecimal purchaseAmount) {
		List<Transaction> transactions = new ArrayList<>();

		LocalDate currentDate = LocalDate.now();

		for (int i = 0; i < plan.getNoOfInstalments(); i++) {
			LocalDate transactionDate;

			if (i == 0) {
				transactionDate = startDate;

				// Check if the transaction date is not after the current date
				if (!transactionDate.isAfter(currentDate)) {
					Transaction transaction = new Transaction(randomDateGenerator(Date.valueOf(transactionDate), Date.valueOf(transactionDate)),
							purchaseAmount, Type.Local, TransactionType.Purchase_With_Instalment, creditCard, merchant);
					transactions.add(transaction);
				}
			} else {
				transactionDate = startDate.plusMonths(i).withDayOfMonth(1);

				// Check if the transaction date is not after the current date
				if (!transactionDate.isAfter(currentDate)) {
					Transaction transaction = new Transaction(Date.valueOf(transactionDate), purchaseAmount, Type.Local,
							TransactionType.Purchase_With_Instalment, creditCard, merchant);
					transactions.add(transaction);
				}
			}
		}

		transactionRepository.saveAll(transactions);
		return transactions;
	}

	private long generateUniqueRandomCardNumber() {
		Random random = new Random();
		StringBuilder cardNumberBuilder = new StringBuilder(CARD_NUMBER_LENGTH);

		// Ensure the first digit is not 0
		cardNumberBuilder.append(random.nextInt(9) + 1);

		// Generate the remaining 15 digits
		for (int i = 1; i < CARD_NUMBER_LENGTH; i++) {
			cardNumberBuilder.append(random.nextInt(10));
		}

		long generatedCardNumber = Long.parseLong(cardNumberBuilder.toString());

		// Recursively generate a new number if it already exists
		if (usedCardNumbers.contains(generatedCardNumber)) {
			return generateUniqueRandomCardNumber(); // Retry if number exists
		}

		usedCardNumbers.add(generatedCardNumber); // Add to used card numbers
		return generatedCardNumber;
	}

	public static long generateUniqueAccountId() {
		Random random = new Random();
		StringBuilder accountIdBuilder = new StringBuilder(accountIdNumberLength);

		// Ensure the first digit is not 0
		accountIdBuilder.append(random.nextInt(9) + 1);

		// Generate the remaining 15 digits
		for (int i = 1; i < accountIdNumberLength; i++) {
			accountIdBuilder.append(random.nextInt(10));
		}

		long generatedAccountNumber = Long.parseLong(accountIdBuilder.toString());

		// Recursively generate a new number if it already exists
		if (usedAccountNumbers.contains(generatedAccountNumber)) {
			return generateUniqueAccountId();
		}

		return generatedAccountNumber;
	}
}
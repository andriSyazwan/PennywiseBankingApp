import '../../styles/TransactionPage.css'
import React, { useState, useEffect } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { Link, useNavigate } from 'react-router-dom';
import { useAPI } from '../../contexts/APIContext';
import Navbar from '../../components/Navbar';

const PurchasePage = () => {

  const { userId } = useAuth();
  const { getInstalmentPlans, instalmentPlans, getMerchantCategories, merchantCategories, getMerchants, merchants, getCreditCards, creditCards, purchase, instalmentPurchase, convertCurrency, convertedAmount } = useAPI()

  const [merchantCategoryCode, setMerchantCategoryCode] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [merchantId, setMerchantId] = useState('');
  const [selectedMerchant, setSelectedMerchant] = useState('');
  const [sourceCardNumber, setSourceCardNumber] = useState('');
  const [selectedCreditCard, setSelectedCreditCard] = useState('');
  const [amount, setAmount] = useState('');
  const [purchaseType, setPurchaseType] = useState('');
  const [instalmentType, setInstalmentType] = useState('');
  const [errorMsg, setErrorMsg] = useState([]);
  const navigate = useNavigate();

  const filteredMerchants = merchants.filter(
    merchant => merchant.merchantCategory.merchantCategoryCode === parseInt(merchantCategoryCode)
  );

  useEffect(() => {
    if (userId == null) {
      console.log('Not logged in, redirecting to login page');
      navigate('/login');
    }
    setErrorMsg('');

    setPurchaseType('normal');

    getInstalmentPlans();
    getCreditCards(userId);
    getMerchantCategories();
    getMerchants();
  }, [userId, navigate]);

  useEffect(() => {
    const selected = merchantCategories.find(merchantCategory => String(merchantCategory.merchantCategoryCode) === merchantCategoryCode);
    setSelectedCategory(selected);
    setMerchantId('')
    setSelectedMerchant('')
  }, [merchantCategoryCode, merchants]);

  useEffect(() => {
    const selected = merchants.find(merchant => String(merchant.merchantId) === merchantId);
    setSelectedMerchant(selected);
  }, [merchantId, merchants]);

  useEffect(() => {
    const selected = creditCards.find(creditCard => String(creditCard.cardNumber) === sourceCardNumber);
    console.log(selected)
    setSelectedCreditCard(selected);
  }, [sourceCardNumber, creditCards]);

  const handlePurchase = async (e) => {
    e.preventDefault();

    try {
      let parsedAmount = parseFloat(amount);

      const currencyCode = selectedMerchant.defaultCurrencyCode;
      console.log(currencyCode);

      if (parsedAmount <= 0) {
        setErrorMsg('Invalid amount. Please enter a valid positive amount.');
        setTimeout(() => {
          setErrorMsg('');
        }, 5000);
        return;
      }

      const convertedAmount = await convertCurrency(currencyCode, parsedAmount);
      console.log(`Converted amount: ${convertedAmount}`);

      if (convertedAmount > selectedCreditCard.formattedAvailableBalance) {
        setErrorMsg('Insufficient funds in the available balance to make the purchase.');
        setTimeout(() => {
          setErrorMsg('');
        }, 5000);
        return;
      }

      console.log(parsedAmount);

      if (purchaseType === 'normal') {
        await purchase(selectedMerchant.merchantId, selectedCreditCard.cardId, parsedAmount);
      } else {
        await instalmentPurchase(selectedMerchant.merchantId, selectedCreditCard.cardId, parsedAmount, instalmentType);
      }
      

      // Show success notification
      window.alert('Purchase completed successfully');

      getMerchants();
      getCreditCards(userId);
    } catch (error) {
      console.error('Error transferring funds:', error);

      // Show error notification
      window.alert('Error during purchase. Please try again.');
    }
  };

  const normalPurchaseForm = () => {
    return (
      <form className="purchaseForm" onSubmit={handlePurchase}>
        <div className="field">
          <label htmlFor="merchantCategoryCode">Category</label>
          <div className="fieldInput">
            <select
              className="globalInput"
              id="merchantCategoryCode"
              name="merchantCategoryCode"
              value={merchantCategoryCode}
              onChange={(e) => setMerchantCategoryCode(e.target.value)}
              required
            >
              <option value="" disabled>Select a category</option>
              {merchantCategories.map(merchantCategory => (
                <option key={merchantCategory.merchantCategoryCode} value={merchantCategory.merchantCategoryCode}>
                  {`${merchantCategory.categoryName}`}
                </option>
              ))}
            </select>
          </div>
        </div>
        <div className="field">
          <label htmlFor="merchantId">Merchant</label>
          <div className="fieldInput">
            <select
              className = "globalInput"
              id="merchantId"
              name="merchantId"
              value={merchantId}
              onChange={(e) => setMerchantId(e.target.value)}
              required
            >
              <option value="" disabled>Select a merchant</option>
              {filteredMerchants.map(merchant => (
                <option key={merchant.merchantId} value={merchant.merchantId}>
                  {`${merchant.name} - ${merchant.defaultCurrencyCode}`}
                </option>
              ))}
            </select>
          </div>
        </div>
        <div className="field">
          <label htmlFor="sourceCardNumber">Pay With</label>
          <div className="fieldInput">
            <select
              className = "globalInput"
              id="sourceCardNumber"
              name="sourceCardNumber"
              value={sourceCardNumber}
              onChange={(e) => setSourceCardNumber(e.target.value)}
              required
            >
              <option value="" disabled>Select a credit card</option>
              {creditCards.map((creditCard) => (
                <option key={creditCard.cardNumber} value={creditCard.cardNumber}>
                  {`•••• •••• •••• ${String(creditCard.cardNumber).slice(-4)} - Available Balance: ${creditCard.formattedAvailableBalance} SGD`}
                </option>
              ))}
            </select>
          </div>
        </div>
        <div className="field">
          <label htmlFor="amount">Amount</label>
          <div className="fieldInput">
            <input
              className="globalInput"
              type="number"
              id="amount"
              name="amount"
              value={amount}
              step='0.01'
              placeholder='0.00'
              onChange={(e) => setAmount(e.target.value)}
              required
            /> <span>{selectedMerchant ? selectedMerchant.defaultCurrencyCode : 'SGD'}</span>
          </div>
        </div>
        {errorMsg && <div className="error-message">{errorMsg}</div>}
        <button type="submit" className="universalButton">Purchase</button>
      </form>
    )
  }

  const instalmentPurchaseForm = () => {
    return (
      <form className="purchaseForm" onSubmit={handlePurchase}>
        <div className="field">
          <label htmlFor="merchantCategoryCode">Category</label>
          <div className="fieldInput">
            <select
              className="globalInput"
              id="merchantCategoryCode"
              name="merchantCategoryCode"
              value={merchantCategoryCode}
              onChange={(e) => setMerchantCategoryCode(e.target.value)}
              required
            >
              <option value="" disabled>Select a category</option>
              {merchantCategories.map(merchantCategory => (
                <option key={merchantCategory.merchantCategoryCode} value={merchantCategory.merchantCategoryCode}>
                  {`${merchantCategory.categoryName}`}
                </option>
              ))}
            </select>
          </div>
        </div>
        <div className="field">
          <label htmlFor="merchantId">Merchant</label>
          <div className="fieldInput">
            <select
              className="globalInput"
              id="merchantId"
              name="merchantId"
              value={merchantId}
              onChange={(e) => setMerchantId(e.target.value)}
              required
            >
              <option value="" disabled>Select a merchant</option>
              {filteredMerchants.map(merchant => (
                <option key={merchant.merchantId} value={merchant.merchantId}>
                  {`${merchant.name} - ${merchant.defaultCurrencyCode}`}
                </option>
              ))}
            </select>
          </div>
        </div>
        <div className="field">
          <label htmlFor="sourceCardNumber">Pay With</label>
          <div className="fieldInput">
            <select
              className="globalInput"
              id="sourceCardNumber"
              name="sourceCardNumber"
              value={sourceCardNumber}
              onChange={(e) => setSourceCardNumber(e.target.value)}
              required
            >
              <option value="" disabled>Select a credit card</option>
              {creditCards.map((creditCard) => (
                <option key={creditCard.cardNumber} value={creditCard.cardNumber}>
                  {`•••• •••• •••• ${String(creditCard.cardNumber).slice(-4)} - Available Balance: ${creditCard.formattedAvailableBalance} SGD`}
                </option>
              ))}
            </select>
          </div>
        </div>
        <div className="field">
          <label htmlFor="amount">Amount</label>
          <div className="fieldInput">
            <input
              className="globalInput"
              type="number"
              id="amount"
              name="amount"
              value={amount}
              step='0.01'
              placeholder='0.00'
              onChange={(e) => setAmount(e.target.value)}
              required
            /> <span>{selectedMerchant ? selectedMerchant.defaultCurrencyCode : 'SGD'}</span>
          </div>
        </div>
        <div className="field">
          <label htmlFor="instalmentType">Instalment</label>
          <div className="fieldInput">
            <select
              className="globalInput"
              id="instalmentType"
              name="instalmentType"
              value={instalmentType}
              onChange={(e) => setInstalmentType(e.target.value)}
              required
            >
              <option value="" disabled>Select an instalment plan</option>
              {instalmentPlans.map((instalmentPlan) => (
                <option key={instalmentPlan.planId} value={instalmentPlan.planId}>
                  {`${instalmentPlan.noOfInstalments}-Month Plan (${instalmentPlan.interestRate}% Interest): $${(amount / instalmentPlan.noOfInstalments * (instalmentPlan.interestRate/100 + 1)).toFixed(2)} per month`}
                </option>
              ))}
            </select>
          </div>
        </div>
        {errorMsg && <div className="error-message">{errorMsg}</div>}
        <button type="submit" className="universalButton">Purchase through Instalments</button>
      </form>
    )
  }

  const renderForm = () => {
    switch (purchaseType) {
      case 'normal':
        return normalPurchaseForm();
      case 'instalment':
        return instalmentPurchaseForm();
      default:
        return null;
    }
  };

  return (
    <div>
      <Navbar />
      <div className="headerContainer">
        <div className="headerTitle">Purchase From Merchant</div>
      </div>

      <main className="purchasePageMain">
        <div className="informationContainer">
          <div className="stringDataSection">
            <h2>Category</h2>
            <p>{selectedCategory ? selectedCategory.categoryName : 'No category selected'}</p>
          </div>
          <div className="stringDataSection">
            <h2>Merchant Name</h2>
            <p>{selectedMerchant ? selectedMerchant.name : 'No merchant selected'}</p>
          </div>
          <div className="stringDataSection">
            <h2>Merchant Currency</h2>
            <p>{selectedMerchant ? selectedMerchant.defaultCurrencyCode : 'No merchant selected'}</p>
          </div>
        </div>

        <div className="informationContainer">
          <div className="stringDataSection">
            <h2>Card Number</h2>
            <p>{selectedCreditCard ? `•••• •••• •••• ${String(selectedCreditCard.cardNumber).slice(-4)}` : 'No account selected'}</p>
          </div>
          <div className="numericalDataSection">
            <h2>Credit Limit</h2>
            <div className="value">
              <p>{selectedCreditCard ? selectedCreditCard.creditLimit : '0.00'}</p> <span>SGD</span>
            </div>
          </div>
          <div className="numericalDataSection">
            <h2>Available Balance</h2>
            <div className="value">
              <p>{selectedCreditCard ? selectedCreditCard.formattedAvailableBalance : '0.00'}</p> <span>SGD</span>
            </div>
          </div>
        </div>

        <div className="purchaseTypeContainer" style={{ display: 'flex', width: '' }}>
          <button
            className="universalButton"
            onClick={() => setPurchaseType('normal')}
            style={{ flex: '1' }}>
            Purchase Now
          </button>
          <button
            className="universalButton"
            onClick={() => setPurchaseType('instalment')}
            style={{ flex: '1' }}>
            Purchase through Instalments
          </button>
        </div>

        {renderForm()}

        <div id="myModal" class="modal">
          <div class="modal-content">
            <span class="close">&times;</span>
            <p>Successful Purchase!</p>
          </div>
        </div>
      </main>
    </div>
  );
};

export default PurchasePage;

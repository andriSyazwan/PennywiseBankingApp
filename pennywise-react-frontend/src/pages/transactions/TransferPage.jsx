import '../../styles/TransactionPage.css'
import React, { useState, useEffect } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { Link, useNavigate } from 'react-router-dom';
import { useAPI } from '../../contexts/APIContext';
import Navbar from '../../components/Navbar';

const TransferPage = () => {

  const { userId } = useAuth();
  const { getAllBankAccounts, allBankAccounts, getBankAccounts, bankAccounts, transfer } = useAPI()

  const [sourceAccountId, setSourceAccountId] = useState('');
  const [selectedSourceAccount, setSelectedSourceAccount] = useState('');
  const [receivingAccountNumber, setReceivingAccountNumber] = useState('');
  const [selectedReceivingAccount, setSelectedReceivingAccount] = useState('');
  const [amount, setAmount] = useState('');

  const [errorMsg, setErrorMsg] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    if (userId == null) {
      console.log('Not logged in, redirecting to login page');
      navigate('/login');
    }
    setErrorMsg('');

    getBankAccounts(userId);
    getAllBankAccounts();
  }, [userId, navigate]);

  useEffect(() => {
    const selected = bankAccounts.find(account => String(account.accountId) === sourceAccountId);
    setSelectedSourceAccount(selected);
  }, [sourceAccountId, bankAccounts]);

  useEffect(() => {
    const selected = allBankAccounts.find(account => String(account.accountNumber) === receivingAccountNumber);
    setSelectedReceivingAccount(selected);
  }, [receivingAccountNumber, allBankAccounts]);

  const handleTransfer = async (e) => {
    e.preventDefault();

    try {
      const parsedAmount = parseFloat(amount);

      if (parsedAmount <= 0) {
        setErrorMsg('Invalid amount. Please enter a valid positive amount.');
        setTimeout(() => {
          setErrorMsg('');
        }, 5000);
        return;
      }

      const maxTransferAmount = selectedSourceAccount.balance

      if (parsedAmount > selectedSourceAccount.balance) {
        setErrorMsg(`Insufficient funds. Please enter an amount smaller than SGD ${maxTransferAmount}.`);
        setTimeout(() => {
          setErrorMsg('');
        }, 5000);
        return;
      }

      if (selectedSourceAccount.accountNumber === selectedReceivingAccount.accountNumber) {
        setErrorMsg(`Unable to transfer to same account, please input another account number to transfer to/from.`);
        setTimeout(() => {
          setErrorMsg('');
        }, 5000);
        return;
      }

      await transfer(sourceAccountId, selectedReceivingAccount.accountId, parsedAmount);

      // Show success notification
      window.alert('Funds transferred successfully');

      // Refetch bank account details to get updated balances
      getBankAccounts(userId);
    } catch (error) {
      console.error('Error transferring funds:', error);

      // Show error notification
      window.alert('Error transferring funds. Please try again.');
    }
  };

  return (
    <div>
      <Navbar />
      <div className="headerContainer">
        <div className="headerTitle">Transfer Funds</div>
      </div>

      <main className="transferPageMain">
        <div className="informationContainer">
          <h1>Source Account</h1>
          <div className="stringDataSection">
            <h2>Account Type</h2>
            <p>{selectedSourceAccount ? selectedSourceAccount.accountType : 'No account selected'}</p>
          </div>
          <div className="stringDataSection">
            <h2>Account Number</h2>
            <p>{selectedSourceAccount ? selectedSourceAccount.accountNumber : 'No account selected'}</p>
          </div>
          <div className="numericalDataSection">
            <h2>Balance</h2>
            <div className="value">
              <p>{selectedSourceAccount ? selectedSourceAccount.balance : '0.00'}</p> <span>SGD</span>
            </div>
          </div>
        </div>

        <div className="informationContainer">
          <h1>Receiving Account</h1>
          <div className="stringDataSection">
            <h2>Account Number</h2>
            <p>
              {receivingAccountNumber ? (
                selectedReceivingAccount ? (
                  selectedReceivingAccount.accountNumber
                ) : (
                  'Invalid Account ID'
                )
              ) : (
                'No account selected'
              )}
            </p>
          </div>
        </div>

        <form className="transferForm" onSubmit={handleTransfer}>
          <div className="field">
            <label htmlFor="sourceAccountId">Source Account</label>
            <div className="fieldInput">
              <select
                className="globalInput"
                id="sourceAccountId"
                name="sourceAccountId"
                value={sourceAccountId}
                onChange={(e) => setSourceAccountId(e.target.value)}
                required
              >
                <option value="" disabled>Select an account</option>
                {bankAccounts.map((account) => (
                  <option key={account.id} value={account.accountId}>
                    {`${account.accountType} - ${account.accountNumber}`}
                  </option>
                ))}
              </select>
            </div>
          </div>
          <div className="field">
            <label htmlFor="receivingAccountNumber">Receiving Account</label>
            <div className="fieldInput">
              <input
                className="globalInput"
                type="number"
                id="receivingAccountNumber"
                name="receivingAccountNumber"
                value={receivingAccountNumber}
                placeholder="Enter an account number"
                onChange={(e) => setReceivingAccountNumber(e.target.value)}
                required
              />
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
              />
              <span>SGD</span>
            </div>
          </div>
          {errorMsg && <div className="error-message">{errorMsg}</div>}
          <button type="submit" className="universalButton">Transfer</button>
        </form>

        <div id="myModal" class="modal">
          <div class="modal-content">
            <span class="close">&times;</span>
            <p>Successful Transfer!</p>
          </div>
        </div>
      </main>
    </div>
  );
};

export default TransferPage;

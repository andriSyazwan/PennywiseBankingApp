import '../../styles/TransactionPage.css'
import React, { useState, useEffect } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import { useAPI } from '../../contexts/APIContext';
import Navbar from '../../components/Navbar';

const DepositPage = () => {

  const { userId } = useAuth();
  const { getBankAccounts, bankAccounts, deposit } = useAPI()

  const [accountId, setAccountID] = useState('');
  const [selectedAccount, setSelectedAccount] = useState(null);
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
  }, [userId, navigate]);

  // Update selected account details when accountId changes
  useEffect(() => {
    const selected = bankAccounts.find(account => String(account.accountId) === accountId);
    setSelectedAccount(selected);
  }, [accountId, bankAccounts]);

  const handleDeposit = async (e) => {
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

      await deposit(accountId, parsedAmount);

      // Show success notification
      window.alert('Funds deposited successfully');

      // Refetch bank account details to get updated balances
      getBankAccounts(userId);
    } catch (error) {
      console.error('Error depositing funds:', error);

      // Show error notification
      window.alert('Error depositing funds. Please try again.');
    }
  };

  return (
    <div className="depositPageContainer">
      <Navbar />
      <div className="headerContainer">
        <div className="headerTitle">Deposit Funds</div>
      </div>

      <main className="depositPageMain">
        <div className="informationContainer">
          <div className="stringDataSection">
            <h2>Account Type</h2>
            <p>{selectedAccount ? selectedAccount.accountType : 'No account selected'}</p>
          </div>
          <div className="stringDataSection">
            <h2>Account Number</h2>
            <p>{selectedAccount ? selectedAccount.accountNumber : 'No account selected'}</p>
          </div>
          <div className="numericalDataSection">
            <h2>Balance</h2>
            <div className="value">
              <p>{selectedAccount ? selectedAccount.balance : '0.00'}</p> <span>SGD</span>
            </div>
          </div>
        </div>

        <form className="depositForm" onSubmit={handleDeposit}>
          <div className="field">
            <label htmlFor="accountId">Account</label>
            <div className="fieldInput" >
              <select
              className="globalInput"
                id="accountId"
                name="accountId"
                value={accountId}
                onChange={(e) => setAccountID(e.target.value)}
                required
              >
                <option value="" disabled>Select an account</option>
                {bankAccounts.map((account) => (
                  <option key={account.id} value={account.accountId}>
                    {`${account.accountType}:\t${account.accountNumber}`}
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
              />
              <span>SGD</span>
            </div>
          </div>
          {errorMsg && <div className="error-message">{errorMsg}</div>}
          <button type="submit" className="universalButton">Deposit</button>
        </form>

        <div id="myModal" class="modal">
          <div class="modal-content">
            <span class="close">&times;</span>
            <p>Successful Deposit!</p>
          </div>
        </div>
      </main>
    </div>
  );
};

export default DepositPage;
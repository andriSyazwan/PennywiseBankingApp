import '../../styles/TransactionPage.css'
import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { useAPI } from '../../contexts/APIContext';
import Navbar from '../../components/Navbar';
import { useNavigate } from 'react-router-dom';

const BillPaymentPage = () => {

    const params = useParams()
    const { userId } = useAuth();
    const { getCreditCard, creditCard, getBankAccounts, bankAccounts, getCreditCardBills, creditCardBills, billPayment } = useAPI()

    const [sourceAccountId, setSourceAccountId] = useState('');
    const [selectedSourceAccount, setSelectedSourceAccount] = useState('')
    const [amount, setAmount] = useState('');

    const [errorMsg, setErrorMsg] = useState([]);
    const navigate = useNavigate();

    const latestBill = creditCardBills.length > 0 ? creditCardBills[2] : null

    useEffect(() => {
        if (userId == null) {
            console.log('Not logged in, redirecting to login page');
            navigate('/login');
        }
        setErrorMsg('');

        getCreditCard(params.cardId);
        getBankAccounts(userId)
        getCreditCardBills(params.cardId);
    }, [userId, navigate]);

    useEffect(() => {
        const selected = bankAccounts.find(account => String(account.accountId) === sourceAccountId);
        setSelectedSourceAccount(selected);
    }, [sourceAccountId, bankAccounts]);

    const handleBillPayment = async (e) => {
        e.preventDefault();

        try {
            const parsedAmount = parseFloat(amount);

            if (parsedAmount > selectedSourceAccount.balance) {
                setErrorMsg('Insufficient funds in account balance to make payment.');
                setTimeout(() => {
                    setErrorMsg('');
                }, 5000);
                return;
            }

            if (parsedAmount <= 0) {
                setErrorMsg('Invalid amount. Please enter a valid positive amount.');
                setTimeout(() => {
                    setErrorMsg('');
                }, 5000);
                return;
            }

            if (parsedAmount < creditCard.minimumPaymentAmount) {
                setErrorMsg(`Amount too small. Please enter at least the minimum payment amount of $${creditCard.minimumPaymentAmount}.`);
                setTimeout(() => {
                    setErrorMsg('');
                }, 5000);
                return;
            }

            await billPayment(latestBill.billId, sourceAccountId, parsedAmount);

            // Show success notification
            window.alert('Bill payment was successful');

            // Refetch bank account details to get updated balances
            getCreditCardBills(params.cardId);
            getBankAccounts(userId);
        } catch (error) {
            console.error('Error submitting bill payment:', error);

            // Show error notification
            window.alert('Error submitting bill payment. Please try again.');
        }
    };

    return (
        <div className="billPaymentPageContainer">
            <Navbar />
            <div className="headerContainer">
                <div className="headerTitle">Credit Card Bill Payment</div>
            </div>

            <main className="billPaymentPageMain">
                <div className="informationContainer">
                    <div className="stringDataSection">
                        <h2>Billing Period</h2>
                        <p>{latestBill ? `${latestBill.formattedBillingPeriodStart} to ${latestBill.formattedBillingPeriodEnd}` : 'No bill found'}</p>
                    </div>
                    <div className="numericalDataSection">
                        <h2>Bill Amount</h2>
                        <div className="value">
                            <p>{latestBill ? latestBill.billAmount : '0.00'}</p> <span>SGD</span>
                        </div>
                    </div>
                    <div className="numericalDataSection">
                        <h2>Amount Paid</h2>
                        <div className="value">
                            <p>{latestBill ? latestBill.amountPaid : '0.00'}</p> <span>SGD</span>
                        </div>
                    </div>
                </div>
                <div className="informationContainer">
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
                <form className="billPaymentForm" onSubmit={handleBillPayment}>
                    <div className="field">
                        <label htmlFor="accountId">Pay With</label>
                        <div className="fieldInput">
                            <select
                                className="globalInput"
                                id="sourceAccountId"
                                name="sourceAccountId"
                                value={sourceAccountId}
                                onChange={(e) => setSourceAccountId(e.target.value)}
                                required
                            >
                                <option value="" disabled>Select an account to pay from</option>
                                {bankAccounts.map(account => (<option key={account.id} value={account.accountId}>
                                    {`${account.accountType} - ${account.accountNumber}`}
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
                    <button class="universalButton" type="submit">Pay Bill</button>
                </form>

                <div id="myModal" class="modal">
                    <div class="modal-content">
                        <span class="close">&times;</span>
                        <p>Successful Bill Payment!</p>
                    </div>
                </div>
            </main>
        </div>
    )
}

export default BillPaymentPage;
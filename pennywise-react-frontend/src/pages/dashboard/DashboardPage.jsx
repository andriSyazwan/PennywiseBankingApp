import '../../styles/DashboardPage.css'
import Navbar from '../../components/Navbar'
import OverviewCard from '../../components/OverviewCard'
import TransactionCardForDashboard from '../../components/TransactionCard'
import { useAuth } from '../../contexts/AuthContext';
import { useAPI } from '../../contexts/APIContext';
import { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';

const DashboardPage = () => {

    const { userId } = useAuth();
    const { getFullName, fullName, getBankAccounts, bankAccounts, getCreditCards, creditCards, getBillsForUser, userBills, getTransactions, transactions } = useAPI();
    const [errorMsg, setErrorMsg] = useState([]);
    const recentTransactions = transactions.slice(0, 5)
    const navigate = useNavigate();

    const outstandingBills = userBills.filter(bill => bill.isPaid === false);

    useEffect(() => {
        if (userId == null) {
            console.log('Not logged in, redirecting to login page')
            navigate('/login')
        }
        setErrorMsg('');

        getFullName(userId);
        getBankAccounts(userId);
        getCreditCards(userId);
        getBillsForUser(userId);
        getTransactions(userId);
    }, [])

    return (
        <div className="dashboardContainer">
            <Navbar />
            <div>{errorMsg}</div>
            <div className="headerContainer">
                <div className="headerTitle">Hello, {fullName}!</div>
                <div className="headerBody">You have {outstandingBills.length} outstanding bill{outstandingBills.length === 1 ? '' : 's'}.</div>
            </div>
            <div className="dashboardContentContainer">
                <div id="heading">Cards and Accounts</div>
                {bankAccounts.map((bankAccount) =>
                    <div>
                        <OverviewCard
                            cardType="bankAccount"
                            account={bankAccount}
                        />
                    </div>
                )}
                {creditCards.map((creditCard) =>
                    <div>
                        <OverviewCard
                            cardType="creditCard"
                            account={creditCard}
                        />
                    </div>
                )}
            </div>
            <div className='dashboardContentContainer'>
                <div id="heading">Recent Transactions</div>
                <div style={{ overflow: 'auto' }}>
                    {transactions.length === 0 ? (
                        <p style={{ color: 'white' }}>No transactions found</p>
                    ) : (
                        recentTransactions.map(transaction => {
                            console.log('Transaction Object:', transaction);
                            return <TransactionCardForDashboard key={transaction.transactionId} transaction={transaction} />
                        })
                    )}
                </div>
            </div>
        </div>
    )

}

export default DashboardPage;

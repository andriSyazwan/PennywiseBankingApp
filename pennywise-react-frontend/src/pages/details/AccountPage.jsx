import '../../styles/DetailsPage.css';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { useAPI } from '../../contexts/APIContext';
import TransactionCard from '../../components/TransactionCard';
import Navbar from '../../components/Navbar';
import { Link, useNavigate } from 'react-router-dom';

const AccountPage = () => {

    const params = useParams()
    const { userId } = useAuth();
    const { getBankAccount, bankAccount, getTransactionsForAccount, transactions } = useAPI()
    const [errorMsg, setErrorMsg] = useState([]);
    const recentTransactions = transactions.slice(0, 5)
    const navigate = useNavigate();

    useEffect(() => {
        if (userId == null) {
            console.log('Not logged in, redirecting to login page')
            navigate('/login')
        }
        setErrorMsg('');

        getBankAccount(params.accountId)
        getTransactionsForAccount(params.accountId)
    }, [])

    return (
        <div className="detailPage">
            <Navbar />
            <div className="detailContainer">
                <div id="heading">
                    {bankAccount.accountType}
                </div>
                <div className="informationSection">
                    <div className="numericalData">
                        <h2>Account No.</h2>                        
                        <div className="value">
                            <p>{bankAccount.accountNumber}</p>
                        </div>
                    </div>
                    <div className="numericalData">
                        <h2>Current Balance</h2>
                        <div className="value">
                            <p>{bankAccount.balance}</p> <span>SGD</span>
                        </div>
                    </div>
                </div>
                <div id="heading">
                    Recent Transactions 
                    <Link to={`/transactions/account/${bankAccount.accountId}`}>
                        <button className="universalButton" style={{width:'100px', margin: '20px'}}>View All</button>
                    </Link>
                </div>
                <div className="transactionContainer" style={{width: '95%'}}>
                    {transactions.length === 0 ? (
                        <p style={{color: 'white'}}>No transactions found</p>
                    ) : (
                        recentTransactions.map(transaction =>
                            <TransactionCard key={transaction.transactionId} transaction={transaction} />
                        )
                    )}
                </div>
            </div>
        </div>
    )
}

export default AccountPage;
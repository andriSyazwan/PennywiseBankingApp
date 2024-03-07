import '../../styles/DetailsPage.css';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { useAPI } from '../../contexts/APIContext';
import TransactionCard from '../../components/TransactionCard';
import CreditCard from '../../components/CreditCard';
import Navbar from '../../components/Navbar';
import { Link, useNavigate } from 'react-router-dom';

const CreditCardPage = () => {

    const params = useParams()
    const { userId } = useAuth();
    const { getCreditCard, creditCard, getCreditCardBills, creditCardBills, getTransactionsForCard, transactions } = useAPI()
    const [errorMsg, setErrorMsg] = useState([]);
    const recentTransactions = transactions.slice(0, 5)
    const navigate = useNavigate();

    const latestBill = creditCardBills.length > 0 ? creditCardBills[2] : null
    const monthNames = [
        'January', 'February', 'March', 'April', 'May', 'June',
        'July', 'August', 'September', 'October', 'November', 'December'
    ];

    useEffect(() => {
        if (userId == null) {
            console.log('Not logged in, redirecting to login page')
            navigate('/login')
        }
        setErrorMsg('');

        console.log(params.cardId)

        getCreditCard(params.cardId)
        getCreditCardBills(params.cardId)
        getTransactionsForCard(params.cardId)
    }, [])

    function formatDateForUrl(date) {
        return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}`;
    }

    return (
        <div className="detailPage">
            <Navbar />
            <div className="detailContainer">
                <CreditCard
                    cardNumber={creditCard.cardNumber}
                    expiryDate={creditCard.expiryDate}
                    displayName={creditCard.displayName}
                />
                <div className="informationSection">
                    <div className="numericalData">
                        <h2>Current Balance</h2>
                        <div className="value">
                            <p>{creditCard.formattedCurrentBalance}</p> <span>SGD</span>
                        </div>
                    </div>
                    <div className="numericalData">
                        <h2>Available Balance</h2>
                        <div className="value">
                            <p>{creditCard.formattedAvailableBalance}</p> <span>SGD</span>
                        </div>
                    </div>
                    <div className="numericalData">
                        <h2>Minimum Payment Due</h2>
                        <div className="value">
                            <p>{creditCard.minimumPaymentAmount}</p> <span>SGD</span>
                        </div>
                    </div>
                    <div className="dateData">
                        <h2>Next Payment Due</h2>
                        <p>{latestBill ? latestBill.formattedDueDate : 'NA'}</p>
                    </div>
                </div>
                <div id="heading">Quick Links</div>
                <div className="informationSection">
                    <div className="quickLinksOptions">
                        <Link to={`/payment/${creditCard.cardId}`}>
                            <div className="iconButton">
                                <img
                                    className="icon"
                                    src="/assets/icons/pay.png"
                                    style={{ width: '50px' }} /><br />
                                Pay Bill</div>
                        </Link>
                        <Link to={`/instalments/${creditCard.cardId}`}>
                            <div className="iconButton">
                                <img
                                    className="icon"
                                    src="/assets/icons/instalment.png"
                                    style={{ width: '50px' }} /><br />
                                Instalment</div>
                        </Link>
                        <Link to={`/rewards/${creditCard.cardId}`}>
                            <div className="iconButton">
                                <img
                                    className="icon"
                                    src="/assets/icons/rewards.png"
                                    style={{ width: '50px' }} /><br />
                                Rewards</div>
                        </Link>
                    </div>
                </div>
                <div id="heading">Recent Statements
                    <Link to={`/month/creditCard/Viewall/${params.cardId}`} className="your-link-button-class">
                        <button className="universalButton" style={{ width: '100px', margin: '20px' }}>View All</button>
                    </Link>
                </div>
                <div className="informationSection">
                    <div className="statementsContainer">
                        {
                            [...Array(3)].map((_, index) => {
                                const currentDate = new Date();
                                const targetDate = new Date(currentDate);
                                targetDate.setMonth(currentDate.getMonth() - index - 1);

                                return (
                                    <Link
                                        to={`/month/creditCard/${params.cardId}/${formatDateForUrl(targetDate)}`}
                                        className="statementLink"
                                        key={index}
                                    >
                                        <div className="statementItem">
                                            {monthNames[targetDate.getMonth()]} {targetDate.getFullYear()}
                                        </div>
                                    </Link>
                                );
                            })
                        }
                    </div>
                </div>
                <div id="heading">
                    Recent Transactions
                    <Link to={`/transactions/creditCard/${creditCard.cardId}`}>
                        <button className="universalButton" style={{ width: '100px', margin: '20px' }}>View All</button>
                    </Link>
                </div>
                <div className="transactionContainer" style={{ width: '95%' }}>
                    {transactions.length === 0 ? (
                        <p style={{ color: 'white' }}>No transactions found</p>
                    ) : (
                        recentTransactions.map(transaction =>
                            <TransactionCard key={transaction.transactionId} transaction={transaction} parentPage="CreditCardPage" />
                        )
                    )}
                </div>
            </div>
        </div>
    )
}

export default CreditCardPage;
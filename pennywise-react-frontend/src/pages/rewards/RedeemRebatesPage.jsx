//import '../styles/RewardsHomePage.css';
import '../../styles/TransactionPage.css';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { useAPI } from '../../contexts/APIContext';
import Navbar from '../../components/Navbar';
import { Link, useNavigate } from 'react-router-dom';

const RedeemRebatesPage = () => {

    const params = useParams();
    const { userId } = useAuth();
    const { getCreditCard, creditCard, redeemRebates } = useAPI()

    const [amount, setAmount] = useState('');
    const [maxRedeemableAmount, setMaxRedeemableAmount] = useState(0);

    const [errorMsg, setErrorMsg] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                if (userId == null) {
                    console.log('Not logged in, redirecting to the login page');
                    navigate('/login');
                    return;
                }
                setErrorMsg('');

                await getCreditCard(params.cardNumber);
                setMaxRedeemableAmount(creditCard?.rebatesBalance || 0);
            } catch (error) {
                setErrorMsg(error.message || 'Error fetching data');
            }
        };

        fetchData();
    }, [userId, params.cardNumber, creditCard?.rebatesBalance])

    const handleRedeem = async (e) => {
        e.preventDefault();

        try {
            const parsedAmount = parseFloat(amount);

            if (parsedAmount <= 0 || isNaN(parsedAmount)) {
                setErrorMsg('Invalid amount. Please enter a valid positive amount.');
                setTimeout(() => {
                    setErrorMsg('');
                }, 5000);
                return errorMsg;
            }

            if (parsedAmount > maxRedeemableAmount) {
                setErrorMsg(`Insufficient cash back. Please enter a smaller amount than SGD ${maxRedeemableAmount}.`);
                setTimeout(() => {
                    setErrorMsg('');
                }, 5000);
                return;
            }

            if (maxRedeemableAmount === 0) {
                setErrorMsg('Sorry, you do not have any cash back left to redeem.');
                setTimeout(() => {
                    setErrorMsg('');
                }, 5000);
                return errorMsg;
            }

            await redeemRebates(creditCard.cardId, parsedAmount);

            // Show success notification
            window.alert('Cash back redeemed successfully');

            // Refetch credit card account details to get updated rebate balance
            await getCreditCard(creditCard.cardNumber);

            // Update maxRedeemableAmount
            setMaxRedeemableAmount(creditCard?.rebatesBalance || 0);
        } catch (error) {
            console.error('Error redeeming rebates:', error);

            // Show error notification
            window.alert('Error during cash back redemption. Please try again.');
        }
    };

    return (
        <div className="RedeemRebatesHomePage">
            <Navbar />
            <div className="headerContainer">
                <div className="headerTitle">Cash Back Redemption</div>
            </div>

            <main className="redeemRebatesPageMain">
                <div className="informationContainer">
                    <div className="stringDataSection">
                        <h2>Card Number</h2>
                        <p>•••• •••• •••• {String(creditCard.cardNumber).slice(-4)}</p>
                    </div>
                    <div className="numericalDataSection">
                        <h2>Cash Back Balance</h2>
                        <div className="value">
                            <p>{maxRedeemableAmount ? parseFloat(maxRedeemableAmount).toFixed(2) : '0.00'}</p> <span>SGD</span>
                        </div>
                    </div>
                </div>

                <form className="redeemRebatesForm" onSubmit={handleRedeem}>
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
                    <button type="submit" className="universalButton">Redeem</button>
                </form>

                <div id="myModal" class="modal">
                    <div class="modal-content">
                        <span class="close">&times;</span>
                        <p>Cash back redeemed successfully.</p>
                    </div>
                </div>
            </main >
        </div >
    );
}

export default RedeemRebatesPage;
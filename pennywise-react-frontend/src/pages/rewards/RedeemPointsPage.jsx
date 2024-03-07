import '../../styles/TransactionPage.css';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { useAPI } from '../../contexts/APIContext';
import Navbar from '../../components/Navbar';
import { Link, useNavigate } from 'react-router-dom';

const RedeemPointsPage = () => {

    const params = useParams();
    const { userId } = useAuth();
    const { getCreditCard, creditCard, redeemPoints } = useAPI()

    const [amountVoucher, setVoucherAmount] = useState('');
    const [amountMiles, setMilesAmount] = useState('');
    const [maxRedeemableAmount, setMaxRedeemableAmount] = useState(0);

    const [errorMsg, setErrorMsg] = useState([]);
    const navigate = useNavigate();

    const defaultOption = "Select Denomination";
    const [selectedVoucherDenomination, setSelectedVoucherDenomination] = useState(defaultOption);
    const [selectedMilesDenomination, setSelectedMilesDenomination] = useState(defaultOption);

    const voucherDenominations = [
        { value: 10, points: 1000 },
        { value: 20, points: 2000 },
        { value: 50, points: 5000 },
        { value: 100, points: 10000 },
    ];

    const milesDenominations = [
        { miles: 1000, points: 1000 },
        { miles: 2000, points: 2000 },
        { miles: 5000, points: 5000 },
        { miles: 10000, points: 10000 },
    ];

    useEffect(() => {
        const fetchData = async () => {
            try {
                if (userId == null) {
                    console.log('Not logged in, redirecting to the login page');
                    navigate('/login');
                    return;
                }
                setErrorMsg('');

                await getCreditCard(params.cardId);
                setMaxRedeemableAmount(creditCard?.pointsBalance || 0);
            } catch (error) {
                setErrorMsg(error.message || 'Error fetching data');
            }
        };

        fetchData();
    }, [userId, params.cardId, creditCard?.pointsBalance])

    // Update redemption amount when the selected VOUCHER denomination changes
    useEffect(() => {
        console.log("selectedVoucherDenomination:", selectedVoucherDenomination);
        const selectedVoucher = voucherDenominations.find(
            (denomination) => denomination.value === parseFloat(selectedVoucherDenomination)
        );

        if (selectedVoucher) {
            setVoucherAmount(selectedVoucher.points);
        }
    }, [selectedVoucherDenomination]);


    // Update redemption amount when the selected MILES denomination changes
    useEffect(() => {
        console.log("selectedMilesDenomination:", selectedMilesDenomination);
        const selectedMiles = milesDenominations.find(
            (denomination) => denomination.miles === parseFloat(selectedMilesDenomination)
        );

        if (selectedMiles) {
            setMilesAmount(selectedMiles.points);
        }
    }, [selectedMilesDenomination]);

    const handleRedeem = async (e) => {
        e.preventDefault();

        try {
            if ((selectedVoucherDenomination === defaultOption && selectedMilesDenomination === defaultOption)) {
                // No miles or vouchers options selected
                setErrorMsg('Please select either a voucher, miles, or both before clicking redeem.');                
                setTimeout(() => {
                    setErrorMsg('');
                }, 5000);
                return errorMsg;
            }

            const parsedAmount = parseFloat(amountVoucher) + parseFloat(amountMiles);

            // Points less than points needed to redeem
            if (parsedAmount > maxRedeemableAmount) {
                setSelectedVoucherDenomination(defaultOption);
                setSelectedMilesDenomination(defaultOption);
                setMilesAmount(parseFloat(0));
                setVoucherAmount(parseFloat(0));

                setErrorMsg(`Insufficient points. You have only ${maxRedeemableAmount} points.`);
                setTimeout(() => {
                    setErrorMsg('');
                }, 5000);
                return;
            }

            setSelectedVoucherDenomination('');
            setSelectedMilesDenomination('');

            if (selectedVoucherDenomination !== defaultOption) {
                if (parseFloat(amountVoucher) <= 0 || isNaN(parseFloat(amountVoucher))) {
                    setErrorMsg('Invalid voucher amount. Please enter a valid positive amount.');
                    setTimeout(() => {
                        setErrorMsg('');
                    }, 5000);
                    return errorMsg;
                }
                await redeemPoints(creditCard.cardId, parseFloat(amountVoucher));
            }

            if (selectedMilesDenomination !== defaultOption) {
                if (parseFloat(amountMiles) <= 0 || isNaN(parseFloat(amountMiles))) {
                    setErrorMsg('Invalid miles amount. Please enter a valid positive amount.');
                    setTimeout(() => {
                        setErrorMsg('');
                    }, 5000);
                    return errorMsg;
                }
                await redeemPoints(creditCard.cardId, parseFloat(amountMiles));
            }

            // Show success notification
            window.alert('Points redeemed successfully. Please check your email.');

            // Refetch credit card account details to get updated points balance
            await getCreditCard(creditCard.cardId);

            // Update maxRedeemableAmount
            setMaxRedeemableAmount(creditCard?.pointsBalance || 0);
            setSelectedVoucherDenomination(defaultOption);
            setSelectedMilesDenomination(defaultOption);
        } catch (error) {
            console.error('Error redeeming points:', error);

            // Show error notification
            window.alert('Error during points redemption. Please try again.');
        }
    };

    return (
        <div className="RedeemPointsHomePage">
            <Navbar />
            <div className="headerContainer">
                <div className="headerTitle">Points Redemption</div>
            </div>

            <main className="redeemPointsPageMain">
                <div className="informationContainer">
                    <div className="stringDataSection">
                        <h2>Card Number</h2>
                        <p>•••• •••• •••• {String(creditCard.cardNumber).slice(-4)}</p>
                    </div>
                    <div className="numericalDataSection">
                        <h2>Points Balance</h2>
                        <div className="value">
                            <p>{creditCard?.pointsBalance ? creditCard.pointsBalance : '0'}</p> <span>Points</span>
                        </div>
                    </div>
                </div>

                <form className="redeemPointsForm" onSubmit={handleRedeem}>
                    <div className="field">
                        <label htmlFor="voucherDenomination">Select Voucher Denomination</label>
                        <select
                            className="globalInput"
                            id="voucherDenomination"
                            name="voucherDenomination"
                            value={selectedVoucherDenomination}
                            onChange={(e) => {
                                console.log('Selected Voucher Denomination:', e.target.value);
                                setSelectedVoucherDenomination(e.target.value);
                            }}
                            required
                        >
                            <option value={defaultOption}>
                                {defaultOption}
                            </option>
                            {voucherDenominations.map((denomination) => (
                                <option key={denomination.value} value={denomination.value}>
                                    ${denomination.value} voucher for {denomination.points} points
                                </option>
                            ))}
                        </select>
                    </div>
                    <div className="imageContainer">
                        <img src="/mbsvoucher.jpg" style={{ width: '400px', filter: 'drop-shadow(0px 0px 3px #000000)', margin: '10px' }} alt="Voucher" />
                    </div>
                    <div className="field">
                        <label htmlFor="milesDenomination">Select Miles Denomination</label>
                        <select
                            className="globalInput"
                            id="milesDenomination"
                            name="milesDenomination"
                            value={selectedMilesDenomination}
                            onChange={(e) => setSelectedMilesDenomination(e.target.value)}
                            required
                        >
                            <option value={defaultOption}>
                                {defaultOption}
                            </option>
                            {milesDenominations.map((denomination) => (
                                <option key={denomination.value} value={denomination.miles}>
                                    {denomination.miles} miles for {denomination.points} points
                                </option>
                            ))}
                        </select>
                    </div>
                    <div className="imageContainer">
                        <img src='/redeemPoints.jpg' style={{ width: '400px', filter: 'drop-shadow(0px 0px 3px #000000)', margin: '10px' }} />
                    </div>
                    {errorMsg && <div className="error-message">{errorMsg}</div>}
                    <button type="submit" className="universalButton">Redeem</button>
                </form>

                <div id="myModal" class="modal">
                    <div class="modal-content">
                        <span class="close">&times;</span>
                        <p>Points back redeemed successfully.</p>
                    </div>
                </div>
            </main>
        </div>
    );
}

export default RedeemPointsPage;
import '../../styles/RewardsHomePage.css';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { useAPI } from '../../contexts/APIContext';
import Navbar from '../../components/Navbar';
import { Link, useNavigate } from 'react-router-dom';

const RewardsHomePage = () => {

    const params = useParams()
    const { userId } = useAuth();
    const { getRebates, rebates, getPoints, points, getCreditCard, creditCard } = useAPI()
    // const { getCreditCard, creditCard, redeemRebates } = useAPI()
    const [totalRebates, setTotalRebates] = useState(0);
    const [totalPoints, setTotalPoints] = useState(0);
    const [errorMsg, setErrorMsg] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                if (userId == null) {
                    console.log('Not logged in, redirecting to login page')
                    navigate('/login');
                    return;
                }
                setErrorMsg('');
                
                console.log(params.cardId);
              
                await getCreditCard(params.cardId);

            } catch (error) {
                setErrorMsg(error.message || 'Error fetching data');
            }
        };

        fetchData();

       
    }, [])



    return (
        <div className="rewardsHomePage">
            <Navbar />
            <div className="banner">
                <img
                    src='/logo512.png'
                    style={{ width: '150px' }}
                />
                <span> Welcome To A World Of Rewards </span>
                <img
                    src='/logo512.png'
                    style={{ width: '150px' }}
                />

            </div>

            <div className="balancesContainer">
                <h3>Cash Back and Points Balance</h3>
                <div className="rebatesBalance">
                    <h4>Cash Back Balance</h4>
                    <div className="valueOfRebates">
                    <p>{creditCard?.rebatesBalance ? parseFloat(creditCard.rebatesBalance).toFixed(2) : '0.00'} SGD</p>
                        <Link to={`/redeem-rebates/${creditCard.cardId}`}>
                            <button className="universalButton" style={{width: '300px'}}>Redeem Cash Back</button>
                        </Link>
                        <Link to={`/rebates-history/${creditCard.cardId}`}>
                            <button className="universalButton" style={{width: '300px'}}>See Cash Back History</button>
                        </Link>
                    </div>
                </div>
                <div className="pointsBalance">
                    <h4>Points Balance</h4>
                    <div className="valueOfPoints">
                    <p>{creditCard?.pointsBalance ? creditCard.pointsBalance : '0'} points</p>
                        <Link to={`/redeem-points/${creditCard.cardId}`}>
                        <button className="universalButton" style={{width: '300px'}}>Redeem Points</button>
                        </Link>
                        <Link to={`/points-history/${creditCard.cardId}`}>
                            <button className="universalButton" style={{width: '300px'}}>See Points History</button>
                        </Link>


                    </div>
                </div>
                <div className="mainSection">
                    <div className="top">

                        <h2>Get Rewarded For Every Dollar Spent</h2>
                        <div className="imageContainer">
                            <img
                                src='/rewardsdining.jpg'
                                style={{ width: '400px' }}
                            />
                            <img
                                src='/rewardsshop.jpeg'
                                style={{ width: '400px' }}
                            />
                            <img
                                src='/rewardsfly.jpg'
                                style={{ width: '400px' }}
                            />
                        </div>
                        <p>
                            Enjoy a life of luxury with your most trusted credit card – The PennyWise Card. Unlock a portal to a world where every purchase is a step towards unabashed indulgence. <br></br>
                            From first-class flights to exclusive members-only events, let your existing card be the pass to a life which you want and deserve<br></br>
                            Revel in the art of treating yourself because, with this card, you're not just earning points and cash backs – you're curating a lifestyle that's uniquely yours. </p>
                    </div>
                    <h3>Earn Cash Back</h3>
                    <div className="earnSection">
                        <img
                            src='/rebates.jpg'
                            style={{ width: '400px' }}
                        />

                        <p>
                            Great Groceries Rewards:<br></br>
                            Earn 1% cash back on purchases from major grocery stores. <br></br>
                            Enjoy exclusive discounts on certain products.<br></br><br></br>

                            Dining Delights:<br></br>
                            Get 1.5% cash back on dining expenses at restaurants or for takeout orders. <br></br>
                            Access special discounts and promotions at partner eateries.<br></br><br></br> </p>

                    </div>
                    <h3>Earn Points</h3>
                    <div className="earnSection">
                        <img
                            src='/points.jpeg'
                            style={{ width: '400px' }}
                        />
                        <p>
                            Miles Magic: <br></br>
                            Earn points that can be converted into miles for your next great adventure with each banking transaction.<br></br>
                            Unlock special promotions for bonus points, accelerating your journey to exciting travel rewards.<br></br><br></br>
                            
                            Dining Points Delight:<br></br>
                            Accumulate points every time you dine out or order in, creating a feast of points for your palate.<br></br>
                            Exchange points for MBS vouchers so you can dine at your favorite restaurants for a delicious experience.<br></br><br></br>
                            
                            Retail Rewards Galore:<br></br>
                            Collect points for every retail escapade, from high-end shopping sprees to everyday purchases.<br></br>
                            Trade your points for MBS vouchers so that you can continue what you love most - retail theraphy.
                        </p>
                    </div>

  

                </div>
            </div>
        </div>
    )
}

export default RewardsHomePage;
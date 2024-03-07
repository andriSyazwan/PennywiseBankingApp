import Navbar from '../../components/Navbar'
import OverviewCard from '../../components/OverviewCard'
import { useAuth } from '../../contexts/AuthContext';
import { useAPI } from '../../contexts/APIContext';
import { useState, useEffect } from "react";
import { Link, useNavigate } from 'react-router-dom';

const CardsPage = () => {
    const [errorMsg, setErrorMsg] = useState([]);
    const { userId, login } = useAuth()
    const { getCreditCards, creditCards } = useAPI()
    const navigate = useNavigate();

    useEffect(() => {
        if (userId == null) {
            console.log('Not logged in, redirecting to login page')
            navigate('/login')
        }
        setErrorMsg('');

        getCreditCards(userId);
    }, [])

    return (
        <div>
            <Navbar />

            <div>{errorMsg}</div>
            <div className="dashboardContentContainer">
                <div id="heading">Cards</div>
                {creditCards.length === 0 ? (
                    <p style={{ color: 'white' }}>No credit cards found</p>
                ) : (
                    creditCards.map(creditCard =>
                        <OverviewCard cardType='creditCard' account={creditCard} />
                    )
                )}
            </div>
        </div>
    )
}

export default CardsPage;
import Navbar from '../../components/Navbar';
import RewardsCard from '../../components/RewardsCard';
import { useAuth } from '../../contexts/AuthContext';
import { useAPI } from '../../contexts/APIContext';
import { useState, useEffect } from "react";
import { useNavigate, useParams } from 'react-router-dom';

const RebatesHistory = () => {
    const { cardId } = useParams();
    const [errorMsg, setErrorMsg] = useState([]);
    const { userId } = useAuth();
    const { getRebates, rebates, getCreditCard, creditCard } = useAPI();
    const navigate = useNavigate();
    const [currentPage, setCurrentPage] = useState(1);
    const rebatesPerPage = 10; // You can adjust this based on your preference

    useEffect(() => {
        setErrorMsg('');
        if (userId == null){
            console.log('Not logged in, redirecting to login page')
            navigate('/login')
        }

        getCreditCard(cardId);
        getRebates(cardId);    
    },[]);

    // Calculate the index range for the current page
    const indexOfLastRebate = currentPage * rebatesPerPage;
    const indexOfFirstRebate = indexOfLastRebate - rebatesPerPage;
    const currentRebates = rebates.slice(indexOfFirstRebate, indexOfLastRebate);

    // Function to handle page change
    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    return (
        <div>
            <Navbar />

            <div className='dashboardContentContainer'>
                <div style={{display: 'flex'}}>
                    <div id="heading" style={{flex: 1}}>Rebates History</div>
                    <div id="subheading" style={{flex: 1}}>
                        <div id="small">CARD NUMBER</div>
                        •••• •••• •••• {String(creditCard.cardNumber).slice(-4)}
                    </div>
                </div>
                <div>
                    <div className="tableHeaderContainer">
                        <div className="heading">Transaction</div>
                        <div className="heading">Date</div>
                        <div className="heading">Amount</div>
                    </div>
                    {currentRebates.length === 0 ? (
                        <div style={{backgroundColor: 'white', padding: '20px'}}>No transactions found</div>
                    ) : (                       
                        currentRebates.map(rebate => (
                            <RewardsCard key={rebate.rebateId} reward={rebate} type='Cash Back'/>
                        ))
                    )}
                </div>

                {/* Pagination buttons */}
                <div>
                    {Array.from({ length: Math.ceil(rebates.length / rebatesPerPage) }).map((_, index) => (
                        <button
                            key={index}
                            onClick={() => handlePageChange(index + 1)}
                            onMouseEnter={(e) => (e.target.style.backgroundColor = 'rgb(246, 80, 80)')}
                            onMouseLeave={(e) => (e.target.style.backgroundColor = index + 1 === currentPage ? 'rgb(206, 46, 46)' : 'rgb(156, 100, 100)')}
                            className="universalButton"
                            style={{
                                backgroundColor: index + 1 === currentPage ? 'rgb(206, 46, 46)' : 'rgb(156, 100, 100)',
                                color: 'white',
                                cursor: 'pointer',
                                borderRadius: '5px',
                                width: '30px',
                            }}
                        >
                            {index + 1}
                        </button>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default RebatesHistory;

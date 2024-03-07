import Navbar from '../../components/Navbar';
import RewardsCard from '../../components/RewardsCard';
import { useAuth } from '../../contexts/AuthContext';
import { useAPI } from '../../contexts/APIContext';
import { useState, useEffect } from "react";
import { useNavigate, useParams } from 'react-router-dom';

const PointsHistory = () => {
    const params = useParams();
    const [errorMsg, setErrorMsg] = useState([]);
    const { userId } = useAuth();
    const { getPoints, points, getCreditCard, creditCard } = useAPI();
    const navigate = useNavigate();
    const [currentPage, setCurrentPage] = useState(1);
    const pointsPerPage = 9;

    useEffect(() => {
        setErrorMsg('');
        if (userId == null){
            console.log('Not logged in, redirecting to login page')
            navigate('/login')
        }
        
        getCreditCard(params.cardId);  
        getPoints(params.cardId);    
    },[]);

    // Calculate the index range for the current page
    const indexOfLastPoint = currentPage * pointsPerPage;
    const indexOfFirstPoint = indexOfLastPoint - pointsPerPage;
    const currentPoints = points.slice(indexOfFirstPoint, indexOfLastPoint);

    // Function to handle page change
    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    return (
        <div>
            <Navbar />

            <div className='dashboardContentContainer'>
                <div style={{display: 'flex'}}>
                    <div id="heading" style={{flex: 1}}>Points History</div>
                    <div id="subheading" style={{flex: 1}}>
                        <div id="small">CARD NUMBER</div>
                        •••• •••• •••• {String(creditCard.cardNumber).slice(-4)}
                    </div>
                </div>
                <div>
                    <div className="tableHeaderContainer">
                        <div className="heading">Transaction</div>
                        <div className="heading">Date</div>
                        <div className="heading">Points</div>
                    </div>
                    {currentPoints.length === 0 ? (
                        <div style={{backgroundColor: 'white', padding: '20px'}}>No transactions found</div>
                    ) : (                       
                        currentPoints.map(point => (
                            <RewardsCard key={point.pointId} reward={point} type='Points'/>
                        ))
                    )}
                </div>

                {/* Pagination buttons */}
                <div>
                    {Array.from({ length: Math.ceil(points.length / pointsPerPage) }).map((_, index) => (
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

export default PointsHistory;

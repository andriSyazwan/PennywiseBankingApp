import Navbar from '../../components/Navbar'
import InstalmentsCard from '../../components/InstalmentsCard';
import DetailedTransactionCard from '../../components/DetailedTransactionCard'
import { useAuth } from '../../contexts/AuthContext';
import { useAPI } from '../../contexts/APIContext';
import { useState, useEffect } from "react";
import { Link, useNavigate, useParams } from 'react-router-dom';

const InstalmentsHistory = () => {

    const { cardId } = useParams()
    const { userId } = useAuth()
    const { getCreditCard, creditCard, getInstalments, instalments, getTransactionsForCard, transactions } = useAPI()
    const [errorMsg, setErrorMsg] = useState([]);
    const navigate = useNavigate();
    const [sortOption, setSortOption] = useState('date'); // 'date' or 'type'
    const [currentPage, setCurrentPage] = useState(1);
    const transactionsPerPage = 9;

    const instalmentTransactions = transactions.filter(transaction => transaction.transactionType === 'Purchase_With_Instalment');
    
    useEffect(() => {
        setErrorMsg('');
        if (userId == null) {
            console.log('Not logged in, redirecting to login page')
            navigate('/login')
        }

        getCreditCard(cardId);
        getInstalments(cardId);
        getTransactionsForCard(cardId);
    }, [])

      // Calculate the index range for the current page
  const indexOfLastTransaction = currentPage * transactionsPerPage;
  const indexOfFirstTransaction = indexOfLastTransaction - transactionsPerPage;
  const currentTransactions = transactions.slice(indexOfFirstTransaction, indexOfLastTransaction);

  // Function to handle page change
  const handlePageChange = (pageNumber) => {
    if (pageNumber !== '...') {
      setCurrentPage(pageNumber);
    }
  };

  // Function to generate an array of page numbers for display
  const generatePageNumbers = () => {
    const totalPageCount = Math.ceil(instalmentTransactions.length / transactionsPerPage);
    const maxDisplayedPages = 5; // Adjust this based on your preference
  
    const currentPageIndex = currentPage - 1;
    let startPage = Math.max(0, currentPageIndex - Math.floor(maxDisplayedPages / 2));
    let endPage = Math.min(totalPageCount - 1, startPage + maxDisplayedPages - 1);
  
    if (endPage - startPage < maxDisplayedPages - 1) {
      startPage = Math.max(0, endPage - maxDisplayedPages + 1);
    }
  
    const pages = Array.from({ length: endPage - startPage + 1 }, (_, index) => startPage + index + 1);
  
    // Add ellipsis at the beginning if needed
    if (startPage > 0) {
      pages.unshift('...');
    }
  
    // Add ellipsis at the end if needed
    if (endPage < totalPageCount - 1) {
      pages.push('...');
    }
  
    return pages;
    };

    return (
        <div>
            <Navbar />

            <div className='dashboardContentContainer' style={{ overflow: 'auto' }}>
                <div style={{ display: 'flex' }}>
                    <div id="heading" style={{ flex: 1 }}>Instalments</div>
                    <div id="subheading" style={{ flex: 1 }}>
                        <div id="small">CARD NUMBER</div>
                        •••• •••• •••• {String(creditCard.cardNumber).slice(-4)}
                    </div>
                </div>
                {instalments.length === 0 ? (
                    <p style={{ color: 'white' }}>No instalments found</p>
                ) : (
                    instalments.map(instalment => (
                        <InstalmentsCard key={instalment.instalmentId} instalment={instalment} />
                    ))
                )}
            </div>

            <div className='dashboardContentContainer'>
                <div style={{ display: 'flex' }}>
                    <div id="heading" style={{ flex: 2 }}>Transaction History</div>
                </div>
                <div>
                    <div className="tableHeaderContainer">
                        <div className="heading">From</div>
                        <div className="heading">To</div>
                        <div className="heading">Type</div>
                        <div className="heading">Date</div>
                        <div className="heading">Amount</div>
                    </div>
                    {instalmentTransactions.length === 0 ? (
                        <div style={{ backgroundColor: 'white', padding: '20px' }}>No instalment transactions found</div>
                    ) : (
                        instalmentTransactions.map(transaction => (
                            <DetailedTransactionCard key={transaction.transactionId} transaction={transaction} />
                        ))
                    )}
                </div>
                    {/* Pagination buttons */}
                <div>
                    {generatePageNumbers().map((pageNumber, index) => (
                    <button
                    key={index}
                    onClick={() => handlePageChange(pageNumber)}
                    onMouseEnter={(e) => e.target.style.backgroundColor =  'rgb(246, 80, 80)'}
                    onMouseLeave={(e) => e.target.style.backgroundColor = pageNumber === currentPage ? 'rgb(206, 46, 46)' : 'rgb(156, 100, 100)'}
                    className="universalButton"
                    style={{
                        backgroundColor: pageNumber === currentPage ? 'rgb(206, 46, 46)' : 'rgb(156, 100, 100)',
                        color: 'white',
                        cursor: pageNumber === '...' ? 'default' : 'pointer',
                        borderRadius: '5px',
                        width: '30px',
                    }}
                    >
                    {pageNumber}
                    </button>
                    ))}
                </div>
            </div>

            

        </div>
    )
}

export default InstalmentsHistory;
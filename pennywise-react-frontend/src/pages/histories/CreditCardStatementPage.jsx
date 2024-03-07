import Navbar from '../../components/Navbar'
import { useAuth } from '../../contexts/AuthContext';
import { useAPI } from '../../contexts/APIContext';
import { useState, useEffect } from "react";
import { Link, useNavigate } from 'react-router-dom';
import { useParams } from 'react-router-dom';
import DetailedTransactionCard from '../../components/DetailedTransactionCard'
import '../../styles/CreditCardStatementPage.css';

const CreditCardStatementPage = () => {
    const [errorMsg, setErrorMsg] = useState([]);
    const { userId } = useAuth()
    const navigate = useNavigate();
    const { cardId, targetDateParam } = useParams();
    const targetDate = new Date(targetDateParam)
    const { getTransactionsForCardByMonth, transactions, bill, getBillFromMonth } = useAPI();
    const [billData, setBillData] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const transactionsPerPage = 9;

    useEffect(() => {
        setErrorMsg('');
        if (userId == null) {
            console.log('Not logged in, redirecting to login page')
            navigate('/login')
        }

        const [year, month] = targetDateParam.split('-');

        getTransactionsForCardByMonth(cardId, month, year);
        getBillFromMonth(cardId, month, year)
            .then((billData) => {
                console.log('this is the bill from api:', billData);
                setBillData(billData);
            })
            .catch((error) => {
                console.error('Error fetching bill:', error);
            });

    }, []);

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
        const totalPageCount = Math.ceil(transactions.length / transactionsPerPage);
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
            <div className="dashboardContentContainer">
                <div id="heading">Bill Details</div>
                <div>
                    <div className="tableHeaderContainer">
                        <div className="heading">Bill ID</div>
                        <div className="heading">Due Date</div>
                        <div className="heading">Total Bill</div>
                    </div>
                </div>
                <div className="billcard">
                    {billData ? (
                        <div style={{ backgroundColor: 'white' }}>
                            <div className="heading" style={{ paddingTop: '10px', paddingLeft: '15px' }}>
                                {billData.billId}
                            </div>
                            <div className="heading" style={{ paddingLeft: '110px' }}>
                                <span className="label">{new Date(billData.dueDate).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' })}</span><br />
                                <span>{new Date(billData.dueDate).toLocaleDateString([], { year: 'numeric', month: 'long', day: 'numeric' })}</span>
                            </div>
                            <div className="heading" style={{ paddingLeft: '85px' }}>
                                <div>
                                    <span className="label">SGD</span><br />
                                    {billData.billAmount.toFixed(2)}
                                </div>
                            </div>
                        </div>
                    ) : (
                        <div style={{ backgroundColor: 'white', padding: '20px' }}>
                            No bill found
                        </div>
                    )}
                </div>
            </div>
            <div className="dashboardContentContainer">
                <div id="heading">Statement Transaction History</div>
                <div>
                    <div className="tableHeaderContainer">
                        <div className="heading">From</div>
                        <div className="heading">To</div>
                        <div className="heading">Type</div>
                        <div className="heading">Date</div>
                        <div className="heading">Amount</div>
                    </div>
                </div>
                <div>
                    {transactions.length === 0 ? (
                        <div style={{ backgroundColor: 'white', padding: '20px' }}>No transactions found</div>
                    ) : (
                        transactions.map(transaction => (
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
                            onMouseEnter={(e) => e.target.style.backgroundColor = 'rgb(246, 80, 80)'}
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

export default CreditCardStatementPage;
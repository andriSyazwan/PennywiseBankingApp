import Navbar from '../../components/Navbar'
import { useParams } from 'react-router-dom';
import { Link, useNavigate } from 'react-router-dom';
import '../../styles/CreditCardViewAllPage.css';

const CreditCardViewAllPage = () => {

    const params = useParams()
    const monthNames = [
        'January', 'February', 'March', 'April', 'May', 'June',
        'July', 'August', 'September', 'October', 'November', 'December'
    ];

    function formatDateForUrl(date) {
        return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}`;
    }

    
    return (
        <div>
            <Navbar/>
            <div className="dashboardContentContainer">                 
                <div id = "heading">Statements In The Last 12 Months</div>   
                <div className="statementsContainerforViewAllPage">
                {
                    [...Array(12)].map((_, index) => {
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
        </div>
    )
}

export default CreditCardViewAllPage;
import '../styles/OverviewCard.css'
import { Link } from 'react-router-dom';

const OverviewCard = (props) => {

    const { account, cardType } = props;

    const getCurrentMonthLastDay = () => {
        const currentDate = new Date();
        const currentYear = currentDate.getFullYear();
        const currentMonth = currentDate.getMonth() + 1;

        const lastDay = new Date(currentYear, currentMonth, 0).getDate();

        const formattedDate = ` ${lastDay < 10 ? '0' + lastDay : lastDay} /
                                ${currentMonth < 10 ? '0' + currentMonth : currentMonth} /
                                ${currentYear}`;

        return formattedDate;
    }
    return (
        <div>
            <div className="overviewCardContainer">
                {
                cardType == 'bankAccount' ?                    
                    <Link to={`/accounts/${account.accountId}`}>
                        <div className="cardContainer">
                            <div className = "accountNumber">
                                <span id = "title">ACCOUNT NUMBER</span> {account.accountNumber}<br/>
                            </div>
                            <div className = "accountType">
                                {account.accountType}
                            </div>
                            <div className = "balance">
                                SGD<br/>
                                <span id = "amount">{account.balance}</span>
                            </div>
                        </div>
                    </Link>
                    :
                    <Link to={`/creditcards/${account.cardId}`}>
                    <div className="cardContainer">
                        <div className = "accountNumber">
                            <span id = "title">CARD NUMBER</span> •••• •••• •••• {String(account.cardNumber).slice(-4)}<br/>
                        </div>
                        <div className = "accountType">
                            <span id = "title">DUE:</span> {getCurrentMonthLastDay()}
                        </div>
                        <div className = "balance">
                            <span id = "title">AVAILABLE BALANCE</span><br/>
                            SGD<br/>
                            <span id = "amount">{account.formattedAvailableBalance}</span>
                        </div>
                    </div>
                    </Link>
                }
            </div>
        </div>
    )

}

export default OverviewCard;

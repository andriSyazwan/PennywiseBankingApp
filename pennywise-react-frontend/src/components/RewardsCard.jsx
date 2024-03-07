import '../styles/DetailedTransactionCard.css';

const RewardsCard = (props) => {

    const { date, amount, creditCard, transaction } = props.reward;
    const { type } = props;
    const originalDate = new Date(date)
    const formattedTime = originalDate.toLocaleString("en-US", {
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
    })

    const formattedDate = originalDate.toLocaleString("en-US", {
        year: "numeric",
        month: "long",
        day: "numeric",
    })

    const renderFrom = () => {                 
        switch (true) {
            case (amount < 0):
                return (
                    <span>
                        REDEMPTION
                    </span>
                );
            case (amount > 0):
                return (
                    <span>
                       CREDIT CARD PURCHASE
                    </span>
                );
            default:
                return null;
        }
    }

    const renderAmount = () => {                 
        switch (type) {
            case 'Cash Back':
                return (                    
                    <div>
                        <span className = "label">SGD</span><br/>
                        {Number(amount).toFixed(2)}
                    </div>
                );
            case 'Points':
                return (
                    <div>
                        {Number(amount).toFixed(0)}
                    </div>
                );
            default:
                return null;
        }
    }

    return (
        <div className="detailedTransactionCard">
            <div className="heading">{
                renderFrom()
            }</div>
            <div className="heading">                
            <span className = "label">{formattedTime}</span><br/>
                <span>{formattedDate}</span></div>
            <div className="heading">{
                renderAmount()
            }</div>
        </div>
    )
}

export default RewardsCard;
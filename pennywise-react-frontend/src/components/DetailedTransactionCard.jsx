import '../styles/DetailedTransactionCard.css';

const DetailedTransactionCardForTransactionsPage = (props) => {

    console.log(props)
    const { transactionId, transactionType, date, amount, type, bankAccount, merchant, sourceAccount, destinationAccount, creditCard } = props.transaction
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
        switch (transactionType) {
            case 'Purchase_With_Instalment':
            case 'Purchase':
                return (
                    <span>
                        <span className="label">CARD NUMBER</span><br />
                        •••• •••• •••• {String(creditCard.cardNumber).slice(-4)}
                    </span>
                );
            case 'Credit_Card_Bill_Payment':
            case 'Transfer':
                return (
                    <span>
                        <span className="label">ACCOUNT NUMBER</span><br />
                        {sourceAccount.accountNumber}
                    </span>
                );
            case 'Credit_Card_Late_Fee':
            case 'Credit_Card_Interest':
            case 'Credit_Card_Balance_Brought_Forward':
            case 'Cashback':
            case 'Deposit':
                return (
                    <span>NA</span>
                );
            case 'Withdraw':
                return (
                    <span>
                        <span className="label">ACCOUNT NUMBER</span><br />
                        {bankAccount.accountNumber}
                    </span>
                );
            default:
                return null;
        }
    }

    const renderTo = () => {
        switch (transactionType) {
            case 'Purchase_With_Instalment':
            case 'Purchase':
                return (
                    <span>
                        <span className="label">MERCHANT</span><br />
                        {merchant.name}
                    </span>
                );
            case 'Transfer':
                return (
                    <span>
                        <span className="label">ACCOUNT NUMBER</span><br />
                        {destinationAccount.accountNumber}
                    </span>
                );
            case 'Deposit':
                return (
                    <span>
                        <span className="label">ACCOUNT NUMBER</span><br />
                        {bankAccount.accountNumber}
                    </span>
                );
            case 'Withdraw':
                return (
                    <span>NA</span>
                );
            case 'Credit_Card_Late_Fee':
            case 'Credit_Card_Interest':
            case 'Credit_Card_Balance_Brought_Forward':
            case 'Credit_Card_Bill_Payment':
            case 'Cashback':
                return (
                    <span>
                        <span className="label">CARD NUMBER</span><br />
                        •••• •••• •••• {String(creditCard.cardNumber).slice(-4)}
                    </span>
                );
            default:
                return null;
        }
    }

    const renderType = () => {
        switch (transactionType) {
            case 'Purchase_With_Instalment':
                return 'Instalment';
            case 'Purchase':
                return 'Purchase';
            case 'Transfer':
                return 'Transfer';
            case 'Deposit':
                return 'Deposit';
            case 'Withdraw':
                return 'Withdraw';
            case 'Credit_Card_Bill_Payment':
                return 'Bill Payment';
            case 'Credit_Card_Late_Fee':
                return 'Credit Card Late Fee';
            case 'Credit_Card_Interest':
                return 'Credit Card Interest';
            case 'Credit_Card_Balance_Brought_Forward':
                return 'Credit Card Balance Brought Forward';
            case 'Cashback':
                return 'Cashback';
            default:
                return null;
        }
    }

    return (
        <div className="detailedTransactionCard">
            <div className="heading">{
                renderFrom()
            }</div>
            <div className="heading">  {
                renderTo()
            }</div>
            <div className="heading">{
                renderType()
            }</div>
            <div className="heading">
                <span className="label">{formattedTime}</span><br />
                <span>{formattedDate}</span></div>
            <div className="heading">
                <div><span className="label">SGD</span><br />
                    {Number(amount).toFixed(2)}</div>
            </div>
        </div>
    )
}

export default DetailedTransactionCardForTransactionsPage;

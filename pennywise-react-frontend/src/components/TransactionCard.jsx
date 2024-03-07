import '../styles/TransactionCard.css'

const TransactionCard = (props) => {

    const { transactionType, merchant, amount, type, destinationAccount, sourceAccount, bankAccount, creditCard } = props.transaction
    const parentPage = props.parentPage

    console.log(props.transaction)

    const renderFromTo = () => {
        switch (transactionType) {
            case 'Purchase':
                return;
            case 'Transfer':
                return (
                    <div>
                        <div> FROM <br />
                            <span className="transferfrom">{sourceAccount.accountNumber}</span>
                        </div>
                        <div> TO <br />
                            <span className="transferto">{destinationAccount.accountNumber}</span>
                        </div>
                    </div>
                );
            case 'Deposit':
                return (
                    <div>
                        TO <br />
                        <span className="deposit">{bankAccount.accountNumber}</span>
                    </div>
                );
            case 'Withdraw':
                return (
                    <div>
                        FROM <br />
                        <span className="withdrawal">{bankAccount.accountNumber}</span>
                    </div>
                );
            case 'Credit_Card_Bill_Payment':
                return parentPage === 'CreditCardPage' ? (
                    <div>
                        FROM <br />
                        <span className="deposit">{sourceAccount.accountNumber}</span>
                    </div>
                ) : (
                    <div>
                        TO <br />
                        <span className="withdrawal">•••• •••• •••• {String(creditCard.cardNumber).slice(-4)}</span>
                    </div>
                );
            default:
                return null;
        }
    }

    const renderType = () => {
        switch (transactionType) {
            case 'Purchase_With_Instalment':
                return (<span><span>{merchant.name}</span> - Instalment</span>);
            case 'Transfer':
                return 'Transfer';
            case 'Deposit':
                return 'Deposit';
            case 'Withdraw':
                return 'Withdraw';
            case 'Credit_Card_Bill_Payment':
                return 'Bill Payment';
            case 'Credit_Card_Late_fee':
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
        <div className="transactionCardContainer">
            <div className="infoContainer">
                <span className="transactionTitle">
                    {
                        transactionType === 'Purchase' ?
                            merchant.name :
                            renderType()}</span><br />
                <span>{type}</span>
            </div>
            <div className="accountContainer">
                {renderFromTo()}
            </div>

            <div className="amountContainer">
                {amount >= 0 ?
                    <div>SGD<br />
                        <span className="amountText" style={{ color: 'darkgreen' }}>{Number(amount).toFixed(2)}</span></div> :
                    <div>SGD<br />
                        <span className="amountText" style={{ color: 'darkred' }}>{Number(amount).toFixed(2)}</span></div>
                }
            </div>
        </div>

    )

}

export default TransactionCard;
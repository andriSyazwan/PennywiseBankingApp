import '../styles/CreditCard.css'

const CreditCard = (props) => {

    const { cardNumber, expiryDate, displayName } = props
    const originalDate = new Date(expiryDate)
    const formattedDate = originalDate.toLocaleString("en-US", {
        month: "2-digit",
        year: "2-digit"
    })

    return (
        <div className="creditCardVisualSection">
            <img className="bankName" src='/logofull.png' />
            <p className="cardNumber">•••• •••• •••• {String(cardNumber).slice(-4)}</p>
            <div className="expiryDate">
                <p className="validThru">VALID THRU</p>
                <p className="expiryDateValue">{formattedDate}</p>
            </div>
            <div className="bottomRow">
                <p className="displayName">{displayName}</p>
                <img className="logo" src='/mastercard-logo.png' />
            </div>
        </div>
    )
}

export default CreditCard;
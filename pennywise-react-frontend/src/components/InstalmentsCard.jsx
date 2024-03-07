import '../styles/InstalmentsCard.css'

const InstalmentsCard = (props) => {

    const { instalmentId, merchant, totalAmount, monthlyAmount, date, isPaid, progressTracker, instalmentPlan } = props.instalment;
    const originalDate = new Date(date);
    const endDate = new Date(originalDate.getFullYear(), originalDate.getMonth() + instalmentPlan.noOfInstalments, originalDate.getDate());

    const formattedDate = originalDate.toLocaleString("en-US", {
        year: "numeric",
        month: "long",
        day: "numeric",
    });

    const formattedEndDate = endDate.toLocaleString("en-US", {
        year: "numeric",
        month: "long",
        day: "numeric",
    });

    const ProgressBalls = ( progressTracker, noOfInstalments ) =>  {
        const balls = [];
        for (let i = 0; i < noOfInstalments; i++) {
            let ballColor = '';
            let title = '';

            if (i < noOfInstalments - progressTracker) {
                if (i === noOfInstalments - progressTracker - 1) {
                    ballColor = 'rgb(202, 123, 19)';
                    title = 'Unpaid';
                } else {
                    ballColor = 'rgb(17, 153, 28)';
                    title = 'Paid';
                }
            } else {
                ballColor = 'rgb(206, 46, 46)';
                title = 'Pending';
            }

            balls.push(
                <div
                    key={i}
                    style={{
                        display: 'inline-block',
                        width: '20px',
                        height: '20px',
                        borderRadius: '50%',
                        backgroundColor: ballColor,
                        margin: '10px 5px 0px',
                    }}
                    title={title} 
                ></div>
            );
        }

        return <div>{balls}</div>;
    }


    return (
        <div>
            <div className="instalmentCardContainer">
                <div className="instalmentCard">
                    <div style={{display: 'flex'}}>
                        <div style={{flex: '1'}}>
                            <div className="instalmentDate">
                                <span id="title">Start Date</span> {formattedDate}
                            </div>
                            <div className="instalmentDate">
                                <span id="title">End Date</span> {formattedEndDate}
                            </div>
                            <div className="merchant">
                                {merchant.name}
                            </div>
                        </div>
                        <div style={{flex: '1'}}>
                            <div className="amount">
                                <span id="title">Total w/ interest</span> {totalAmount.toFixed(2)} SGD
                            </div>
                            <div className="amount">
                                <span id="title">Payable per Month</span> {monthlyAmount.toFixed(2)} SGD
                            </div>
                            <div className="interestRate">
                                <span id="title">Interest Rate</span> {instalmentPlan.interestRate}%
                            </div>
                        </div>
                    </div>
                    <div style={{display: 'flex'}}>
                        {ProgressBalls(progressTracker, instalmentPlan.noOfInstalments)}
                    </div>
                </div>
            </div>
        </div>
    )
}

export default InstalmentsCard;
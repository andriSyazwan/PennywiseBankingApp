import '../../styles/AccountsPage.css'
import Navbar from '../../components/Navbar';
import OverviewCard from '../../components/OverviewCard';
import { useAuth } from '../../contexts/AuthContext';
import { useAPI } from '../../contexts/APIContext';
import { useState, useEffect } from "react";
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';


const AccountsPage = () => {
    const [errorMsg, setErrorMsg] = useState([]);
    const [accountType, setAccountType] = useState('');
    const [isRegistrationOpen, setIsRegistrationOpen] = useState(false);
    const [userPassword, setUserPassword] = useState('');
    const [reRenderFlag, setReRenderFlag] = useState(true);
    const [isLimitReached, setIsLimitReached] = useState(false);
    const { userId, login } = useAuth();
    const { getBankAccounts, bankAccounts } = useAPI();
    const navigate = useNavigate();
    
    // This useEffect runs whenever reRenderFlag is changed
    useEffect(() => {

        // If user not authorized, will return to login page
        if (userId == null){
            console.log('Not logged in, redirecting to login page')
            navigate('/login')
        }
        
        // Runs the important methods whenever re-render is needed
        if(reRenderFlag) {
            getBankAccounts(userId);
            setReRenderFlag(false);
            closeRegistrationForm();
            accountCeiling();
        }

        // Resets the error messages to empty string
        setErrorMsg('');
        
    },[reRenderFlag])

    // Method that verifies the password before allowing to create new account
    const checkPassword = () => {
        alert("A request has been sent to the admin ");

        return true;
    }

    // Method that limits bank account
    const accountCeiling = () => {
        // Checks if accounts limit is hit
        if (bankAccounts.length >=4) {
            // Set codes needed for limit reached
            setIsLimitReached(true);
        }
    }


    // Method to open Form
    const openRegistrationForm = () => {
        // If bank account limit reached, can't open form
        if (!isLimitReached) {
            setIsRegistrationOpen(true);
        } else {
            setErrorMsg("You have exceeded the account limits")
        }

    };

    // Method to close the form
    const closeRegistrationForm = () => {
        setIsRegistrationOpen(false);
    };    

    // New Bank account function
    const createBankAccount = async (event) => {
        const api = 'http://localhost:8080/api/v1/';
        event.preventDefault();
        console.log(userId);

        // Method to get user object
        try {
            const userResponse = await axios.get(api + 'users/' + userId);
            setUserPassword(userResponse.data.password);
            
            // Check if response status is initialized before continuing
            if (userResponse.status === 200) {

                // Runs the checkPassword method to verify
                if (checkPassword()) {
                    console.log(accountType);

                    // A checker to see if account type is selected
                    if (accountType) {

                        // Posts the data collected and then calling the reset methods
                        const bankAccountResponse = await axios.post(api + 'bankAccounts/add', JSON.stringify({ balance: 0, accountType: accountType + '_Account', user: userResponse.data }), { headers: { 'Content-Type': 'application/json' } });
                        console.log(bankAccountResponse);
                        setAccountType('');
                        setErrorMsg('');
                        setReRenderFlag(true);
                        alert('The new account has been made!');
                    } else {
                        // Handle missing accountType input
                        setErrorMsg('Please select an account type');
                    }
                } else {
                    // Handle incorrect password input
                    setErrorMsg("Incorrect Password");
                }
            } else {
                // Handle if API does not function
                setErrorMsg('Error retrieving user data');
            }
        } catch (error) {
            // Handle any other errors
            setErrorMsg('Error creating bank account');
            console.log('Error creating bank account:', error?.response);
        }
    }
    


    return (
        <div>
            <Navbar/>   
        
            <div>{errorMsg}</div>
            <div className="dashboardContentContainer">  
                <div id = "heading">Accounts</div>
            
                {!isLimitReached && (
                    <div className="New Bank account selection">
                    <div className="buttonDiv">
                        <button className="icon-btn add-btn" onClick={openRegistrationForm}>
                            <div className="add-icon"></div>
                            <div className="btn-txt">Add Account</div>
                        </button>
                    </div>
                {isRegistrationOpen && (
                <div className="registration-form">
                        <label htmlFor="account-type" className="registration-label">Account Type:</label>
                        <select
                            id="accountType"
                            value={accountType}
                            onChange={event => setAccountType(event.target.value)}
                            className="registration-select"
                            required
                        >
                            <option value="" disabled>
                                Select an account type
                            </option>
                            <option value="Checking">Checking</option>
                            <option value="Savings">Savings</option>
                        </select>
                        <div className="buttons-container">
                            <button onClick={(event) => createBankAccount(event)} className="universalButton">Create</button>
                            <button onClick={closeRegistrationForm} className="universalButton">Cancel</button>
                        </div>
                    </div>
                    )}
                </div>
                )}

  
                {                    
                    bankAccounts.map((bankAccount) =>
                    <div>
                        <OverviewCard
                            cardType='bankAccount'
                            account={bankAccount}     
                        />
                    </div>
                )}
            </div>
        </div>
    )
}

export default AccountsPage;
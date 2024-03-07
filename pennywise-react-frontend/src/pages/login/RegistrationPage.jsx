import '../../styles/LoginPage.css';
import { useRef, useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

const RegistrationPage = () => {

    const userRef = useRef()
    const usernameRef = useRef();
    const emailRef = useRef();
    const passwordRef = useRef();
    const firstNameRef = useRef();
    const lastNameRef = useRef();
    const addressRef = useRef();
    const accountTypeRef = useRef();
    const api = 'http://localhost:8080/api/v1/'

    const [username, setUsername] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [address, setAddress] = useState('')
    const [accountType, setAccountType] = useState('')
    const [errMsg, setErrMsg] = useState('')
    const [success, setSuccess] = useState(false)
    const [usernameError, setUsernameError] = useState('')
    const [emailError, setEmailError] = useState('')

    // Clears error message the moment any of the below is changed
    useEffect(() => {
        setErrMsg('');
        checkUsername();
        checkEmail();
    }, [username, email])

    // Method to check if username already exists
    const checkUsername = async () => {

        // A request that checks if already exists
        await axios.get(api + 'users/username/' + username)
        .then(response => {
            setUsernameError("Username already exists!");
            console.log("Username Error")
        })
        .catch(error => {
            if(error?.response?.status === 404) {
                setUsernameError('');
            } else {
                console.log(error?.response);
            }
            
        })
    }

    // Method to check if email alreadt exists
    const checkEmail = async (event) => {

        // A request that checks if user alr exist from email
        await axios.get(api + 'users/email/' + email)
        .then(response => {
            setEmailError("Email already exists!");
            console.log("Email Error");
        })
        .catch(error => {
            if(error?.response?.status === 404) {
                setEmailError('');
            } else {
                console.log(error?.response);
            }
        })
    }

    // Method that handles the whole submit button
    const handleSubmit = async (event) => {
        // Prevents accidental refreshes in the process
        event.preventDefault()

        // If username and email don't already exist
        if(usernameError !== '' || emailError !== '') {
            // Set error message if submitted but not passed the requirements
            setErrMsg("Username or email already exists. Please choose a different one")
        } else { 
            // Upon successful entries

            // Adds the users using post
            await axios.post(api + 'users/add', JSON.stringify({ username: username, email: email, password: password, firstName: firstName, lastName: lastName, address: address }), { headers: { 'Content-Type': 'application/json' } })
            .then(response => {
                // Resets the useStates used once created success
                console.log(response)
                setEmail('')
                setPassword('')
                setFirstName('')
                setLastName('')
                setAddress('')
            })
            .catch(error => {
                // Supposedly catches the already taken error but does not work
                if (error?.response?.status === 409) {
                    setErrMsg("Username is already taken")
                    console.log("Username is already taken")
                    console.log(error?.response)
                } else {
                    setErrMsg('Error registering user')
                    console.log('Error registering user')
                    console.log(error?.response)
                }
            })

            // Gets the created user via username for adding bank acc
            const createdUser = await axios.get(api + 'users/username/' + username)
            .then(response => {
                console.log(response)
                setUsername('')
                return response.data
            })
            .catch(error => {
                setErrMsg('Error retrieving created user')
                console.log('Error retrieving created user')
            })

            // Adds the bank account into the system
            await axios.post(api + 'bankAccounts/add', JSON.stringify({ balance: 0, accountType: accountType + '_Account', user: createdUser }), { headers: { 'Content-Type': 'application/json' } })
            .then(response => {
                console.log(response)
                setAccountType('')
                setSuccess(true)
            })
            .catch(error => {
                setErrMsg('Error creating bank account')
                console.log('Error creating bank account')
                console.log(error?.response)
            })
        }
        
    }

    return (
        <div className="page">
            {success ? (
                <div className="successSection">
                    <div>
                        <div id="heading" style={{margin: '20px'}}>Registration successful!</div>
                        <Link to="/login"><button className="universalButton">Click here to Login</button></Link>
                    </div>
                </div>
            ) : (
                <div className="loginSection">
                    <div className="loginFormSection">
                        <form onSubmit={handleSubmit}>
                            <div id="heading" style={{margin: '20px'}}>Sign Up</div>
                            
                            <div className="field">
                                <label htmlFor='username'>Username</label>
                                <input
                                    type='text'
                                    className="globalInput"
                                    ref={usernameRef}
                                    value={username}
                                    onChange={event => {
                                        setUsername(event.target.value);
                                    }}
                                    required
                                />
                            </div>
                            {/* <div className="ErrorMessage">{usernameError}</div> */}
                            <div className="field">
                                <label htmlFor='email'>Email</label>
                                <input
                                    type='email'
                                    className="globalInput"
                                    ref={emailRef}
                                    value={email}
                                    onChange={event => {
                                        setEmail(event.target.value);
                                    }}
                                    required
                                />
                            </div>
                            {/* <div className="ErrorMessage">{emailError}</div> */}
                            <div className="field">
                                <label htmlFor='password'>Password</label>
                                <input
                                    type='password'
                                    className="globalInput"
                                    ref={passwordRef}
                                    value={password}
                                    onChange={event => setPassword(event.target.value)}
                                    required
                                />
                            </div>
                            <div className="field">
                                <label htmlFor='firstName'>First Name</label>
                                <input
                                    type='text'
                                    className="globalInput"
                                    ref={firstNameRef}
                                    value={firstName}
                                    onChange={event => setFirstName(event.target.value)}
                                    required
                                />
                            </div>
                            <div className="field">
                                <label htmlFor='lastName'>Last Name</label>
                                <input
                                    type='text'
                                    className="globalInput"
                                    ref={lastNameRef}
                                    value={lastName}
                                    onChange={event => setLastName(event.target.value)}
                                    required
                                />
                            </div>
                            <div className="field">
                                <label htmlFor='address'>Address</label>
                                <input
                                    type='text'
                                    className="globalInput"
                                    ref={addressRef}
                                    value={address}
                                    onChange={event => setAddress(event.target.value)}
                                    required
                                />
                            </div>
                            <div className="field">
                                <label htmlFor='accountType'>Account Type</label>
                                <select
                                    id="accountType"
                                    className="globalInput"
                                    value={accountType}
                                    ref={accountTypeRef}
                                    onChange={event => setAccountType(event.target.value)}
                                    required
                                >
                                    <option value="" disabled>
                                        Select an account type
                                    </option>
                                    <option value="Checking">Checking</option>
                                    <option value="Savings">Savings</option>
                                </select>
                            </div>
                            <div>{errMsg !== '' && <p className="ErrorMessage">{errMsg}</p>}</div>
                            <button className="universalButton">Submit</button>
                        </form>
                        <Link to="/login"><button className="universalButton">Back to Login</button></Link>
                    </div>
                </div>)}
        </div>

    )

}

export default RegistrationPage;

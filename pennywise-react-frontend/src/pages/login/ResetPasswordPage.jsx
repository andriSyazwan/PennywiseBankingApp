import '../../styles/ResetPasswordPage.css';
import { useRef, useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

const ResetPasswordPage = () => {
    // API Related imports
    const userRef = useRef()
    const api = 'http://localhost:8080/api/v1/'
    
    // Necessary constants and useStates
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [confirmPassword, setConfirmPassword] = useState('')
    const [email, setEmail] = useState('')
    const [success, setSuccess] = useState(false)
    const [userObj, setUserObj] = useState({})
    const [userId, setUserId] = useState()
    const [errMsg, setErrMsg] = useState('')
    const [usernameError, setUsernameError] = useState('')
    const [emailError, setEmailError] = useState('')
    const [passwordError, setPasswrodError] = useState('')

    useEffect(() => {
        setUsernameError('');
        usernameChecker();
    }, [username])


    // Crosscheck password
    const passwordChecker = () => {
        // Just compares between password and confirm password
        if(password === confirmPassword) {
            return true;
        }
        return false;
    }

    // Checks if username exist in database
    const usernameChecker = async () => {
        
        // A request that checks if user exists
        await axios.get(api + 'users/username/' + username)
        .then(response => {
            setUserObj(response.data);
            setUserId(response.data.userId);
            console.log(response.data.userId);
            console.log(response.data);
            setUsernameError('');
        })
        .catch(error => {
            if(error?.response?.status === 404) {
                setUsernameError('Please enter a valid username');
            } else {
                console.log(error?.response);
                setUsernameError(error?.response);
            }
            
        })

    }

    // Check if email is correct email in selected user
    const emailChecker = () => {
        // Retrieve the userObj.email
        if (userObj.email === email) {
            return true;
        }
        return false;
    }

    // Handle Submit request
    const handleSubmit = async (event) => {
        event.preventDefault();

        // Check if username valid
        if(usernameError === '') {
            // Check if email is from the user
            if(emailChecker()) {
                // Check if confirm passwords are true
                if(passwordChecker()) {
                    // Handle all met conditions
                    // Adds the bank account into the system
                    await axios.patch(api + 'users/' + userId, JSON.stringify({ password: password }), { headers: { 'Content-Type': 'application/json' } })
                    .then(response => {
                        console.log(response)
                        setErrMsg('')
                        alert("A request has been sent to " + email)
                        // Wait 3 seconds
                        setTimeout(() => {
                            setSuccess(true)
                            alert("Your password reset has been approved");
                          }, 3000);
                    })
                    .catch(error => {
                        setErrMsg('Error resetting password')
                        console.log('Error resetting password')
                        console.log(error?.response)
                        
                    })

                } else {
                    // Handle password mismatch
                    setErrMsg('The two passwords do not match. Please try again');
                }
            } else {
                // Handles invalid email
                setErrMsg('The email does not match the user that is found');
            }
        } else {
            // Handles username errors
            setErrMsg('The username is not found');
        }

    }

    return (
<div className="page">
            {success ? (
                <div className="successSection">
                    <div className="squareDisplay">
                    <div id="heading" style={{margin: '20px'}}>Reset successful!</div>
                        <Link to="/login"><button className="universalButton">Click here to Login</button></Link>
                    </div>
                </div>
            ) : (
                <div className="loginSection">
                    <div className="loginFormSection">
                        <form onSubmit={handleSubmit}>
                            <div id="heading" style={{margin: '20px'}}>Reset Password</div>
                            <div className="field">
                                <input
                                    type='text'
                                    className="globalInput"
                                    ref={userRef}
                                    value={username}
                                    placeholder='Username'
                                    onChange={event => setUsername(event.target.value)}
                                    required
                                />
                            </div>
                            {/* <div className="ErrorMessage">{usernameError !== '' && <p className="errorMsg">{usernameError}</p>}</div> */}
                            <div className="field">
                                <input
                                    type='email'
                                    className="globalInput"
                                    ref={userRef}
                                    value={email}
                                    placeholder='Email'
                                    onChange={event => setEmail(event.target.value)}
                                    required
                                />
                            </div>
                            <div className="ErrorMessage">{emailError === 'Invalid email' && <p className="errorMsg">{emailError}</p>}</div>
                            <div className="field">
                                <input
                                    type='password'
                                    className="globalInput"
                                    ref={userRef}
                                    value={password}
                                    placeholder='New Password'
                                    onChange={event => setPassword(event.target.value)}
                                    required
                                />
                            </div>
                            <div className="field">
                                <input
                                    type='password'
                                    className="globalInput"
                                    ref={userRef}
                                    value={confirmPassword}
                                    placeholder='Confirm Password'
                                    onChange={event => setConfirmPassword(event.target.value)}
                                    required
                                />
                            </div>
                            {/* <div className="ErrorMessage">{passwordError === 'Passwords do not match' && <p className="errorMsg">{passwordError}</p>}</div> */}
                            {errMsg !== '' && <p className="ErrorMessage">{errMsg}</p>}
                            <button className="universalButton">Reset Password</button>
                        </form>
                        <Link to='/login'><button className="universalButton">Cancel</button></Link>
                    </div>
                </div>)}
        </div>
    )
}

export default ResetPasswordPage;
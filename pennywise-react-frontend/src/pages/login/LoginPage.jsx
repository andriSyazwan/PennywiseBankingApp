import '../../styles/LoginPage.css';
import { useRef, useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import axios from 'axios';

const LoginPage = () => {

    const { userId, login } = useAuth()
    const userRef = useRef()
    const api = 'http://localhost:8080/api/v1/users'

    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [errMsg, setErrMsg] = useState('')
    const [success, setSuccess] = useState(false)

    useEffect(() => {
        setErrMsg('')
    }, [])

    const handleSubmit = async (event) => {
        event.preventDefault()

        await axios.post(api + '/login', JSON.stringify({ username: username, password: password }), { headers: { 'Content-Type': 'application/json' } })
            .then(response => {
                console.log(response.status)
                console.log("Response from POST request: " + response.data.userId)
                console.log("Previous userId stored is " + userId)

                setUsername('')
                setPassword('')
                setErrMsg('')
                setSuccess(true)
                login(response?.data?.userId)
                console.log("UserId has been updated")
                console.log("Current userId stored is " + userId)
            })
            .catch(error => {
                if (error?.response?.status === 401) {
                    setErrMsg("Invalid password")
                    console.log("Invalid password")
                    console.log(error?.response?.data)
                } else if (error?.response?.status === 404) {
                    setErrMsg("Invalid username")
                    console.log("Invalid username")
                    console.log(error?.response?.data)
                } else {
                    setErrMsg("Error logging in")
                    console.log("Error logging in")
                    console.log(error?.response?.data)
                }
            })
    }

    return (
        <div className="page">
            {success ? (
                <div className="successSection">
                    <div className="squareDisplay">
                            <div id="heading" style={{margin: '20px'}}>You are logged in!</div>
                        <Link to="/home"><button className="universalButton">Go to Home</button></Link>
                    </div>
                </div>
            ) : (
                <div className="loginSection">
                    <div className="loginFormSection">
                        <form onSubmit={handleSubmit}>
                            <div id="heading" style={{margin: '20px'}}>Log In</div>
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
                            <div className="field">
                                <input
                                    type='password'
                                    className="globalInput"
                                    ref={userRef}
                                    value={password}
                                    placeholder='Password'
                                    onChange={event => setPassword(event.target.value)}
                                    required
                                />
                            </div>
                            <div className="ErrorMessage">{errMsg !== '' && <p className="errorMsg">{errMsg}</p>}</div>
                            <button className="universalButton">Log In</button>
                        </form>
                        <Link to='/signup'><button className="universalButton">Sign Up</button></Link>
                        <Link to='/resetpassword'><button className="universalButton">Forgot Password?</button></Link>
                    </div>
                </div>)}
        </div>

    )

}

export default LoginPage;

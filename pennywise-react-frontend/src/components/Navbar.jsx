import '../styles/Navbar.css'
import { useAuth } from '../contexts/AuthContext';
import { Link, useNavigate, useLocation } from 'react-router-dom';

const Navbar = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { logout } = useAuth();

    const handleLogout = () => {
        console.log(location.pathname)
        logout();
        navigate("/login");
    };

    const getActiveClass = (path) => {
        return location.pathname === path ? 'active' : '';
    };
    
    const goBack = () => {
        navigate(-1);
    };

    return (
        <div>
            <div className="navbarContainer">
                <div className="backButtonContainer">                    
                    <button onClick={goBack} className="backButton">
                        ◀
                    </button>
                </div>
                <div className="logoContainer">
                    <Link to="/" className="navbarLogo">
                        <img
                            src='/logofull.png'
                            style={{ width: '150px' }}
                        />
                    </Link>
                </div>
                <div className="buttonContainer">
                    <Link to="/"
                        className={getActiveClass('/')}>
                        Home
                    </Link>
                </div>
                <div className="buttonContainer">
                    <Link to="/deposit"
                        className={getActiveClass('/deposit')}>
                        Deposit
                    </Link>
                </div>
                <div className="buttonContainer">
                    <Link to="/withdrawal"
                        className={getActiveClass('/withdrawal')}>
                        Withdrawal
                    </Link>
                </div>
                <div className="buttonContainer">
                    <Link to="/transfer"
                        className={getActiveClass('/transfer')}>
                        Transfer
                    </Link>
                </div>
                <div className="buttonContainer">
                    <Link to="/purchase"
                        className={getActiveClass('/purchase')}>
                        Purchase
                    </Link>
                </div>
                <div className="buttonContainer">
                    <Link to="/cards"
                        className={getActiveClass('/cards')}>
                        Cards
                    </Link>
                </div>
                <div className="buttonContainer">
                    <Link to="/transactions"
                        className={getActiveClass('/transactions')}>
                        Transactions
                    </Link>
                </div>
                <div className="buttonContainer">
                    <Link to="/accounts"
                        className={getActiveClass('/accounts')}>
                        Accounts
                    </Link>
                </div>
                <div className="logoutContainer">
                    <Link to="/login"
                        className="logoutButton"
                        onClick={handleLogout}>
                        Logout
                    </Link>
                </div>
                
            </div>
            <div className="fakePadding">
                filler text
            </div>
        </div>
    )

}

export default Navbar;

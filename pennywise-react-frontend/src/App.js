import './App.css';
import { Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { APIProvider } from './contexts/APIContext';
import DashboardPage from './pages/dashboard/DashboardPage';
import LoginPage from './pages/login/LoginPage';
import RegistrationPage from './pages/login/RegistrationPage';
import ResetPasswordPage from './pages/login/ResetPasswordPage';
import AccountsPage from './pages/dashboard/AccountsPage';
import AccountPage from './pages/details/AccountPage';
import CardsPage from './pages/dashboard/CardsPage';
import CreditCardPage from './pages/details/CreditCardPage';
import DepositPage from './pages/transactions/DepositPage';
import WithdrawalPage from './pages/transactions/WithdrawalPage';
import TransferPage from './pages/transactions/TransferPage';
import PurchasePage from './pages/transactions/PurchasePage';
import BillPaymentPage from './pages/transactions/BillPaymentPage'
import TransactionsPage from './pages/histories/TransactionHistory';
import SpecificTransactionPage from './pages/histories/SpecificTransactionHistory';
import RewardsHomePage from './pages/rewards/RewardsHomePage';
import RebatesHistory from './pages/histories/RebatesHistory';
import PointsHistory from './pages/histories/PointsHistory';
import RedeemRebatesPage from './pages/rewards/RedeemRebatesPage';
import RedeemPointsPage from './pages/rewards/RedeemPointsPage';
import InstalmentsHistory from './pages/histories/InstalmentsHistory';
import CreditCardStatementPage from './pages/histories/CreditCardStatementPage';
import CreditCardViewAllPage from './pages/histories/CreditCardViewAllPage';

function App() {
  return (
    <AuthProvider><APIProvider>
      <div className="App">
        <Routes>
          <Route path='/' element={<DashboardPage />} />
          <Route path='/home' element={<DashboardPage />} />
          <Route path='/login' element={<LoginPage />} />
          <Route path='/signup' element={<RegistrationPage />} />
          <Route path='/resetpassword' element={<ResetPasswordPage />} />
          <Route path='/accounts' element={<AccountsPage />} />
          <Route path='/accounts/:accountId' element={<AccountPage />} />
          <Route path='/cards/' element={<CardsPage />} />
          <Route path='/creditcards/:cardId' element={<CreditCardPage />} />
          <Route path='/deposit' element={<DepositPage />} />
          <Route path='/withdrawal' element={<WithdrawalPage />} />
          <Route path='/transfer' element={<TransferPage />} />
          <Route path='/purchase' element={<PurchasePage />} />
          <Route path='/payment/:cardId' element={<BillPaymentPage />} />
          <Route path='/transactions' element={<TransactionsPage />} />
          <Route path='/transactions/:type/:id' element={<SpecificTransactionPage />} />
          <Route path='/rewards/:cardId' element={<RewardsHomePage />} />
          <Route path='/rebates-history/:cardId' element={<RebatesHistory />} />
          <Route path='/points-history/:cardId' element={<PointsHistory />} />
          <Route path='/redeem-rebates/:cardId' element={<RedeemRebatesPage />} />
          <Route path='/redeem-points/:cardId' element={<RedeemPointsPage />} />
          <Route path='/instalments/:cardId' element={<InstalmentsHistory />} />
          <Route path='/month/creditCard/:cardId/:targetDateParam' element={<CreditCardStatementPage />} />
          <Route path='/month/creditCard/viewAll/:cardId' element={<CreditCardViewAllPage />} />
        </Routes>
      </div>
      </APIProvider></AuthProvider>
  );
}

export default App;
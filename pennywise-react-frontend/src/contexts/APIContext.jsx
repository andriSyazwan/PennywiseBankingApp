import { createContext, useContext, useState } from 'react';
import axios from 'axios';

const APIContext = createContext()

export const APIProvider = ({ children }) => {

    const [allBankAccounts, setAllBankAccounts] = useState([]);
    const [bankAccounts, setBankAccounts] = useState([]);
    const [bankAccount, setBankAccount] = useState({ balance: 0 })
    const [creditCards, setCreditCards] = useState([]);
    const [creditCard, setCreditCard] = useState({})
    const [creditCardBills, setCreditCardBills] = useState([])
    const [userBills, setUserBills] = useState([])
    const [transactions, setTransactions] = useState([]);
    const [fullName, setFullName] = useState([]);
    const [merchants, setMerchants] = useState([]);
    const [merchantCategories, setMerchantCategories] = useState([]);
    const [rebates, setRebates] = useState([]);
    const [points, setPoints] = useState([]);
    const [instalments, setInstalments] = useState([]);
    const [instalmentPlans, setInstalmentPlans] = useState([]);
    const [convertedAmount, setConvertedAmount] = useState([]);
    const [bill, setBill] = useState([])
    const [errorMsg, setErrorMsg] = useState([]);

    const api = 'http://localhost:8080/api/v1';

    const getAllBankAccounts = () => {
        axios.get(api + '/bankAccounts', {})
            .then(
                response => {
                    const formattedAccounts = response.data.map(formatBankAccount)
                    setAllBankAccounts(formattedAccounts);
                    console.log(response);
                })
            .catch(errors => { setErrorMsg(errors.message) })
    }

    const getBankAccounts = (userId) => {
        axios.get(api + '/bankAccounts/user/' + userId, {})
            .then(
                response => {
                    const formattedAccounts = response.data.map(formatBankAccount)
                    setBankAccounts(formattedAccounts);
                    console.log(response);
                })
            .catch(errors => { setErrorMsg(errors.message) })
    }

    const getBankAccount = (accountId) => {
        axios.get(api + '/bankAccounts/' + accountId, {})
            .then(
                response => {
                    const formattedAccount = formatBankAccount(response.data)
                    setBankAccount(formattedAccount);
                    console.log(response);
                })
            .catch(errors => { setErrorMsg(errors.message) })
    }

    const getCreditCards = (userId) => {
        axios.get(api + '/creditCards/user/' + userId, {})
            .then(
                response => {
                    const formattedCreditCards = response.data.map(formatCreditCard)
                    setCreditCards(formattedCreditCards);
                    console.log(response);
                })
            .catch(errors => { setErrorMsg(errors.message) })
    }

    const getCreditCard = (cardId) => {
        axios.get(api + '/creditCards/' + cardId, {})
            .then(
                response => {
                    const formattedCreditCard = formatCreditCard(response.data)
                    setCreditCard(formattedCreditCard);
                    console.log(response);
                })
            .catch(errors => { setErrorMsg(errors.message) })
    }

    const getCreditCardBills = (cardNumber) => {
        axios.get(api + '/creditCardBills/creditCard/' + cardNumber, {})
            .then(
                response => {
                    const formattedCreditCardBills = response.data.map(formatCreditCardBills)
                    setCreditCardBills(formattedCreditCardBills);
                    console.log(response);
                })
            .catch(errors => { setErrorMsg(errors.message) })
    }

    const getBillsForUser = (userId) => {
        axios.get(api + '/creditCardBills/user/' + userId, {})
            .then(
                response => {
                    const formattedCreditCardBills = response.data.map(formatCreditCardBills)
                    setUserBills(formattedCreditCardBills);
                    console.log(response);
                })
            .catch(errors => { setErrorMsg(errors.message) })
    }

    const getBillFromMonth = (cardId, month, year) => {
        return axios.get(api + '/creditCardBills/creditCard/' + cardId + '/month', {
            params: { month, year },
        })
            .then((response) => {
                console.log('API response in getBillFromMonth:', response.data);
                setBill(response.data);
                return response.data;
            })
            .catch((error) => {
                setErrorMsg(error.message);
                throw error;
            });
    };

    const getTransactions = (userId) => {
        axios.get(api + '/transactions/user/' + userId, {})
            .then(
                response => {
                    const sortedData = response.data.sort((a, b) => new Date(b.date) - new Date(a.date));
                    setTransactions(sortedData);
                    console.log(response);
                })
            .catch(errors => { setErrorMsg(errors.message) })
    }

    const getMerchants = () => {
        axios.get(api + '/merchants', {})
            .then(
                response => {
                    setMerchants(response.data);
                    console.log(response);
                })
            .catch(errors => { setErrorMsg(errors.message) })
    }

    const getMerchantCategories = () => {
        axios.get(api + '/merchants/categories', {})
            .then(
                response => {
                    setMerchantCategories(response.data);
                    console.log(response);
                })
            .catch(errors => { setErrorMsg(errors.message) })
    }

    const getTransactionsForAccount = (accountId) => {
        axios.get(api + '/transactions/account/' + accountId, {})
            .then(
                response => {
                    const sortedData = response.data.sort((a, b) => new Date(b.date) - new Date(a.date));
                    setTransactions(sortedData);
                    console.log(response);
                })
            .catch(errors => { setErrorMsg(errors.message) })
    }

    const getTransactionsForCard = (cardNumber) => {
        axios.get(api + '/transactions/creditCard/' + cardNumber, {})
            .then(
                response => {
                    const sortedData = response.data.sort((a, b) => new Date(b.date) - new Date(a.date));
                    setTransactions(sortedData);
                    console.log(response);
                })
            .catch(errors => { setErrorMsg(errors.message) })
    }

    const getTransactionsForCardByMonth = (cardId, month, year) => {
        // Log the request details before making the request
        console.log("Axios request configuration:", {
            method: 'get',
            url: api + `/transactions/creditCard/${cardId}`,
            params: { month, year },
        });
        axios.get(api + `/transactions/month/creditCard/${cardId}`, { params: { month, year } })
            .then(
                response => {
                    const sortedData = response.data.sort((a, b) => new Date(b.date) - new Date(a.date));
                    setTransactions(sortedData);
                    console.log(response);
                })
            .catch(errors => { setErrorMsg(errors.message) })
    }

    const convertCurrency = async (currencyCode, amount) => {
        console.log('this is the amount before conversion: ' + amount)

        try {
            const response = await axios.get(api + `/fxRates/convert`, { params: { currencyCode, amount } })
            console.log('this is the amount after conversion: ' + response.data);

            return response.data;
        } catch (error) {
            setErrorMsg(error.message);
            throw error;
        }
    };

    const getFullName = (userId) => {
        axios.get(api + '/users/' + userId, {})
            .then(
                response => {
                    setFullName(response.data.firstName);
                    console.log(response);
                })
            .catch(errors => { setErrorMsg(errors.message) })
    }

    const getRebates = (cardId) => {
        axios.get(api + '/rebates/creditCard/' + cardId, {})
            .then(
                response => {
                    setRebates(response.data);
                    console.log(response.data);
                    console.log('Rebates:', rebates);

                }).catch(errors => { setErrorMsg(errors.message) })
    }

    const getPoints = (cardId) => {
        axios.get(api + '/points/creditCard/' + cardId, {})
            .then(
                response => {
                    setPoints(response.data);
                    console.log(response.data);
                    console.log('Points:', points);

                }).catch(errors => { setErrorMsg(errors.message) })
    }

    const getInstalments = (cardId) => {
        axios.get(api + '/instalmentTransactions/creditCard/' + cardId, {})
            .then(
                response => {
                    setInstalments(response.data);
                    console.log(response.data);
                    console.log('Instalment Plans:', instalmentPlans);

                }).catch(errors => { setErrorMsg(errors.message) })
    }

    const getInstalmentPlans = () => {
        axios.get(api + '/instalmentPlans', {})
            .then(
                response => {
                    setInstalmentPlans(response.data);
                    console.log(response.data);
                    console.log('Instalment Plans:', instalmentPlans);

                }).catch(errors => { setErrorMsg(errors.message) })
    }

    const deposit = async (accountId, amount) => {
        try {
            const response = await axios.post(`${api}/bankAccounts/deposit/${accountId}?amount=${amount}`);
            return response.data;
        } catch (error) {
            console.error('Error depositing funds:', error);
            throw error;
        }
    };

    const withdrawal = async (accountId, amount) => {
        try {
            const response = await axios.post(`${api}/bankAccounts/withdraw/${accountId}?amount=${amount}`);
            return response.data;
        } catch (error) {
            console.error('Error withdrawing funds:', error);
            throw error;
        }
    };

    const transfer = async (sourceAccountId, receivingAccountId, amount) => {
        try {
            const params = {
                sourceAccountId: sourceAccountId,
                receivingAccountId: receivingAccountId,
                amount: amount
            };

            const response = await axios.post(`${api}/bankAccounts/transfer`, null, { params: params });
            return response.data;
        } catch (error) {
            console.error('Error transferring funds:', error);
            throw error;
        }
    };

    const purchase = async (merchantId, creditCardId, amount) => {
        try {
            const params = {
                creditCardId: creditCardId,
                amount: amount,
                merchantId: merchantId
            };

            const response = await axios.post(`${api}/creditCards/purchase`, null, { params: params });
            return response.data;
        } catch (error) {
            console.error('Error while attempting purchase:', error);
            throw error;
        }
    };

    const instalmentPurchase = async (merchantId, creditCardId, amount, instalmentPlanId) => {
        try {
            const params = {
                creditCardId: creditCardId,
                amount: amount,
                merchantId: merchantId,
                instalmentPlanId: instalmentPlanId
            };

            const response = await axios.post(`${api}/creditCards/instalmentPurchase`, null, { params: params });
            return response.data;
        } catch (error) {
            console.error('Error while attempting purchase:', error);
            throw error;
        }
    };

    const billPayment = async (billId, accountId, amount) => {
        try {
            const params = {
                billId: billId,
                bankAccountId: accountId,
                amount: amount
            };

            const response = await axios.post(`${api}/creditCards/ccBillPayment`, null, { params: params });
            return response.data;
        } catch (error) {
            console.error('Error submitting bill payment:', error);
            throw error;
        }
    };

    const redeemRebates = async (cardId, amount) => {
        try {
            const params = {
                amount: amount
            };

            const response = await axios.post(`${api}/creditCards/${cardId}/redeemRebate?`, null, { params: params });
            return response.data;
        } catch (error) {
            console.error('Error redeeming rebates:', error);
            throw error;
        }
    };


    const redeemPoints = async (cardId, points) => {
        try {
            const params = {

                points: points
            };

            const response = await axios.post(`${api}/creditCards/${cardId}/redeemPoints`, null, { params: params });
            return response.data;
        } catch (error) {
            console.error('Error redeeming points:', error);
            throw error;
        }
    };

    // Formatting function to format the balance and account type
    const formatBankAccount = (account) => {
        // Format balance to 2 decimal points
        const formattedBalance = account.balance.toFixed(2);

        // Format account type
        let formattedAccountType = account.accountType.replace('_', ' '); // Replace underscores with spaces
        formattedAccountType = formattedAccountType.replace(/\b\w/g, (c) => c.toUpperCase()); // Capitalize words

        return {
            ...account,
            balance: formattedBalance,
            accountType: formattedAccountType,
        };
    };

    // Formatting function to format the balances and account type
    const formatCreditCard = (creditCard) => {
        // Format balances and amount to 2 decimal points
        const formattedCreditLimit = creditCard.creditLimit.toFixed(2)
        const formattedMinimumPaymentAmount = creditCard.minimumPaymentAmount.toFixed(2)
        const formattedCurrentBalance = creditCard.creditBalance.toFixed(2)
        const formattedAvailableBalance = (parseFloat(creditCard.creditLimit) - parseFloat(formattedCurrentBalance)).toFixed(2)

        return {
            ...creditCard,
            creditLimit: formattedCreditLimit,
            minimumPaymentAmount: formattedMinimumPaymentAmount,
            formattedCurrentBalance,
            formattedAvailableBalance
        };
    };

    // Formatting function to format credit card bills
    const formatCreditCardBills = (bill) => {
        // Format amount to 2 decimal points
        const formattedBillAmount = bill.billAmount.toFixed(2);
        const formattedAmountPaid = bill.amountPaid.toFixed(2);

        const originalBillingPeriodStart = new Date(bill.billingPeriodStart)
        const originalBillingPeriodEnd = new Date(bill.billingPeriodEnd)
        const originalDueDate = new Date(bill.dueDate)
        const originalStatementDate = new Date(bill.billingPeriodEnd)

        // Format billing period start and end dates
        const formattedBillingPeriodStart = originalBillingPeriodStart.toLocaleString("en-GB", {
            day: "2-digit",
            month: "2-digit",
            year: "2-digit"
        })
        const formattedBillingPeriodEnd = originalBillingPeriodEnd.toLocaleString("en-GB", {
            day: "2-digit",
            month: "2-digit",
            year: "2-digit"
        })

        // Format due date
        const formattedDueDate = originalDueDate.toLocaleString("en-GB", {
            day: "2-digit",
            month: "2-digit",
            year: "numeric"
        })

        // Format statement date
        const formattedStatementDate = originalStatementDate.toLocaleString("en-GB", {
            month: "short",
            year: "numeric"
        })

        return {
            ...bill,
            billAmount: formattedBillAmount,
            amountPaid: formattedAmountPaid,
            formattedBillingPeriodStart,
            formattedBillingPeriodEnd,
            formattedDueDate,
            formattedStatementDate
        }
    };

    return (
        <APIContext.Provider value={{
            getAllBankAccounts, allBankAccounts,
            getBankAccounts, bankAccounts,
            getBankAccount, bankAccount,
            getCreditCards, creditCards,
            getCreditCard, creditCard,
            getCreditCardBills, creditCardBills,
            getBillsForUser, userBills,
            getTransactions, getTransactionsForAccount, getTransactionsForCard, transactions,
            getFullName, fullName,
            getMerchants, merchants,
            getMerchantCategories, merchantCategories,
            getRebates, rebates,
            getPoints, points,
            getInstalments, instalments,
            getInstalmentPlans, instalmentPlans,
            deposit, withdrawal, transfer, purchase, instalmentPurchase, billPayment,
            redeemRebates, redeemPoints,
            getTransactionsForCardByMonth, convertCurrency,
            getBillFromMonth, bill
        }}>
            {children}
        </APIContext.Provider>
    )
}

export const useAPI = () => useContext(APIContext)
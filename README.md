# OOAD Banking Management System

## Task 4: Core Java Model

Implemented in GitHub Codespaces using Java.

### Model
- Customer, IndividualCustomer and CompanyCustomer
- Account, SavingsAccount, InvestmentAccount and ChequeAccount
- Withdrawable and InterestBearing interfaces
- Transaction records for opening deposits, deposits, withdrawals and interest

### Account rules
- Savings: 0.05% monthly interest; no withdrawals.
- Investment: minimum opening deposit of BWP 500;
  5% monthly interest; deposits and withdrawals.
- Cheque: employed individual customers with employer details;
  deposits and withdrawals; no interest.
- Withdrawals cannot exceed the available balance.
- A customer can own multiple accounts.

### Compile
```bash
javac -d out src/banking/model/*.java
```

### Run checks
```bash
java -cp out banking.model.SavingsAccountTest
java -cp out banking.model.InvestmentAccountTest
java -cp out banking.model.ChequeAccountTest
java -cp out banking.model.TransactionTest
```

GUI, controllers and database integration are not yet implemented.
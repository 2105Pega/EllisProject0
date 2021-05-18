# Project 0 Banking App


- logs are stored at logs/bankingapp.log
- application data is stored in src/main/resources/bankdata.txt
- the data file checked into the repo has these accounts:
  - alice : password is maven, account type is client
  - bob : password is java, account type is client
  - charlie : password is chocolate, account type is employee
## Class Hierarchy

### Models

- User
- Client : User
- Employee : User
- Account
- Transaction
- Deposit : Transaction
- Withdraw : Transaction
- Transfer : Transaction

### Services
- UserManager
- TransactionManager
- AccountManager
- Dao
- UserDao: Dao
- TransactionDao : Dao
- AccountDao : Dao
- Format
- Input
- Persistence

### Controller
- Driver
- UserInterface
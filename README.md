# Project 0 Banking App

## Instructions

Build with Maven and execute tests by running build.bat (Windows) or build.sh (Linux/Mac).

Run the project with run.bat (Windows) or build.sh (Linux/Mac).

You will need a jdbcbank.properties files with the correct database credentials to run this application.

Logs are stored in both logs/bankingapp.log and the database configured in the properties file. The logs stored in the database can be viewed by the admin in the application.

Users currently available in database:

admin : adminpassword

alice : apass

bob : bpass

## Class Hierarchy

### Models

- User
- Client : User
- Account
- Transaction

### Services
- UserManager
- TransactionManager
- AccountManager
- ConnectionManager
- Dao
- UserDao: Dao
- TransactionDao : Dao
- AccountDao : Dao
- Format
- Input
- Persistence

### Exceptions
- TransactionFailedException

### Controller
- Driver
- UserInterface

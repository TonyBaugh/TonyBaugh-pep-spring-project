package com.example.service;

import java.util.List;

import javax.security.auth.login.LoginException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.entity.Account;
import com.example.exception.PasswordLengthTooShortException;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.exception.UsernameBlankException;
import com.example.repository.AccountRepository;

@Service
@Validated
public class AccountService {

    @Autowired
    private final AccountRepository accountRepository;

    // Constructor based dependency injection
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /*
        Method to retrieve all users for testing purposes
    */

    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }
        
    /*
        ## 1: Our API should be able to process new User registrations.
        As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register. The body will contain a representation of a JSON Account, but will not contain an accountId.
        - The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. 
        If all these conditions are met, the response body should contain a JSON of the Account, including its accountId. The response status should be 200 OK, which is the default. The new account should be persisted to the database.
        - If the registration is not successful due to a duplicate username, the response status should be 409. (Conflict)
        - If the registration is not successful for some other reason, the response status should be 400. (Client error)  
    */

    // rollbackOn Technically not needed, but good practice for future expansion
    @Transactional(rollbackOn = {UsernameAlreadyExistsException.class, UsernameBlankException.class, PasswordLengthTooShortException.class})
    public Account registerAccount(Account newAccount) throws UsernameAlreadyExistsException,
                                                            UsernameBlankException,
                                                            PasswordLengthTooShortException{
        //Check if username is blank, password >= 4 characters, and ensure account name doesn't exist

        if (accountRepository.findByUsername(newAccount.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException();
        } else if (newAccount.getUsername().length() == 0) {
            throw new UsernameBlankException();
        } else if (newAccount.getPassword().length() < 4) {
            throw new PasswordLengthTooShortException();
        } else {                                                 
        return accountRepository.save(newAccount);
        }
    }

    /*
        ## 2: Our API should be able to process User logins.
        As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. The request body will contain a JSON representation of an Account.
        - The login will be successful if and only if the username and password provided in the request body JSON match a real account existing on the database. 
        If successful, the response body should contain a JSON of the account in the response body, including its accountId. The response status should be 200 OK, which is the default.
        - If the login is not successful, the response status should be 401. (Unauthorized)
    */
    @Transactional(rollbackOn = LoginException.class)
    public Account accountLogin(Account account) throws LoginException{
        Account verifiedAccount = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword())
                                                    .orElseThrow(() -> new LoginException("Invalid username or password."));
        System.out.println("Account Verified.");
        return verifiedAccount;
    }
}

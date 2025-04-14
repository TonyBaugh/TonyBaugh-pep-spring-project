package com.example.service;

import java.util.List;

import javax.security.auth.login.LoginException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.entity.Account;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.repository.AccountRepository;

@Service
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
     * ------> New User Registration Service <------
     */
    // rollbackOn Technically not needed, but good practice for future expansion
    @Transactional(rollbackOn = UsernameAlreadyExistsException.class)
    public Account registerAccount(Account newAccount) throws UsernameAlreadyExistsException{
        

        if (accountRepository.findByUsername(newAccount.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException();
        } else {                                                 
        return accountRepository.save(newAccount);
        }
    }

    /*
     * ------> User login Service <------
     */
    @Transactional(rollbackOn = LoginException.class)
    public Account accountLogin(Account account) throws LoginException{
        Account verifiedAccount = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword())
                                                    .orElseThrow(() -> new LoginException("Invalid username or password."));        
        return verifiedAccount;
    }
}

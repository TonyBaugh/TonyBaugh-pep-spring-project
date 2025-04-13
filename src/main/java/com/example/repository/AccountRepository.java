package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{
    // For checking if an account name is taken prior to registration
    Optional<Account> findByUsername(String username);
    // For login verification    
    Optional<Account> findByUsernameAndPassword(String username, String password);
    
}

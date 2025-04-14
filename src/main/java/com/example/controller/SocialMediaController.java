package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.ApiResponse;
import com.example.dto.MessageText;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.AccountDoesNotExistException;
import com.example.exception.MessageDoesNotExistException;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.service.AccountService;
import com.example.service.MessageService;

// Designate class as a Controller, Spring Boot will look for beans in this class. RestController = Controller + ResponseBody.
@RestController
@RequestMapping("/")
public class SocialMediaController {

    
    private final AccountService accountService;    
    private final MessageService messageService;

    // Constructor based DI    
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }


    /*
     * ------> New User Registration <------
     */
    @PostMapping("register")    
    public ResponseEntity<Account> registerAccount(@Valid @RequestBody Account newAccount) {
        try {            
            accountService.registerAccount(newAccount);
        }catch (UsernameAlreadyExistsException e) {            
            return ResponseEntity.status(HttpStatus.CONFLICT)
                            .build();
        }
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(newAccount);
    }

    /*
    * ------> Method to retrieve all users for testing purposes <------
    */

    @GetMapping("users")
    public ResponseEntity<List<Account>> getAccounts() {
        List<Account> accountList = accountService.getAccounts();
        
        return ResponseEntity.status(HttpStatus.OK)
                            .body(accountList);
    }

    /*
     * ------> Process User Logins <------
     */

    @PostMapping("login")
    public ResponseEntity<Account> accountLogin(@RequestBody Account account){
        Account verifiedAccount;

        try {
            verifiedAccount = accountService.accountLogin(account);        
        } catch (LoginException le){        
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .build();
        }
        
        return ResponseEntity.status(HttpStatus.OK)
                            .body(verifiedAccount);
    }


    /*
     * ------> Post New Message <------
     */

    @PostMapping("messages")
    public ResponseEntity<Message> postMessage(@Valid @RequestBody Message message) {
        try {
            messageService.postMessage(message);
        } catch (AccountDoesNotExistException e) {            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .build();
        }
        return ResponseEntity.status(HttpStatus.OK)
                                .body(message);
    }

    /*
     * ------> Retrieve All Messages <------
     */

     @GetMapping("messages")
     public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.status(HttpStatus.OK)
        .body(messageService.getAllMessages());
     }

    /*
     * ------> Retrieve Message By Id <------
     */

    @GetMapping("messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        return ResponseEntity.status(HttpStatus.OK)
        .body(messageService.getMessageById(messageId));
    }

    /*
     * ------> Delete Message By Id <------
     */

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
        try {
            messageService.deleteMessageById(messageId);
        } catch (MessageDoesNotExistException e) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(1);
    }

    /*
     * ------> Update Message Text <------
     */

    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateMessageById(@PathVariable Integer messageId, @Valid @RequestBody MessageText messageText) {        
        try {
            messageService.updateMessageById(messageId, messageText);
        } catch (MessageDoesNotExistException e) {        
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(1);
    }

    /*
     * ------> Retrieve All Messages By User <------
     */

    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Integer accountId) {
        List<Message> messageList = new ArrayList<>();
        try {
            messageList = messageService.findMessagesByAccountId(accountId);
        } catch (AccountDoesNotExistException e) {
            return ResponseEntity.status(HttpStatus.OK).body(messageList);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(messageList);
    }

}

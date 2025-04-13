package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.MessageText;
import com.example.entity.Message;
import com.example.exception.AccountDoesNotExistException;
import com.example.exception.MessageDoesNotExistException;
import com.example.exception.MessageTextInvalidException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    // Constructor based DI
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /*
     ## 3: Our API should be able to process the creation of new messages.
    As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages. The request body will contain a JSON representation of a message, which should be persisted to the database, but will not contain a messageId.
    - The creation of the message will be successful if and only if the messageText is not blank, is not over 255 characters, and postedBy refers to a real, existing user. 
    If successful, the response body should contain a JSON of the message, including its messageId. The response status should be 200, which is the default. The new message should be persisted to the database.
    - If the creation of the message is not successful, the response status should be 400. (Client error)
    */

    public Message postMessage(Message message) throws MessageTextInvalidException, AccountDoesNotExistException{

        // Check if Message text is blank, Message text is > 255 characters, and posted by a real, existing user
        if (message.getMessageText().length() > 255 || message.getMessageText().length() == 0){
            throw new MessageTextInvalidException();
        } else if (!accountRepository.findById(message.getPostedBy()).isPresent()) {
            throw new AccountDoesNotExistException();
        } else  {
        return messageRepository.save(message);
        }
    }
    /*
    ## 4: Our API should be able to retrieve all messages.
    As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.    
    - The response body should contain a JSON representation of a list containing all messages retrieved from the database. 
    It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.
    */

    public List<Message> getAllMessages() {        
        return messageRepository.findAll();
    }

    /*
    ## 5: Our API should be able to retrieve a message by its ID.
    As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{messageId}.
    - The response body should contain a JSON representation of the message identified by the messageId. 
    It is expected for the response body to simply be empty if there is no such message. The response status should always be 200, which is the default.
    */

    public Message getMessageById(Integer Id) {
        System.out.println("In the getMessageById Service call");
        System.out.println("Message ID = " + Id);
        return messageRepository.findById(Id).orElse(null);                                
    }

    /*
    ## 6: Our API should be able to delete a message identified by a message ID.
    As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{messageId}.
    - The deletion of an existing message should remove an existing message from the database. 
    If the message existed, the response body should contain the number of rows updated (1). The response status should be 200, which is the default.
    - If the message did not exist, the response status should be 200, but the response body should be empty. 
    This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint should respond with the same type of response.
    */

    public Integer deleteMessageById(Integer Id) {
        if (messageRepository.findById(Id).isPresent()) {            
            messageRepository.deleteById(Id);
            System.out.println("Message Deleted");
            return 1;
        } else {
            System.out.println("Message not found.");
            return null;
        }
        
    }

    /*
    ## 7: Our API should be able to update a message text identified by a message ID.
    As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{messageId}. 
    The request body should contain a new messageText values to replace the message identified by messageId. The request body can not be guaranteed to contain any other information.
    - The update of a message should be successful if and only if the message id already exists and the new messageText is not blank and is not over 255 characters. 
    If the update is successful, the response body should contain the number of rows updated (1), and the response status should be 200, which is the default. The message existing on the database should have the updated messageText.
    - If the update of the message is not successful for any reason, the response status should be 400. (Client error)
    */

    public Integer updateMessageById(Integer messageId, MessageText messageText) throws MessageDoesNotExistException, MessageTextInvalidException{
        
        Message updatedMessage = messageRepository.findById(messageId).orElseThrow(() -> new MessageDoesNotExistException());
        
        if (messageText.getMessageText().length() > 255 || messageText.getMessageText().length() == 0) {
            throw new MessageTextInvalidException();
        } else {
            System.out.println(messageText.getMessageText().length());
            messageRepository.save(updatedMessage);
            return 1;
        }          
    }

    /*
    ## 8: Our API should be able to retrieve all messages written by a particular user.
    As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{accountId}/messages.
    - The response body should contain a JSON representation of a list containing all messages posted by a particular user, which is retrieved from the database. 
    It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.
    */

    public List<Message> findMessagesByAccountId(Integer accountId) throws AccountDoesNotExistException{
        List<Message> messageList = messageRepository.findByPostedBy(accountId);
        accountRepository.findById(accountId).orElseThrow(() -> new AccountDoesNotExistException());
        return messageList;
    }
}

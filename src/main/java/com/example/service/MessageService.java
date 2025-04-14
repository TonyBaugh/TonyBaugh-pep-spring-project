package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.MessageText;
import com.example.entity.Message;
import com.example.exception.AccountDoesNotExistException;
import com.example.exception.MessageDoesNotExistException;
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
     * ------> Post New Message <------
     */
    public Message postMessage(Message message) throws AccountDoesNotExistException{
        accountRepository.findById(message.getPostedBy()).orElseThrow(() -> new AccountDoesNotExistException());

        Message newMessage = new Message();
        newMessage.setMessageText(message.getMessageText());
        
        return messageRepository.save(message);
        
    }
    
    /*
     * ------> Retrieve All Messages <------
     */
    public List<Message> getAllMessages() {        
        return messageRepository.findAll();
    }

    /*
     * ------> Retrieve Message By Id <------
     */
    public Message getMessageById(Integer Id) {        
        return messageRepository.findById(Id).orElse(null);                                
    }

    /*
     * ------> Delete Message By Id <------
     */
    public Integer deleteMessageById(Integer Id) throws MessageDoesNotExistException{
        messageRepository.findById(Id).orElseThrow(() -> new MessageDoesNotExistException());

        messageRepository.deleteById(Id);       
        return 1;
        
        
    }

    /*
     * ------> Update Message Text <------
     */
    public Integer updateMessageById(Integer messageId, MessageText messageText) throws MessageDoesNotExistException{        
        Message updatedMessage = messageRepository.findById(messageId).orElseThrow(() -> new MessageDoesNotExistException());

        updatedMessage.setMessageText(messageText.getMessageText());
        
        messageRepository.save(updatedMessage);
        return 1;        
    }

    /*
     * ------> Retrieve All Messages By User <------
     */
    public List<Message> findMessagesByAccountId(Integer accountId) throws AccountDoesNotExistException{
        List<Message> messageList = messageRepository.findByPostedBy(accountId);

        accountRepository.findById(accountId).orElseThrow(() -> new AccountDoesNotExistException());
        return messageList;
    }
}

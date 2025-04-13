package com.example.dto;

public class MessageText {
    private String messageText;

    // No args constructor
    public MessageText() {

    }

    // constructor with messageText
    public MessageText(String messageText){
        this.messageText = messageText;
    }

    // Getter for messageText
    public String getMessageText() {
        return this.messageText;
    }

}

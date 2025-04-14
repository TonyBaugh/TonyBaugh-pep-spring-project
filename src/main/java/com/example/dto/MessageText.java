package com.example.dto;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor
// This dto exists to pass messageText data around when the rest of the message fields are not needed
public class MessageText {
    @Size(min=1, max=255)
    private String messageText;
}

package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor

// ApiResponse wrapper. Can be used to return status and error information in case of service method failure, or an object in case of service method success.
/* 
Really wanted to use this for all of the methods to standardize return output but after figuring it out locally in my IDE and then getting it into gitpod, I realized the tests were 
read-only and I couldn't modify them to extract the data field from my ApiResponse DTO. I had to revert everything back to returning account/message objects, primitives,
or null. I eventually switched to using .build() to return an empty response body instead of null. Couldn't bear to delete the ApiResponse wrapper though because I spent 
much time on it. It lives here for future use :)
*/ 
public class ApiResponse<T> {

    private T data;
    private int status;
    private boolean success;
    private String message;

}

package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor

// ApiResponse wrapper for standardizing response structure (data, status, success flag, and message).
/*
Originally intended to wrap all controller responses with this DTO for consistent output formatting. 
However, due to test suite restrictions (read-only, expecting raw entity/primitives), it failed in Gitpod.
Reverted back to entities/primitives to maintain compatibility, but preserved the class for future use or expansion -- and was too proud of it to delete :)
*/

public class ApiResponse<T> {

    private T data;
    private int status;
    private boolean success;
    private String message;

}

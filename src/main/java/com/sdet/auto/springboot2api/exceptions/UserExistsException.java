package com.sdet.auto.springboot2api.exceptions;

public class UserExistsException extends Exception{

    public UserExistsException(String message) {
        super(message);
    }
}

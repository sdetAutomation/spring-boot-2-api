package com.sdet.auto.springboot2api.exceptions;

public class UserNameNotFoundException extends Exception {

    public UserNameNotFoundException(String message) {
        super(message);
    }
}

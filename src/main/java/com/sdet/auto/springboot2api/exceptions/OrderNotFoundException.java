package com.sdet.auto.springboot2api.exceptions;

public class OrderNotFoundException extends Exception {

    public OrderNotFoundException(String message) {
        super(message);
    }
}

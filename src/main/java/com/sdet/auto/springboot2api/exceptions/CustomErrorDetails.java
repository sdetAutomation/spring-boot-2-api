package com.sdet.auto.springboot2api.exceptions;

import java.util.Date;

// simple custom error details bean
public class CustomErrorDetails {

    private Date timestamp;
    private String message;
    private String errordetails;

    public CustomErrorDetails(Date timestamp, String message, String errordetails) {
        this.timestamp = timestamp;
        this.message = message;
        this.errordetails = errordetails;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getErrordetails() {
        return errordetails;
    }
}

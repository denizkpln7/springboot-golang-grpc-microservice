package com.denizkpln.payment_service.exception;

public class MyCustomError extends RuntimeException{
    public MyCustomError(String message) {
        super(message);
    }
}
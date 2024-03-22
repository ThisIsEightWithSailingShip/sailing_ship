package com.eight.sailingship.error.order;

public class BalanceInsufficientException extends RuntimeException{
    public BalanceInsufficientException(String message) {
        super(message);
    }
}

package com.aldob.exceptions.customs;

public class InvalidTicketStatusTransitionException extends RuntimeException {

    public InvalidTicketStatusTransitionException(String message) {
        super(message);
    }
}

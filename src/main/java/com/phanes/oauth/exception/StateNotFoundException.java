package com.phanes.oauth.exception;

public class StateNotFoundException extends RuntimeException {

    public StateNotFoundException(String message) {
        super(message);
    }
}

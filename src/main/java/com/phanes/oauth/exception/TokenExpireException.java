package com.phanes.oauth.exception;

public class TokenExpireException extends RuntimeException{

    public TokenExpireException(String message) {
        super(message);
    }
}

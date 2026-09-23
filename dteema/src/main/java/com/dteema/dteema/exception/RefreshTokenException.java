package com.dteema.dteema.exception;

import org.springframework.http.HttpStatus;

public class RefreshTokenException extends GlobalException {

    public RefreshTokenException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
package com.dteema.dteema.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ChatSessionUnauthorizedException extends RuntimeException {

    public ChatSessionUnauthorizedException(String message) {
        super(message);
    }
}

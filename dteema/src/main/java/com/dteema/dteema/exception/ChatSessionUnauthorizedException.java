package com.dteema.dteema.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ChatSessionUnauthorizedException extends GlobalException {

    public ChatSessionUnauthorizedException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}

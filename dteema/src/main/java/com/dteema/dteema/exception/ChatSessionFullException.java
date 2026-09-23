package com.dteema.dteema.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ChatSessionFullException extends GlobalException {

    public ChatSessionFullException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}

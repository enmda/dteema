package com.dteema.dteema.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ChatSessionNotFoundException extends GlobalException {

    public ChatSessionNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

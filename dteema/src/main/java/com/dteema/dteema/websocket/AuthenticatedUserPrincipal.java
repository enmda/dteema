package com.dteema.dteema.websocket;

import com.dteema.dteema.model.User;

import java.security.Principal;

public record AuthenticatedUserPrincipal(User user) implements Principal {

    @Override
    public String getName() {
        return user.getUsername();
    }
}

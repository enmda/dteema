package com.dteema.dteema.websocket;

import com.dteema.dteema.model.User;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Component
public class UserHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        User user = (User) attributes.get(JwtHandshakeInterceptor.USER_ATTRIBUTE);
        return new AuthenticatedUserPrincipal(user);
    }
}

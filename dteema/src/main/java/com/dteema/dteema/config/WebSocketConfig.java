package com.dteema.dteema.config;

import com.dteema.dteema.websocket.ChatWebSocketHandler;
import com.dteema.dteema.websocket.JwtHandshakeInterceptor;
import com.dteema.dteema.websocket.UserHandshakeHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;
    private final UserHandshakeHandler userHandshakeHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "/ws/chat/{sessionId}")
                .addInterceptors(jwtHandshakeInterceptor)
                .setHandshakeHandler(userHandshakeHandler)
                .setAllowedOriginPatterns("*");
    }
}

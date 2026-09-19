package com.dteema.dteema.websocket;

import com.dteema.dteema.dto.chat.ChatMessageRequest;
import com.dteema.dteema.dto.chat.ChatMessageResponse;
import com.dteema.dteema.dto.chat.WebSocketErrorResponse;
import com.dteema.dteema.model.User;
import com.dteema.dteema.service.ChatService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final String CHAT_PATH_PREFIX = "/ws/chat/";

    private final ChatService chatService;
    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final ConcurrentHashMap<UUID, Set<WebSocketSession>> sessionsByChat = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        UUID chatSessionId = extractSessionId(webSocketSession);
        User user = currentUser(webSocketSession);

        try {
            chatService.joinOrValidateParticipant(chatSessionId, user);
            sessionsByChat.computeIfAbsent(chatSessionId, ignored -> ConcurrentHashMap.newKeySet())
                    .add(webSocketSession);
            webSocketSession.getAttributes().put("chatSessionId", chatSessionId);
        } catch (RuntimeException ex) {
            sendError(webSocketSession, ex.getMessage());
            webSocketSession.close(CloseStatus.POLICY_VIOLATION);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage textMessage) throws Exception {
        UUID chatSessionId = (UUID) webSocketSession.getAttributes().get("chatSessionId");
        if (chatSessionId == null) {
            sendError(webSocketSession, "Chat session was not initialized");
            return;
        }

        ChatMessageRequest request;
        try {
            request = objectMapper.readValue(textMessage.getPayload(), ChatMessageRequest.class);
        } catch (JacksonException ex) {
            sendError(webSocketSession, "Invalid message payload");
            return;
        }

        Set<ConstraintViolation<ChatMessageRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            sendError(webSocketSession, violations.iterator().next().getMessage());
            return;
        }

        ChatMessageResponse response = chatService.sendMessage(
                chatSessionId,
                currentUser(webSocketSession),
                request.getContent()
        );

        broadcast(chatSessionId, response);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus status) {
        UUID chatSessionId = (UUID) webSocketSession.getAttributes().get("chatSessionId");
        if (chatSessionId == null) {
            return;
        }

        Set<WebSocketSession> chatSessions = sessionsByChat.get(chatSessionId);
        if (chatSessions == null) {
            return;
        }

        chatSessions.remove(webSocketSession);
        if (chatSessions.isEmpty()) {
            sessionsByChat.remove(chatSessionId, chatSessions);
        }
    }

    private void broadcast(UUID chatSessionId, ChatMessageResponse response) throws IOException, JacksonException {
        TextMessage message = new TextMessage(objectMapper.writeValueAsString(response));
        Set<WebSocketSession> chatSessions = sessionsByChat.getOrDefault(chatSessionId, Set.of());
        for (WebSocketSession chatSession : chatSessions) {
            if (chatSession.isOpen()) {
                chatSession.sendMessage(message);
            }
        }
    }

    private void sendError(WebSocketSession webSocketSession, String message) throws IOException, JacksonException {
        if (!webSocketSession.isOpen()) {
            return;
        }

        webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(
                WebSocketErrorResponse.builder()
                        .type("ERROR")
                        .message(message)
                        .build()
        )));
    }

    private User currentUser(WebSocketSession webSocketSession) {
        AuthenticatedUserPrincipal principal = (AuthenticatedUserPrincipal) webSocketSession.getPrincipal();
        return principal.user();
    }

    private UUID extractSessionId(WebSocketSession webSocketSession) {
        URI uri = webSocketSession.getUri();
        if (uri == null || uri.getPath() == null || !uri.getPath().startsWith(CHAT_PATH_PREFIX)) {
            throw new IllegalArgumentException("Chat session id is missing");
        }

        return UUID.fromString(uri.getPath().substring(CHAT_PATH_PREFIX.length()));
    }
}

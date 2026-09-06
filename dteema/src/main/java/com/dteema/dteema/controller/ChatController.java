package com.dteema.dteema.controller;

import com.dteema.dteema.dto.chat.ChatMessageResponse;
import com.dteema.dteema.dto.chat.CreateChatSessionResponse;
import com.dteema.dteema.model.User;
import com.dteema.dteema.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/sessions")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateChatSessionResponse createSession(@AuthenticationPrincipal User user) {
        return chatService.createSession(user);
    }

    @GetMapping("/sessions/{sessionId}/messages")
    public List<ChatMessageResponse> getMessages(@PathVariable UUID sessionId,
                                                 @AuthenticationPrincipal User user) {
        return chatService.getMessages(sessionId, user);
    }
}

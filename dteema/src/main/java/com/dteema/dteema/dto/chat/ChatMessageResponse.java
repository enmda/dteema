package com.dteema.dteema.dto.chat;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ChatMessageResponse(
        UUID id,
        UUID sessionId,
        UUID senderId,
        String senderUsername,
        String content,
        LocalDateTime createdAt
) {
}

package com.dteema.dteema.dto.chat;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateChatSessionResponse(UUID sessionId) {
}

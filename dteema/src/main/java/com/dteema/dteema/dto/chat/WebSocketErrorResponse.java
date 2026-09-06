package com.dteema.dteema.dto.chat;

import lombok.Builder;

@Builder
public record WebSocketErrorResponse(String type, String message) {
}

package com.dteema.dteema.dto.common;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private HttpStatus status;
    private String error;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private List<String> details;

    public ErrorResponse(HttpStatus status, String error, String path) {
        this.status = status;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
}
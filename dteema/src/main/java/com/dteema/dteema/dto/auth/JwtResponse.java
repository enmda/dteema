package com.dteema.dteema.dto.auth;

import com.dteema.dteema.model.Role;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private String accessToken;
    private String refreshToken;

    @Builder.Default
    private String type = "Bearer";

    private UUID id;
    private String username;
    private String email;
    private Set<Role> roles;
}

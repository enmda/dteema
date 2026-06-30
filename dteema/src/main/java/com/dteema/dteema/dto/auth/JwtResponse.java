package com.dteema.dteema.dto.auth;

import com.dteema.dteema.model.Role;
import lombok.*;

import java.util.Set;

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

    private Long id;
    private String username;
    private String email;
    private Set<Role> roles;
}
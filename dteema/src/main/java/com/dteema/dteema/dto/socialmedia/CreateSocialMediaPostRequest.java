package com.dteema.dteema.dto.socialmedia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSocialMediaPostRequest {

    @NotBlank(message = "Post content is required")
    @Size(min = 1, max = 5000, message = "Post content must be between 1 and 5000 characters")
    private String content;
}

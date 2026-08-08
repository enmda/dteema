package com.dteema.dteema.controller;

import com.dteema.dteema.dto.common.PageResponse;
import com.dteema.dteema.dto.socialmedia.CreateSocialMediaPostRequest;
import com.dteema.dteema.dto.socialmedia.SocialMediaPostResponse;
import com.dteema.dteema.model.User;
import com.dteema.dteema.service.SocialMediaPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/socialmedia/posts")
@RequiredArgsConstructor
public class SocialMediaPostController {

    private final SocialMediaPostService socialMediaPostService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SocialMediaPostResponse createPost(@Valid @RequestBody CreateSocialMediaPostRequest request,
                                              @AuthenticationPrincipal User user) {
        return socialMediaPostService.createPost(request, user);
    }

    @GetMapping("/feed")
    public PageResponse<SocialMediaPostResponse> getFeed(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "20") int size) {
        return socialMediaPostService.getFeed(page, size);
    }
}

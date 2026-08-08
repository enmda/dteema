package com.dteema.dteema.service;

import com.dteema.dteema.dto.common.PageResponse;
import com.dteema.dteema.dto.mapper.SocialMediaPostMapper;
import com.dteema.dteema.dto.socialmedia.CreateSocialMediaPostRequest;
import com.dteema.dteema.dto.socialmedia.SocialMediaPostResponse;
import com.dteema.dteema.model.User;
import com.dteema.dteema.model.socialmedia.SocialMediaPost;
import com.dteema.dteema.repository.SocialMediaPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocialMediaPostService {

    private static final int MAX_PAGE_SIZE = 100;

    private final SocialMediaPostRepository socialMediaPostRepository;
    private final SocialMediaPostMapper socialMediaPostMapper;

    @Transactional
    public SocialMediaPostResponse createPost(CreateSocialMediaPostRequest request, User author) {
        SocialMediaPost post = socialMediaPostMapper.toModel(request, author);

        return socialMediaPostMapper.toResponse(socialMediaPostRepository.save(post));
    }

    @Transactional(readOnly = true)
    public PageResponse<SocialMediaPostResponse> getFeed(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);

        Page<SocialMediaPostResponse> posts = socialMediaPostRepository
                .findAllByOrderByCreatedAtDesc(PageRequest.of(safePage, safeSize))
                .map(socialMediaPostMapper::toResponse);

        return PageResponse.<SocialMediaPostResponse>builder()
                .content(posts.getContent())
                .page(posts.getNumber())
                .size(posts.getSize())
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .last(posts.isLast())
                .first(posts.isFirst())
                .build();
    }
}

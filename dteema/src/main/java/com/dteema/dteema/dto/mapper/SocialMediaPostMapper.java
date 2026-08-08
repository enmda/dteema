package com.dteema.dteema.dto.mapper;

import com.dteema.dteema.dto.socialmedia.CreateSocialMediaPostRequest;
import com.dteema.dteema.dto.socialmedia.SocialMediaPostResponse;
import com.dteema.dteema.model.User;
import com.dteema.dteema.model.socialmedia.SocialMediaPost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SocialMediaPostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", source = "author")
    @Mapping(target = "content", expression = "java(trimContent(request.getContent()))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SocialMediaPost toModel(CreateSocialMediaPostRequest request, User author);

    SocialMediaPostResponse toResponse(SocialMediaPost post);

    default String trimContent(String content) {
        return content == null ? null : content.trim();
    }
}

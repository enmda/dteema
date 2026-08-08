package com.dteema.dteema.repository;

import com.dteema.dteema.model.socialmedia.SocialMediaPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SocialMediaPostRepository extends JpaRepository<SocialMediaPost, UUID> {

    Page<SocialMediaPost> findAllByOrderByCreatedAtDesc(Pageable pageable);
}

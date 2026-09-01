package com.dteema.dteema.repository;

import com.dteema.dteema.model.chatting.SessionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SessionParticipantRepository extends JpaRepository<SessionParticipant, UUID> {

    long countBySessionId(UUID sessionId);

    boolean existsBySessionIdAndUserId(UUID sessionId, UUID userId);

    Optional<SessionParticipant> findBySessionIdAndUserId(UUID sessionId, UUID userId);
}

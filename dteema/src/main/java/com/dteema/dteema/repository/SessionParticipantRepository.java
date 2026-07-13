package com.dteema.dteema.repository;

import com.dteema.dteema.model.SessionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionParticipantRepository extends JpaRepository<UUID, SessionParticipant> {

}

package com.dteema.dteema.repository;

import com.dteema.dteema.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionRepository extends JpaRepository<UUID, Session> {

}

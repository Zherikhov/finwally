package com.finwally.infrastructure.persistence.repository;

import com.finwally.domain.entity.UserSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserSessionJpaRepository extends JpaRepository<UserSessionEntity, UUID> {
}

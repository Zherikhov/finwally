package com.finwally.infrastructure.persistence.repository;

import com.finwally.domain.entity.UserIdentityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserIdentityJpaRepository extends JpaRepository<UserIdentityEntity, String> {
}

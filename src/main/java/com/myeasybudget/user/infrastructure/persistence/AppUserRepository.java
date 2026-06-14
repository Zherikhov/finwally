package com.myeasybudget.user.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUserEntity, UUID> {

    boolean existsByEmailNormalizedAndDeletedAtIsNull(String emailNormalized);

    Optional<AppUserEntity> findByIdAndDeletedAtIsNull(UUID id);
}

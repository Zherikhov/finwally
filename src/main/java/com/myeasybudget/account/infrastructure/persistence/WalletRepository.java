package com.myeasybudget.account.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {

    List<WalletEntity> findByUserIdAndDeletedAtIsNullOrderBySortOrderAscNameAsc(UUID userId);

    Optional<WalletEntity> findByIdAndUserIdAndDeletedAtIsNull(UUID id, UUID userId);

    boolean existsByUserIdAndNameNormalizedAndDeletedAtIsNull(UUID userId, String nameNormalized);

    boolean existsByUserIdAndNameNormalizedAndDeletedAtIsNullAndIdNot(
            UUID userId, String nameNormalized, UUID id);

    Optional<WalletEntity> findByUserIdAndIsDefaultTrueAndDeletedAtIsNullAndArchivedAtIsNull(UUID userId);
}

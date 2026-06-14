package com.myeasybudget.transaction.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FinancialTransactionRepository
        extends JpaRepository<FinancialTransactionEntity, UUID>,
                JpaSpecificationExecutor<FinancialTransactionEntity> {

    @EntityGraph(attributePaths = {"entries", "entries.wallet", "entries.category"})
    Optional<FinancialTransactionEntity> findByIdAndUserIdAndDeletedAtIsNull(UUID id, UUID userId);
}

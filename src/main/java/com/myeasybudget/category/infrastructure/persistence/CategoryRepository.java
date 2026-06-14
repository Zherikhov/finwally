package com.myeasybudget.category.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    List<CategoryEntity> findByUserIdAndDeletedAtIsNullOrderByTypeAscSortOrderAscNameAsc(UUID userId);

    List<CategoryEntity> findByUserIdAndTypeAndDeletedAtIsNullOrderBySortOrderAscNameAsc(
            UUID userId, CategoryType type);

    Optional<CategoryEntity> findByIdAndUserIdAndDeletedAtIsNull(UUID id, UUID userId);

    /**
     * Name-uniqueness check that mirrors the partial unique index in the DB, treating a
     * null parent as the synthetic root used by that index. The optional {@code excludeId}
     * lets updates ignore the row being edited.
     */
    @Query("""
            select count(c) > 0 from CategoryEntity c
            where c.user.id = :userId
              and c.type = :type
              and c.nameNormalized = :nameNormalized
              and c.deletedAt is null
              and (:parentId is null and c.parent is null
                   or c.parent.id = :parentId)
              and (:excludeId is null or c.id <> :excludeId)
            """)
    boolean existsSiblingWithName(
            @Param("userId") UUID userId,
            @Param("type") CategoryType type,
            @Param("parentId") UUID parentId,
            @Param("nameNormalized") String nameNormalized,
            @Param("excludeId") UUID excludeId);

    boolean existsByParentIdAndDeletedAtIsNull(UUID parentId);
}

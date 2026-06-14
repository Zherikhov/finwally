package com.myeasybudget.category.application;

import com.myeasybudget.category.infrastructure.persistence.CategoryEntity;
import com.myeasybudget.category.infrastructure.persistence.CategoryType;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CategoryView(
        UUID id,
        CategoryType type,
        String name,
        UUID parentId,
        String icon,
        String color,
        int sortOrder,
        OffsetDateTime archivedAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public static CategoryView from(CategoryEntity category) {
        return new CategoryView(
                category.getId(),
                category.getType(),
                category.getName(),
                category.getParent() == null ? null : category.getParent().getId(),
                category.getIcon(),
                category.getColor(),
                category.getSortOrder() == null ? 0 : category.getSortOrder(),
                category.getArchivedAt(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}

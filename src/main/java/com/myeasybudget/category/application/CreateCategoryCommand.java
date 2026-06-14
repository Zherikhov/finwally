package com.myeasybudget.category.application;

import com.myeasybudget.category.infrastructure.persistence.CategoryType;
import java.util.UUID;

public record CreateCategoryCommand(
        CategoryType type,
        String name,
        UUID parentId,
        String icon,
        String color,
        Integer sortOrder
) {
}

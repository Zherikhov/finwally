package com.myeasybudget.category.application;

import java.util.UUID;

public record UpdateCategoryCommand(
        String name,
        UUID parentId,
        String icon,
        String color,
        Integer sortOrder
) {
}

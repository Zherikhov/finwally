package com.myeasybudget.category.presentation;

import com.myeasybudget.category.application.CreateCategoryCommand;
import com.myeasybudget.category.infrastructure.persistence.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CreateCategoryRequest(
        @NotNull
        CategoryType type,

        @NotBlank
        @Size(max = 120)
        String name,

        UUID parentId,

        @Size(max = 64)
        String icon,

        @Size(max = 32)
        String color,

        Integer sortOrder
) {

    public CreateCategoryCommand toCommand() {
        return new CreateCategoryCommand(type, name, parentId, icon, color, sortOrder);
    }
}

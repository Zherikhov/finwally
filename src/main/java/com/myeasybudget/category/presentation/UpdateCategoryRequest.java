package com.myeasybudget.category.presentation;

import com.myeasybudget.category.application.UpdateCategoryCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record UpdateCategoryRequest(
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

    public UpdateCategoryCommand toCommand() {
        return new UpdateCategoryCommand(name, parentId, icon, color, sortOrder);
    }
}

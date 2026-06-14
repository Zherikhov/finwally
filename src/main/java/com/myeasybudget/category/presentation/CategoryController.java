package com.myeasybudget.category.presentation;

import com.myeasybudget.category.application.CategoryService;
import com.myeasybudget.category.application.CategoryView;
import com.myeasybudget.category.infrastructure.persistence.CategoryType;
import com.myeasybudget.user.security.AuthenticatedUserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryView> list(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @RequestParam(required = false) CategoryType type) {
        return categoryService.listCategories(principal.userId(), type);
    }

    @GetMapping("/{categoryId}")
    public CategoryView get(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable UUID categoryId) {
        return categoryService.getCategory(principal.userId(), categoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryView create(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @Valid @RequestBody CreateCategoryRequest request) {
        return categoryService.createCategory(principal.userId(), request.toCommand());
    }

    @PutMapping("/{categoryId}")
    public CategoryView update(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable UUID categoryId,
            @Valid @RequestBody UpdateCategoryRequest request) {
        return categoryService.updateCategory(principal.userId(), categoryId, request.toCommand());
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable UUID categoryId) {
        categoryService.deleteCategory(principal.userId(), categoryId);
    }
}

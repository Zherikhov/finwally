package com.myeasybudget.category.application;

import com.myeasybudget.category.infrastructure.persistence.CategoryEntity;
import com.myeasybudget.category.infrastructure.persistence.CategoryRepository;
import com.myeasybudget.category.infrastructure.persistence.CategoryType;
import com.myeasybudget.shared.application.BusinessRuleViolationException;
import com.myeasybudget.shared.application.ResourceConflictException;
import com.myeasybudget.shared.application.ResourceNotFoundException;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionEntryRepository;
import com.myeasybudget.user.infrastructure.persistence.AppUserRepository;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionEntryRepository transactionEntryRepository;
    private final AppUserRepository appUserRepository;

    public CategoryService(
            CategoryRepository categoryRepository,
            TransactionEntryRepository transactionEntryRepository,
            AppUserRepository appUserRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.transactionEntryRepository = transactionEntryRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryView> listCategories(UUID userId, CategoryType type) {
        List<CategoryEntity> categories = (type == null)
                ? categoryRepository.findByUserIdAndDeletedAtIsNullOrderByTypeAscSortOrderAscNameAsc(userId)
                : categoryRepository.findByUserIdAndTypeAndDeletedAtIsNullOrderBySortOrderAscNameAsc(userId, type);
        return categories.stream().map(CategoryView::from).toList();
    }

    @Transactional(readOnly = true)
    public CategoryView getCategory(UUID userId, UUID categoryId) {
        return CategoryView.from(requireCategory(userId, categoryId));
    }

    @Transactional
    public CategoryView createCategory(UUID userId, CreateCategoryCommand command) {
        String name = command.name().trim();
        String normalized = normalize(name);

        CategoryEntity parent = resolveParent(userId, command.parentId(), command.type());

        if (categoryRepository.existsSiblingWithName(
                userId, command.type(), command.parentId(), normalized, null)) {
            throw new ResourceConflictException("A category with this name already exists at this level.");
        }

        CategoryEntity category = new CategoryEntity();
        category.setUser(appUserRepository.getReferenceById(userId));
        category.setParent(parent);
        category.setType(command.type());
        category.setName(name);
        category.setNameNormalized(normalized);
        category.setIcon(blankToNull(command.icon()));
        category.setColor(blankToNull(command.color()));
        category.setSortOrder(command.sortOrder() == null ? 0 : command.sortOrder());

        try {
            return CategoryView.from(categoryRepository.saveAndFlush(category));
        } catch (DataIntegrityViolationException ex) {
            throw new ResourceConflictException("A category with this name already exists at this level.");
        }
    }

    @Transactional
    public CategoryView updateCategory(UUID userId, UUID categoryId, UpdateCategoryCommand command) {
        CategoryEntity category = requireCategory(userId, categoryId);

        String name = command.name().trim();
        String normalized = normalize(name);

        CategoryEntity parent = resolveParent(userId, command.parentId(), category.getType());
        guardAgainstCycle(category, parent);

        if (categoryRepository.existsSiblingWithName(
                userId, category.getType(), command.parentId(), normalized, categoryId)) {
            throw new ResourceConflictException("A category with this name already exists at this level.");
        }

        category.setParent(parent);
        category.setName(name);
        category.setNameNormalized(normalized);
        category.setIcon(blankToNull(command.icon()));
        category.setColor(blankToNull(command.color()));
        if (command.sortOrder() != null) {
            category.setSortOrder(command.sortOrder());
        }

        try {
            categoryRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ResourceConflictException("A category with this name already exists at this level.");
        }
        return CategoryView.from(category);
    }

    @Transactional
    public void deleteCategory(UUID userId, UUID categoryId) {
        CategoryEntity category = requireCategory(userId, categoryId);
        if (categoryRepository.existsByParentIdAndDeletedAtIsNull(categoryId)) {
            throw new ResourceConflictException("This category has sub-categories and cannot be deleted.");
        }
        if (transactionEntryRepository.existsByCategoryIdAndUserId(categoryId, userId)) {
            throw new ResourceConflictException(
                    "This category is used by transactions and cannot be deleted. Archive it instead.");
        }
        category.setDeletedAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    private CategoryEntity resolveParent(UUID userId, UUID parentId, CategoryType childType) {
        if (parentId == null) {
            return null;
        }
        CategoryEntity parent = requireCategory(userId, parentId);
        if (parent.getType() != childType) {
            throw new BusinessRuleViolationException("A category must have the same type as its parent.");
        }
        return parent;
    }

    private void guardAgainstCycle(CategoryEntity category, CategoryEntity proposedParent) {
        CategoryEntity cursor = proposedParent;
        while (cursor != null) {
            if (cursor.getId().equals(category.getId())) {
                throw new BusinessRuleViolationException("A category cannot be its own ancestor.");
            }
            cursor = cursor.getParent();
        }
    }

    private CategoryEntity requireCategory(UUID userId, UUID categoryId) {
        return categoryRepository.findByIdAndUserIdAndDeletedAtIsNull(categoryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category was not found."));
    }

    private static String normalize(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}

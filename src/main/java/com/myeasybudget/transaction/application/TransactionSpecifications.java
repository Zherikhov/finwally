package com.myeasybudget.transaction.application;

import com.myeasybudget.transaction.infrastructure.persistence.FinancialTransactionEntity;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionEntryEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

/**
 * Builds a {@link Specification} for transaction history queries from optional filters.
 * Filters that touch line items (wallet/category) join {@code entries}; the query is made
 * distinct so a header is not returned once per matching entry.
 */
final class TransactionSpecifications {

    private TransactionSpecifications() {
    }

    static Specification<FinancialTransactionEntity> forUserAndCriteria(
            UUID userId, TransactionSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("user").get("id"), userId));
            predicates.add(cb.isNull(root.get("deletedAt")));

            if (criteria.type() != null) {
                predicates.add(cb.equal(root.get("type"), criteria.type()));
            }
            if (criteria.status() != null) {
                predicates.add(cb.equal(root.get("status"), criteria.status()));
            }
            if (criteria.source() != null) {
                predicates.add(cb.equal(root.get("source"), criteria.source()));
            }
            if (criteria.dateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("occurredDate"), criteria.dateFrom()));
            }
            if (criteria.dateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("occurredDate"), criteria.dateTo()));
            }

            boolean joinsEntries = criteria.walletId() != null || criteria.categoryId() != null;
            if (joinsEntries) {
                Join<FinancialTransactionEntity, TransactionEntryEntity> entries = root.join("entries");
                if (criteria.walletId() != null) {
                    predicates.add(cb.equal(entries.get("wallet").get("id"), criteria.walletId()));
                }
                if (criteria.categoryId() != null) {
                    predicates.add(cb.equal(entries.get("category").get("id"), criteria.categoryId()));
                }
                if (query != null) {
                    query.distinct(true);
                }
            }

            if (criteria.query() != null && !criteria.query().isBlank()) {
                String like = "%" + criteria.query().trim().toLowerCase(Locale.ROOT) + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), like),
                        cb.like(cb.lower(root.get("description")), like),
                        cb.like(cb.lower(root.get("merchantName")), like)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

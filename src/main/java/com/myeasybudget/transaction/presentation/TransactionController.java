package com.myeasybudget.transaction.presentation;

import com.myeasybudget.shared.presentation.PageResponse;
import com.myeasybudget.transaction.application.TransactionSearchCriteria;
import com.myeasybudget.transaction.application.TransactionService;
import com.myeasybudget.transaction.application.TransactionView;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionSource;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionStatus;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionType;
import com.myeasybudget.user.security.AuthenticatedUserPrincipal;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * History + search. All filters are optional; results are paginated and default to
     * most-recent first.
     */
    @GetMapping
    public PageResponse<TransactionView> search(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) TransactionStatus status,
            @RequestParam(required = false) TransactionSource source,
            @RequestParam(required = false) UUID walletId,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(required = false) String query,
            @PageableDefault(size = 20, sort = "occurredDate", direction = Sort.Direction.DESC) Pageable pageable) {
        TransactionSearchCriteria criteria = new TransactionSearchCriteria(
                type, status, source, walletId, categoryId, dateFrom, dateTo, query);
        return PageResponse.from(transactionService.search(principal.userId(), criteria, pageable));
    }

    @GetMapping("/{transactionId}")
    public TransactionView get(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable UUID transactionId) {
        return transactionService.getTransaction(principal.userId(), transactionId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionView create(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @Valid @RequestBody CreateTransactionRequest request) {
        return transactionService.createTransaction(principal.userId(), request.toCommand());
    }

    @PutMapping("/{transactionId}")
    public TransactionView update(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable UUID transactionId,
            @Valid @RequestBody UpdateTransactionRequest request) {
        return transactionService.updateTransaction(principal.userId(), transactionId, request.toCommand());
    }

    @DeleteMapping("/{transactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable UUID transactionId) {
        transactionService.deleteTransaction(principal.userId(), transactionId);
    }
}

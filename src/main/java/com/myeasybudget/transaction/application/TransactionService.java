package com.myeasybudget.transaction.application;

import com.myeasybudget.account.infrastructure.persistence.WalletEntity;
import com.myeasybudget.account.infrastructure.persistence.WalletRepository;
import com.myeasybudget.category.infrastructure.persistence.CategoryEntity;
import com.myeasybudget.category.infrastructure.persistence.CategoryRepository;
import com.myeasybudget.shared.application.BusinessRuleViolationException;
import com.myeasybudget.shared.application.ResourceNotFoundException;
import com.myeasybudget.transaction.infrastructure.persistence.FinancialTransactionEntity;
import com.myeasybudget.transaction.infrastructure.persistence.FinancialTransactionRepository;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionEntryEntity;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionSource;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionStatus;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionType;
import com.myeasybudget.user.infrastructure.persistence.AppUserRepository;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private final FinancialTransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;
    private final AppUserRepository appUserRepository;

    public TransactionService(
            FinancialTransactionRepository transactionRepository,
            WalletRepository walletRepository,
            CategoryRepository categoryRepository,
            AppUserRepository appUserRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.categoryRepository = categoryRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional(readOnly = true)
    public Page<TransactionView> search(UUID userId, TransactionSearchCriteria criteria, Pageable pageable) {
        return transactionRepository
                .findAll(TransactionSpecifications.forUserAndCriteria(userId, criteria), pageable)
                .map(TransactionView::from);
    }

    @Transactional(readOnly = true)
    public TransactionView getTransaction(UUID userId, UUID transactionId) {
        return TransactionView.from(requireTransaction(userId, transactionId));
    }

    @Transactional
    public TransactionView createTransaction(UUID userId, CreateTransactionCommand command) {
        FinancialTransactionEntity transaction = new FinancialTransactionEntity();
        transaction.setUser(appUserRepository.getReferenceById(userId));
        transaction.setType(command.type());
        transaction.setStatus(command.status() == null ? TransactionStatus.POSTED : command.status());
        transaction.setSource(TransactionSource.MANUAL);
        applyHeader(transaction, command.occurredAt(), command.title(), command.description(),
                command.merchantName());
        applyEntries(userId, transaction, command.type(), command.entries());

        return TransactionView.from(transactionRepository.saveAndFlush(transaction));
    }

    @Transactional
    public TransactionView updateTransaction(UUID userId, UUID transactionId, UpdateTransactionCommand command) {
        FinancialTransactionEntity transaction = requireTransaction(userId, transactionId);

        transaction.setType(command.type());
        transaction.setStatus(command.status() == null ? transaction.getStatus() : command.status());
        applyHeader(transaction, command.occurredAt(), command.title(), command.description(),
                command.merchantName());

        // Replace line items wholesale; orphanRemoval deletes the previous ones.
        transaction.getEntries().clear();
        applyEntries(userId, transaction, command.type(), command.entries());

        return TransactionView.from(transactionRepository.saveAndFlush(transaction));
    }

    @Transactional
    public void deleteTransaction(UUID userId, UUID transactionId) {
        FinancialTransactionEntity transaction = requireTransaction(userId, transactionId);
        transaction.setDeletedAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    private void applyHeader(
            FinancialTransactionEntity transaction,
            OffsetDateTime occurredAt,
            String title,
            String description,
            String merchantName) {
        if (occurredAt == null) {
            throw new BusinessRuleViolationException("A transaction must have an occurrence date/time.");
        }
        transaction.setOccurredAt(occurredAt);
        transaction.setOccurredDate(occurredAt.toLocalDate());
        transaction.setTitle(blankToNull(title));
        transaction.setDescription(blankToNull(description));
        transaction.setMerchantName(blankToNull(merchantName));
    }

    private void applyEntries(
            UUID userId,
            FinancialTransactionEntity transaction,
            TransactionType type,
            List<EntryCommand> entryCommands) {
        if (entryCommands == null || entryCommands.isEmpty()) {
            throw new BusinessRuleViolationException("A transaction must have at least one entry.");
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (EntryCommand entryCommand : entryCommands) {
            BigDecimal amount = entryCommand.amount();
            if (amount == null || amount.signum() == 0) {
                throw new BusinessRuleViolationException("Each entry amount must be a non-zero value.");
            }

            WalletEntity wallet = walletRepository
                    .findByIdAndUserIdAndDeletedAtIsNull(entryCommand.walletId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Wallet was not found."));

            CategoryEntity category = null;
            if (entryCommand.categoryId() != null) {
                category = categoryRepository
                        .findByIdAndUserIdAndDeletedAtIsNull(entryCommand.categoryId(), userId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category was not found."));
            }

            TransactionEntryEntity entry = new TransactionEntryEntity();
            entry.setTransaction(transaction);
            entry.setUser(transaction.getUser());
            entry.setWallet(wallet);
            entry.setCategory(category);
            entry.setAmount(amount);
            entry.setCurrencyCode(resolveCurrency(entryCommand.currencyCode(), wallet));
            entry.setNote(blankToNull(entryCommand.note()));
            transaction.getEntries().add(entry);

            sum = sum.add(amount);
        }

        enforceLedgerRules(type, transaction.getEntries(), sum);
    }

    private static void enforceLedgerRules(
            TransactionType type, List<TransactionEntryEntity> entries, BigDecimal sum) {
        switch (type) {
            case EXPENSE -> {
                if (entries.stream().anyMatch(e -> e.getAmount().signum() > 0)) {
                    throw new BusinessRuleViolationException("Expense entries must be negative amounts.");
                }
            }
            case INCOME -> {
                if (entries.stream().anyMatch(e -> e.getAmount().signum() < 0)) {
                    throw new BusinessRuleViolationException("Income entries must be positive amounts.");
                }
            }
            case TRANSFER -> {
                if (entries.size() < 2) {
                    throw new BusinessRuleViolationException("A transfer needs at least two entries.");
                }
                if (sum.signum() != 0) {
                    throw new BusinessRuleViolationException("Transfer entries must net to zero.");
                }
            }
            case ADJUSTMENT -> {
                // Any non-zero set of entries is allowed for manual corrections.
            }
        }
    }

    private FinancialTransactionEntity requireTransaction(UUID userId, UUID transactionId) {
        return transactionRepository.findByIdAndUserIdAndDeletedAtIsNull(transactionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction was not found."));
    }

    private static String resolveCurrency(String currencyCode, WalletEntity wallet) {
        if (currencyCode == null || currencyCode.isBlank()) {
            return wallet.getCurrencyCode();
        }
        return currencyCode.trim().toUpperCase(Locale.ROOT);
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}

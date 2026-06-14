package com.myeasybudget.account.application;

import com.myeasybudget.account.infrastructure.persistence.WalletEntity;
import com.myeasybudget.account.infrastructure.persistence.WalletRepository;
import com.myeasybudget.shared.application.ResourceConflictException;
import com.myeasybudget.shared.application.ResourceNotFoundException;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionEntryRepository;
import com.myeasybudget.user.infrastructure.persistence.AppUserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionEntryRepository transactionEntryRepository;
    private final AppUserRepository appUserRepository;

    public WalletService(
            WalletRepository walletRepository,
            TransactionEntryRepository transactionEntryRepository,
            AppUserRepository appUserRepository
    ) {
        this.walletRepository = walletRepository;
        this.transactionEntryRepository = transactionEntryRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional(readOnly = true)
    public List<WalletView> listWallets(UUID userId) {
        List<WalletEntity> wallets =
                walletRepository.findByUserIdAndDeletedAtIsNullOrderBySortOrderAscNameAsc(userId);
        Map<UUID, BigDecimal> movements = transactionEntryRepository.sumPostedAmountsByWallet(userId).stream()
                .collect(Collectors.toMap(WalletBalanceView::walletId, WalletBalanceView::total));
        return wallets.stream()
                .map(wallet -> WalletView.from(wallet, currentBalance(wallet, movements.get(wallet.getId()))))
                .toList();
    }

    @Transactional(readOnly = true)
    public WalletView getWallet(UUID userId, UUID walletId) {
        WalletEntity wallet = requireWallet(userId, walletId);
        BigDecimal movement = transactionEntryRepository.sumPostedAmountForWallet(userId, walletId);
        return WalletView.from(wallet, currentBalance(wallet, movement));
    }

    @Transactional
    public WalletView createWallet(UUID userId, CreateWalletCommand command) {
        String name = command.name().trim();
        String normalized = normalize(name);
        if (walletRepository.existsByUserIdAndNameNormalizedAndDeletedAtIsNull(userId, normalized)) {
            throw new ResourceConflictException("A wallet with this name already exists.");
        }

        WalletEntity wallet = new WalletEntity();
        wallet.setUser(appUserRepository.getReferenceById(userId));
        wallet.setName(name);
        wallet.setNameNormalized(normalized);
        wallet.setType(command.type());
        wallet.setCurrencyCode(command.currencyCode().trim().toUpperCase(Locale.ROOT));
        wallet.setOpeningBalance(command.openingBalance() == null ? BigDecimal.ZERO : command.openingBalance());
        wallet.setOpeningBalanceDate(
                command.openingBalanceDate() == null ? LocalDate.now() : command.openingBalanceDate());
        wallet.setSortOrder(command.sortOrder() == null ? 0 : command.sortOrder());

        if (command.isDefault()) {
            clearExistingDefault(userId);
            wallet.setIsDefault(true);
        } else {
            wallet.setIsDefault(false);
        }

        try {
            WalletEntity saved = walletRepository.saveAndFlush(wallet);
            return WalletView.from(saved, saved.getOpeningBalance());
        } catch (DataIntegrityViolationException ex) {
            throw new ResourceConflictException("A wallet with this name already exists.");
        }
    }

    @Transactional
    public WalletView updateWallet(UUID userId, UUID walletId, UpdateWalletCommand command) {
        WalletEntity wallet = requireWallet(userId, walletId);

        String name = command.name().trim();
        String normalized = normalize(name);
        if (walletRepository.existsByUserIdAndNameNormalizedAndDeletedAtIsNullAndIdNot(userId, normalized, walletId)) {
            throw new ResourceConflictException("A wallet with this name already exists.");
        }

        wallet.setName(name);
        wallet.setNameNormalized(normalized);
        wallet.setType(command.type());
        wallet.setCurrencyCode(command.currencyCode().trim().toUpperCase(Locale.ROOT));
        if (command.openingBalance() != null) {
            wallet.setOpeningBalance(command.openingBalance());
        }
        if (command.openingBalanceDate() != null) {
            wallet.setOpeningBalanceDate(command.openingBalanceDate());
        }
        if (command.sortOrder() != null) {
            wallet.setSortOrder(command.sortOrder());
        }

        if (command.isDefault() && !Boolean.TRUE.equals(wallet.getIsDefault())) {
            clearExistingDefault(userId);
            wallet.setIsDefault(true);
        } else if (!command.isDefault()) {
            wallet.setIsDefault(false);
        }

        try {
            walletRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ResourceConflictException("A wallet with this name already exists.");
        }
        BigDecimal movement = transactionEntryRepository.sumPostedAmountForWallet(userId, walletId);
        return WalletView.from(wallet, currentBalance(wallet, movement));
    }

    @Transactional
    public void deleteWallet(UUID userId, UUID walletId) {
        WalletEntity wallet = requireWallet(userId, walletId);
        if (transactionEntryRepository.existsByWalletIdAndUserId(walletId, userId)) {
            throw new ResourceConflictException(
                    "This wallet has transactions and cannot be deleted. Archive it instead.");
        }
        wallet.setDeletedAt(java.time.OffsetDateTime.now(java.time.ZoneOffset.UTC));
        wallet.setIsDefault(false);
    }

    private WalletEntity requireWallet(UUID userId, UUID walletId) {
        return walletRepository.findByIdAndUserIdAndDeletedAtIsNull(walletId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet was not found."));
    }

    private void clearExistingDefault(UUID userId) {
        walletRepository.findByUserIdAndIsDefaultTrueAndDeletedAtIsNullAndArchivedAtIsNull(userId)
                .ifPresent(existing -> existing.setIsDefault(false));
    }

    private static BigDecimal currentBalance(WalletEntity wallet, BigDecimal movement) {
        BigDecimal opening = wallet.getOpeningBalance() == null ? BigDecimal.ZERO : wallet.getOpeningBalance();
        return opening.add(movement == null ? BigDecimal.ZERO : movement);
    }

    private static String normalize(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }
}

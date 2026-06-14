package com.myeasybudget.transaction.infrastructure.persistence;

import com.myeasybudget.account.application.WalletBalanceView;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionEntryRepository extends JpaRepository<TransactionEntryEntity, UUID> {

    /**
     * Net movement (sum of signed amounts) per wallet across all POSTED, non-deleted
     * transactions for a user. Combined with each wallet's opening balance this yields
     * the current balance. Wallets with no entries are simply absent from the result.
     */
    @Query("""
            select new com.myeasybudget.account.application.WalletBalanceView(
                e.wallet.id, coalesce(sum(e.amount), 0))
            from TransactionEntryEntity e
            where e.user.id = :userId
              and e.transaction.status = com.myeasybudget.transaction.infrastructure.persistence.TransactionStatus.POSTED
              and e.transaction.deletedAt is null
            group by e.wallet.id
            """)
    List<WalletBalanceView> sumPostedAmountsByWallet(@Param("userId") UUID userId);

    @Query("""
            select coalesce(sum(e.amount), 0)
            from TransactionEntryEntity e
            where e.user.id = :userId
              and e.wallet.id = :walletId
              and e.transaction.status = com.myeasybudget.transaction.infrastructure.persistence.TransactionStatus.POSTED
              and e.transaction.deletedAt is null
            """)
    BigDecimal sumPostedAmountForWallet(@Param("userId") UUID userId, @Param("walletId") UUID walletId);

    boolean existsByWalletIdAndUserId(UUID walletId, UUID userId);

    boolean existsByCategoryIdAndUserId(UUID categoryId, UUID userId);
}

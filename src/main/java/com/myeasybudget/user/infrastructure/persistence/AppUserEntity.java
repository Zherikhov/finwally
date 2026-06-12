package com.myeasybudget.user.infrastructure.persistence;

import com.myeasybudget.account.infrastructure.persistence.WalletEntity;
import com.myeasybudget.category.infrastructure.persistence.CategoryEntity;
import com.myeasybudget.shared.infrastructure.persistence.BaseAuditableEntity;
import com.myeasybudget.transaction.infrastructure.persistence.FinancialTransactionEntity;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionEntryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "app_users")
public class AppUserEntity extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "email", nullable = false, length = 320)
    private String email;

    @Column(name = "email_normalized", nullable = false, length = 320)
    private String emailNormalized;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "avatar_url", columnDefinition = "text")
    private String avatarUrl;

    @Column(name = "default_currency_code", nullable = false, length = 3)
    private String defaultCurrencyCode = "EUR";

    @Column(name = "locale", nullable = false, length = 10)
    private String locale = "en";

    @Column(name = "timezone", nullable = false, length = 64)
    private String timezone = "Europe/Berlin";

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private UserStatus status = UserStatus.ACTIVE;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<AuthIdentityEntity> authIdentities = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<WalletEntity> wallets = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<CategoryEntity> categories = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<FinancialTransactionEntity> financialTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<TransactionEntryEntity> transactionEntries = new ArrayList<>();
}

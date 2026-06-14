package com.myeasybudget.account.presentation;

import com.myeasybudget.account.application.WalletService;
import com.myeasybudget.account.application.WalletView;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping
    public List<WalletView> list(@AuthenticationPrincipal AuthenticatedUserPrincipal principal) {
        return walletService.listWallets(principal.userId());
    }

    @GetMapping("/{walletId}")
    public WalletView get(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable UUID walletId) {
        return walletService.getWallet(principal.userId(), walletId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WalletView create(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @Valid @RequestBody CreateWalletRequest request) {
        return walletService.createWallet(principal.userId(), request.toCommand());
    }

    @PutMapping("/{walletId}")
    public WalletView update(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable UUID walletId,
            @Valid @RequestBody UpdateWalletRequest request) {
        return walletService.updateWallet(principal.userId(), walletId, request.toCommand());
    }

    @DeleteMapping("/{walletId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable UUID walletId) {
        walletService.deleteWallet(principal.userId(), walletId);
    }
}

package com.finwally.application.service;

import com.finwally.domain.entity.UserEntity;
import com.finwally.domain.enums.UserTimeZoneSettings;
import com.finwally.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<UserEntity> findById(UUID userId) {
        return userJpaRepository.findById(userId);
    }

    @Transactional
    public void save(UserEntity userEntity) {
        log.debug("Saving user with email: {}", userEntity.getEmailNormalized());
        userJpaRepository.save(userEntity);
    }

    @Transactional
    public boolean existsByEmailNormalized(String email) {
        return userJpaRepository.existsByEmailNormalized(email);
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findByEmail(String email) {
        return userJpaRepository.findByEmailNormalized(email.trim().toLowerCase());
    }

    @Transactional(readOnly = true)
    public UserEntity authenticate(String email, String rawPassword) {
        UserEntity user = findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user;
    }

    @Transactional
    public void createUser(String email, String rawPassword, String displayName, String timezone) {
        log.info("Creating new user with email: {}", email);
        String normalizedEmail = email.trim().toLowerCase();

        if (existsByEmailNormalized(normalizedEmail)) {
            log.warn("Attempt to create user with existing email: {}", normalizedEmail);
            throw new IllegalArgumentException("User with this email already exists");
        }

        UserTimeZoneSettings settings = UserTimeZoneSettings.fromTimezone(timezone);

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setEmailNormalized(normalizedEmail);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setDisplayName(displayName);
        user.setTimezone(settings.getTimezone());
        user.setLocale(settings.getLocale());
        user.setBaseCurrencyCode(settings.getBaseCurrencyCode());

        UserEntity savedUser = userJpaRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
    }
}

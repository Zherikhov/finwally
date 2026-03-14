package com.finwally.application.service;

import com.finwally.domain.entity.UserSessionEntity;
import com.finwally.infrastructure.persistence.repository.UserSessionJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSessionService {

    private final UserSessionJpaRepository userSessionJpaRepository;

    @Transactional(readOnly = true)
    public Optional<UserSessionEntity> findById(UUID id) {
        return userSessionJpaRepository.findById(id);
    }

    @Transactional
    public UserSessionEntity save(UserSessionEntity userSession) {
        log.debug("Saving session for user: {}", userSession.getUser().getId());
        return userSessionJpaRepository.save(userSession);
    }
}

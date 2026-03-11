package com.finwally.application.service;

import com.finwally.domain.entity.UserEntity;
import com.finwally.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;

    @Transactional(readOnly = true)
    public Optional<UserEntity> findById(UUID userId) {
        return userJpaRepository.findById(userId);
    }

    @Transactional
    public void save(UserEntity userEntity) {
        userJpaRepository.save(userEntity);
    }

    @Transactional
    public boolean existsByEmailNormalized(String email) {
        return userJpaRepository.existsByEmailNormalized(email);
    }
}

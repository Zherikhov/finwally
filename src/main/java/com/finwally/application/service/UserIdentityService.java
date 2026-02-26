package com.finwally.application.service;

import com.finwally.domain.entity.UserIdentityEntity;
import com.finwally.infrastructure.persistence.repository.UserIdentityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserIdentityService {

    private final UserIdentityJpaRepository userIdentityJpaRepository;

    @Transactional(readOnly = true)
    public Optional<UserIdentityEntity> findById(String id) {
        return userIdentityJpaRepository.findById(id);
    }

    @Transactional
    public UserIdentityEntity save(UserIdentityEntity userIdentity) {
        return userIdentityJpaRepository.save(userIdentity);
    }
}

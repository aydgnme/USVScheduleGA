package com.dm.service;

import com.dm.data.entity.SecretaryProfileEntity;
import com.dm.data.repository.SecretaryProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecretaryService {

    private final SecretaryProfileRepository secretaryProfileRepository;

    public SecretaryService(SecretaryProfileRepository secretaryProfileRepository) {
        this.secretaryProfileRepository = secretaryProfileRepository;
    }

    public Optional<SecretaryProfileEntity> findByUserEmail(String email) {
        return secretaryProfileRepository.findByUser_Email(email);
    }
}

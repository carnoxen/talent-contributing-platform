package com.bitor.tft.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bitor.tft.entity.Verification;

public interface VerificationRepository extends CrudRepository<Verification, Long> {
    public Optional<Verification> findByUsername(String username);

    public Optional<Verification> findByUsernameAndCode(String username, String code);
}

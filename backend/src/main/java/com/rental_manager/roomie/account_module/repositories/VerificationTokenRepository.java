package com.rental_manager.roomie.account_module.repositories;

import com.rental_manager.roomie.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    VerificationToken saveAndFlush(VerificationToken verificationToken);

    @Transactional(propagation = Propagation.MANDATORY)
    Optional<VerificationToken> findByTokenValueAndExpirationDateAfter(String tokenValue, LocalDateTime date);
}

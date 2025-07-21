package com.rental_manager.roomie.account_module.repositories;

import com.rental_manager.roomie.entities.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, UUID> {

    Optional<ResetPasswordToken> findByAccount_email(String email);

    Optional<ResetPasswordToken> findByTokenValueAndExpirationDateAfter(String tokenValue, LocalDateTime date);
}

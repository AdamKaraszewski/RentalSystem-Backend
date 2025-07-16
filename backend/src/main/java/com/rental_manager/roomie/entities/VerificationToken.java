package com.rental_manager.roomie.entities;

import com.rental_manager.roomie.config.database.DatabaseConstraints;
import com.rental_manager.roomie.config.database.DatabaseStructures;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = DatabaseStructures.TABLE_EMAIL_VERIFICATION_TOKENS)
@NoArgsConstructor
public class VerificationToken extends OneTimeUseToken {

    @Column(name = DatabaseStructures.TOKEN_VALUE_COLUMN, updatable = false, nullable = false, unique = true,
            length = DatabaseConstraints.EMAIL_VERIFICATION_TOKEN_LENGTH)
    @Getter
    private String tokenValue;

    public VerificationToken(String tokenValue , Account account, long tokenLifeTime) {
        super(account, LocalDateTime.now().plusMinutes(tokenLifeTime));
        this.tokenValue = tokenValue;
    }
}

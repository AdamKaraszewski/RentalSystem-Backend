package com.rental_manager.roomie.entities;

import com.rental_manager.roomie.config.database.DatabaseConstraints;
import com.rental_manager.roomie.config.database.DatabaseStructures;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = DatabaseStructures.TABLE_EMAIL_VERIFICATION_TOKENS)
public class VerificationToken extends AbstractEntity {

    @Column(name = DatabaseStructures.TOKEN_VALUE_COLUMN, updatable = false, nullable = false, unique = true,
            length = DatabaseConstraints.EMAIL_VERIFICATION_TOKEN_LENGTH)
    private String tokenValue;

    @Column(name = DatabaseStructures.EXPIRATION_DATE_COLUMN, nullable = false, updatable = false)
    private LocalDateTime expirationDate;

    @OneToOne
    @JoinColumn(name = DatabaseStructures.ACCOUNT_ID_COLUMN, referencedColumnName = DatabaseStructures.ID_COLUMN,
            updatable = false)
    private Account account;
}

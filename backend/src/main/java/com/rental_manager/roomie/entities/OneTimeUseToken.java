package com.rental_manager.roomie.entities;

import com.rental_manager.roomie.config.database.DatabaseConstraints;
import com.rental_manager.roomie.config.database.DatabaseStructures;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@NoArgsConstructor
public abstract class OneTimeUseToken {

    @Id
    private UUID id;

    @Column(name = DatabaseStructures.EXPIRATION_DATE_COLUMN, updatable = false, nullable = false)
    private LocalDateTime expirationDate;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    private Account account;

    @Version
    @Column(name = DatabaseStructures.VERSION_COLUMN, nullable = false)
    private Long version;

    @Column(name = DatabaseStructures.WAS_USED, nullable = false)
    @Setter
    private boolean wasUsed = false;

    public OneTimeUseToken(Account account, LocalDateTime expirationDate) {
        this.account = account;
        this.expirationDate = expirationDate;
    }
}

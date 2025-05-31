package com.rental_manager.roomie.entities;

import com.rental_manager.roomie.config.database.DatabaseConstraints;
import com.rental_manager.roomie.config.database.DatabaseStructures;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = DatabaseStructures.TABLE_ACCOUNT)
@NoArgsConstructor
public class Account extends AbstractEntity {

    @Column(name = DatabaseStructures.USERNAME_COLUMN, nullable = false,
            length = DatabaseConstraints.USERNAME_MAX_LENGTH)
    @Getter
    private String username;

    @Column(name = DatabaseStructures.IS_ACTIVE_COLUMN, nullable = false)
    @Getter @Setter
    private boolean isActive;

    @Column(name = DatabaseStructures.IS_VERIFIED_COLUMN, nullable = false)
    @Getter @Setter
    private boolean isVerified;
}

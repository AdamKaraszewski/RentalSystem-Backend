package com.rental_manager.roomie.entities;

import com.rental_manager.roomie.config.database.DatabaseStructures;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor
@Getter
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = DatabaseStructures.ID_COLUMN, nullable = false, updatable = false)
    private UUID id;

    @Version
    @Column(name = DatabaseStructures.VERSION_COLUMN, nullable = false)
    private Long version;
}

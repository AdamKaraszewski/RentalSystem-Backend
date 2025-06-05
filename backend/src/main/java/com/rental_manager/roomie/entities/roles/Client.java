package com.rental_manager.roomie.entities.roles;

import com.rental_manager.roomie.config.database.DatabaseStructures;
import com.rental_manager.roomie.entities.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = DatabaseStructures.TABLE_CLIENT_DATA)
public class Client extends Role {

    @Column(name = DatabaseStructures.SIMPLE_COLUMN)
    private String simpleColumn;
}

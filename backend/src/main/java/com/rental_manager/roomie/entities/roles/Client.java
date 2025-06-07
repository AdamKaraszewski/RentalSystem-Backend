package com.rental_manager.roomie.entities.roles;

import com.rental_manager.roomie.config.database.DatabaseStructures;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = DatabaseStructures.TABLE_CLIENT_DATA)
@NoArgsConstructor
public class Client extends Role {

    @Column(name = DatabaseStructures.SIMPLE_COLUMN)
    private String simpleColumn;

    public Client(Account account) {
        super(RolesEnum.CLIENT, account);
        this.simpleColumn = "CLIENT_SIMPLE_COLUMN_EXAMPLE_VALUE";
    }
}

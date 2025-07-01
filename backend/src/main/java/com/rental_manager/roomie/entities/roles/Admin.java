package com.rental_manager.roomie.entities.roles;

import com.rental_manager.roomie.config.database.DatabaseStructures;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = DatabaseStructures.TABLE_ADMIN_DATA)
@NoArgsConstructor
public class Admin extends Role {

    @Column(name = DatabaseStructures.SIMPLE_COLUMN)
    private String simpleColumn;

    public Admin(Account account) {
        super(RolesEnum.ADMIN, account);
        this.simpleColumn = "ADMIN_SIMPLE_COLUMN_EXAMPLE_VALUE";
    }
}

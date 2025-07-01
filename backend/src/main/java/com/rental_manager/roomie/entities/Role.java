package com.rental_manager.roomie.entities;

import com.rental_manager.roomie.config.database.DatabaseConstraints;
import com.rental_manager.roomie.config.database.DatabaseStructures;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = DatabaseStructures.TABLE_ACCOUNT_ROLE, uniqueConstraints = {
        @UniqueConstraint(
                name = DatabaseConstraints.UNIQUE_ACCOUNT_ID_ROLE,
                columnNames = {DatabaseStructures.ACCOUNT_ID_COLUMN, DatabaseStructures.ROLE_NAME_COLUMN}) })
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public abstract class Role extends AbstractEntity {

    @Column(name = DatabaseStructures.ROLE_NAME_COLUMN, nullable = false)
    @Enumerated(value = EnumType.STRING)
    @Getter @Setter
    private RolesEnum role;

    @Column(name = DatabaseStructures.IS_ACTIVE_COLUMN, nullable = false)
    @Getter @Setter
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = DatabaseStructures.ACCOUNT_ID_COLUMN, nullable = false)
    private Account account;

    public Role(RolesEnum role, Account account) {
        this.isActive = true;
        this.role = role;
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role1 = (Role) o;
        return role1.getRole() == this.role;
    }

    @Override
    public int hashCode() {
        return role.name().hashCode();
    }
}
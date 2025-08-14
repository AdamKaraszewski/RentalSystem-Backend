package com.rental_manager.roomie.entities;

import com.rental_manager.roomie.config.Constraints;
import com.rental_manager.roomie.config.database.DatabaseStructures;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = DatabaseStructures.TABLE_ACCOUNTS, uniqueConstraints = {
        @UniqueConstraint(
                name = Constraints.UNIQUE_USERNAME_INDEX,
                columnNames = {DatabaseStructures.USERNAME_COLUMN}) })
@SecondaryTable(
        name = DatabaseStructures.TABLE_PERSONAL_DATA,
        pkJoinColumns = @PrimaryKeyJoinColumn(name = DatabaseStructures.ACCOUNT_ID_COLUMN), uniqueConstraints = {
                @UniqueConstraint(
                        name = Constraints.UNIQUE_ACCOUNT_ID,
                        columnNames = DatabaseStructures.ACCOUNT_ID_COLUMN) })
@NoArgsConstructor
public class Account extends AbstractEntity {

    @Column(table = DatabaseStructures.TABLE_PERSONAL_DATA, name = DatabaseStructures.FIRST_NAME_COLUMN,
            nullable = false, length = Constraints.FIRST_NAME_MAX_LENGTH)
    @Getter @Setter
    private String firstName;

    @Column(table = DatabaseStructures.TABLE_PERSONAL_DATA, name = DatabaseStructures.LAST_NAME_COLUMN,
            nullable = false, length = Constraints.LAST_NAME_MAX_LENGTH)
    @Getter @Setter
    private String lastName;

    @Column(name = DatabaseStructures.USERNAME_COLUMN, nullable = false, updatable = false,
            length = Constraints.USERNAME_MAX_LENGTH)
    @Getter
    private String username;

    @Column(table = DatabaseStructures.TABLE_PERSONAL_DATA, name = DatabaseStructures.EMAIL_COLUMN, nullable = false,
            length = Constraints.EMAIL_MAX_LENGTH)
    @Getter @Setter
    private String email;

    @Column(name = DatabaseStructures.IS_ACTIVE_COLUMN, nullable = false)
    @Getter @Setter
    private boolean isActive = true;

    @Column(name = DatabaseStructures.IS_VERIFIED_COLUMN, nullable = false)
    @Getter @Setter
    private boolean isVerified = false;

    @Column(name = DatabaseStructures.PASSWORD_COLUMN, nullable = false,
            length = Constraints.PASSWORD_MAX_LENGTH)
    @Getter @Setter
    private String password;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @Getter
    private final Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
    }

    public Account(String firstName, String lastName, String username, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}

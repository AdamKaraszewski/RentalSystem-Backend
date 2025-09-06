package com.rental_manager.roomie.authentication_module.repositories;

import com.rental_manager.roomie.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountAuthenticationRepository extends JpaRepository<Account, UUID> {

    @Transactional(propagation = Propagation.MANDATORY)
    Optional<Account> findByEmail(String email);
}

package com.rental_manager.roomie.account_module.repositories;

import com.rental_manager.roomie.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Transactional(propagation = Propagation.MANDATORY)
    Optional<Account> findByEmail(String email);

    @Transactional(propagation = Propagation.MANDATORY)
    Optional<Account> findById(UUID id);

    @Transactional(propagation = Propagation.MANDATORY)
    List<Account> findAll();

    @Transactional(propagation = Propagation.MANDATORY)
    Account saveAndFlush(Account account);
}

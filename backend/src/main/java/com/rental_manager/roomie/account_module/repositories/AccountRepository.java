package com.rental_manager.roomie.account_module.repositories;

import com.rental_manager.roomie.entities.Account;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Transactional(propagation = Propagation.MANDATORY)
    Optional<Account> findByEmail(String email);

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    Optional<Account> findById(UUID id);

    @Transactional(propagation = Propagation.MANDATORY)
    List<Account> findAll();

    @Transactional(propagation = Propagation.MANDATORY)
    Account saveAndFlush(Account account);
}

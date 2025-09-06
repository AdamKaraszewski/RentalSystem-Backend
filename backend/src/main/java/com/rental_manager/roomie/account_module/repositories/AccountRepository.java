package com.rental_manager.roomie.account_module.repositories;

import com.rental_manager.roomie.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {

    @Transactional(propagation = Propagation.MANDATORY)
    Optional<Account> findByEmail(String email);

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    Optional<Account> findById(UUID id);

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    Account saveAndFlush(Account account);

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    Page<Account> findAll(Specification<Account> spec, Pageable pageable);
}

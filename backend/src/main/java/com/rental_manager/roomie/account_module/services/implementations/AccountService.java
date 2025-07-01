package com.rental_manager.roomie.account_module.services.implementations;

import com.rental_manager.roomie.account_module.repositories.AccountRepository;
import com.rental_manager.roomie.account_module.repositories.VerificationTokenRepository;
import com.rental_manager.roomie.account_module.services.interfaces.IAccountService;
import com.rental_manager.roomie.config.database.DatabaseConstraints;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.Role;
import com.rental_manager.roomie.entities.VerificationToken;
import com.rental_manager.roomie.entities.roles.Admin;
import com.rental_manager.roomie.entities.roles.Client;
import com.rental_manager.roomie.entities.roles.Landlord;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.AccountDoesNotOweAnyRoleException;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.RoleAlreadyOwnedException;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.RoleDoesNotExistException;
import com.rental_manager.roomie.exceptions.business_logic_exceptions.RoleIsNotOwnedException;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;

@Service
public class AccountService implements IAccountService {

    @Value("${email.verification.token.life-time.minutes}")
    private long tokenLifeTime;

    private final AccountRepository accountRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    private final Random randomGenerator = new SecureRandom();

    public AccountService(AccountRepository accountRepository,
                          VerificationTokenRepository verificationTokenRepository) {
        this.accountRepository = accountRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    public void registerClient(Account account) {
        Client clientRole = new Client(account);
        String tokenValue = RandomStringUtils.random(DatabaseConstraints.EMAIL_VERIFICATION_TOKEN_LENGTH, '0',
                'z' + 1, true, true, null, randomGenerator);
        VerificationToken verificationToken = new VerificationToken(tokenValue, account, tokenLifeTime);
        account.addRole(clientRole);
        accountRepository.saveAndFlush(account);
        verificationTokenRepository.saveAndFlush(verificationToken);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    public void addRole(UUID accountId, RolesEnum roleName) throws AccountNotFoundException, RoleAlreadyOwnedException {
        Account account = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
        switch (roleName) {
            case RolesEnum.ADMIN -> addAdminRole(account);
            case RolesEnum.CLIENT -> addClientRole(account);
            case RolesEnum.LANDLORD -> addLandlordRole(account);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    public void archiveRole(UUID accountId, RolesEnum roleName) throws AccountNotFoundException, RoleIsNotOwnedException,
            AccountDoesNotOweAnyRoleException {
        Account account = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
        Set<Role> rolesOwned = account.getRoles();
        int rolesCount = (int) rolesOwned.stream()
                .filter(Role::isActive)
                .count();
        Role roleToBeArchived = rolesOwned.stream()
                .filter(r -> r.getRole() == roleName && r.isActive())
                .findFirst()
                .orElseThrow(RoleIsNotOwnedException::new);
        if (rolesCount == 1) {
            throw new AccountDoesNotOweAnyRoleException();
        }
        roleToBeArchived.setActive(false);
        accountRepository.saveAndFlush(account);
    }

    @Transactional(propagation = Propagation.MANDATORY, transactionManager = "accountModuleTransactionManager")
    private void addAdminRole(Account account) throws RoleAlreadyOwnedException {
        Optional<Role> role = account.getRoles().stream()
                .filter(r -> r.getRole() == RolesEnum.ADMIN)
                .findFirst();
        if (role.isPresent()) {
           if (role.get().isActive()) {
               throw new RoleAlreadyOwnedException();
           } else {
               role.get().setActive(true);
           }
        } else {
            Role adminRole = new Admin(account);
            account.addRole(adminRole);
        }
        accountRepository.saveAndFlush(account);
    }

    @Transactional(propagation = Propagation.MANDATORY, transactionManager = "accountModuleTransactionManager")
    private void addClientRole(Account account) throws RoleAlreadyOwnedException {
        Optional<Role> role = account.getRoles().stream()
                .filter(r -> r.getRole() == RolesEnum.CLIENT)
                .findFirst();
        if (role.isPresent()) {
            if (role.get().isActive()) {
                throw new RoleAlreadyOwnedException();
            } else {
                role.get().setActive(true);
            }
        } else {
            Role clientRole = new Client(account);
            account.addRole(clientRole);
        }
        accountRepository.saveAndFlush(account);
    }

    @Transactional(propagation = Propagation.MANDATORY, transactionManager = "accountModuleTransactionManager")
    private void addLandlordRole(Account account) throws RoleAlreadyOwnedException {
        Optional<Role> role = account.getRoles().stream()
                .filter(r -> r.getRole() == RolesEnum.LANDLORD)
                .findFirst();
        if (role.isPresent()) {
            if (role.get().isActive()) {
                throw new RoleAlreadyOwnedException();
            } else {
                role.get().setActive(true);
            }
        } else {
            Role landlordRole = new Landlord(account);
            account.addRole(landlordRole);
        }
        accountRepository.saveAndFlush(account);
    }
}

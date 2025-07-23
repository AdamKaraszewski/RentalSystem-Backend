package com.rental_manager.roomie.account_module.services.implementations;

import com.rental_manager.roomie.account_module.dtos.AccountOnPageDTO;
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
import com.rental_manager.roomie.exceptions.business_logic_exceptions.*;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import com.rental_manager.roomie.utils.PagingResult;
import com.rental_manager.roomie.utils.account_module_utils.AccountConverter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;

@Service
public class AccountService implements IAccountService {

    private final long tokenLifeTime;

    private final AccountRepository accountRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    private final Random randomGenerator = new SecureRandom();

    public AccountService(@Value("${email.verification.token.life-time.minutes}") long tokenLifeTime,
                          AccountRepository accountRepository,
                          VerificationTokenRepository verificationTokenRepository) {
        this.tokenLifeTime = tokenLifeTime;
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
            case RolesEnum.ADMIN -> addRole(account, roleName, Admin::new);
            case RolesEnum.CLIENT -> addRole(account, roleName, Client::new);
            case RolesEnum.LANDLORD -> addRole(account, roleName, Landlord::new);
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
    private void addRole(Account account, RolesEnum roleName, Function<Account, Role> roleConstructor) throws
            RoleAlreadyOwnedException {
        Optional<Role> roleToBeActivated = account.getRoles().stream()
                .filter(r -> r.isActive() && r.getRole() == roleName)
                .findFirst();
        if(roleToBeActivated.isPresent()) {
            if (roleToBeActivated.get().isActive()) {
                throw new RoleAlreadyOwnedException();
            } else {
                roleToBeActivated.get().setActive(true);
            }
        } else {
            Role roleToBeAdded = roleConstructor.apply(account);
            account.addRole(roleToBeAdded);
        }
        accountRepository.saveAndFlush(account);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    public void blockAccount(UUID accountId) throws AccountNotFoundException, AccountAlreadyBlockedException {
        Account accountToBeBlocked = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
        if(!accountToBeBlocked.isActive()) {
            throw new AccountAlreadyBlockedException();
        }
        accountToBeBlocked.setActive(false);
        accountRepository.saveAndFlush(accountToBeBlocked);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    public void activateAccount(UUID accountId) throws AccountNotFoundException, AccountAlreadyActiveException {
        Account accountToBeActivated = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
        if(accountToBeActivated.isActive()) {
            throw new AccountAlreadyActiveException();
        }
        accountToBeActivated.setActive(true);
        accountRepository.saveAndFlush(accountToBeActivated);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    public PagingResult<AccountOnPageDTO> getAllAccountsWithPagination(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Account> accountsPage = accountRepository.findAllByOrderByUsernameAsc(pageable);
        List<AccountOnPageDTO> accountOnPageDTOs = accountsPage.stream()
                .map(AccountConverter::convertAccountToAccountOnPageDto).toList();
        return new PagingResult<>(
                accountOnPageDTOs,
                accountsPage.getTotalPages(),
                accountsPage.getTotalElements(),
                accountsPage.getSize(),
                accountsPage.getNumber(),
                accountsPage.isEmpty()
        );
    }
}

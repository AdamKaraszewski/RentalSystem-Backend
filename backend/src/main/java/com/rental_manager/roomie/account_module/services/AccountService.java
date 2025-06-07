package com.rental_manager.roomie.account_module.services;

import com.rental_manager.roomie.account_module.repositories.AccountRepository;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.VerificationToken;
import com.rental_manager.roomie.entities.roles.Client;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class AccountService implements IAccountService {

    @Value("${email.verification.token.life-time.minutes}")
    private long tokenLifeTime;

    private final AccountRepository accountRepository;
    private final Random randomGenerator = new SecureRandom();

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    public void registerClient(Account account) {
        Client clientRole = new Client(account);
        String tokenValue = RandomStringUtils.random(128, '0', 'z' + 1, true, true,
                null, randomGenerator);
        VerificationToken verificationToken = new VerificationToken(tokenValue, account, tokenLifeTime);
        account.addRole(clientRole);
        account.setVerificationToken(verificationToken);
        accountRepository.saveAndFlush(account);
    }
}

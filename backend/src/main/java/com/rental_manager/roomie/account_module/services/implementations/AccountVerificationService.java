package com.rental_manager.roomie.account_module.services.implementations;

import com.rental_manager.roomie.account_module.repositories.AccountRepository;
import com.rental_manager.roomie.account_module.repositories.VerificationTokenRepository;
import com.rental_manager.roomie.account_module.services.interfaces.IAccountVerificationService;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.VerificationToken;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.VerificationTokenDoesNotMatchException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AccountVerificationService implements IAccountVerificationService {

    private final AccountRepository accountRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    public AccountVerificationService(AccountRepository accountRepository,
                                      VerificationTokenRepository verificationTokenRepository) {
        this.accountRepository = accountRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    public void verifyAccountUsingVerificationToken(String verificationTokenValue)
            throws VerificationTokenDoesNotMatchException {
        VerificationToken verificationToken = verificationTokenRepository
                .findByTokenValueAndExpirationDateAfterAndAccount_isVerifiedFalse(verificationTokenValue, LocalDateTime.now())
                .orElseThrow(VerificationTokenDoesNotMatchException::new);
        Account accountToBeVerified = verificationToken.getAccount();
        accountToBeVerified.setVerified(true);
        accountRepository.saveAndFlush(accountToBeVerified);
        verificationToken.setWasUsed(true);
        verificationTokenRepository.saveAndFlush(verificationToken);
    }
}

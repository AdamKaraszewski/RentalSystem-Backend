package com.rental_manager.roomie.account_module.services.implementations;

import com.rental_manager.roomie.account_module.repositories.AccountRepository;
import com.rental_manager.roomie.account_module.repositories.ResetPasswordTokenRepository;
import com.rental_manager.roomie.account_module.services.interfaces.IResetPasswordService;
import com.rental_manager.roomie.config.database.DatabaseConstraints;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.ResetPasswordToken;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.AccountNotFoundException;
import com.rental_manager.roomie.exceptions.resource_not_found_exceptions.ResetPasswordTokenDoesNotMatchException;
import com.rental_manager.roomie.exceptions.validation_exceptions.PasswordDoesNotMatchException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class ResetPasswordService implements IResetPasswordService {

    @Value("${reset.password.token.life-time.minutes}")
    private long tokenLifeTime;

    private final AccountRepository accountRepository;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final Random randomGenerator = new SecureRandom();

    public ResetPasswordService(AccountRepository accountRepository,
                                ResetPasswordTokenRepository resetPasswordTokenRepository) {
        this.accountRepository = accountRepository;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    public void generateResetPasswordToken(String email) throws AccountNotFoundException {
        //check if account with entered email exist
        Account account = accountRepository.findByEmail(email).orElseThrow(AccountNotFoundException::new);
        String tokenValue = RandomStringUtils.random(DatabaseConstraints.RESET_PASSWORD_TOKEN_LENGTH, '0',
                'z' + 1, true, true, null, randomGenerator);
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(tokenValue, account, tokenLifeTime);
        //check if there is a token linked with account. If token exists delete it and generate a new one
        Optional<ResetPasswordToken> existingResetPasswordToken = resetPasswordTokenRepository.findByAccount_email(email);
        existingResetPasswordToken.ifPresent(resetPasswordTokenRepository::delete);
        resetPasswordTokenRepository.saveAndFlush(resetPasswordToken);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "accountModuleTransactionManager")
    public void resetPassword(String tokenValue, String newPassword, String repeatNewPassword) throws
            PasswordDoesNotMatchException, ResetPasswordTokenDoesNotMatchException {
        if (!Objects.equals(newPassword, repeatNewPassword)) {
            throw new PasswordDoesNotMatchException();
        }
        ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findByTokenValue(tokenValue)
                .orElseThrow(ResetPasswordTokenDoesNotMatchException::new);
        Account account = resetPasswordToken.getAccount();
        account.setPassword(newPassword);
        accountRepository.saveAndFlush(account);
        resetPasswordTokenRepository.delete(resetPasswordToken);
    }
}

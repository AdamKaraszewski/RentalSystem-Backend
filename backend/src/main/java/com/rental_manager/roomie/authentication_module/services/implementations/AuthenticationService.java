package com.rental_manager.roomie.authentication_module.services.implementations;

import com.rental_manager.roomie.authentication_module.repositories.AccountAuthenticationRepository;
import com.rental_manager.roomie.authentication_module.services.interfaces.IAuthenticationService;
import com.rental_manager.roomie.config.database.TransactionManagersIds;
import com.rental_manager.roomie.exceptions.ExceptionMessages;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService implements IAuthenticationService {

    private final AccountAuthenticationRepository accountAuthenticationRepository;

    public AuthenticationService(AccountAuthenticationRepository accountAuthenticationRepository) {
        this.accountAuthenticationRepository = accountAuthenticationRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            transactionManager = TransactionManagersIds.AUTHENTICATION_MODULE_TX_MANAGER)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountAuthenticationRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }
}

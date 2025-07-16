package com.rental_manager.roomie;

import com.rental_manager.roomie.account_module.dtos.RegisterClientDTO;
import com.rental_manager.roomie.config.database.DatabaseConstraints;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.ResetPasswordToken;
import com.rental_manager.roomie.entities.VerificationToken;
import com.rental_manager.roomie.entities.roles.Admin;
import com.rental_manager.roomie.entities.roles.Client;
import com.rental_manager.roomie.entities.roles.Landlord;
import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

public class AccountModuleTestUtility {

    public static final RegisterClientDTO TEST_REGISTER_CLIENT_DTO_NO_1 = new RegisterClientDTO("first_name_1",
            "last_name_1", "username_1", "email_1", "password_1");

    public static final UUID ID = UUID.randomUUID();
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String USERNAME = "username";
    public static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    public static final String NEW_PASSWORD = "newPassword";
    private static final Random RANDOM_GENERATOR = new SecureRandom();
    public static final String VERIFICATION_TOKEN_VALUE = RandomStringUtils.random(
            DatabaseConstraints.EMAIL_VERIFICATION_TOKEN_LENGTH, '0', 'z' + 1, true, true, null, RANDOM_GENERATOR);
    public static final String RESET_PASSWORD_TOKEN_VALUE = RandomStringUtils.random(
            DatabaseConstraints.RESET_PASSWORD_TOKEN_LENGTH, '0', 'z' + 1, true, true, null, RANDOM_GENERATOR);
    public static final long VERIFICATION_TOKEN_LIFE_TIME = 1440; //minutes
    public static final long REST_PASSWORD_TOKEN_LIFE_TIME = 15; //minutes


    private AccountModuleTestUtility() {}

    public static Account createNotVerifiedAccountWithClientRole(String username, String email, String firstname,
                                                                 String lastname) {
        return new Account(firstname, lastname, username, email, PASSWORD);
    }

    public static Account createNotVerifiedAccountWithClientRole() {
        Account account = createNotVerifiedAccount();
        Client clientRole = new Client(account);
        account.addRole(clientRole);
        return account;
    }

    public static Account createNotVerifiedAccountWithLandlordRole() {
        Account account = createNotVerifiedAccount();
        Landlord landlordRole = new Landlord(account);
        account.addRole(landlordRole);
        return account;
    }

    public static Account createNotVerifiedAccountWithAdminRole() {
        Account account = createNotVerifiedAccount();
        Admin adminRole = new Admin(account);
        account.addRole(adminRole);
        return account;
    }

    private static Account createNotVerifiedAccount() {
        return new Account(FIRST_NAME, LAST_NAME, USERNAME, EMAIL, PASSWORD);
        //VerificationToken verificationToken = new VerificationToken(VERIFICATION_TOKEN_VALUE, account, VERIFICATION_TOKEN_LIFE_TIME);
        //return account;
    }

    public static VerificationToken createVerificationTokenLinkedToNotVerifiedAccountWithClientRole() {
        Account account = new Account(FIRST_NAME, LAST_NAME, USERNAME, EMAIL, PASSWORD);
        Client clientRole = new Client(account);
        account.addRole(clientRole);
        return new VerificationToken(VERIFICATION_TOKEN_VALUE, account, VERIFICATION_TOKEN_LIFE_TIME);
    }

    public static ResetPasswordToken createResetPasswordToken() {
        Account account = createNotVerifiedAccountWithClientRole();
        return new ResetPasswordToken(RESET_PASSWORD_TOKEN_VALUE, account, REST_PASSWORD_TOKEN_LIFE_TIME);
    }

    public static ResetPasswordToken createResetPasswordToken(String username, String email, String firstname,
                                                              String lastname, String tokenValue) {
        Account account = createNotVerifiedAccountWithClientRole(username, email, firstname, lastname);
        return new ResetPasswordToken(tokenValue, account, REST_PASSWORD_TOKEN_LIFE_TIME);
    }
}

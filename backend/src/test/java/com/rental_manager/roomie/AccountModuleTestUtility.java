package com.rental_manager.roomie;

import com.rental_manager.roomie.account_module.dtos.RegisterClientDTO;
import com.rental_manager.roomie.config.Constraints;
import com.rental_manager.roomie.entities.Account;
import com.rental_manager.roomie.entities.ResetPasswordToken;
import com.rental_manager.roomie.entities.VerificationToken;
import com.rental_manager.roomie.entities.roles.Admin;
import com.rental_manager.roomie.entities.roles.Client;
import com.rental_manager.roomie.entities.roles.Landlord;
import com.rental_manager.roomie.entities.roles.RolesEnum;
import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class AccountModuleTestUtility {

    //AccountDto fields
    public static final String USERNAME_FIELD = "username";
    public static final String FIRST_NAME_FIELD = "firstName";
    public static final String LAST_NAME_FIELD = "lastName";
    public static final String EMAIL_FIELD = "email";
    public static final String PASSWORD_FILED = "password";
    public static final String ROLE_FIELD = "role";

    //ResetPasswordDto fields
    public static final String NEW_PASSWORD_FIELD = "newPassword";
    public static final String REPEAT_NEW_PASSWORD_FIELD = "repeatNewPassword";

    public static final UUID SIMPLE_UUID = UUID.randomUUID();

    public static final String USERNAME_NO_1 = "a_username_no_1";
    public static final String USERNAME_NO_2 = "b_username_no_2";
    public static final String USERNAME_NO_3 = "c_username_no_3";
    public static final String USERNAME_NO_4 = "d_username_no_4";
    public static final String USERNAME_NO_5 = "e_username_no_5";
    public static final String USERNAME_NO_6 = "f_username_no_6";

    public static final String EMAIL_NO_1 = "email.no1@example.com";
    public static final String EMAIL_NO_2 = "email.no2@example.com";
    public static final String EMAIL_NO_3 = "email.no3@example.com";
    public static final String EMAIL_NO_4 = "email.no4@example.com";
    public static final String EMAIL_NO_5 = "email.no5@example.com";
    public static final String EMAIL_NO_6 = "email.no6@example.com";

    public static final String FIRST_NAME_NO_1 = "firstNameNoA";
    public static final String FIRST_NAME_NO_2 = "firstNameNoB";
    public static final String FIRST_NAME_NO_3 = "firstNameNoC";
    public static final String FIRST_NAME_NO_4 = "firstNameNoD";
    public static final String FIRST_NAME_NO_5 = "firstNameNoE";
    public static final String FIRST_NAME_NO_6 = "firstNameNoF";

    public static final String LAST_NAME_NO_1 = "lastNameNoA";
    public static final String LAST_NAME_NO_2 = "lastNameNoB";
    public static final String LAST_NAME_NO_3 = "lastNameNoC";
    public static final String LAST_NAME_NO_4 = "lastNameNoD";
    public static final String LAST_NAME_NO_5 = "lastNameNoE";
    public static final String LAST_NAME_NO_6 = "lastNameNoF";

    public static final String PASSWORD = "password";
    public static final String NEW_PASSWORD = "newPassword";

    public static final RegisterClientDTO TEST_REGISTER_CLIENT_DTO = new RegisterClientDTO(
            FIRST_NAME_NO_1,
            LAST_NAME_NO_1,
            USERNAME_NO_1,
            EMAIL_NO_1,
            PASSWORD
    );


    private static final Random RANDOM_GENERATOR = new SecureRandom();
    public static final String VERIFICATION_TOKEN_VALUE = RandomStringUtils.random(
            Constraints.EMAIL_VERIFICATION_TOKEN_LENGTH, '0', 'z' + 1, true, true, null, RANDOM_GENERATOR);
    public static final String RESET_PASSWORD_TOKEN_VALUE = RandomStringUtils.random(
            Constraints.RESET_PASSWORD_TOKEN_LENGTH, '0', 'z' + 1, true, true, null, RANDOM_GENERATOR);
    public static final long VERIFICATION_TOKEN_LIFE_TIME = 1440; //minutes
    public static final long REST_PASSWORD_TOKEN_LIFE_TIME = 15; //minutes


    private AccountModuleTestUtility() {}

    public static Account createAccount(String firstname, String lastname, String username, String email,
                                        boolean isVerified, boolean isActive, List<RolesEnum> roles) {
        Account account = new Account(firstname, lastname, username, email, PASSWORD);
        account.setVerified(isVerified);
        account.setActive(isActive);
        for (RolesEnum role : roles) {
            addRoleToAccount(account, role);
        }
        return account;
    }

    private static void addRoleToAccount(Account account, RolesEnum role) {
        switch (role) {
            case CLIENT -> account.addRole(new Client(account));
            case LANDLORD -> account.addRole(new Landlord(account));
            case ADMIN -> account.addRole(new Admin(account));
        }
    }

    public static VerificationToken createVerificationToken(Account account, String tokenValue) {
        return new VerificationToken(tokenValue, account, VERIFICATION_TOKEN_LIFE_TIME);
    }

    public static ResetPasswordToken createResetPasswordToken(Account account, String tokenValue) {
        return new ResetPasswordToken(tokenValue, account, REST_PASSWORD_TOKEN_LIFE_TIME);
    }
}

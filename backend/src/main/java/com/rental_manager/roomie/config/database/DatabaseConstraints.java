package com.rental_manager.roomie.config.database;

public class DatabaseConstraints {

    public static final int USERNAME_MAX_LENGTH = 32;
    public static final int FIRST_NAME_MAX_LENGTH = 32;
    public static final int LAST_NAME_MAX_LENGTH = 32;
    public static final int EMAIL_MAX_LENGTH = 64;
    public static final int PASSWORD_MAX_LENGTH = 64;

    public static final int EMAIL_VERIFICATION_TOKEN_LENGTH = 128;

    public static final String UNIQUE_USERNAME_INDEX = "UNIQUE_USERNAME_INDEX";
    public static final String UNIQUE_ACCOUNT_ID = "UNIQUE_ACCOUNT_ID";
    public static final String UNIQUE_ACCOUNT_ID_ROLE = "UNIQUE_ACCOUNT_ID_ROLE";

}

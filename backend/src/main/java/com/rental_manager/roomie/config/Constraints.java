package com.rental_manager.roomie.config;

public class Constraints {

    public static final int USERNAME_MIN_LENGTH = 3;
    public static final int USERNAME_MAX_LENGTH = 32;
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9_\\-]+$";

    public static final int FIRST_NAME_MIN_LENGTH = 1;
    public static final int FIRST_NAME_MAX_LENGTH = 32;
    public static final String FIRST_NAME_REGEX = "^([\\p{L}][\\p{L}' \\-]*[\\p{L}]|)$";

    public static final int LAST_NAME_MIN_LENGTH = 1;
    public static final int LAST_NAME_MAX_LENGTH = 32;
    public static final String LAST_NAME_REGEX = "^([\\p{L}][\\p{L}' \\-]*[\\p{L}]|)$";

    public static final int EMAIL_MAX_LENGTH = 64;

    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 64;

    public static final int EMAIL_VERIFICATION_TOKEN_LENGTH = 128;
    public static final int RESET_PASSWORD_TOKEN_LENGTH = 128;

    public static final String UNIQUE_USERNAME_INDEX = "UNIQUE_USERNAME_INDEX";
    public static final String UNIQUE_ACCOUNT_ID = "UNIQUE_ACCOUNT_ID";
    public static final String UNIQUE_ACCOUNT_ID_ROLE = "UNIQUE_ACCOUNT_ID_ROLE";

}

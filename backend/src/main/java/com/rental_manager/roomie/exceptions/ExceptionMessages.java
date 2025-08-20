package com.rental_manager.roomie.exceptions;

public class ExceptionMessages {

    // RESOURCE NOT FOUND EXCEPTIONS
    public static final String ACCOUNT_NOT_FOUND = "account.did.not.found";
    public static final String VERIFICATION_TOKEN_DOES_NOT_MATCH = "verification.token.does.not.match";
    public static final String RESET_PASSWORD_TOKEN_DOES_NOT_MATCH = "reset.password.token.does.not.match";

    // VALIDATION EXCEPTIONS
    public static final String FIELD_NULL_VALUE = "field.value.null";

    public static final String FIRST_NAME_LENGTH_NOT_VALID = "account.first.name.length.not.valid";
    public static final String FIRST_NAME_REGEX_NOT_FOLLOWED = "account.first.name.regex.not.followed";

    public static final String LAST_NAME_LENGTH_NOT_VALID = "account.last.name.length.not.valid";
    public static final String LAST_NAME_REGEX_NOT_FOLLOWED = "account.last.name.regex.not.followed";

    public static final String USERNAME_LENGTH_NOT_VALID = "account.username.length.not.valid";
    public static final String USERNAME_REGEX_NOT_FOLLOWED = "account.username.regex.not.followed";

    public static final String EMAIL_NOT_VALID = "account.email.not.valid";
    public static final String PASSWORD_LENGTH_NOT_VALID = "account.password.length.not.valid";

    public static final String ROLE_DOES_NOT_EXIST = "specified.role.does.not.exist";

    public static final String PASSWORD_DOES_NOT_MATCH = "repeat.password.does.not.match";

    //BUSINESS LOGIC EXCEPTION
    //ROLES
    public static final String ROLE_ALREADY_OWNED = "role.is.already.owned";
    public static final String ROlE_IS_NOT_OWNED = "role.is.not.owned";
    public static final String ACCOUNT_DOES_NOT_OWE_ANY_ROLE = "account.does.not.owe.any.role";
    //BLOCK ACCOUNT
    public static final String ACCOUNT_ALREADY_BLOCKED = "account.already.blocked";
    public static final String ACCOUNT_ALREADY_ACTIVE = "account.already.active";
}

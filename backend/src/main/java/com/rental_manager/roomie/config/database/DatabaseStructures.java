package com.rental_manager.roomie.config.database;

public class DatabaseStructures {

    // TABLES
    public final static String TABLE_ACCOUNTS = "ACCOUNTS";
    public final static String TABLE_PERSONAL_DATA = "PERSONAL_DATA";
    public final static String TABLE_ACCOUNT_ROLE = "ACCOUNT_ROLE";
    public final static String TABLE_CLIENT_DATA = "CLIENT_DATA";
    public final static String TABLE_ADMIN_DATA = "ADMIN_DATA";
    public final static String TABLE_LANDLORD_DATA = "LANDLORD";
    public final static String TABLE_EMAIL_VERIFICATION_TOKENS = "EMAIL_VERIFICATION_TOKENS";

    // ABSTRACT ENTITY
    public final static String ID_COLUMN = "ID";
    public final static String VERSION_COLUMN = "VERSION";

    // COMMONS
    public final static String IS_ACTIVE_COLUMN = "IS_ACTIVE";
    public final static String EXPIRATION_DATE_COLUMN = "EXPIRATION_DATE";

    // ACCOUNT COLUMNS
    public final static String SIMPLE_COLUMN = "SIMPLE_COLUMN";
    public final static String USERNAME_COLUMN = "USERNAME";
    public final static String IS_VERIFIED_COLUMN = "IS_VERIFIED";
    public final static String PASSWORD_COLUMN = "PASSWORD";
    public final static String FIRST_NAME_COLUMN = "FIRST_NAME";
    public final static String LAST_NAME_COLUMN = "LAST_NAME";
    public final static String EMAIL_COLUMN = "EMAIL";

    // ACCOUNT_ID COLUMN - FOREIGN KEY
    public final static String ACCOUNT_ID_COLUMN = "ACCOUNT_ID";

    // ACCOUNT_ROLE COLUMNS
    public final static String ROLE_NAME_COLUMN = "ROLE_NAME";

    // EMAIL_VERIFICATION_TOKEN
    public final static String TOKEN_VALUE_COLUMN = "TOKEN_VALUE";


}

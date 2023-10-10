package com.gyenese.treasury.constants;


import lombok.experimental.UtilityClass;

@UtilityClass
public final class EmailPatterns {

    public static final String SUPPORT_SUBJECT = "An error occurred in Eur Treasury Service";
    public static final String SUPPORT_BODY = "Dear Developers, \n Hope you are All doing well, but the following error occurred:\n {0}";

    public static final String NOTIFY_ACCOUNT_ERROR_SUBJECT = "An error occurred during Your transaction";
    public static final String NOTIFY_ACCOUNT_ERROR_BODY = "Dear {0} {1}, \n Hope You are doing well.\n During Your sending {2} {3} an error occurred, " +
            "so Your transaction have not been completed successfully";
    public static final String NOTIFY_ACCOUNT_FOR_NOT_ENOUGH_BALANCE_SUBJECT = "Your balance is not sufficient!";
    public static final String NOTIFY_ACCOUNT_FOR_NOT_ENOUGH_BALANCE_BODY = "Dear {0} {1}, \n Hope You are doing well.\n" +
            "Your balance is not sufficient for this transaction: {2} {3}.!";
}

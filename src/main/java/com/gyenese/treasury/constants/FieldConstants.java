package com.gyenese.treasury.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class FieldConstants {

    // mutation
    public static final String DB_FIELD_MUTATION_TRANSACTION_ID = "transaction_id";
    public static final String DB_FIELD_MUTATION_AMOUNT = "amount";
    public static final String DB_FIELD_MUTATION_PARTNER_NAME = "partner_name";
    public static final String DB_FIELD_MUTATION_XFER_DATE = "xfer_date";
    public static final String DB_FIELD_MUTATION_CURRENCY = "currency";
    // account
    public static final String DB_FIELD_ACCOUNT_ID = "id";
    public static final String DB_FIELD_FIRST_NAME = "first_name";
    public static final String DB_FIELD_LAST_NAME = "last_name";
    public static final String DB_FIELD_EMAIL = "email";
    // balance
    public static final String DB_FIELD_BALANCE_ID = "id";
    public static final String DB_FIELD_BALANCE_ACCOUNT_ID = "account_id";
    public static final String DB_FIELD_BALANCE_AMOUNT = "amount";
    public static final String DB_FIELD_BALANCE_CURRENCY = "currency";

    public static final String DB_QUERY_PARAM_ID = "id";
    public static final String DB_QUERY_PARAM_ID_LIST = "ids";
    public static final String DB_QUERY_PARAM_CURRENCY = "currency";
    public static final String DB_QUERY_PARAM_BALANCE_ACCOUNT_ID = "accountId";
    public static final String DB_QUERY_PARAM_BALANCE_ACCOUNT_IDS = "accountIds";
    public static final String DB_QUERY_PARAM_AMOUNT = "amount";
    public static final String DB_QUERY_PARAM_SENDING_ID = "sendingId";
    public static final String DB_QUERY_PARAM_RECEIVING_ID = "receivingId";
    public static final String DB_QUERY_PARAM_TRANSACTION_DATE = "xferDate";

}

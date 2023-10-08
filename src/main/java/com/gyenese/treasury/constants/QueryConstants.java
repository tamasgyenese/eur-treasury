package com.gyenese.treasury.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class QueryConstants {

    public static final String GET_MUTATIONS = "SELECT result.id                              AS transaction_id,\n"
            + "       result.amount                          AS amount,\n"
            + "       concat(a.first_name, ' ', a.last_name) AS partner_name,\n"
            + "       result.xfer_date                       AS xfer_date,\n"
            + "       result.currency                        AS currency\n"
            + "\n"
            + "FROM (SELECT t.id,\n"
            + "             t.xfer_date,\n"
            + "             t.currency,\n"
            + "             CASE\n"
            + "                 WHEN t.sending_id = :id THEN -1 * t.amount\n"
            + "                 WHEN t.receiving_id = :id THEN amount\n"
            + "                 END                                                amount,\n"
            + "             IF(t.sending_id = :id, t.receiving_id, t.receiving_id) partner\n"
            + "      FROM transaction t\n"
            + "      WHERE t.receiving_id = :id\n"
            + "         OR t.sending_id = :id) AS result\n"
            + "         INNER JOIN account a ON a.id = result.partner;";

    public static final String CHECK_ACCOUNT_EXISTS = "SELECT COUNT(*)\n"
            + "FROM account a\n"
            + "WHERE a.id = :id;";

    public static final String LIST_ACCOUNT_IDS_FROM_BALANCE_BY_CURRENCY_AND_IDS = "SELECT b.account_id\n"
            + "FROM balance b\n"
            + "WHERE b.account_id IN (:ids)\n"
            + "  AND b.currency = :currency;";

    public static final String INSERT_BALANCE = "INSERT INTO balance(account_id, currency)\n"
            + "VALUES (:account_id, :currency);";

    public static final String SELECT_BALANCE_FOR_UPDATE = "SELECT *\n"
            + "FROM balance b\n"
            + "WHERE b.account_id IN (:accountIds)\n"
            + "  AND b.currency = :currency FOR\n"
            + "UPDATE;";

    public static final String UPDATE_BALANCE_AMOUNT_BY_ACCOUNT_AND_CURRENCY = "UPDATE balance b\n"
            + "SET b.amount = amount + :amount\n"
            + "WHERE b.account_id = :accountId\n"
            + "  AND b.currency = :currency;";

    public static final String INSERT_TRANSACTION = "INSERT INTO transaction(sending_id, receiving_id, amount, currency, xfer_date)\n"
            + "VALUES (:sendingId, :receivingId, :amount, :currency, :xferDate);";

}

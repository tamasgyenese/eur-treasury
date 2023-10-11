package com.gyenese.treasury.exception;

public class AccountDaoException extends Exception {

    public AccountDaoException(String message) {
        super(message);
    }

    public AccountDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountDaoException(Throwable cause) {
        super(cause);
    }
}

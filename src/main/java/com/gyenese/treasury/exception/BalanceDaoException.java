package com.gyenese.treasury.exception;

public class BalanceDaoException extends Exception {

    public BalanceDaoException(String message) {
        super(message);
    }

    public BalanceDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public BalanceDaoException(Exception e) {
        super(e);
    }
}

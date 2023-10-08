package com.gyenese.treasury.exception;

public class TransactionDaoException extends Exception{

    public TransactionDaoException(String message) {
        super(message);
    }

    public TransactionDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionDaoException(Throwable cause) {
        super(cause);
    }
}

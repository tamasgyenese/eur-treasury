package com.gyenese.treasury.exception;

public class BalanceNotEnoughException extends Exception {

    public BalanceNotEnoughException() {
        super();
    }

    public BalanceNotEnoughException(String message) {
        super(message);
    }

    public BalanceNotEnoughException(String message, Throwable cause) {
        super(message, cause);
    }

    public BalanceNotEnoughException(Throwable cause) {
        super(cause);
    }
}

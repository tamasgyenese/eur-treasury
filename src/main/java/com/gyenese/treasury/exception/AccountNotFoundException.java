package com.gyenese.treasury.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(long id) {
        super(String.format("Account not found with the given id: %03d", id));
    }
}

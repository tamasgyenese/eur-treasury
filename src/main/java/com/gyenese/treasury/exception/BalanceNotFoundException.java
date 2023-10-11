package com.gyenese.treasury.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BalanceNotFoundException extends RuntimeException {

    public BalanceNotFoundException(long id, String currency) {
        super(String.format("Balance not found with the given id: %03d for currency: %s", id, currency));
    }
}

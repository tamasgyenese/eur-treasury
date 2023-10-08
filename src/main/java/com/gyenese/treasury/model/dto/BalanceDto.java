package com.gyenese.treasury.model.dto;

import lombok.Data;

@Data
public class BalanceDto {

    private long id;
    private long accountId;
    private double amount;
    private String currency;

}

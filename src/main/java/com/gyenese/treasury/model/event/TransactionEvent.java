package com.gyenese.treasury.model.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionEvent {

    private long receivingId;
    private long sendingId;
    private double amount;
    private LocalDateTime xferDate;
}

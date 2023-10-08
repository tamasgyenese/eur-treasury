package com.gyenese.treasury.model.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class MutationDto {

    private long transactionId;
    private double amount;
    private String partnerName;
    private String currency;
    private LocalDateTime date;
}

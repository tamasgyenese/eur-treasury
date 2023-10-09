package com.gyenese.treasury.messaging;

import com.gyenese.treasury.model.event.TransactionEvent;
import com.gyenese.treasury.service.TreasuryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class KafkaListeners {

    private final TreasuryService treasuryService;

    @KafkaListener(topics = "eur-treasury-topic", groupId = "eur-treasury-group-id", containerFactory = "transactionListener")
    public void consumeTransaction(TransactionEvent transactionEvent) {
        treasuryService.updateBalancesByTransactions(transactionEvent.getAmount(), transactionEvent.getSendingId(), transactionEvent.getReceivingId(), "EUR", transactionEvent.getXferDate());
    }


}

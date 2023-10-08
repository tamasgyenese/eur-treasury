package com.gyenese.treasury.messaging;

import com.gyenese.treasury.model.event.TransactionEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(topics = "eur_treasury", groupId = "groupId", containerFactory = "transactionListener")
    public void consumeTransaction(TransactionEvent transactionEvent) {

    }


}

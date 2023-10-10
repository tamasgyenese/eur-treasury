package com.gyenese.treasury.messaging;

import com.gyenese.treasury.model.event.TransactionEvent;
import com.gyenese.treasury.service.TreasuryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

public class KafkaListenersTest {

    @InjectMocks
    private KafkaListeners onTest;

    @Mock
    private TreasuryService treasuryService;

    @Captor
    private ArgumentCaptor<Long> sendingIdCaptor = ArgumentCaptor.forClass(Long.class);

    @Captor
    private ArgumentCaptor<Long> receivingIdCaptor = ArgumentCaptor.forClass(Long.class);

    @Captor
    private ArgumentCaptor<Double> amountCaptor = ArgumentCaptor.forClass(Double.class);

    @Captor
    private ArgumentCaptor<String> currencyCaptor = ArgumentCaptor.forClass(String.class);


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCallinService() {
        TransactionEvent event = new TransactionEvent();
        event.setReceivingId(1L);
        event.setSendingId(2L);
        event.setAmount(3.3);
        event.setXferDate(LocalDateTime.now());
        doNothing().when(treasuryService).updateBalancesByTransactions(anyLong(), anyLong(), anyLong(), anyString(), any());

        onTest.consumeTransaction(event);

        verify(treasuryService).updateBalancesByTransactions(amountCaptor.capture(), sendingIdCaptor.capture(), receivingIdCaptor.capture(), currencyCaptor.capture(), any());

        assertEquals(1L, receivingIdCaptor.getValue());
        assertEquals(2L, sendingIdCaptor.getValue());
        assertEquals(3.3, amountCaptor.getValue());
        assertEquals("EUR", currencyCaptor.getValue());
    }


}
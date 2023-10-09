package com.gyenese.treasury.service;

import com.gyenese.treasury.dao.AccountRepository;
import com.gyenese.treasury.dao.BalanceRepository;
import com.gyenese.treasury.dao.TransactionRepository;
import com.gyenese.treasury.exception.BalanceDaoException;
import com.gyenese.treasury.exception.TransactionDaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TreasuryServiceTest {

    @InjectMocks
    private TreasuryService onTest;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<Long> recIdCapture = ArgumentCaptor.forClass(Long.class);

    @Captor
    private ArgumentCaptor<Long> sendIdCapture = ArgumentCaptor.forClass(Long.class);

    @Captor
    private ArgumentCaptor<Double> amountCapture = ArgumentCaptor.forClass(Double.class);

    @Captor
    private ArgumentCaptor<String> currencyCapture = ArgumentCaptor.forClass(String.class);

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test update balances and insert transactions when everything is okay and Balances are already exist")
    public void testUpdateBalances1() throws BalanceDaoException, TransactionDaoException {
        long receivingId = 1L;
        long sendingId = 2L;

        when(balanceRepository.getAmountByAccountIdAndCurrency(anyLong(), anyString())).thenReturn(111111.11111);
        when(balanceRepository.listAccountByCurrency(anyString(), anyList())).thenReturn(List.of(receivingId, sendingId));
        when(balanceRepository.selectForUpdateByAccountsAndCurrency(anyList(), anyString())).thenReturn(new ArrayList<>());
        doNothing().when(balanceRepository).updateAmountByAccountAndCurrency(anyLong(), anyLong(), anyString());
        doNothing().when(transactionRepository).save(anyLong(), anyLong(), anyDouble(), anyString(), any());

        onTest.updateBalancesByTransactions(45.2, sendingId, receivingId, "EUR", LocalDateTime.now());

        verify(transactionRepository).save(sendIdCapture.capture(), recIdCapture.capture(), amountCapture.capture(), currencyCapture.capture(), any());

        assertEquals(receivingId, recIdCapture.getValue());
        assertEquals(sendingId, sendIdCapture.getValue());
        assertEquals(45.2, amountCapture.getValue());
        assertEquals("EUR", currencyCapture.getValue());

    }


}
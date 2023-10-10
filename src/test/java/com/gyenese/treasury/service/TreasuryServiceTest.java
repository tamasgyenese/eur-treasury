package com.gyenese.treasury.service;

import com.gyenese.treasury.dao.AccountRepository;
import com.gyenese.treasury.dao.BalanceRepository;
import com.gyenese.treasury.dao.TransactionRepository;
import com.gyenese.treasury.exception.*;
import com.gyenese.treasury.model.dto.AccountDto;
import com.gyenese.treasury.model.dto.BalanceDto;
import com.gyenese.treasury.model.dto.MutationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

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

    @Captor
    private ArgumentCaptor<List<Long>> longListCapture = ArgumentCaptor.forClass(List.class);

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(onTest, "supportEmail", "treasury_eur@bitvavo_nonvalid.com");
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

    @Test
    @DisplayName("Test update balances and insert transaction when everything is okay and no balance exists")
    public void testUpdateBalances2() throws BalanceDaoException, TransactionDaoException {
        long receivingId = 1L;
        long sendingId = 2L;

        when(balanceRepository.getAmountByAccountIdAndCurrency(anyLong(), anyString())).thenReturn(111111.11111);
        when(balanceRepository.listAccountByCurrency(anyString(), anyList())).thenReturn(new ArrayList<>());
        when(balanceRepository.selectForUpdateByAccountsAndCurrency(anyList(), anyString())).thenReturn(new ArrayList<>());
        doNothing().when(balanceRepository).updateAmountByAccountAndCurrency(anyLong(), anyLong(), anyString());
        doNothing().when(transactionRepository).save(anyLong(), anyLong(), anyDouble(), anyString(), any());

        onTest.updateBalancesByTransactions(45.2, sendingId, receivingId, "EUR", LocalDateTime.now());

        verify(transactionRepository).save(sendIdCapture.capture(), recIdCapture.capture(), amountCapture.capture(), currencyCapture.capture(), any());
        verify(balanceRepository).saveAll(longListCapture.capture(), currencyCapture.capture());

        assertEquals(List.of(2L, 1L), longListCapture.getValue());
        assertEquals(receivingId, recIdCapture.getValue());
        assertEquals(sendingId, sendIdCapture.getValue());
        assertEquals(45.2, amountCapture.getValue());
        assertEquals("EUR", currencyCapture.getValue());
    }

    @Test
    @DisplayName("Test update balances and insert transaction and BalanceDaoException happens during select for update")
    public void testUpdateBalances3() throws BalanceDaoException, AccountDaoException {
        long receivingId = 1L;
        long sendingId = 2L;

        AccountDto accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setFirstName("first_name");
        accountDto.setLastName("last_name");
        accountDto.setEmail("email");

        when(balanceRepository.getAmountByAccountIdAndCurrency(anyLong(), anyString())).thenReturn(111111.11111);
        when(balanceRepository.listAccountByCurrency(anyString(), anyList())).thenReturn(new ArrayList<>());
        when(balanceRepository.selectForUpdateByAccountsAndCurrency(anyList(), anyString())).thenThrow(new BalanceDaoException("error"));
        when(accountRepository.getAccountById(anyLong())).thenReturn(accountDto);

        onTest.updateBalancesByTransactions(45.2, sendingId, receivingId, "EUR", LocalDateTime.now());

        verify(emailService, times(1)).send(eq("email"), eq("An error occurred during Your transaction"), eq("Dear first_name last_name, \n" +
                " Hope You are doing well.\n" +
                " During Your sending 45,2 EUR an error occurred, so Your transaction have not been completed successfully"));

        verify(emailService, times(1)).send(eq("treasury_eur@bitvavo_nonvalid.com"), eq("An error occurred in Eur Treasury Service"), eq("Dear Developers, \n" +
                " Hope you are All doing well, but the following error occurred:\n" +
                " com.gyenese.treasury.exception.BalanceDaoException: error"));

    }

    @Test
    @DisplayName("Test update balances and insert transaction and BalanceDaoException happens during listAccountByCurrency")
    public void testUpdateBalances4() throws BalanceDaoException, AccountDaoException {
        long receivingId = 1L;
        long sendingId = 2L;

        AccountDto accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setFirstName("first_name");
        accountDto.setLastName("last_name");
        accountDto.setEmail("email");

        when(balanceRepository.getAmountByAccountIdAndCurrency(anyLong(), anyString())).thenReturn(111111.11111);
        when(balanceRepository.listAccountByCurrency(anyString(), anyList())).thenThrow(new BalanceDaoException("error"));
        when(accountRepository.getAccountById(anyLong())).thenReturn(accountDto);

        onTest.updateBalancesByTransactions(45.2, sendingId, receivingId, "EUR", LocalDateTime.now());

        verify(emailService, times(1)).send(eq("email"), eq("An error occurred during Your transaction"), eq("Dear first_name last_name, \n" +
                " Hope You are doing well.\n" +
                " During Your sending 45,2 EUR an error occurred, so Your transaction have not been completed successfully"));

        verify(emailService, times(1)).send(eq("treasury_eur@bitvavo_nonvalid.com"), eq("An error occurred in Eur Treasury Service"), eq("Dear Developers, \n" +
                " Hope you are All doing well, but the following error occurred:\n" +
                " com.gyenese.treasury.exception.BalanceDaoException: error"));

    }

    @Test
    @DisplayName("Test update balances and insert transaction and BalanceDaoException happens during updateAmountByAccountAndCurrency")
    public void testUpdateBalances5() throws BalanceDaoException, AccountDaoException {
        long receivingId = 1L;
        long sendingId = 2L;

        AccountDto accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setFirstName("first_name");
        accountDto.setLastName("last_name");
        accountDto.setEmail("email");

        when(balanceRepository.getAmountByAccountIdAndCurrency(anyLong(), anyString())).thenReturn(111111.11111);
        when(balanceRepository.selectForUpdateByAccountsAndCurrency(anyList(), anyString())).thenReturn(new ArrayList<>());
        when(balanceRepository.listAccountByCurrency(anyString(), anyList())).thenReturn(List.of(receivingId, sendingId));
        doThrow(new BalanceDaoException("error")).when(balanceRepository).updateAmountByAccountAndCurrency(anyLong(), anyDouble(), anyString());


        when(accountRepository.getAccountById(anyLong())).thenReturn(accountDto);

        onTest.updateBalancesByTransactions(45.2, sendingId, receivingId, "EUR", LocalDateTime.now());

        verify(emailService, times(1)).send(eq("email"), eq("An error occurred during Your transaction"), eq("Dear first_name last_name, \n" +
                " Hope You are doing well.\n" +
                " During Your sending 45,2 EUR an error occurred, so Your transaction have not been completed successfully"));

        verify(emailService, times(1)).send(eq("treasury_eur@bitvavo_nonvalid.com"), eq("An error occurred in Eur Treasury Service"), eq("Dear Developers, \n" +
                " Hope you are All doing well, but the following error occurred:\n" +
                " com.gyenese.treasury.exception.BalanceDaoException: error"));
    }

    @Test
    @DisplayName("Test update balances and insert transaction and TransactionDaoException happens during save transactions")
    public void testUpdateBalances6() throws BalanceDaoException, TransactionDaoException, AccountDaoException {
        long receivingId = 1L;
        long sendingId = 2L;

        AccountDto accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setFirstName("first_name");
        accountDto.setLastName("last_name");
        accountDto.setEmail("email");

        when(balanceRepository.getAmountByAccountIdAndCurrency(anyLong(), anyString())).thenReturn(111111.11111);
        when(balanceRepository.selectForUpdateByAccountsAndCurrency(anyList(), anyString())).thenReturn(new ArrayList<>());
        when(balanceRepository.listAccountByCurrency(anyString(), anyList())).thenReturn(List.of(receivingId, sendingId));
        doNothing().when(balanceRepository).updateAmountByAccountAndCurrency(anyLong(), anyLong(), anyString());

        doThrow(new TransactionDaoException("error")).when(transactionRepository).save(anyLong(), anyLong(), anyDouble(), anyString(), any());

        when(accountRepository.getAccountById(anyLong())).thenReturn(accountDto);

        onTest.updateBalancesByTransactions(45.2, sendingId, receivingId, "EUR", LocalDateTime.now());

        verify(emailService, times(1)).send(eq("email"), eq("An error occurred during Your transaction"), eq("Dear first_name last_name, \n" +
                " Hope You are doing well.\n" +
                " During Your sending 45,2 EUR an error occurred, so Your transaction have not been completed successfully"));

        verify(emailService, times(1)).send(eq("treasury_eur@bitvavo_nonvalid.com"), eq("An error occurred in Eur Treasury Service"), eq("Dear Developers, \n" +
                " Hope you are All doing well, but the following error occurred:\n" +
                " com.gyenese.treasury.exception.TransactionDaoException: error"));
    }

    @Test
    @DisplayName("Test update balances and insert transaction but the balance is not enough for the transaction")
    public void testUpdateBalances7() throws BalanceDaoException, TransactionDaoException, AccountDaoException {
        long receivingId = 1L;
        long sendingId = 2L;

        AccountDto accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setFirstName("first_name");
        accountDto.setLastName("last_name");
        accountDto.setEmail("email");

        when(balanceRepository.getAmountByAccountIdAndCurrency(anyLong(), anyString())).thenReturn(111111.11111);
        when(balanceRepository.selectForUpdateByAccountsAndCurrency(anyList(), anyString())).thenReturn(new ArrayList<>());
        when(balanceRepository.listAccountByCurrency(anyString(), anyList())).thenReturn(List.of(receivingId, sendingId));
        doNothing().when(balanceRepository).updateAmountByAccountAndCurrency(anyLong(), anyLong(), anyString());

        doThrow(new TransactionDaoException("error")).when(transactionRepository).save(anyLong(), anyLong(), anyDouble(), anyString(), any());

        when(accountRepository.getAccountById(anyLong())).thenReturn(accountDto);

        onTest.updateBalancesByTransactions(324123424.3, sendingId, receivingId, "EUR", LocalDateTime.now());

        verify(emailService, times(1)).send(eq("email"), eq("Your balance is not sufficient!"), eq("Dear first_name last_name, \n Hope You are doing well.\n" +
                "Your balance is not sufficient for this transaction: 324 123 424,3 EUR.!"));

    }

    @Test
    @DisplayName("Test update balances and insert transaction but the balance is not enough for the transaction and getting AccountDaoException")
    public void testUpdateBalances8() throws BalanceDaoException, TransactionDaoException, AccountDaoException {
        long receivingId = 1L;
        long sendingId = 2L;

        when(balanceRepository.getAmountByAccountIdAndCurrency(anyLong(), anyString())).thenReturn(111111.11111);
        when(balanceRepository.selectForUpdateByAccountsAndCurrency(anyList(), anyString())).thenReturn(new ArrayList<>());
        when(balanceRepository.listAccountByCurrency(anyString(), anyList())).thenReturn(List.of(receivingId, sendingId));
        doNothing().when(balanceRepository).updateAmountByAccountAndCurrency(anyLong(), anyLong(), anyString());

        doThrow(new TransactionDaoException("error")).when(transactionRepository).save(anyLong(), anyLong(), anyDouble(), anyString(), any());

        when(accountRepository.getAccountById(anyLong())).thenThrow(new AccountDaoException("error"));

        onTest.updateBalancesByTransactions(42341212.4, sendingId, receivingId, "EUR", LocalDateTime.now());

        verify(emailService, times(1)).send(eq("treasury_eur@bitvavo_nonvalid.com"), eq("An error occurred in Eur Treasury Service"), eq("Dear Developers, \n" +
                " Hope you are All doing well, but the following error occurred:\n" +
                " com.gyenese.treasury.exception.AccountDaoException: error"));

    }

    @Test
    @DisplayName("Test update balances and insert transaction and BalanceDaoException happens during updateAmountByAccountAndCurrency and also an AccountDaoException during get Account")
    public void testUpdateBalances9() throws BalanceDaoException, AccountDaoException {
        long receivingId = 1L;
        long sendingId = 2L;


        when(balanceRepository.getAmountByAccountIdAndCurrency(anyLong(), anyString())).thenReturn(111111.11111);
        when(balanceRepository.selectForUpdateByAccountsAndCurrency(anyList(), anyString())).thenReturn(new ArrayList<>());
        when(balanceRepository.listAccountByCurrency(anyString(), anyList())).thenReturn(List.of(receivingId, sendingId));
        doThrow(new BalanceDaoException("error")).when(balanceRepository).updateAmountByAccountAndCurrency(anyLong(), anyDouble(), anyString());

        when(accountRepository.getAccountById(anyLong())).thenThrow(new AccountDaoException("error"));

        onTest.updateBalancesByTransactions(45.2, sendingId, receivingId, "EUR", LocalDateTime.now());

        verify(emailService, times(1)).send(eq("treasury_eur@bitvavo_nonvalid.com"), eq("An error occurred in Eur Treasury Service"), eq("Dear Developers, \n" +
                " Hope you are All doing well, but the following error occurred:\n" +
                " com.gyenese.treasury.exception.AccountDaoException: error"));
    }

    @Test
    @DisplayName("Test list mutations and everything is okay")
    public void testListMutations1() throws AccountDaoException, TransactionDaoException {
        when(accountRepository.isAccountExists(anyLong())).thenReturn(true);
        when(transactionRepository.listMutations(anyLong())).thenReturn(new ArrayList<>() {{
            add(new MutationDto());
        }});

        List<MutationDto> result = onTest.listMutationByAccountId(1L);

        assertNotNull(result);

    }

    @Test
    @DisplayName("Test list mutations and AccountDaoException happens")
    public void testListMutations2() throws AccountDaoException {
        when(accountRepository.isAccountExists(anyLong())).thenThrow(new AccountDaoException("error"));

        Exception exception = assertThrows(InternalServerError.class, () -> onTest.listMutationByAccountId(1L));
    }

    @Test
    @DisplayName("Test list mutations but Account is not found")
    public void testListMutations3() throws AccountDaoException {
        when(accountRepository.isAccountExists(anyLong())).thenReturn(false);

        Exception exception = assertThrows(AccountNotFoundException.class, () -> onTest.listMutationByAccountId(1L));
    }

    @Test
    @DisplayName("Test list mutations and TransactionDaoException happens")
    public void testListMutations4() throws AccountDaoException, TransactionDaoException {
        when(accountRepository.isAccountExists(anyLong())).thenReturn(true);
        when(transactionRepository.listMutations(anyLong())).thenThrow(new TransactionDaoException("error"));

        Exception exception = assertThrows(InternalServerError.class, () -> onTest.listMutationByAccountId(1L));
    }

    @Test
    @DisplayName("Test get balance and everything is okay")
    public void testGetBalanceTest1() throws AccountDaoException, BalanceDaoException {
        when(accountRepository.isAccountExists(anyLong())).thenReturn(true);
        when(balanceRepository.getBalanceByAccountIdAndCurrency(anyLong(), anyString())).thenReturn(new ArrayList<>() {{
            add(new BalanceDto());
        }});

        BalanceDto result = onTest.getBalanceByAccountIdAndCurrency(1L, "EUR");

        assertNotNull(result);
    }

    @Test
    @DisplayName("Test get balance and AccountDaoException happens")
    public void testGetBalanceTest2() throws AccountDaoException {
        when(accountRepository.isAccountExists(anyLong())).thenThrow(new AccountDaoException("error"));

        Exception exception = assertThrows(InternalServerError.class, () -> onTest.getBalanceByAccountIdAndCurrency(1L, "EUR"));
    }

    @Test
    @DisplayName("Test get balance but Account is not found")
    public void testGetBalanceTest3() throws AccountDaoException {
        when(accountRepository.isAccountExists(anyLong())).thenReturn(false);

        Exception exception = assertThrows(AccountNotFoundException.class, () -> onTest.getBalanceByAccountIdAndCurrency(1L, "EUR"));
    }

    @Test
    @DisplayName("Test get balance and BalanceDaoException happens")
    public void testGetBalanceTest4() throws AccountDaoException, BalanceDaoException {
        when(accountRepository.isAccountExists(anyLong())).thenReturn(true);
        when(balanceRepository.getBalanceByAccountIdAndCurrency(anyLong(), anyString())).thenThrow(new BalanceDaoException("error"));

        Exception exception = assertThrows(InternalServerError.class, () -> onTest.getBalanceByAccountIdAndCurrency(1L, "EUR"));
    }

    @Test
    @DisplayName("Test get balance but balance not exists")
    public void testGetBalanceTest5() throws AccountDaoException, BalanceDaoException {
        when(accountRepository.isAccountExists(anyLong())).thenReturn(true);
        when(balanceRepository.getBalanceByAccountIdAndCurrency(anyLong(), anyString())).thenReturn(null);

        Exception exception = assertThrows(BalanceNotFoundException.class, () -> onTest.getBalanceByAccountIdAndCurrency(1L, "EUR"));
    }


}
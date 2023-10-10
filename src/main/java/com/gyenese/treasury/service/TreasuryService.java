package com.gyenese.treasury.service;


import com.gyenese.treasury.dao.AccountRepository;
import com.gyenese.treasury.dao.BalanceRepository;
import com.gyenese.treasury.dao.TransactionRepository;
import com.gyenese.treasury.exception.*;
import com.gyenese.treasury.model.dto.AccountDto;
import com.gyenese.treasury.model.dto.BalanceDto;
import com.gyenese.treasury.model.dto.MutationDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.gyenese.treasury.constants.EmailPatterns.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class TreasuryService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;
    private final EmailService emailService;

    @Value("${support.email}")
    private String supportEmail;


    @Transactional(rollbackFor = Exception.class)
    public void updateBalancesByTransactions(double amount, long sendingId, long receivingId, String currency, LocalDateTime xrefDate) {
        log.debug("Update balances for sending Account: {} and receiving Account: {} for {} {}", sendingId, receivingId, amount, xrefDate);
        try {
            checkBalance(sendingId, amount, currency);
            List<Long> ids = List.of(sendingId, receivingId);
            checkBalancesAndCreateIfNotExists(currency, ids);
            balanceRepository.selectForUpdateByAccountsAndCurrency(ids, currency);
            balanceRepository.updateAmountByAccountAndCurrency(sendingId, -1 * amount, currency);
            balanceRepository.updateAmountByAccountAndCurrency(receivingId, amount, currency);
            transactionRepository.save(sendingId, receivingId, amount, currency, xrefDate);
        } catch (BalanceDaoException | TransactionDaoException e) {
            try {
                AccountDto accountDto = accountRepository.getAccountById(sendingId);
                emailService.send(accountDto.getEmail(), NOTIFY_ACCOUNT_ERROR_SUBJECT,
                        MessageFormat.format(NOTIFY_ACCOUNT_ERROR_BODY, accountDto.getFirstName(), accountDto.getLastName(), amount, currency));
                emailService.send(supportEmail, SUPPORT_SUBJECT, MessageFormat.format(SUPPORT_BODY, e));
            } catch (AccountDaoException accountDaoException) {
                emailService.send(supportEmail, SUPPORT_SUBJECT, MessageFormat.format(SUPPORT_BODY, accountDaoException));
            }
        } catch (BalanceNotEnoughException e) {
            try {
                AccountDto accountDto = accountRepository.getAccountById(sendingId);
                emailService.send(accountDto.getEmail(), NOTIFY_ACCOUNT_FOR_NOT_ENOUGH_BALANCE_SUBJECT,
                        MessageFormat.format(NOTIFY_ACCOUNT_FOR_NOT_ENOUGH_BALANCE_BODY, accountDto.getFirstName(), accountDto.getLastName(), amount, currency));
            } catch (AccountDaoException accountDaoException) {
                emailService.send(supportEmail, SUPPORT_SUBJECT, MessageFormat.format(SUPPORT_BODY, accountDaoException));
            }
        }
    }

    public List<MutationDto> listMutationByAccountId(long id) {
        log.debug("Get mutations for account: {}", id);
        boolean isAccountExists;
        try {
            isAccountExists = accountRepository.isAccountExists(id);
        } catch (AccountDaoException e) {
            throw new InternalServerError("Unknown server error during getting mutations");
        }
        if (!isAccountExists) {
            throw new AccountNotFoundException(id);
        }
        try {
            return transactionRepository.listMutations(id);
        } catch (TransactionDaoException e) {
            throw new InternalServerError("Unknown server error during getting mutations");
        }
    }

    public BalanceDto getBalanceByAccountIdAndCurrency(long accountId, String currency) {
        log.debug("Get balance for Account: {} with currency: {}", accountId, currency);
        boolean isAccountExists;
        try {
            isAccountExists = accountRepository.isAccountExists(accountId);
        } catch (AccountDaoException e) {
            throw new InternalServerError("Unknown server error during getting mutations");
        }
        if (!isAccountExists) {
            log.debug("Account not found, id: {}", accountId);
            throw new AccountNotFoundException(accountId);
        }
        List<BalanceDto> balanceByAccountIdAndCurrency;
        try {
            balanceByAccountIdAndCurrency = balanceRepository.getBalanceByAccountIdAndCurrency(accountId, currency);
        } catch (BalanceDaoException e) {
            throw new InternalServerError("Unknown server error during getting mutations");
        }
        if (CollectionUtils.isEmpty(balanceByAccountIdAndCurrency)) {
            log.debug("Balance not found for account: {} with currency: {}", accountId, currency);
            throw new BalanceNotFoundException(accountId, currency);
        }
        return balanceByAccountIdAndCurrency.get(0);
    }

    private void checkBalance(long accountId, double amount, String currency) throws BalanceDaoException, BalanceNotEnoughException {
        log.debug("Check balance for Account {}: {} {}", accountId, amount, currency);
        Double currentBalance = balanceRepository.getAmountByAccountIdAndCurrency(accountId, currency);
        log.debug("Balance for Account {}: {} {}", accountId, currentBalance, currency);
        if (currentBalance.compareTo(amount) > 0) {
            return;
        }
        throw new BalanceNotEnoughException();
    }

    private void checkBalancesAndCreateIfNotExists(String currency, List<Long> accountIds) throws BalanceDaoException {
        log.debug("Check accounts for {} currency: {}", currency, accountIds);
        List<Long> existingAccountIds = balanceRepository.listAccountByCurrency(currency, accountIds);
        log.debug("Existing accounts for {} currency:  {}", currency, existingAccountIds);
        List<Long> tempList = new ArrayList<>(accountIds);
        tempList.removeAll(existingAccountIds);
        if (tempList.size() != 0) {
            balanceRepository.saveAll(tempList, currency);
        }
    }
}

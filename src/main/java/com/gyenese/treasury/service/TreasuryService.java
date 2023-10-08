package com.gyenese.treasury.service;


import com.gyenese.treasury.dao.AccountRepository;
import com.gyenese.treasury.dao.BalanceRepository;
import com.gyenese.treasury.dao.TransactionRepository;
import com.gyenese.treasury.exception.AccountNotFoundException;
import com.gyenese.treasury.exception.BalanceDaoException;
import com.gyenese.treasury.exception.InternalServerError;
import com.gyenese.treasury.exception.TransactionDaoException;
import com.gyenese.treasury.model.dto.MutationDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class TreasuryService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;


    @Transactional(rollbackFor = Exception.class)
    public void updateBalancesByTransactions(double amount, long sendingId, long receivingId, String currency, LocalDateTime xfetDate) {
        try {
            List<Long> ids = List.of(sendingId, receivingId);
            checkBalancesAndCreateIfNotExists(currency, ids);
            balanceRepository.selectForUpdateByAccountsAndCurrency(ids, currency);
            balanceRepository.updateAmountByAccountAndCurrency(sendingId, -1 * amount, currency);
            balanceRepository.updateAmountByAccountAndCurrency(receivingId, amount, currency);
            transactionRepository.save(sendingId, receivingId, amount, currency, xfetDate);
        } catch (BalanceDaoException | TransactionDaoException e) {
            e.printStackTrace();
        }
    }

    public List<MutationDto> getMutationByAccountId(long id) {
        log.debug("Get mutations for account: {}", id);
        boolean isAccountExists = accountRepository.isAccountExist(id);
        if (!isAccountExists) {
            throw new AccountNotFoundException(id);
        }
        try {
            return transactionRepository.listMutations(id);
        } catch (TransactionDaoException e) {
            throw new InternalServerError("Unknown server error during getting mutations");
        }
    }

    private void checkBalancesAndCreateIfNotExists(String currency, List<Long> accountIds) throws BalanceDaoException {
        log.debug("Check accounts for {} currency: {}", currency, accountIds);
        List<Long> existingAccountIds = balanceRepository.listAccountByCurrency(currency, accountIds);
        log.debug("Existing accounts for {} currency:  {}", currency, existingAccountIds);
        if (CollectionUtils.isEmpty(existingAccountIds)) {
            balanceRepository.saveAll(accountIds, currency);
            return;
        }
        List<Long> tempList = new ArrayList<>(accountIds);
        tempList.removeAll(existingAccountIds);
        if (tempList.size() == 0) {
            balanceRepository.saveAll(tempList, currency);
        }
    }
}

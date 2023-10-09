package com.gyenese.treasury.dao;

import com.gyenese.treasury.exception.BalanceDaoException;
import com.gyenese.treasury.model.dto.BalanceDto;

import java.util.List;

public interface BalanceRepository {

    List<Long> listAccountByCurrency(String currency, List<Long> accountIds) throws BalanceDaoException;

    void saveAll(List<Long> accountIds, String currency) throws BalanceDaoException;

    List<BalanceDto> selectForUpdateByAccountsAndCurrency(List<Long> accountIds, String currency) throws BalanceDaoException;

    void updateAmountByAccountAndCurrency(long accountId, double amount, String currency) throws BalanceDaoException;

    Double getAmountByAccountIdAndCurrency(long accountId, String currency) throws BalanceDaoException;

    List<BalanceDto> getBalanceByAccountIdAndCurrency(long accountId, String currency) throws BalanceDaoException;
}

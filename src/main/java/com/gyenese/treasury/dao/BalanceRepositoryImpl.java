package com.gyenese.treasury.dao;

import com.gyenese.treasury.constants.QueryConstants;
import com.gyenese.treasury.exception.BalanceDaoException;
import com.gyenese.treasury.model.dto.BalanceDto;
import com.gyenese.treasury.model.rowmapper.BalanceRowMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gyenese.treasury.constants.FieldConstants.*;
import static com.gyenese.treasury.constants.QueryConstants.*;

@AllArgsConstructor
@Repository
@Slf4j
public class BalanceRepositoryImpl implements BalanceRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Long> listAccountByCurrency(String currency, List<Long> accountIds) throws BalanceDaoException {
        log.debug("Check ids {} for {} currency ", accountIds, currency);
        try {
            Map<String, Object> namedParameters = new HashMap<>();
            namedParameters.put(DB_QUERY_PARAM_ID_LIST, accountIds);
            namedParameters.put(DB_QUERY_PARAM_CURRENCY, currency);
            return namedParameterJdbcTemplate.queryForList(LIST_ACCOUNT_IDS_FROM_BALANCE_BY_CURRENCY_AND_IDS, namedParameters, Long.class);
        } catch (Exception e) {
            log.error("Error during getting account ids from balance by account ids {} and currency {}", accountIds, currency);
            log.error("stack: ", e);
            throw new BalanceDaoException(e);
        }
    }

    @Override
    public void saveAll(List<Long> accountIds, String currency) throws BalanceDaoException {
        log.debug("Create balances for accounts: {} for {} currency", accountIds, currency);
        try {
            for (Long accountId : accountIds) {
                Map<String, Object> namedParameters = new HashMap<>();
                namedParameters.put(DB_QUERY_PARAM_BALANCE_ACCOUNT_ID, accountId);
                namedParameters.put(DB_QUERY_PARAM_CURRENCY, currency);
                namedParameterJdbcTemplate.update(INSERT_BALANCE, namedParameters);
            }
        } catch (Exception e) {
            log.error("Error during create balance for accounts: {} with currency: {}", accountIds, currency);
            log.error("stack: ", e);
            throw new BalanceDaoException(e);
        }
    }

    @Override
    public List<BalanceDto> selectForUpdateByAccountsAndCurrency(List<Long> accountIds, String currency) throws BalanceDaoException {
        log.debug("Call select for update to lock rows for ids: {} and currency: {}", accountIds, currency);
        try {
            Map<String, Object> namedParameters = new HashMap<>();
            namedParameters.put(DB_QUERY_PARAM_BALANCE_ACCOUNT_IDS, accountIds);
            namedParameters.put(DB_QUERY_PARAM_CURRENCY, currency);
            return namedParameterJdbcTemplate.query(QueryConstants.GET_BALANCE_FOR_UPDATE, namedParameters, new BalanceRowMapper());
        } catch (Exception e) {
            log.error("Error during call select for update to lock rows for ids: {} and currency: {}", accountIds, currency);
            log.error("stack: ", e);
            throw new BalanceDaoException(e);
        }
    }

    @Override
    public void updateAmountByAccountAndCurrency(long accountId, double amount, String currency) throws BalanceDaoException {
        log.debug("Update amount for account: {} with amount: {} {}", accountId, amount, currency);
        try {
            Map<String, Object> namedParameters = new HashMap<>();
            namedParameters.put(DB_QUERY_PARAM_BALANCE_ACCOUNT_ID, accountId);
            namedParameters.put(DB_QUERY_PARAM_CURRENCY, currency);
            namedParameters.put(DB_QUERY_PARAM_AMOUNT, amount);
            namedParameterJdbcTemplate.update(QueryConstants.UPDATE_BALANCE_AMOUNT_BY_ACCOUNT_AND_CURRENCY, namedParameters);
        } catch (Exception e) {
            log.error("Error during call update amount for account: {} with amount: {} {}", accountId, amount, currency);
            log.error("stack: ", e);
            throw new BalanceDaoException(e);
        }
    }

    @Override
    public Double getAmountByAccountIdAndCurrency(long accountId, String currency) throws BalanceDaoException {
        log.debug("Get {} amount for account: {}", currency, accountId);
        try {
            Map<String, Object> namedParameters = new HashMap<>();
            namedParameters.put(DB_QUERY_PARAM_BALANCE_ACCOUNT_ID, accountId);
            namedParameters.put(DB_QUERY_PARAM_CURRENCY, currency);
            return namedParameterJdbcTemplate.queryForObject(GET_AMOUNT_BY_ACCOUNT_ID_AND_CURRENCY, namedParameters, Double.class);
        } catch (Exception e) {
            log.error("Error during call get {} amount for account: {}", currency, accountId);
            log.error("stack: ", e);
            throw new BalanceDaoException(e);
        }
    }

    @Override
    public List<BalanceDto> getBalanceByAccountIdAndCurrency(long accountId, String currency) throws BalanceDaoException {
        log.debug("Get {} balance for account: {}", currency, accountId);
        try {
            Map<String, Object> namedParameters = new HashMap<>();
            namedParameters.put(DB_QUERY_PARAM_BALANCE_ACCOUNT_ID, accountId);
            namedParameters.put(DB_QUERY_PARAM_CURRENCY, currency);
            return namedParameterJdbcTemplate.query(GET_BALANCE_BY_ACCOUNT_ID_AND_CURRENCY, namedParameters, new BalanceRowMapper());
        } catch (Exception e) {
            log.error("Error during call get {} balance for account: {}", currency, accountId);
            log.error("stack: ", e);
            throw new BalanceDaoException(e);
        }

    }
}

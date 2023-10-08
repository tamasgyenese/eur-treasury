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
import static com.gyenese.treasury.constants.QueryConstants.INSERT_BALANCE;
import static com.gyenese.treasury.constants.QueryConstants.LIST_ACCOUNT_IDS_FROM_BALANCE_BY_CURRENCY_AND_IDS;

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
        try {
            Map<String, Object> namedParameters = new HashMap<>();
            namedParameters.put(DB_QUERY_PARAM_BALANCE_ACCOUNT_IDS, accountIds);
            namedParameters.put(DB_QUERY_PARAM_CURRENCY, currency);
            return namedParameterJdbcTemplate.query(QueryConstants.SELECT_BALANCE_FOR_UPDATE, namedParameters, new BalanceRowMapper());
        } catch (Exception e) {
            // todo logs
            throw new BalanceDaoException(e);
        }
    }

    @Override
    public void updateAmountByAccountAndCurrency(Long accountId, double amount, String currency) throws BalanceDaoException {
        //todo logs
        try {
            Map<String, Object> namedParameters = new HashMap<>();
            namedParameters.put(DB_QUERY_PARAM_BALANCE_ACCOUNT_ID, accountId);
            namedParameters.put(DB_QUERY_PARAM_CURRENCY, currency);
            namedParameters.put(DB_QUERY_PARAM_AMOUNT, amount);
            namedParameterJdbcTemplate.update(QueryConstants.UPDATE_BALANCE_AMOUNT_BY_ACCOUNT_AND_CURRENCY, namedParameters);
        } catch (Exception e) {
            throw new BalanceDaoException(e);
        }
    }
}

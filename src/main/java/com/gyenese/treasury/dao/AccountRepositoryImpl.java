package com.gyenese.treasury.dao;


import com.gyenese.treasury.constants.FieldConstants;
import com.gyenese.treasury.exception.AccountDaoException;
import com.gyenese.treasury.model.dto.AccountDto;
import com.gyenese.treasury.model.rowmapper.AccountRowMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

import static com.gyenese.treasury.constants.QueryConstants.CHECK_ACCOUNT_EXISTS;
import static com.gyenese.treasury.constants.QueryConstants.GET_ACCOUNT_BY_ID;

@AllArgsConstructor
@Repository
@Slf4j
public class AccountRepositoryImpl implements AccountRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public boolean isAccountExists(long id) throws AccountDaoException {
        log.debug("Check account exists with id: {}", id);
        try {
            Map<String, Object> namedParameters = new HashMap<>();
            namedParameters.put(FieldConstants.DB_QUERY_PARAM_ID, id);
            Long one = 1L;
            return one.equals(namedParameterJdbcTemplate.queryForObject(CHECK_ACCOUNT_EXISTS, namedParameters, Long.class));
        } catch (Exception e) {
            log.error("Error during checking account for id: {}", id);
            log.error("stack: ", e);
            throw new AccountDaoException(e);
        }
    }

    @Override
    public AccountDto getAccountById(long id) throws AccountDaoException {
        log.debug("Get account for id: {}", id);
        try {
            Map<String, Object> namedParameters = new HashMap<>();
            namedParameters.put(FieldConstants.DB_FIELD_ACCOUNT_ID, id);
            return namedParameterJdbcTemplate.query(GET_ACCOUNT_BY_ID, namedParameters, new AccountRowMapper()).get(0);
        } catch (Exception e) {
            log.error("Error during getting email for id: {}", id);
            log.error("stack: ", e);
            throw new AccountDaoException(e);
        }
    }
}

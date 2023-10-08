package com.gyenese.treasury.dao;


import com.gyenese.treasury.constants.FieldConstants;
import com.gyenese.treasury.exception.InternalServerError;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

import static com.gyenese.treasury.constants.QueryConstants.CHECK_ACCOUNT_EXISTS;

@AllArgsConstructor
@Repository
@Slf4j
public class AccountRepositoryImpl implements AccountRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public boolean isAccountExist(long id) {
        log.debug("Check account exists with id: {}", id);
        try {
            Map<String, Object> namedParameters = new HashMap<>();
            namedParameters.put(FieldConstants.DB_QUERY_PARAM_ID, id);
            return 1L == namedParameterJdbcTemplate.queryForObject(CHECK_ACCOUNT_EXISTS, namedParameters, Long.class);
        } catch (Exception e) {
            log.error("Error during checkin account for id: {}", id);
            log.error("stack: ", e);
            throw new InternalServerError("Unknown server error during getting mutations");
        }
    }
}

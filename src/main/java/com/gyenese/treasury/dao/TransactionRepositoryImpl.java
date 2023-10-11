package com.gyenese.treasury.dao;

import com.gyenese.treasury.constants.FieldConstants;
import com.gyenese.treasury.constants.QueryConstants;
import com.gyenese.treasury.exception.TransactionDaoException;
import com.gyenese.treasury.model.dto.MutationDto;
import com.gyenese.treasury.model.rowmapper.MutationMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gyenese.treasury.constants.FieldConstants.*;

@Repository
@AllArgsConstructor
@Slf4j
public class TransactionRepositoryImpl implements TransactionRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<MutationDto> listMutations(long id) throws TransactionDaoException {
        log.debug("Get mutations for id: {}", id);
        try {
            Map<String, Object> namedParameters = new HashMap<>();
            namedParameters.put(FieldConstants.DB_FIELD_ACCOUNT_ID, id);
            return namedParameterJdbcTemplate.query(QueryConstants.GET_MUTATIONS, namedParameters, new MutationMapper());
        } catch (Exception e) {
            log.error("Error during getting mutation for account: {}", id);
            log.error("stack: ", e);
            throw new TransactionDaoException(e);
        }
    }

    @Override
    public void save(long sendingId, long receivingId, double amount, String currency, LocalDateTime xferDate) throws TransactionDaoException {
        log.debug("Save transaction between accounts: {} {} with amount: {} {}", sendingId, receivingId, amount, currency);
        try {
            Map<String, Object> namedParameters = new HashMap<>();
            namedParameters.put(DB_QUERY_PARAM_SENDING_ID, sendingId);
            namedParameters.put(DB_QUERY_PARAM_RECEIVING_ID, receivingId);
            namedParameters.put(DB_QUERY_PARAM_AMOUNT, amount);
            namedParameters.put(DB_QUERY_PARAM_TRANSACTION_DATE, xferDate);
            namedParameters.put(DB_QUERY_PARAM_CURRENCY, currency);
            namedParameterJdbcTemplate.update(QueryConstants.INSERT_TRANSACTION, namedParameters);
        } catch (Exception e) {
            log.error("Error dugin call save transaction between accounts: {} {} with amount: {} {}", sendingId, receivingId, amount, currency);
            log.error("stack: ", e);
            throw new TransactionDaoException(e);
        }
    }

}

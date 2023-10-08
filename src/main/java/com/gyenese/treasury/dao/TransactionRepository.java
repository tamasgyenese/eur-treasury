package com.gyenese.treasury.dao;

import com.gyenese.treasury.exception.TransactionDaoException;
import com.gyenese.treasury.model.dto.MutationDto;


import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository {

    List<MutationDto> listMutations(long id) throws TransactionDaoException;

    void save(long sendingId, long receivingId, double amount, String currency, LocalDateTime xferDate) throws TransactionDaoException;
}

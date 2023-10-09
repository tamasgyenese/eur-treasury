package com.gyenese.treasury.dao;

import com.gyenese.treasury.exception.AccountDaoException;
import com.gyenese.treasury.model.dto.AccountDto;
import com.gyenese.treasury.model.dto.BalanceDto;

public interface AccountRepository {

    boolean isAccountExists(long id) throws AccountDaoException;

    AccountDto getAccountById(long id) throws AccountDaoException;
}

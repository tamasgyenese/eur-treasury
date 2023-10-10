package com.gyenese.treasury.dao;

import com.gyenese.treasury.exception.AccountDaoException;
import com.gyenese.treasury.model.dto.AccountDto;

public interface AccountRepository {

    boolean isAccountExists(long id) throws AccountDaoException;

    AccountDto getAccountById(long id) throws AccountDaoException;
}

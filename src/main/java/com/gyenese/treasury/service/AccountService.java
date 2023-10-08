package com.gyenese.treasury.service;

import com.gyenese.treasury.dao.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

}

package com.gyenese.treasury.dao;

import com.gyenese.treasury.exception.AccountDaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;

import static com.gyenese.treasury.constants.FieldConstants.DB_FIELD_ACCOUNT_ID;
import static com.gyenese.treasury.constants.FieldConstants.DB_QUERY_PARAM_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AccountRepositoryImplTest {

    @InjectMocks
    private AccountRepositoryImpl onTest;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Check when Account is exist, the DB must return with value 1")
    public void testIsAccountExist() throws AccountDaoException {
        Long id = 1L;
        when(namedParameterJdbcTemplate.queryForObject(anyString(), eq(new HashMap<>() {{
            put(DB_QUERY_PARAM_ID, id);
        }}), eq(Long.class))).thenReturn(1L);

        assertTrue(onTest.isAccountExists(id));
    }

    @Test
    @DisplayName("Check when Account is not exist, the DB must return with value 0")
    public void testIsAccountNotExist() throws AccountDaoException {
        Long id = 1L;
        when(namedParameterJdbcTemplate.queryForObject(anyString(), eq(new HashMap<>() {{
            put(DB_QUERY_PARAM_ID, id);
        }}), eq(Long.class))).thenReturn(0L);

        assertFalse(onTest.isAccountExists(id));
    }

    @Test
    @DisplayName("DB error during checking Account is exist")
    public void errorDuringCheckinAccountIsExist() throws AccountDaoException {
        Long id = 1L;
        when(namedParameterJdbcTemplate.queryForObject(anyString(), eq(new HashMap<>() {{
            put(DB_QUERY_PARAM_ID, id);
        }}), eq(Long.class))).thenThrow(new RuntimeException());

        Exception exception = assertThrows(AccountDaoException.class, () -> {
            onTest.isAccountExists(id);
        });
    }


    @Test
    @DisplayName("Error during getting email by error")
    public void errorDuringGettingEmailById() {
        Long id = 1L;
        when(namedParameterJdbcTemplate.queryForObject(anyString(), eq(new HashMap<>() {{
            put(DB_FIELD_ACCOUNT_ID, id);
        }}), eq(String.class))).thenThrow(new RuntimeException());

        Exception exception = assertThrows(AccountDaoException.class, () -> {
            onTest.getAccountById(id);
        });
    }


}
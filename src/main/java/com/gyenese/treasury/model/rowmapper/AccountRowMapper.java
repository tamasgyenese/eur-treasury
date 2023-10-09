package com.gyenese.treasury.model.rowmapper;

import com.gyenese.treasury.model.dto.AccountDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.gyenese.treasury.constants.FieldConstants.*;

public class AccountRowMapper implements RowMapper<AccountDto> {


    @Override
    public AccountDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(rs.getLong(DB_FIELD_ACCOUNT_ID));
        accountDto.setFirstName(rs.getString(DB_FIELD_FIRST_NAME));
        accountDto.setLastName(rs.getString(DB_FIELD_LAST_NAME));
        accountDto.setEmail(rs.getString(DB_FIELD_EMAIL));

        return accountDto;
    }
}

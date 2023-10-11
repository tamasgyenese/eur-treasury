package com.gyenese.treasury.model.rowmapper;


import com.gyenese.treasury.constants.FieldConstants;
import com.gyenese.treasury.model.dto.BalanceDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class BalanceRowMapper implements RowMapper<BalanceDto> {


    @Override
    public BalanceDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        BalanceDto balanceDto = new BalanceDto();
        balanceDto.setId(rs.getLong(FieldConstants.DB_FIELD_BALANCE_ID));
        balanceDto.setAccountId(rs.getLong(FieldConstants.DB_FIELD_BALANCE_ACCOUNT_ID));
        balanceDto.setAmount(rs.getDouble(FieldConstants.DB_FIELD_BALANCE_AMOUNT));
        balanceDto.setCurrency(rs.getString(FieldConstants.DB_FIELD_BALANCE_CURRENCY));

        return balanceDto;
    }
}

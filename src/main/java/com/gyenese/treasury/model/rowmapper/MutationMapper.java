package com.gyenese.treasury.model.rowmapper;

import com.gyenese.treasury.constants.FieldConstants;
import com.gyenese.treasury.model.dto.MutationDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

public class MutationMapper implements RowMapper<MutationDto> {


    @Override
    public MutationDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        MutationDto mutationDto = new MutationDto();
        mutationDto.setTransactionId(rs.getLong(FieldConstants.DB_FIELD_MUTATION_TRANSACTION_ID));
        mutationDto.setAmount(rs.getDouble(FieldConstants.DB_FIELD_MUTATION_AMOUNT));
        mutationDto.setPartnerName(rs.getString(FieldConstants.DB_FIELD_MUTATION_PARTNER_NAME));
        mutationDto.setCurrency(rs.getString(FieldConstants.DB_FIELD_MUTATION_CURRENCY));
        mutationDto.setDate(rs.getDate(FieldConstants.DB_FIELD_MUTATION_XFER_DATE).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        return mutationDto;
    }
}

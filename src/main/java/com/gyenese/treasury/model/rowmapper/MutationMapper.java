package com.gyenese.treasury.model.rowmapper;

import com.gyenese.treasury.constants.FieldConstants;
import com.gyenese.treasury.model.dto.MutationDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MutationMapper implements RowMapper<MutationDto> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public MutationDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        MutationDto mutationDto = new MutationDto();
        mutationDto.setTransactionId(rs.getLong(FieldConstants.DB_FIELD_MUTATION_TRANSACTION_ID));
        mutationDto.setAmount(rs.getDouble(FieldConstants.DB_FIELD_MUTATION_AMOUNT));
        mutationDto.setPartnerName(rs.getString(FieldConstants.DB_FIELD_MUTATION_PARTNER_NAME));
        mutationDto.setCurrency(rs.getString(FieldConstants.DB_FIELD_MUTATION_CURRENCY));
        mutationDto.setDate(stringToLocalDateTime(rs.getString(FieldConstants.DB_FIELD_MUTATION_XFER_DATE)));

        return mutationDto;
    }

    private LocalDateTime stringToLocalDateTime(String dateString) {
        return LocalDateTime.parse(dateString, formatter);
    }
}

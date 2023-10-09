package com.gyenese.treasury.controller;

import com.gyenese.treasury.exception.AccountDaoException;
import com.gyenese.treasury.model.dto.BalanceDto;
import com.gyenese.treasury.model.dto.MutationDto;
import com.gyenese.treasury.service.TreasuryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/treasury", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Slf4j
public class TreasuryController {

    private final TreasuryService treasuryService;


    @GetMapping("/mutation/{id}")
    public ResponseEntity<List<MutationDto>> listMutations(@PathVariable Long id) {
        log.debug("List mutations for account {}", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(treasuryService.listMutationByAccountId(id));
    }

    @GetMapping("/balance/{id}/{currency}")
    public ResponseEntity<BalanceDto> getBalance(@PathVariable Long id, @PathVariable String currency) {
        log.debug("");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BalanceDto());
    }

}

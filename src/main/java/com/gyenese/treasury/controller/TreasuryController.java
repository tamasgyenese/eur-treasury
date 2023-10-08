package com.gyenese.treasury.controller;

import com.gyenese.treasury.dao.TransactionRepositoryImpl;
import com.gyenese.treasury.model.dto.BalanceDto;
import com.gyenese.treasury.model.dto.MutationDto;
import lombok.AllArgsConstructor;
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
public class TreasuryController {

    private final TransactionRepositoryImpl mutationRepository;

    @GetMapping("/mutation/{id}")
    public ResponseEntity<List<MutationDto>> listMutations(@PathVariable Integer id) {
        //List<MutationDto> mutationDtos = mutationRepository.listMutations(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity<BalanceDto> getBalance(@PathVariable Integer id) {
        // todo
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BalanceDto());
    }
}

package com.gyenese.treasury.controller;


import com.gyenese.treasury.model.dto.BalanceDto;
import com.gyenese.treasury.model.dto.MutationDto;
import com.gyenese.treasury.service.TreasuryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(
        name = "REST API for Treasure Service",
        description = "REST API to fetch balance details for Accounts"
)
@RestController
@RequestMapping(path = "/api/v1/treasury", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Slf4j
public class TreasuryController {

    private final TreasuryService treasuryService;


    @Operation(
            description = "Fetch transaction mutations for Account"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status Account not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    })
    @GetMapping("/mutation/{id}")
    public ResponseEntity<List<MutationDto>> listMutations(@PathVariable Long id) {
        log.debug("List mutations for account {}", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(treasuryService.listMutationByAccountId(id));
    }

    @Operation(
            description = "Fetch balance with the given currency for Account"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status Account/Balance not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    })
    @GetMapping("/balance/{id}/{currency}")
    public ResponseEntity<BalanceDto> getBalance(@PathVariable Long id, @PathVariable String currency) {
        log.debug("Get balance for Account: {} eur: {}", id, currency);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(treasuryService.getBalanceByAccountIdAndCurrency(id, currency));
    }

}

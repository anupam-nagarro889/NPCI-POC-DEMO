package com.npci.integration.controllers;


import com.npci.integration.dto.TransactionDTO;
import com.npci.integration.service.TransactionService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<?> initiateTransaction(@RequestBody TransactionDTO transactionDTO) throws BadRequestException {
        return ResponseEntity.ok(transactionService.initiateTransaction(transactionDTO));

    }
}

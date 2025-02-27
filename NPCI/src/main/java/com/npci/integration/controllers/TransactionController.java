package com.npci.integration.controllers;


import com.npci.integration.dto.TransactionDTO;
import com.npci.integration.service.TransactionService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/getTransaction/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable String transactionId) {
        return ResponseEntity.ok(transactionService.getTransaction(transactionId));
    }

    @DeleteMapping("/deleteTransaction/{transactionId}")
    public ResponseEntity<String> deleteTransaction(@PathVariable String transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok("Transaction deleted successfully");
    }

    @GetMapping("/listTransactions")
    public ResponseEntity<List<TransactionDTO>> listTransactions(){
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
}

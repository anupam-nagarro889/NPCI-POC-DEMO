package com.npci.integration.controllers;


import com.npci.integration.dto.TransactionDTO;
import com.npci.integration.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/rest/npci/transaction-management")
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction")
    public ResponseEntity<?> initiateTransaction(@RequestBody TransactionDTO transactionDTO) throws BadRequestException {
        log.info("Initiating a new transactions");
        return ResponseEntity.ok(transactionService.initiateTransaction(transactionDTO));

    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable String transactionId) {
        log.info("Fetching a transaction");
        return ResponseEntity.ok(transactionService.getTransaction(transactionId));
    }

    @DeleteMapping("/transaction/{transactionId}")
    public ResponseEntity<String> deleteTransaction(@PathVariable String transactionId) {
        log.info("Deleting a transaction");
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok("Transaction deleted successfully");
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> listTransactions(){
        log.info("Listing transactions");
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
}

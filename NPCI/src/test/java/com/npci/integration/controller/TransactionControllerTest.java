package com.npci.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.npci.integration.controllers.TransactionController;
import com.npci.integration.dto.TransactionDTO;
import com.npci.integration.service.TransactionService;
import com.npci.integration.util.TestUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    private final String TRANSACTION_CONTROLLER_PATH = "/api/transactions";

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;

    TestUtility testUtility;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        testUtility = new TestUtility();
    }

    @Test
    void testInitiateTransaction() throws Exception {
        // Sample transaction data
        TransactionDTO transactionDTO = testUtility.getTransactionDto("txn123", "merch1", "merch2", "ref1");

        // Mock service call
        Map<String, String> response = new HashMap<>();
        response.put("Transaction id", transactionDTO.getTransactionId());
        when(transactionService.initiateTransaction(any(TransactionDTO.class))).thenReturn(response);
        objectMapper.registerModule(new JavaTimeModule());

        // Perform POST request
        mockMvc.perform(post(TRANSACTION_CONTROLLER_PATH + "/initiate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$['Transaction id']").value("txn123"));
    }

    @Test
    void testGetTransaction() throws Exception {
        // Sample transaction
        String transactionId = "TXN123";
        TransactionDTO transactionDTO = testUtility.getTransactionDto(transactionId, "merch1", "merch2", "ref1");

        // Mock service call
        when(transactionService.getTransaction(transactionId)).thenReturn(transactionDTO);

        // Perform GET request
        mockMvc.perform(get(TRANSACTION_CONTROLLER_PATH + "/getTransaction/{transactionId}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionId").value(transactionId));
    }

    @Test
    void testDeleteTransaction() throws Exception {
        // Sample transaction ID
        String transactionId = "TXN123";

        // Mock service call
        doNothing().when(transactionService).deleteTransaction(transactionId);

        // Perform DELETE request
        mockMvc.perform(delete(TRANSACTION_CONTROLLER_PATH + "/deleteTransaction/{transactionId}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction deleted successfully"));
    }

    @Test
    void testListTransactions() throws Exception {
        // Sample transactions list
        List<TransactionDTO> transactions = Arrays.asList(
                testUtility.getTransactionDto("TXN123", "user1", "user2", "REF1"),
                testUtility.getTransactionDto("TXN124", "user3", "user4", "REF2")
        );

        // Mock service call
        when(transactionService.getAllTransactions()).thenReturn(transactions);

        // Perform GET request
        mockMvc.perform(get(TRANSACTION_CONTROLLER_PATH + "/listTransactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].transactionId").value("TXN123"));
    }

}

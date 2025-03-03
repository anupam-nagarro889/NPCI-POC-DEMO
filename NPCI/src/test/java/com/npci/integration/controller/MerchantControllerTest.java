package com.npci.integration.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.npci.integration.controllers.MerchantController;
import com.npci.integration.dto.MerchantDTO;
import com.npci.integration.service.MerchantService;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class MerchantControllerTest {

    @Mock
    private MerchantService merchantService;

    @InjectMocks
    private MerchantController merchantController;

    private MockMvc mockMvc;

    TestUtility testUtility;

    ObjectMapper objectMapper = new ObjectMapper();

    private final String MERCHANT_CONTROLLER_PATH = "/rest/service/merchant/api" ;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(merchantController).build();
        testUtility = new TestUtility();

    }

    @Test
    void testGetAllMerchants() throws Exception {
        // Sample Data
        MerchantDTO merchant1 = testUtility.getMerchantDto(1L, "PH123", "Phone Pay", "url1");
        MerchantDTO merchant2 = testUtility.getMerchantDto(2L, "PAYTM123", "Paytm", "url2");
        List<MerchantDTO> merchantList = Arrays.asList(merchant1, merchant2);

        // Mocking service response
        when(merchantService.getAllMerchant()).thenReturn(merchantList);

        // Perform GET request and validate response
        mockMvc.perform(get(MERCHANT_CONTROLLER_PATH+"/getAllMerchants")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Phone Pay"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Paytm"));
    }

    @Test
    void testAddMerchant() throws Exception {
        // Sample Merchant Data
        MerchantDTO merchant1 = testUtility.getMerchantDto(1L, "PH123", "Phone Pay", "url1");

        when(merchantService.addMerchantDetails(any(MerchantDTO.class))).thenReturn(merchant1);

        // Perform POST Request
        mockMvc.perform(post(MERCHANT_CONTROLLER_PATH + "/addMerchant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(merchant1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.merchantCode").value("PH123"));

        // Verify Service Call
        verify(merchantService, times(1)).addMerchantDetails(any(MerchantDTO.class));
    }

    @Test
    void testUpdateMerchantDetails() throws Exception {
        // Sample Merchant Data
        MerchantDTO merchant1 = testUtility.getMerchantDto(1L, "PH123", "Phone Pay", "url1");

        when(merchantService.updateMerchantData(any(MerchantDTO.class))).thenReturn(merchant1);

        // Perform PUT Request
        mockMvc.perform(put(MERCHANT_CONTROLLER_PATH + "/updateMerchant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(merchant1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Phone Pay"));

        // Verify Service Call
        verify(merchantService, times(1)).updateMerchantData(any(MerchantDTO.class));
    }

    @Test
    void testDeleteMerchant() throws Exception {
        // Mock Service Response
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        when(merchantService.deleteMerchant(1L)).thenReturn(response);
        mockMvc.perform(delete(MERCHANT_CONTROLLER_PATH + "/deleteMerchant/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted").value(true));

        // Verify Service Call
        verify(merchantService, times(1)).deleteMerchant(1L);
    }

    @Test
    void testGetMerchantById() throws Exception{
        MerchantDTO merchantDTO = testUtility.getMerchantDto(1L, "m1", "merchant1", "url1");

        when(merchantService.getMerchantByID(1L)).thenReturn(merchantDTO);
        mockMvc.perform(get(MERCHANT_CONTROLLER_PATH + "/getMerchant/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.merchantCode").value("m1"));

    }


}

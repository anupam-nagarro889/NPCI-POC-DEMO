package com.npci.integration.controller;


import com.npci.integration.controllers.MerchantController;
import com.npci.integration.dto.MerchantDTO;
import com.npci.integration.service.MerchantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    void testGetAllMerchants() throws Exception {
        // Sample Data
        MerchantDTO merchant1 = new MerchantDTO();
        merchant1.setId(1L);
        merchant1.setMerchantCode("PH123");
        merchant1.setName("Phone Pay");
        merchant1.setCallbackUrl("url1");
        MerchantDTO merchant2 = new MerchantDTO();
        merchant2.setId(2L);
        merchant2.setMerchantCode("PAYTM123");
        merchant2.setName("Paytm");
        merchant2.setCallbackUrl("url2");
        List<MerchantDTO> merchantList = Arrays.asList(merchant1, merchant2);

        // Mocking service response
        when(merchantService.getAllMerchant()).thenReturn(merchantList);

        // Initialize MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(merchantController).build();

        // Perform GET request and validate response
        mockMvc.perform(get("/rest/service/merchant/api/getAllMerchants")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Phone Pay"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Paytm"));
    }
}

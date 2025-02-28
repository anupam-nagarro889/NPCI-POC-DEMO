package com.npci.integration.service;


import com.npci.integration.dto.MerchantDTO;
import com.npci.integration.mapper.MerchantMapper;
import com.npci.integration.models.Merchant;
import com.npci.integration.repository.MerchantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MerchantServiceTest {

    @Mock
    private MerchantRepository repository;

    @Mock
    private MerchantMapper merchantMapper;

    @Mock
    private RedisService redisService; // Mocked but not used in this test

    @InjectMocks
    private MerchantService merchantService;

    @Test
    void testGetAllMerchant() {
        // Step 1: Create Mock Data
        Merchant merchant1 = new Merchant();
        Merchant merchant2 = new Merchant();
        merchant1.setId(1L);
        merchant1.setMerchantCode("PH123");
        merchant1.setName("Phone Pay");
        merchant1.setCallbackUrl("url1");

        merchant2.setId(2L);
        merchant2.setMerchantCode("PAYTM123");
        merchant2.setName("Paytm");
        merchant2.setCallbackUrl("url2");
        List<Merchant> merchantList = Arrays.asList(merchant1, merchant2);

        MerchantDTO merchantDTO1 = new MerchantDTO();
        MerchantDTO merchantDTO2 = new MerchantDTO();
        merchantDTO1.setId(1L);
        merchantDTO1.setMerchantCode("PH123");
        merchantDTO1.setName("Phone Pay");
        merchantDTO1.setCallbackUrl("url1");

        merchantDTO2.setId(2L);
        merchantDTO2.setMerchantCode("PAYTM123");
        merchantDTO2.setName("Paytm");
        merchantDTO2.setCallbackUrl("url2");
        List<MerchantDTO> merchantDTOList = Arrays.asList(merchantDTO1, merchantDTO2);

        // Step 2: Mock Repository and Mapper
        when(repository.findAll()).thenReturn(merchantList);
        when(merchantMapper.merchantToDtoList(merchantList)).thenReturn(merchantDTOList);

        // Step 3: Call Service Method
        List<MerchantDTO> result = merchantService.getAllMerchant();

        // Step 4: Assertions
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Phone Pay", result.get(0).getName());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Paytm", result.get(1).getName());
    }
}

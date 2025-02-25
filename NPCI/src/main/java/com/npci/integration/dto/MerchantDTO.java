package com.npci.integration.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantDTO {
    private Long id;
    private String merchantCode;
    private String name;
    private String callbackUrl;
}

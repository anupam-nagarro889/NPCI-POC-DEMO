package com.npci.integration.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentGatewayDTO {
    private Long id;
    private String gatewayCode;
    private String gatewayName;
    private String apiUrl;
}

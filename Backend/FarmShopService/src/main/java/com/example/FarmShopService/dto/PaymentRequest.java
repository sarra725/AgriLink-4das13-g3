package com.example.FarmShopService.dto;

import com.example.FarmShopService.enumeration.MethodePayment;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

    @NotNull
    private Long commandeId;

    @NotNull
    private Long clientId;

    @NotNull
    private MethodePayment methode;

    private Long carteId;
}
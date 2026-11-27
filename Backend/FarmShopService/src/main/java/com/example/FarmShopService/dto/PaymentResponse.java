package com.example.FarmShopService.dto;

import com.example.FarmShopService.enumeration.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentResponse {

    private Long id;

    private BigDecimal montant;

    private LocalDateTime datePayment;

    private MethodePayment methode;

    private StatutPayment statut;

    private Long commandeId;

    private StatutCommande statutCommande;

    private Long clientId;

    private Long agriculteurId;
}
package com.example.FarmShopService.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LigneCommandeResponse {

    private Long id;

    private Long offreId;

    private String nomProduit;

    private String imageUrl;

    private String unite;

    private int qte;

    private BigDecimal prixUnit;

    private BigDecimal sousTotal;
}
package com.example.FarmShopService.dto;

import com.example.FarmShopService.enumeration.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OffreProduitResponse {

    private Long idOffre;

    private String nomProduit;

    private String description;

    private BigDecimal prixUnitaire;

    private String unite;

    private Double quantiteDispo;

    private QualiteProduit qualite;

    private StatutOffre statut;

    private LocalDateTime datePub;

    private String imageUrl;

    private CategorieProduit categorie;

    private Long agriculteurId;

    private String nomAgriculteur;

    private String prenomAgriculteur;
}
package com.example.FarmShopService.dto;

import com.example.FarmShopService.enumeration.StatutCommande;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CommandeResponse {

    private Long id;

    private LocalDateTime dateCommande;

    private BigDecimal montantTotal;

    private String adresseLivraison;

    private StatutCommande statut;

    // Informations du client

    private Long clientId;

    private String nomClient;

    private String prenomClient;

    // Informations de l'agriculteur

    private Long agriculteurId;

    private String nomAgriculteur;

    private String prenomAgriculteur;

    // Produits commandés

    private List<LigneCommandeResponse> lignesCommandes;
}
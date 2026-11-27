package com.example.FarmShopService.dto;

import com.example.FarmShopService.enumeration.TypeCarte;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CarteBancaireResponse {

    private Long idCarte;

    private String derniersQuatreChiffres;

    private LocalDate expirationDate;

    private TypeCarte type;

    private Long clientId;

    private String nomClient;

    private String prenomClient;
}
package com.example.FarmShopService.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LigneCommandeRequest {

    @NotNull
    private Long offreId;

    @Positive
    private int qte;
}
package com.example.FarmShopService.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommandeRequest {

    @NotNull
    private Long clientId;

    @NotBlank
    private String adresseLivraison;

    @NotEmpty
    @Valid
    private List<@NotNull LigneCommandeRequest> lignesCommandes;
}
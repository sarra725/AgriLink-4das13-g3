package com.example.FarmShopService.dto;

import com.example.FarmShopService.enumeration.TypeCarte;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CarteBancaireRequest {

    private Long clientId;

    @NotBlank
    @Pattern(regexp = "[0-9]{4}")
    private String derniersQuatreChiffres;

    @NotNull
    @FutureOrPresent
    private LocalDate expirationDate;

    @NotNull
    private TypeCarte type;
}
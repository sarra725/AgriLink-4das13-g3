package com.example.FarmShopService.dto;

import com.example.FarmShopService.enumeration.CategorieProduit;
import com.example.FarmShopService.enumeration.QualiteProduit;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OffreProduitRequest {

    @NotBlank(message = "Le nom du produit est obligatoire")
    private String nomProduit;

    @NotBlank(message = "La description est obligatoire")
    @Size(max = 2000)
    private String description;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.001",
            message = "Le prix doit être positif")
    @Digits(integer = 9, fraction = 3)
    private BigDecimal prixUnitaire;

    @NotBlank(message = "L'unité est obligatoire")
    private String unite;

    @NotNull(message = "La quantité est obligatoire")
    @PositiveOrZero(message = "La quantité doit être positive ou nulle")
    private Double quantiteDispo;

    @NotNull(message = "La qualité est obligatoire")
    private QualiteProduit qualite;

    @NotNull(message = "La catégorie est obligatoire")
    private CategorieProduit categorie;

    private String imageUrl;

    @NotNull(message = "L'identifiant de l'agriculteur est obligatoire")
    @Positive
    private Long agriculteurId;
}
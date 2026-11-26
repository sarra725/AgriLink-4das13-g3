package com.example.FarmShopService.entities;

import com.example.FarmShopService.enumeration.CategorieProduit;
import com.example.FarmShopService.enumeration.QualiteProduit;
import com.example.FarmShopService.enumeration.StatutOffre;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "offres_produits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OffreProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOffre;

    @Column(nullable = false)
    private String nomProduit;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal prixUnitaire;

    private String unite;

    @Column(nullable = false)
    private Double quantiteDispo;

    @Enumerated(EnumType.STRING)
    private QualiteProduit qualite;

    @Enumerated(EnumType.STRING)
    private StatutOffre statut = StatutOffre.ACTIVE;

    private LocalDateTime datePub;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private CategorieProduit categorie;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agriculteur_id", nullable = false)
    private Agriculteur agriculteur;

    @JsonIgnore
    @OneToMany(mappedBy = "offreProduit")
    private List<LigneCommande> lignesCommandes =
            new ArrayList<>();

    @PrePersist
    protected void onCreate() {

        if (datePub == null) {
            datePub = LocalDateTime.now();
        }
    }
}
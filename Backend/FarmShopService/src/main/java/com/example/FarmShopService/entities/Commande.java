package com.example.FarmShopService.entities;

import com.example.FarmShopService.enumeration.StatutCommande;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commandes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateCommande;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal montantTotal = BigDecimal.ZERO;

    private String adresseLivraison;

    @Enumerated(EnumType.STRING)
    private StatutCommande statut =
            StatutCommande.EN_ATTENTE;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agriculteur_id", nullable = false)
    private Agriculteur agriculteur;

    @OneToMany(
            mappedBy = "commande",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<LigneCommande> lignesCommandes =
            new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "commande")
    private Payment payment;

    @PrePersist
    protected void onCreate() {
        if (dateCommande == null) {
            dateCommande = LocalDateTime.now();
        }
    }
}
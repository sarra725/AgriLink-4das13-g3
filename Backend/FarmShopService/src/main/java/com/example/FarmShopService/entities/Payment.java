package com.example.FarmShopService.entities;

import com.example.FarmShopService.enumeration.MethodePayment;
import com.example.FarmShopService.enumeration.StatutPayment;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal montant;

    private LocalDateTime datePayment;

    @Enumerated(EnumType.STRING)
    private MethodePayment methode;

    @Enumerated(EnumType.STRING)
    private StatutPayment statut =
            StatutPayment.EN_ATTENTE;

    @OneToOne(optional = false)
    @JoinColumn(
            name = "commande_id",
            nullable = false,
            unique = true
    )
    private Commande commande;
}
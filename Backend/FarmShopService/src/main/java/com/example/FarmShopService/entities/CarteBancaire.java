package com.example.FarmShopService.entities;

import com.example.FarmShopService.enumeration.TypeCarte;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "cartes_bancaires")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarteBancaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarte;

    @Column(length = 4)
    private String derniersQuatreChiffres;

    @JsonIgnore
    private String tokenCarte;

    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    private TypeCarte type;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;
}
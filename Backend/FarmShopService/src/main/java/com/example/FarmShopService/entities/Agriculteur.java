package com.example.FarmShopService.entities;

import com.example.FarmShopService.enumeration.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agriculteurs")
@Getter
@Setter
@AllArgsConstructor
public class Agriculteur extends User {

    private String localisation;

    private String specialite;

    @JsonIgnore
    @OneToMany(mappedBy = "agriculteur")
    private List<OffreProduit> offres =
            new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "agriculteur")
    private List<Commande> commandes =
            new ArrayList<>();

    public Agriculteur() {
        setRole(Role.AGRICULTEUR);
    }
}
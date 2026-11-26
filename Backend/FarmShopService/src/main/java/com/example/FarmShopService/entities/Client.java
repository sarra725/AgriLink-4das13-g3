package com.example.FarmShopService.entities;

import com.example.FarmShopService.enumeration.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@Getter
@Setter
@AllArgsConstructor
public class Client extends User {

    private String entreprise;

    private String adresseLivraison;

    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private List<Commande> commandes =
            new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private List<CarteBancaire> cartesBancaires =
            new ArrayList<>();

    public Client() {
        setRole(Role.CLIENT);
    }
}
package com.example.FarmShopService.entities;

import com.example.FarmShopService.enumeration.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private String telephone;

    private String adresse;

    private String imageUrl;

    private LocalDateTime dateCreation;

    private Boolean actif = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    private String entreprise;

    @PrePersist
    protected void onCreate() {

        if (dateCreation == null) {
            dateCreation = LocalDateTime.now();
        }
    }
}
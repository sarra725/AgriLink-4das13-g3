package com.example.FarmShopService.repositories;

import com.example.FarmShopService.entities.LigneCommande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigneCommandeRepository
        extends JpaRepository<LigneCommande, Long> {

    List<LigneCommande> findByCommandeId(Long commandeId);

    List<LigneCommande> findByOffreProduitIdOffre(Long offreId);
}
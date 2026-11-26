package com.example.FarmShopService.Interfservices;

import com.example.FarmShopService.dto.OffreProduitRequest;
import com.example.FarmShopService.dto.OffreProduitResponse;
import com.example.FarmShopService.enumeration.CategorieProduit;
import com.example.FarmShopService.enumeration.StatutOffre;

import java.util.List;

public interface IOffreProduitService {

    // CREATE
    OffreProduitResponse publierOffre(
            OffreProduitRequest request
    );

    // READ
    List<OffreProduitResponse> getAllOffres();

    OffreProduitResponse getOffreById(Long idOffre);

    // UPDATE
    OffreProduitResponse modifierOffre(
            Long idOffre,
            OffreProduitRequest request
    );

    // DELETE
    void supprimerOffre(Long idOffre);

    // Désactivation
    OffreProduitResponse desactiverOffre(Long idOffre);

    // Activation
    OffreProduitResponse activerOffre(Long idOffre);

    // Offres d'un agriculteur
    List<OffreProduitResponse> getOffresByAgriculteur(
            Long agriculteurId
    );

    // Filtrer par catégorie
    List<OffreProduitResponse> getOffresByCategorie(
            CategorieProduit categorie
    );

    // Filtrer par statut
    List<OffreProduitResponse> getOffresByStatut(
            StatutOffre statut
    );

    // Recherche
    List<OffreProduitResponse> rechercherOffres(
            String motCle
    );

    // Offres disponibles
    List<OffreProduitResponse> getOffresDisponibles();

    // Offres disponibles par catégorie
    List<OffreProduitResponse> getOffresDisponiblesByCategorie(
            CategorieProduit categorie
    );

    // Trier les offres
    List<OffreProduitResponse> trierOffres(String sort);

    // Modifier la quantité disponible
    OffreProduitResponse modifierStock(
            Long idOffre,
            Double quantite
    );
}
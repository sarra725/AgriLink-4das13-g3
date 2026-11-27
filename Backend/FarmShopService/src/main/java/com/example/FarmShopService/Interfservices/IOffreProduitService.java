package com.example.FarmShopService.Interfservices;

import com.example.FarmShopService.dto.OffreProduitRequest;
import com.example.FarmShopService.dto.OffreProduitResponse;
import com.example.FarmShopService.enumeration.CategorieProduit;
import com.example.FarmShopService.enumeration.StatutOffre;

import java.util.List;

public interface IOffreProduitService {

    OffreProduitResponse publierOffre(
            OffreProduitRequest request);

    List<OffreProduitResponse> getAllOffres();

    OffreProduitResponse getOffreById(Long idOffre);

    OffreProduitResponse modifierOffre(
            Long idOffre,
            OffreProduitRequest request);

    void supprimerOffre(
            Long idOffre,
            Long agriculteurId);

    OffreProduitResponse desactiverOffre(
            Long idOffre,
            Long agriculteurId);

    OffreProduitResponse activerOffre(
            Long idOffre,
            Long agriculteurId);

    List<OffreProduitResponse> getOffresByAgriculteur(
            Long agriculteurId);

    List<OffreProduitResponse> getOffresByCategorie(
            CategorieProduit categorie);

    List<OffreProduitResponse> getOffresByStatut(
            StatutOffre statut);

    List<OffreProduitResponse> rechercherOffres(
            String motCle);

    List<OffreProduitResponse> getOffresDisponibles();

    List<OffreProduitResponse> getOffresDisponiblesByCategorie(
            CategorieProduit categorie);

    List<OffreProduitResponse> trierOffres(String sort);

    OffreProduitResponse modifierStock(
            Long idOffre,
            Long agriculteurId,
            Double quantite);
}
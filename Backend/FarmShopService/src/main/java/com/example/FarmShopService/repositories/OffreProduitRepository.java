package com.example.FarmShopService.repositories;

import com.example.FarmShopService.entities.OffreProduit;
import com.example.FarmShopService.enumeration.CategorieProduit;
import com.example.FarmShopService.enumeration.StatutOffre;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OffreProduitRepository
        extends JpaRepository<OffreProduit, Long> {

    // Offres d'un agriculteur
    List<OffreProduit> findByAgriculteurId(Long agriculteurId);

    // Offres par catégorie
    List<OffreProduit> findByCategorie(CategorieProduit categorie);

    // Offres par statut
    List<OffreProduit> findByStatut(StatutOffre statut);

    // Recherche par nom
    List<OffreProduit> findByNomProduitContainingIgnoreCase(
            String nomProduit
    );

    // Produits disponibles
    List<OffreProduit> findByStatutAndQuantiteDispoGreaterThan(
            StatutOffre statut,
            Double quantite
    );

    // Produits disponibles par catégorie
    List<OffreProduit> findByCategorieAndStatutAndQuantiteDispoGreaterThan(
            CategorieProduit categorie,
            StatutOffre statut,
            Double quantite
    );

    // Vérifier si une offre est utilisée dans une commande
    boolean existsByIdOffreAndLignesCommandesIsNotEmpty(
            Long idOffre
    );
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM OffreProduit o WHERE o.idOffre = :id")
    Optional<OffreProduit> findByIdForUpdate(
            @Param("id") Long id);
}
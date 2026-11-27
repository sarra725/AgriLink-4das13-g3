package com.example.FarmShopService.repositories;

import com.example.FarmShopService.entities.Commande;
import com.example.FarmShopService.enumeration.StatutCommande;

import com.example.FarmShopService.enumeration.StatutPayment;
import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommandeRepository
        extends JpaRepository<Commande, Long> {

    List<Commande> findByClientIdOrderByDateCommandeDesc(
            Long clientId);

    List<Commande> findByAgriculteurIdOrderByDateCommandeDesc(
            Long agriculteurId);

    List<Commande> findByClientIdAndStatut(
            Long clientId,
            StatutCommande statut);

    List<Commande> findByAgriculteurIdAndStatut(
            Long agriculteurId,
            StatutCommande statut);

    List<Commande> findByStatut(
            StatutCommande statut);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Commande c WHERE c.id = :id")
    Optional<Commande> findByIdForUpdate(
            @Param("id") Long id);

    List<Commande> findByAgriculteurIdAndPaymentStatutOrderByDateCommandeDesc(
            Long agriculteurId,
            StatutPayment statut
    );
}
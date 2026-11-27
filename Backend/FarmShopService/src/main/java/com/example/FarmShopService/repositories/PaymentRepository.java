package com.example.FarmShopService.repositories;

import com.example.FarmShopService.entities.Payment;
import com.example.FarmShopService.enumeration.StatutPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    Optional<Payment> findByCommandeId(Long commandeId);

    List<Payment> findByCommandeClientId(Long clientId);

    List<Payment> findByCommandeAgriculteurId(
            Long agriculteurId);

    List<Payment> findByStatut(StatutPayment statut);

    boolean existsByCommandeId(Long commandeId);

    @Query(
            value = "SELECT * FROM payments WHERE id = :id FOR UPDATE",
            nativeQuery = true
    )
    Optional<Payment> findByIdForUpdate(@Param("id") Long id);
}
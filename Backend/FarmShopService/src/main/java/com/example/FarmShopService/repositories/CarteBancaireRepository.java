package com.example.FarmShopService.repositories;

import com.example.FarmShopService.entities.CarteBancaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarteBancaireRepository
        extends JpaRepository<CarteBancaire, Long> {

    List<CarteBancaire> findByClientId(Long clientId);
}
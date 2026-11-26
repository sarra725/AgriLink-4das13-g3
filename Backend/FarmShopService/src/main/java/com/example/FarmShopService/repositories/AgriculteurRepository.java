package com.example.FarmShopService.repositories;

import com.example.FarmShopService.entities.Agriculteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgriculteurRepository
        extends JpaRepository<Agriculteur, Long> {

}
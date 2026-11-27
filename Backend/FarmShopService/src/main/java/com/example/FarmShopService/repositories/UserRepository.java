package com.example.FarmShopService.repositories;

import com.example.FarmShopService.entities.User;
import com.example.FarmShopService.enumeration.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndRole(Long id, Role role);

    boolean existsByIdAndRole(Long id, Role role);
}
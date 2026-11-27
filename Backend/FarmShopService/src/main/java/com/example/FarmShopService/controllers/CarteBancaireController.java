package com.example.FarmShopService.controllers;

import com.example.FarmShopService.Interfservices.ICarteBancaireService;
import com.example.FarmShopService.dto.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cartes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CarteBancaireController {

    private final ICarteBancaireService carteService;

    @PostMapping
    public ResponseEntity<CarteBancaireResponse> ajouterCarte(
            @Valid @RequestBody CarteBancaireRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(carteService.ajouterCarte(request));
    }

    @GetMapping("/{idCarte}")
    public ResponseEntity<CarteBancaireResponse> getCarteById(
            @PathVariable Long idCarte) {

        return ResponseEntity.ok(
                carteService.getCarteById(idCarte)
        );
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CarteBancaireResponse>>
    getCartesByClient(@PathVariable Long clientId) {

        return ResponseEntity.ok(
                carteService.getCartesByClient(clientId)
        );
    }

    @PutMapping("/{idCarte}")
    public ResponseEntity<CarteBancaireResponse> modifierCarte(
            @PathVariable Long idCarte,
            @Valid @RequestBody CarteBancaireRequest request) {

        return ResponseEntity.ok(
                carteService.modifierCarte(idCarte, request)
        );
    }

    @DeleteMapping("/{idCarte}")
    public ResponseEntity<Void> supprimerCarte(
            @PathVariable Long idCarte) {

        carteService.supprimerCarte(idCarte);

        return ResponseEntity.noContent().build();
    }
}
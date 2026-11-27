package com.example.FarmShopService.controllers;

import com.example.FarmShopService.Interfservices.IOffreProduitService;
import com.example.FarmShopService.dto.OffreProduitRequest;
import com.example.FarmShopService.dto.OffreProduitResponse;
import com.example.FarmShopService.enumeration.CategorieProduit;
import com.example.FarmShopService.enumeration.StatutOffre;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offres")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class OffreProduitController {

    private final IOffreProduitService offreProduitService;

    @PostMapping
    public ResponseEntity<OffreProduitResponse> publierOffre(
            @Valid @RequestBody OffreProduitRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(offreProduitService.publierOffre(request));
    }


    @GetMapping
    public ResponseEntity<List<OffreProduitResponse>>
    getAllOffres() {

        return ResponseEntity.ok(
                offreProduitService.getAllOffres()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<OffreProduitResponse> getOffreById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                offreProduitService.getOffreById(id)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<OffreProduitResponse> modifierOffre(
            @PathVariable Long id,
            @Valid @RequestBody OffreProduitRequest request) {

        return ResponseEntity.ok(
                offreProduitService.modifierOffre(id, request)
        );
    }



    @DeleteMapping("/{idOffre}")
    public ResponseEntity<Void> supprimerOffre(
            @PathVariable Long idOffre,
            @RequestParam Long agriculteurId) {

        offreProduitService.supprimerOffre(
                idOffre,
                agriculteurId
        );

        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{idOffre}/desactiver")
    public ResponseEntity<OffreProduitResponse> desactiverOffre(
            @PathVariable Long idOffre,
            @RequestParam Long agriculteurId) {

        return ResponseEntity.ok(
                offreProduitService.desactiverOffre(
                        idOffre,
                        agriculteurId
                )
        );
    }

    @PatchMapping("/{idOffre}/activer")
    public ResponseEntity<OffreProduitResponse> activerOffre(
            @PathVariable Long idOffre,
            @RequestParam Long agriculteurId) {

        return ResponseEntity.ok(
                offreProduitService.activerOffre(
                        idOffre,
                        agriculteurId
                )
        );
    }



    @GetMapping("/agriculteur/{agriculteurId}")
    public ResponseEntity<List<OffreProduitResponse>>
    getOffresByAgriculteur(
            @PathVariable Long agriculteurId) {

        return ResponseEntity.ok(
                offreProduitService.getOffresByAgriculteur(
                        agriculteurId
                )
        );
    }


    @GetMapping("/categorie/{categorie}")
    public ResponseEntity<List<OffreProduitResponse>>
    getOffresByCategorie(
            @PathVariable CategorieProduit categorie) {

        return ResponseEntity.ok(
                offreProduitService.getOffresByCategorie(categorie)
        );
    }


    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<OffreProduitResponse>>
    getOffresByStatut(
            @PathVariable StatutOffre statut) {

        return ResponseEntity.ok(
                offreProduitService.getOffresByStatut(statut)
        );
    }



    @GetMapping("/recherche")
    public ResponseEntity<List<OffreProduitResponse>>
    rechercherOffres(
            @RequestParam String motCle) {

        return ResponseEntity.ok(
                offreProduitService.rechercherOffres(motCle)
        );
    }



    @GetMapping("/disponibles")
    public ResponseEntity<List<OffreProduitResponse>>
    getOffresDisponibles() {

        return ResponseEntity.ok(
                offreProduitService.getOffresDisponibles()
        );
    }




    @GetMapping("/disponibles/categorie/{categorie}")
    public ResponseEntity<List<OffreProduitResponse>>
    getOffresDisponiblesByCategorie(
            @PathVariable CategorieProduit categorie) {

        return ResponseEntity.ok(
                offreProduitService
                        .getOffresDisponiblesByCategorie(categorie)
        );
    }



    @GetMapping("/tri")
    public ResponseEntity<List<OffreProduitResponse>>
    trierOffres(
            @RequestParam(defaultValue = "newest") String sort) {

        return ResponseEntity.ok(
                offreProduitService.trierOffres(sort)
        );
    }



    @PatchMapping("/{idOffre}/stock")
    public ResponseEntity<OffreProduitResponse> modifierStock(
            @PathVariable Long idOffre,
            @RequestParam Long agriculteurId,
            @RequestParam Double quantite) {

        return ResponseEntity.ok(
                offreProduitService.modifierStock(
                        idOffre,
                        agriculteurId,
                        quantite
                )
        );
    }
}
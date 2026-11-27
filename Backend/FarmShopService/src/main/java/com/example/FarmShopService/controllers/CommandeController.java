package com.example.FarmShopService.controllers;

import com.example.FarmShopService.Interfservices.ICommandeService;
import com.example.FarmShopService.dto.CommandeRequest;
import com.example.FarmShopService.dto.CommandeResponse;
import com.example.FarmShopService.enumeration.StatutCommande;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CommandeController {

    private final ICommandeService commandeService;


    // ==========================================
    // 1. PASSER UNE COMMANDE
    // ==========================================

    @PostMapping
    public ResponseEntity<CommandeResponse> passerCommande(
            @Valid @RequestBody CommandeRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        commandeService.passerCommande(request)
                );
    }


    // ==========================================
    // 2. CONSULTER TOUTES LES COMMANDES
    // ==========================================

    @GetMapping
    public ResponseEntity<List<CommandeResponse>>
    getAllCommandes() {

        return ResponseEntity.ok(
                commandeService.getAllCommandes()
        );
    }


    // ==========================================
    // 3. CONSULTER UNE COMMANDE PAR ID
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<CommandeResponse> getCommandeById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                commandeService.getCommandeById(id)
        );
    }


    // ==========================================
    // 4. MODIFIER UNE COMMANDE
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<CommandeResponse> modifierCommande(
            @PathVariable Long id,
            @Valid @RequestBody CommandeRequest request) {

        return ResponseEntity.ok(
                commandeService.modifierCommande(
                        id,
                        request
                )
        );
    }


    // ==========================================
    // 5. SUPPRIMER UNE COMMANDE
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerCommande(
            @PathVariable Long id) {

        commandeService.supprimerCommande(id);

        return ResponseEntity.noContent().build();
    }


    // ==========================================
    // 6. CONSULTER LES COMMANDES D'UN CLIENT
    // ==========================================

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CommandeResponse>>
    getCommandesByClient(
            @PathVariable Long clientId) {

        return ResponseEntity.ok(
                commandeService.getCommandesByClient(clientId)
        );
    }


    // ==========================================
    // 7. HISTORIQUE DES COMMANDES D'UN CLIENT
    // ==========================================

    @GetMapping("/client/{clientId}/historique")
    public ResponseEntity<List<CommandeResponse>>
    getHistoriqueClient(
            @PathVariable Long clientId) {

        return ResponseEntity.ok(
                commandeService.getHistoriqueClient(clientId)
        );
    }


    // ==========================================
    // 8. ANNULER UNE COMMANDE
    // ==========================================

    @PatchMapping("/{id}/annuler")
    public ResponseEntity<CommandeResponse> annulerCommande(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                commandeService.annulerCommande(id)
        );
    }


    // ==========================================
    // 9. COMMANDES D'UN CLIENT PAR STATUT
    // ==========================================

    @GetMapping("/client/{clientId}/statut/{statut}")
    public ResponseEntity<List<CommandeResponse>>
    getCommandesClientByStatut(
            @PathVariable Long clientId,
            @PathVariable StatutCommande statut) {

        return ResponseEntity.ok(
                commandeService.getCommandesClientByStatut(
                        clientId,
                        statut
                )
        );
    }


    // ==========================================
    // 10. COMMANDES D'UN AGRICULTEUR
    // ==========================================

    @GetMapping("/agriculteur/{agriculteurId}")
    public ResponseEntity<List<CommandeResponse>>
    getCommandesByAgriculteur(
            @PathVariable Long agriculteurId) {

        return ResponseEntity.ok(
                commandeService.getCommandesByAgriculteur(
                        agriculteurId
                )
        );
    }


    // ==========================================
    // 11. ACCEPTER UNE COMMANDE
    // ==========================================

    @PatchMapping("/{id}/accepter")
    public ResponseEntity<CommandeResponse> accepterCommande(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                commandeService.accepterCommande(id)
        );
    }


    // ==========================================
    // 12. REFUSER UNE COMMANDE
    // ==========================================

    @PatchMapping("/{id}/refuser")
    public ResponseEntity<CommandeResponse> refuserCommande(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                commandeService.refuserCommande(id)
        );
    }


    // ==========================================
    // 13. MODIFIER LE STATUT D'UNE COMMANDE
    // ==========================================

    @PatchMapping("/{id}/statut")
    public ResponseEntity<CommandeResponse> modifierStatut(
            @PathVariable Long id,
            @RequestParam StatutCommande statut) {

        return ResponseEntity.ok(
                commandeService.modifierStatut(id, statut)
        );
    }


    // ==========================================
    // 14. COMMANDES AGRICULTEUR PAR STATUT
    // ==========================================

    @GetMapping("/agriculteur/{agriculteurId}/statut/{statut}")
    public ResponseEntity<List<CommandeResponse>>
    getCommandesAgriculteurByStatut(
            @PathVariable Long agriculteurId,
            @PathVariable StatutCommande statut) {

        return ResponseEntity.ok(
                commandeService.getCommandesAgriculteurByStatut(
                        agriculteurId,
                        statut
                )
        );
    }


    // ==========================================
    // 15. TOUTES LES COMMANDES PAR STATUT
    // ==========================================

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<CommandeResponse>>
    getCommandesByStatut(
            @PathVariable StatutCommande statut) {

        return ResponseEntity.ok(
                commandeService.getCommandesByStatut(statut)
        );
    }
    // ==========================================
// COMMANDES PAYEES D'UN AGRICULTEUR
// ==========================================

    @GetMapping("/agriculteur/{agriculteurId}/payees")
    public ResponseEntity<List<CommandeResponse>>
    getCommandesPayeesByAgriculteur(
            @PathVariable Long agriculteurId) {

        return ResponseEntity.ok(
                commandeService.getCommandesPayeesByAgriculteur(
                        agriculteurId
                )
        );
    }
}
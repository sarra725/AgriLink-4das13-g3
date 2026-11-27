package com.example.FarmShopService.controllers;

import com.example.FarmShopService.Interfservices.IPaymentService;
import com.example.FarmShopService.dto.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    private final IPaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> effectuerPayment(
            @Valid @RequestBody PaymentRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(paymentService.effectuerPayment(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(
            @PathVariable Long id,
            @RequestParam Long userId) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(id, userId)
        );
    }

    @GetMapping("/commande/{commandeId}")
    public ResponseEntity<PaymentResponse> getPaymentByCommande(
            @PathVariable Long commandeId,
            @RequestParam Long userId) {

        return ResponseEntity.ok(
                paymentService.getPaymentByCommande(
                        commandeId, userId)
        );
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PaymentResponse>>
    getPaymentsByClient(@PathVariable Long clientId) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByClient(clientId)
        );
    }

    @GetMapping("/agriculteur/{agriculteurId}")
    public ResponseEntity<List<PaymentResponse>>
    getPaymentsByAgriculteur(
            @PathVariable Long agriculteurId) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByAgriculteur(
                        agriculteurId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> modifierPayment(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequest request) {

        return ResponseEntity.ok(
                paymentService.modifierPayment(id, request)
        );
    }

    @PatchMapping("/{id}/annuler")
    public ResponseEntity<PaymentResponse> annulerPayment(
            @PathVariable Long id,
            @RequestParam Long clientId) {

        return ResponseEntity.ok(
                paymentService.annulerPayment(id, clientId)
        );
    }

    @PatchMapping("/{id}/valider")
    public ResponseEntity<PaymentResponse> validerPayment(
            @PathVariable Long id,
            @RequestParam Long agriculteurId) {

        return ResponseEntity.ok(
                paymentService.validerPayment(
                        id, agriculteurId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerPayment(
            @PathVariable Long id,
            @RequestParam Long clientId) {

        paymentService.supprimerPayment(id, clientId);

        return ResponseEntity.noContent().build();
    }
}
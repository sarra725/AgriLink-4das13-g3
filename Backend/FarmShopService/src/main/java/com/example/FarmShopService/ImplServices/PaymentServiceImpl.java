package com.example.FarmShopService.ImplServices;

import com.example.FarmShopService.Interfservices.IPaymentService;
import com.example.FarmShopService.dto.*;
import com.example.FarmShopService.entities.*;
import com.example.FarmShopService.enumeration.*;
import com.example.FarmShopService.repositories.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final CommandeRepository commandeRepository;
    private final CarteBancaireRepository carteBancaireRepository;
    private final UserRepository userRepository;

    // ==========================================
    // 1. EFFECTUER UN PAIEMENT
    // ==========================================

    @Override
    public PaymentResponse effectuerPayment(
            PaymentRequest request) {

        Commande commande = verrouillerCommande(
                request.getCommandeId()
        );

        verifierClientCommande(
                commande, request.getClientId()
        );

        if (commande.getStatut() != StatutCommande.ACCEPTEE) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La commande doit être acceptée"
            );
        }

        if (paymentRepository.existsByCommandeId(
                commande.getId())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Un paiement existe déjà pour cette commande"
            );
        }

        Payment payment = new Payment();

        payment.setCommande(commande);

        // Montant récupéré automatiquement
        payment.setMontant(commande.getMontantTotal());

        payment.setDatePayment(LocalDateTime.now());

        appliquerMethode(payment, commande, request);

        commande.setPayment(payment);

        return toResponse(
                paymentRepository.save(payment)
        );
    }

    // ==========================================
    // 2. AFFICHER UN PAIEMENT PAR ID
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(
            Long id, Long userId) {

        Payment payment = trouverPayment(id);

        verifierAcces(payment, userId);

        return toResponse(payment);
    }

    // ==========================================
    // 3. AFFICHER LE PAIEMENT D'UNE COMMANDE
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByCommande(
            Long commandeId, Long userId) {

        Payment payment = paymentRepository
                .findByCommandeId(commandeId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Paiement introuvable"
                        )
                );

        verifierAcces(payment, userId);

        return toResponse(payment);
    }

    // ==========================================
    // 4. AFFICHER LES PAIEMENTS D'UN CLIENT
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByClient(
            Long clientId) {

        verifierUtilisateur(clientId, Role.CLIENT);

        return paymentRepository
                .findByCommandeClientId(clientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ==========================================
    // 5. AFFICHER LES PAIEMENTS D'UN AGRICULTEUR
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByAgriculteur(
            Long agriculteurId) {

        verifierUtilisateur(
                agriculteurId, Role.AGRICULTEUR
        );

        return paymentRepository
                .findByCommandeAgriculteurId(agriculteurId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ==========================================
    // 6. MODIFIER UN PAIEMENT NON FINALISE
    // ==========================================

    @Override
    public PaymentResponse modifierPayment(
            Long id, PaymentRequest request) {

        Payment payment = verrouillerPayment(id);

        Commande commande = payment.getCommande();

        verifierClientCommande(
                commande, request.getClientId()
        );

        if (!commande.getId().equals(
                request.getCommandeId())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Impossible de changer la commande"
            );
        }

        verifierPaymentModifiable(payment);

        if (commande.getStatut() != StatutCommande.ACCEPTEE) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Commande non disponible pour le paiement"
            );
        }

        payment.setDatePayment(LocalDateTime.now());

        appliquerMethode(payment, commande, request);

        return toResponse(
                paymentRepository.save(payment)
        );
    }

    // ==========================================
    // 7. ANNULER UN PAIEMENT
    // ==========================================

    @Override
    public PaymentResponse annulerPayment(
            Long id, Long clientId) {

        Payment payment = verrouillerPayment(id);

        verifierClientCommande(
                payment.getCommande(), clientId
        );

        verifierPaymentModifiable(payment);

        payment.setStatut(StatutPayment.ANNULE);

        return toResponse(
                paymentRepository.save(payment)
        );
    }

    // ==========================================
    // 8. VALIDER UN PAIEMENT EN ATTENTE
    // ==========================================

    @Override
    public PaymentResponse validerPayment(
            Long id, Long agriculteurId) {

        Payment payment = verrouillerPayment(id);

        Commande commande = payment.getCommande();

        verifierUtilisateur(
                agriculteurId, Role.AGRICULTEUR
        );

        if (!commande.getAgriculteur().getId()
                .equals(agriculteurId)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Ce paiement ne vous concerne pas"
            );
        }

        if (payment.getStatut() != StatutPayment.EN_ATTENTE) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Le paiement n'est pas en attente"
            );
        }

        if (commande.getStatut() != StatutCommande.ACCEPTEE) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Commande non acceptée"
            );
        }

        payment.setStatut(StatutPayment.REUSSI);

        payment.setDatePayment(LocalDateTime.now());

        commande.setStatut(StatutCommande.CONFIRMEE);

        commandeRepository.save(commande);

        return toResponse(
                paymentRepository.save(payment)
        );
    }

    // ==========================================
    // 9. SUPPRIMER UN PAIEMENT ANNULE OU ECHOUE
    // ==========================================

    @Override
    public void supprimerPayment(
            Long id, Long clientId) {

        Payment payment = verrouillerPayment(id);

        Commande commande = payment.getCommande();

        verifierClientCommande(commande, clientId);

        if (payment.getStatut() != StatutPayment.ANNULE
                && payment.getStatut() != StatutPayment.ECHOUE) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce paiement ne peut pas être supprimé"
            );
        }

        commande.setPayment(null);

        paymentRepository.delete(payment);
    }

    // ==========================================
    // GESTION DE LA METHODE DE PAIEMENT
    // ==========================================

    private void appliquerMethode(
            Payment payment,
            Commande commande,
            PaymentRequest request) {

        if (request.getMethode() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Méthode de paiement obligatoire"
            );
        }

        payment.setMethode(request.getMethode());

        if (request.getMethode()
                == MethodePayment.CARTE_BANCAIRE) {

            verifierCarte(
                    request.getCarteId(),
                    request.getClientId()
            );

            // SIMULATION UNIQUEMENT
            payment.setStatut(StatutPayment.REUSSI);

            commande.setStatut(StatutCommande.CONFIRMEE);

            commandeRepository.save(commande);

        } else {

            // Espèces ou virement :
            // validation manuelle dans le simulateur
            payment.setStatut(StatutPayment.EN_ATTENTE);
        }
    }

    // ==========================================
    // VERIFIER LA CARTE BANCAIRE
    // ==========================================

    private void verifierCarte(
            Long carteId, Long clientId) {

        if (carteId == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Carte bancaire obligatoire"
            );
        }

        CarteBancaire carte = carteBancaireRepository
                .findById(carteId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Carte bancaire introuvable"
                        )
                );

        if (!carte.getClient().getId().equals(clientId)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Cette carte ne vous appartient pas"
            );
        }

        if (carte.getExpirationDate()
                .isBefore(LocalDate.now())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Carte bancaire expirée"
            );
        }
    }

    // ==========================================
    // METHODES PRIVEES
    // ==========================================

    private Payment trouverPayment(Long id) {

        return paymentRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Paiement introuvable"
                        )
                );
    }

    private Commande verrouillerCommande(Long id) {

        return commandeRepository.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Commande introuvable"
                        )
                );
    }

    private Payment verrouillerPayment(Long id) {

        Payment payment = trouverPayment(id);

        verrouillerCommande(
                payment.getCommande().getId()
        );

        return paymentRepository.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Paiement introuvable"
                        )
                );
    }

    private User verifierUtilisateur(
            Long userId, Role role) {

        if (userId == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Utilisateur obligatoire"
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Utilisateur introuvable"
                        )
                );

        if (user.getRole() != role
                || !Boolean.TRUE.equals(user.getActif())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Accès interdit"
            );
        }

        return user;
    }

    private void verifierClientCommande(
            Commande commande, Long clientId) {

        verifierUtilisateur(clientId, Role.CLIENT);

        if (!commande.getClient().getId().equals(clientId)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Cette commande ne vous appartient pas"
            );
        }
    }

    private void verifierAcces(
            Payment payment, Long userId) {

        Commande commande = payment.getCommande();

        if (commande.getClient().getId().equals(userId)) {

            verifierUtilisateur(userId, Role.CLIENT);

        } else if (commande.getAgriculteur().getId()
                .equals(userId)) {

            verifierUtilisateur(userId, Role.AGRICULTEUR);

        } else {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Vous ne pouvez pas consulter ce paiement"
            );
        }
    }

    private void verifierPaymentModifiable(Payment payment) {

        if (payment.getStatut() != StatutPayment.EN_ATTENTE
                && payment.getStatut() != StatutPayment.ECHOUE) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce paiement ne peut pas être modifié"
            );
        }
    }

    private PaymentResponse toResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();

        response.setId(payment.getId());

        response.setMontant(payment.getMontant());

        response.setDatePayment(payment.getDatePayment());

        response.setMethode(payment.getMethode());

        response.setStatut(payment.getStatut());

        response.setCommandeId(
                payment.getCommande().getId()
        );

        response.setStatutCommande(
                payment.getCommande().getStatut()
        );

        response.setClientId(
                payment.getCommande().getClient().getId()
        );

        response.setAgriculteurId(
                payment.getCommande().getAgriculteur().getId()
        );

        return response;
    }
}
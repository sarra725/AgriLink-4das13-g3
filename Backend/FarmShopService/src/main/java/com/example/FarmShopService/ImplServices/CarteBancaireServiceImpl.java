package com.example.FarmShopService.ImplServices;

import com.example.FarmShopService.Interfservices.ICarteBancaireService;
import com.example.FarmShopService.dto.*;
import com.example.FarmShopService.entities.*;
import com.example.FarmShopService.enumeration.Role;
import com.example.FarmShopService.repositories.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CarteBancaireServiceImpl
        implements ICarteBancaireService {

    private final CarteBancaireRepository carteBancaireRepository;
    private final UserRepository userRepository;

    // ==========================================
    // 1. AJOUTER UNE CARTE
    // ==========================================

    @Override
    public CarteBancaireResponse ajouterCarte(
            CarteBancaireRequest request) {

        User client = verifierClient(request.getClientId());

        CarteBancaire carte = new CarteBancaire();

        carte.setClient(client);

        remplirCarte(carte, request);

        // Token fictif pour la simulation
        carte.setTokenCarte(
                "test_" + UUID.randomUUID()
        );

        return toResponse(
                carteBancaireRepository.save(carte)
        );
    }

    // ==========================================
    // 2. AFFICHER UNE CARTE PAR ID
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public CarteBancaireResponse getCarteById(Long idCarte) {

        CarteBancaire carte = trouverCarte(idCarte);

        return toResponse(carte);
    }

    // ==========================================
    // 3. AFFICHER LES CARTES D'UN CLIENT
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<CarteBancaireResponse> getCartesByClient(
            Long clientId) {

        verifierClient(clientId);

        return carteBancaireRepository
                .findByClientId(clientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ==========================================
    // 4. MODIFIER UNE CARTE
    // ==========================================

    @Override
    public CarteBancaireResponse modifierCarte(
            Long idCarte,
            CarteBancaireRequest request) {

        CarteBancaire carte = trouverCarte(idCarte);

        remplirCarte(carte, request);

        carte.setTokenCarte("test_" + UUID.randomUUID());

        return toResponse(
                carteBancaireRepository.save(carte)
        );
    }

    // ==========================================
    // 5. SUPPRIMER UNE CARTE
    // ==========================================

    @Override
    public void supprimerCarte(Long idCarte) {

        CarteBancaire carte = trouverCarte(idCarte);

        carteBancaireRepository.delete(carte);
    }

    // ==========================================
    // METHODES PRIVEES
    // ==========================================

    private User verifierClient(Long clientId) {

        if (clientId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Client obligatoire"
            );
        }

        User user = userRepository.findById(clientId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Client introuvable"
                        )
                );

        if (user.getRole() != Role.CLIENT) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Seul un client peut gérer des cartes"
            );
        }

        if (!Boolean.TRUE.equals(user.getActif())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Compte désactivé"
            );
        }

        return user;
    }

    private CarteBancaire trouverCarte(Long idCarte) {

        return carteBancaireRepository.findById(idCarte)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Carte bancaire introuvable"
                        )
                );
    }



    private void remplirCarte(
            CarteBancaire carte,
            CarteBancaireRequest request) {

        if (request.getExpirationDate() == null
                || request.getExpirationDate()
                .isBefore(LocalDate.now())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Carte expirée"
            );
        }

        if (request.getDerniersQuatreChiffres() == null
                || !request.getDerniersQuatreChiffres()
                .matches("[0-9]{4}")) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Les quatre derniers chiffres sont invalides"
            );
        }

        if (request.getType() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Type de carte obligatoire"
            );
        }

        carte.setDerniersQuatreChiffres(
                request.getDerniersQuatreChiffres()
        );

        carte.setExpirationDate(
                request.getExpirationDate()
        );

        carte.setType(request.getType());
    }

    private CarteBancaireResponse toResponse(
            CarteBancaire carte) {

        CarteBancaireResponse response =
                new CarteBancaireResponse();

        response.setIdCarte(carte.getIdCarte());

        response.setDerniersQuatreChiffres(
                carte.getDerniersQuatreChiffres()
        );

        response.setExpirationDate(
                carte.getExpirationDate()
        );

        response.setType(carte.getType());

        response.setClientId(
                carte.getClient().getId()
        );

        response.setNomClient(
                carte.getClient().getNom()
        );

        response.setPrenomClient(
                carte.getClient().getPrenom()
        );

        return response;
    }
}
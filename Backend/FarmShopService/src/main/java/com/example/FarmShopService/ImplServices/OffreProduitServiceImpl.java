package com.example.FarmShopService.ImplServices;

import com.example.FarmShopService.Interfservices.IOffreProduitService;
import com.example.FarmShopService.dto.OffreProduitRequest;
import com.example.FarmShopService.dto.OffreProduitResponse;
import com.example.FarmShopService.entities.Agriculteur;
import com.example.FarmShopService.entities.OffreProduit;
import com.example.FarmShopService.enumeration.CategorieProduit;
import com.example.FarmShopService.enumeration.StatutOffre;
import com.example.FarmShopService.repositories.AgriculteurRepository;
import com.example.FarmShopService.repositories.OffreProduitRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OffreProduitServiceImpl
        implements IOffreProduitService {

    private final OffreProduitRepository offreProduitRepository;

    private final AgriculteurRepository agriculteurRepository;


    // ==========================================
    // 1. PUBLIER UNE OFFRE
    // ==========================================

    @Override
    public OffreProduitResponse publierOffre(
            OffreProduitRequest request) {

        Agriculteur agriculteur = agriculteurRepository
                .findById(request.getAgriculteurId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Agriculteur introuvable"
                ));

        OffreProduit offre = new OffreProduit();

        remplirOffre(offre, request);

        offre.setAgriculteur(agriculteur);

        if (offre.getQuantiteDispo() == 0) {

            offre.setStatut(StatutOffre.RUPTURE_STOCK);

        } else {

            offre.setStatut(StatutOffre.ACTIVE);
        }

        OffreProduit savedOffre =
                offreProduitRepository.save(offre);

        return toResponse(savedOffre);
    }


    // ==========================================
    // 2. AFFICHER TOUTES LES OFFRES
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<OffreProduitResponse> getAllOffres() {

        return offreProduitRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ==========================================
    // 3. AFFICHER UNE OFFRE PAR ID
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public OffreProduitResponse getOffreById(Long idOffre) {

        OffreProduit offre = trouverOffre(idOffre);

        return toResponse(offre);
    }


    // ==========================================
    // 4. MODIFIER UNE OFFRE
    // ==========================================

    @Override
    public OffreProduitResponse modifierOffre(
            Long idOffre,
            OffreProduitRequest request) {

        OffreProduit offre = trouverOffre(idOffre);

        // Interdire le changement du propriétaire
        if (!offre.getAgriculteur().getId()
                .equals(request.getAgriculteurId())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Impossible de changer le propriétaire de l'offre"
            );
        }

        remplirOffre(offre, request);

        actualiserStatutStock(offre);

        return toResponse(
                offreProduitRepository.save(offre)
        );
    }


    // ==========================================
    // 5. SUPPRIMER UNE OFFRE
    // ==========================================

    @Override
    public void supprimerOffre(Long idOffre) {

        OffreProduit offre = trouverOffre(idOffre);

        boolean utiliseeDansCommande =
                offreProduitRepository
                        .existsByIdOffreAndLignesCommandesIsNotEmpty(
                                idOffre
                        );

        if (utiliseeDansCommande) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cette offre appartient à une commande. "
                            + "Veuillez la désactiver au lieu de la supprimer."
            );
        }

        offreProduitRepository.delete(offre);
    }


    // ==========================================
    // 6. DESACTIVER UNE OFFRE
    // ==========================================

    @Override
    public OffreProduitResponse desactiverOffre(Long idOffre) {

        OffreProduit offre = trouverOffre(idOffre);

        offre.setStatut(StatutOffre.INACTIVE);

        return toResponse(
                offreProduitRepository.save(offre)
        );
    }


    // ==========================================
    // 7. ACTIVER UNE OFFRE
    // ==========================================

    @Override
    public OffreProduitResponse activerOffre(Long idOffre) {

        OffreProduit offre = trouverOffre(idOffre);

        if (offre.getQuantiteDispo() <= 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Impossible d'activer une offre sans stock"
            );
        }

        offre.setStatut(StatutOffre.ACTIVE);

        return toResponse(
                offreProduitRepository.save(offre)
        );
    }


    // ==========================================
    // 8. OFFRES D'UN AGRICULTEUR
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<OffreProduitResponse> getOffresByAgriculteur(
            Long agriculteurId) {

        if (!agriculteurRepository.existsById(agriculteurId)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Agriculteur introuvable"
            );
        }

        return offreProduitRepository
                .findByAgriculteurId(agriculteurId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ==========================================
    // 9. FILTRER PAR CATEGORIE
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<OffreProduitResponse> getOffresByCategorie(
            CategorieProduit categorie) {

        return offreProduitRepository
                .findByCategorie(categorie)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ==========================================
    // 10. FILTRER PAR STATUT
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<OffreProduitResponse> getOffresByStatut(
            StatutOffre statut) {

        return offreProduitRepository
                .findByStatut(statut)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ==========================================
    // 11. RECHERCHER UN PRODUIT
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<OffreProduitResponse> rechercherOffres(
            String motCle) {

        return offreProduitRepository
                .findByNomProduitContainingIgnoreCase(motCle)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ==========================================
    // 12. AFFICHER LES OFFRES DISPONIBLES
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<OffreProduitResponse> getOffresDisponibles() {

        return offreProduitRepository
                .findByStatutAndQuantiteDispoGreaterThan(
                        StatutOffre.ACTIVE,
                        0.0
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ==========================================
    // 13. OFFRES DISPONIBLES PAR CATEGORIE
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<OffreProduitResponse>
    getOffresDisponiblesByCategorie(
            CategorieProduit categorie) {

        return offreProduitRepository
                .findByCategorieAndStatutAndQuantiteDispoGreaterThan(
                        categorie,
                        StatutOffre.ACTIVE,
                        0.0
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ==========================================
    // 14. TRIER LES OFFRES
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<OffreProduitResponse> trierOffres(String sort) {

        Sort tri = switch (sort) {

            case "price_asc" ->
                    Sort.by("prixUnitaire").ascending();

            case "price_desc" ->
                    Sort.by("prixUnitaire").descending();

            case "name_asc" ->
                    Sort.by("nomProduit").ascending();

            case "name_desc" ->
                    Sort.by("nomProduit").descending();

            case "newest" ->
                    Sort.by("datePub").descending();

            case "oldest" ->
                    Sort.by("datePub").ascending();

            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Critère de tri invalide"
            );
        };

        return offreProduitRepository.findAll(tri)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ==========================================
    // 15. MODIFIER LE STOCK
    // ==========================================

    @Override
    public OffreProduitResponse modifierStock(
            Long idOffre,
            Double quantite) {

        if (quantite == null
                || !Double.isFinite(quantite)
                || quantite < 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La quantité doit être positive ou nulle"
            );
        }

        OffreProduit offre = trouverOffre(idOffre);

        offre.setQuantiteDispo(quantite);

        actualiserStatutStock(offre);

        return toResponse(
                offreProduitRepository.save(offre)
        );
    }


    // ==========================================
    // METHODES UTILITAIRES PRIVEES
    // ==========================================

    private OffreProduit trouverOffre(Long idOffre) {

        return offreProduitRepository.findById(idOffre)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Offre introuvable avec l'identifiant : "
                                + idOffre
                ));
    }


    private void remplirOffre(
            OffreProduit offre,
            OffreProduitRequest request) {

        if (request.getQuantiteDispo() == null
                || !Double.isFinite(request.getQuantiteDispo())
                || request.getQuantiteDispo() < 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Quantité invalide"
            );
        }

        offre.setNomProduit(request.getNomProduit());
        offre.setDescription(request.getDescription());
        offre.setPrixUnitaire(request.getPrixUnitaire());
        offre.setUnite(request.getUnite());
        offre.setQuantiteDispo(request.getQuantiteDispo());
        offre.setQualite(request.getQualite());
        offre.setCategorie(request.getCategorie());
        offre.setImageUrl(request.getImageUrl());
    }


    private void actualiserStatutStock(OffreProduit offre) {

        // Une offre désactivée ou expirée ne doit pas
        // être réactivée automatiquement.

        if (offre.getStatut() == StatutOffre.INACTIVE
                || offre.getStatut() == StatutOffre.EXPIREE) {

            return;
        }

        if (offre.getQuantiteDispo() <= 0) {

            offre.setStatut(StatutOffre.RUPTURE_STOCK);

        } else {

            offre.setStatut(StatutOffre.ACTIVE);
        }
    }


    private OffreProduitResponse toResponse(OffreProduit offre) {

        OffreProduitResponse response =
                new OffreProduitResponse();

        response.setIdOffre(offre.getIdOffre());
        response.setNomProduit(offre.getNomProduit());
        response.setDescription(offre.getDescription());
        response.setPrixUnitaire(offre.getPrixUnitaire());
        response.setUnite(offre.getUnite());
        response.setQuantiteDispo(offre.getQuantiteDispo());
        response.setQualite(offre.getQualite());
        response.setStatut(offre.getStatut());
        response.setDatePub(offre.getDatePub());
        response.setImageUrl(offre.getImageUrl());
        response.setCategorie(offre.getCategorie());

        response.setAgriculteurId(
                offre.getAgriculteur().getId()
        );

        response.setNomAgriculteur(
                offre.getAgriculteur().getNom()
        );

        response.setPrenomAgriculteur(
                offre.getAgriculteur().getPrenom()
        );

        return response;
    }
}
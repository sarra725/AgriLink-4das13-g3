package com.example.FarmShopService.ImplServices;

import com.example.FarmShopService.Interfservices.ICommandeService;
import com.example.FarmShopService.dto.*;
import com.example.FarmShopService.entities.*;
import com.example.FarmShopService.enumeration.*;
import com.example.FarmShopService.repositories.*;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
@Transactional
public class CommandeServiceImpl implements ICommandeService {

    private final CommandeRepository commandeRepository;
    private final OffreProduitRepository offreProduitRepository;
    private final UserRepository userRepository;


    // ==========================================
    // 1. PASSER UNE COMMANDE
    // ==========================================

    @Override
    public CommandeResponse passerCommande(
            CommandeRequest request) {

        User client = verifierUtilisateur(
                request.getClientId(),
                Role.CLIENT
        );

        Commande commande = new Commande();

        commande.setClient(client);
        commande.setAdresseLivraison(
                request.getAdresseLivraison()
        );

        commande.setStatut(StatutCommande.EN_ATTENTE);

        remplirCommande(commande, request);

        return toResponse(
                commandeRepository.save(commande)
        );
    }


    // ==========================================
    // 2. CONSULTER TOUTES LES COMMANDES
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<CommandeResponse> getAllCommandes() {

        return commandeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ==========================================
    // 3. CONSULTER UNE COMMANDE PAR ID
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public CommandeResponse getCommandeById(Long id) {

        Commande commande = trouverCommande(id);

        return toResponse(commande);
    }


    // ==========================================
    // 4. MODIFIER UNE COMMANDE
    // ==========================================

    @Override
    public CommandeResponse modifierCommande(
            Long id,
            CommandeRequest request) {

        Commande commande =
                trouverCommandePourModification(id);

        verifierEnAttente(commande);

        verifierAbsencePaiement(commande);

        // Restituer les anciennes quantités
        restituerStock(commande);

        // Supprimer les anciennes lignes
        commande.getLignesCommandes().clear();

        // Modifier l'adresse de livraison
        commande.setAdresseLivraison(
                request.getAdresseLivraison()
        );

        // Ajouter les nouvelles lignes
        // et recalculer le montant total
        remplirCommande(commande, request);

        return toResponse(
                commandeRepository.save(commande)
        );
    }


    // ==========================================
    // 5. SUPPRIMER UNE COMMANDE
    // ==========================================

    @Override
    public void supprimerCommande(Long id) {

        Commande commande =
                trouverCommandePourModification(id);

        verifierEnAttente(commande);

        verifierAbsencePaiement(commande);

        restituerStock(commande);

        commandeRepository.delete(commande);
    }


    // ==========================================
    // 6. CONSULTER LES COMMANDES D'UN CLIENT
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<CommandeResponse> getCommandesByClient(
            Long clientId) {

        verifierUtilisateur(clientId, Role.CLIENT);

        return commandeRepository
                .findByClientIdOrderByDateCommandeDesc(clientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ==========================================
    // 7. HISTORIQUE DES COMMANDES DU CLIENT
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<CommandeResponse> getHistoriqueClient(
            Long clientId) {

        verifierUtilisateur(clientId, Role.CLIENT);

        return commandeRepository
                .findByClientIdOrderByDateCommandeDesc(clientId)
                .stream()
                .filter(commande ->
                        commande.getStatut()
                                == StatutCommande.LIVREE)
                .map(this::toResponse)
                .toList();
    }


    // ==========================================
    // 8. ANNULER UNE COMMANDE
    // ==========================================

    @Override
    public CommandeResponse annulerCommande(Long id) {

        Commande commande =
                trouverCommandePourModification(id);

        verifierEnAttente(commande);

        verifierAbsencePaiement(commande);

        // Remettre les produits dans le stock
        restituerStock(commande);

        commande.setStatut(StatutCommande.ANNULEE);

        return toResponse(
                commandeRepository.save(commande)
        );
    }


    // ==========================================
    // 9. COMMANDES D'UN CLIENT PAR STATUT
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<CommandeResponse> getCommandesClientByStatut(
            Long clientId,
            StatutCommande statut) {

        verifierUtilisateur(clientId, Role.CLIENT);

        return commandeRepository
                .findByClientIdAndStatut(clientId, statut)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ==========================================
    // 10. COMMANDES D'UN AGRICULTEUR
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<CommandeResponse> getCommandesByAgriculteur(
            Long agriculteurId) {

        verifierUtilisateur(
                agriculteurId,
                Role.AGRICULTEUR
        );

        return commandeRepository
                .findByAgriculteurIdOrderByDateCommandeDesc(
                        agriculteurId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ==========================================
    // 11. ACCEPTER UNE COMMANDE
    // ==========================================

    @Override
    public CommandeResponse accepterCommande(Long id) {

        Commande commande =
                trouverCommandePourModification(id);

        verifierEnAttente(commande);

        commande.setStatut(StatutCommande.ACCEPTEE);

        return toResponse(
                commandeRepository.save(commande)
        );
    }


    // ==========================================
    // 12. REFUSER UNE COMMANDE
    // ==========================================

    @Override
    public CommandeResponse refuserCommande(Long id) {

        Commande commande =
                trouverCommandePourModification(id);

        verifierEnAttente(commande);

        verifierAbsencePaiement(commande);

        restituerStock(commande);

        commande.setStatut(StatutCommande.REFUSEE);

        return toResponse(
                commandeRepository.save(commande)
        );
    }


    // ==========================================
    // 13. MODIFIER LE STATUT D'UNE COMMANDE
    // ==========================================

    @Override
    public CommandeResponse modifierStatut(
            Long id,
            StatutCommande nouveauStatut) {

        Commande commande =
                trouverCommandePourModification(id);

        // Seul le service de paiement peut
        // confirmer une commande
        if (nouveauStatut == StatutCommande.CONFIRMEE) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La confirmation est effectuée "
                            + "par le service de paiement"
            );
        }

        StatutCommande statutActuel =
                commande.getStatut();

        boolean transitionValide = switch (statutActuel) {

            case CONFIRMEE ->
                    nouveauStatut
                            == StatutCommande.EN_PREPARATION;

            case EN_PREPARATION ->
                    nouveauStatut
                            == StatutCommande.EN_LIVRAISON;

            case EN_LIVRAISON ->
                    nouveauStatut
                            == StatutCommande.LIVREE;

            default -> false;
        };

        if (!transitionValide) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Transition impossible de "
                            + statutActuel
                            + " vers "
                            + nouveauStatut
            );
        }

        commande.setStatut(nouveauStatut);

        return toResponse(
                commandeRepository.save(commande)
        );
    }


    // ==========================================
    // 14. COMMANDES AGRICULTEUR PAR STATUT
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<CommandeResponse>
    getCommandesAgriculteurByStatut(
            Long agriculteurId,
            StatutCommande statut) {

        verifierUtilisateur(
                agriculteurId,
                Role.AGRICULTEUR
        );

        return commandeRepository
                .findByAgriculteurIdAndStatut(
                        agriculteurId,
                        statut
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ==========================================
    // 15. TOUTES LES COMMANDES PAR STATUT
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<CommandeResponse> getCommandesByStatut(
            StatutCommande statut) {

        return commandeRepository
                .findByStatut(statut)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ==========================================
    // METHODES PRIVEES
    // ==========================================


    // ==========================================
    // Rechercher un utilisateur
    // ==========================================

    private User trouverUtilisateur(Long userId) {

        if (userId == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "L'identifiant utilisateur est obligatoire"
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Utilisateur introuvable"
                        )
                );

        if (!Boolean.TRUE.equals(user.getActif())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Le compte utilisateur est désactivé"
            );
        }

        return user;
    }


    // ==========================================
    // Vérifier le rôle d'un utilisateur
    // ==========================================

    private User verifierUtilisateur(
            Long userId,
            Role role) {

        User user = trouverUtilisateur(userId);

        if (user.getRole() != role) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "L'utilisateur doit avoir le rôle " + role
            );
        }

        return user;
    }


    // ==========================================
    // Rechercher une commande
    // ==========================================

    private Commande trouverCommande(Long id) {

        return commandeRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Commande introuvable"
                        )
                );
    }


    // ==========================================
    // Rechercher et verrouiller une commande
    // ==========================================

    private Commande trouverCommandePourModification(
            Long id) {

        return commandeRepository.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Commande introuvable"
                        )
                );
    }


    // ==========================================
    // Rechercher et verrouiller une offre
    // ==========================================

    private OffreProduit trouverOffre(Long id) {

        return offreProduitRepository.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Offre introuvable : " + id
                        )
                );
    }


    // ==========================================
    // Vérifier si la commande est EN_ATTENTE
    // ==========================================

    private void verifierEnAttente(Commande commande) {

        if (commande.getStatut()
                != StatutCommande.EN_ATTENTE) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cette opération est autorisée uniquement "
                            + "pour une commande EN_ATTENTE"
            );
        }
    }


    // ==========================================
    // Vérifier l'absence d'un paiement
    // ==========================================

    private void verifierAbsencePaiement(
            Commande commande) {

        if (commande.getPayment() != null) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Un paiement est déjà associé "
                            + "à cette commande"
            );
        }
    }


    // ==========================================
    // Remplir les lignes de la commande
    // Calculer le total
    // Déduire les quantités du stock
    // ==========================================

    private void remplirCommande(
            Commande commande,
            CommandeRequest request) {

        if (request.getLignesCommandes() == null
                || request.getLignesCommandes().isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La commande doit contenir "
                            + "au moins un produit"
            );
        }

        BigDecimal montantTotal = BigDecimal.ZERO;

        User agriculteur = null;

        // Regrouper les quantités par offre
        Map<Long, Integer> quantites = new TreeMap<>();

        for (LigneCommandeRequest ligneRequest
                : request.getLignesCommandes()) {

            if (ligneRequest == null
                    || ligneRequest.getOffreId() == null
                    || ligneRequest.getQte() <= 0) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Ligne de commande invalide"
                );
            }

            try {

                quantites.merge(
                        ligneRequest.getOffreId(),
                        ligneRequest.getQte(),
                        Math::addExact
                );

            } catch (ArithmeticException e) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Quantité trop importante"
                );
            }
        }


        for (Map.Entry<Long, Integer> entry
                : quantites.entrySet()) {

            Long offreId = entry.getKey();

            int quantite = entry.getValue();

            OffreProduit offre = trouverOffre(offreId);


            // Vérifier si l'offre est active

            if (offre.getStatut() != StatutOffre.ACTIVE) {

                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Offre indisponible : "
                                + offre.getNomProduit()
                );
            }


            // Vérifier le stock

            if (offre.getQuantiteDispo() == null
                    || !Double.isFinite(
                    offre.getQuantiteDispo())
                    || offre.getQuantiteDispo() < quantite) {

                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Stock insuffisant pour : "
                                + offre.getNomProduit()
                );
            }


            // Vérifier le prix

            if (offre.getPrixUnitaire() == null
                    || offre.getPrixUnitaire()
                    .compareTo(BigDecimal.ZERO) <= 0) {

                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Prix invalide pour : "
                                + offre.getNomProduit()
                );
            }


            // Vérifier que toutes les offres
            // appartiennent au même agriculteur

            if (agriculteur == null) {

                agriculteur = verifierUtilisateur(
                        offre.getAgriculteur().getId(),
                        Role.AGRICULTEUR
                );

            } else if (!agriculteur.getId().equals(
                    offre.getAgriculteur().getId())) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Une commande doit contenir uniquement "
                                + "les produits du même agriculteur"
                );
            }


            // Créer une ligne de commande

            LigneCommande ligne = new LigneCommande();

            ligne.setCommande(commande);

            ligne.setOffreProduit(offre);

            ligne.setQte(quantite);

            ligne.setPrixUnit(offre.getPrixUnitaire());

            commande.getLignesCommandes().add(ligne);


            // Calculer le sous-total

            BigDecimal sousTotal =
                    offre.getPrixUnitaire()
                            .multiply(
                                    BigDecimal.valueOf(quantite)
                            );

            montantTotal = montantTotal.add(sousTotal);


            // Déduire la quantité commandée du stock

            double nouveauStock =
                    offre.getQuantiteDispo() - quantite;

            offre.setQuantiteDispo(nouveauStock);


            // Vérifier si le produit est en rupture

            if (nouveauStock <= 0) {

                offre.setStatut(
                        StatutOffre.RUPTURE_STOCK
                );
            }

            offreProduitRepository.save(offre);
        }


        // Associer l'agriculteur à la commande

        commande.setAgriculteur(agriculteur);


        // Enregistrer le montant total

        commande.setMontantTotal(montantTotal);
    }


    // ==========================================
    // Restituer les quantités au stock
    // ==========================================

    private void restituerStock(Commande commande) {

        Map<Long, Integer> quantites = new TreeMap<>();

        for (LigneCommande ligne
                : commande.getLignesCommandes()) {

            quantites.merge(
                    ligne.getOffreProduit().getIdOffre(),
                    ligne.getQte(),
                    Math::addExact
            );
        }

        for (Map.Entry<Long, Integer> entry
                : quantites.entrySet()) {

            OffreProduit offre =
                    trouverOffre(entry.getKey());

            double nouveauStock =
                    offre.getQuantiteDispo()
                            + entry.getValue();

            offre.setQuantiteDispo(nouveauStock);


            // Réactiver une offre en rupture
            // lorsque son stock redevient positif

            if (offre.getStatut()
                    == StatutOffre.RUPTURE_STOCK
                    && nouveauStock > 0) {

                offre.setStatut(StatutOffre.ACTIVE);
            }

            offreProduitRepository.save(offre);
        }
    }


    // ==========================================
    // Convertir LigneCommande en DTO Response
    // ==========================================

    private LigneCommandeResponse toLigneResponse(
            LigneCommande ligne) {

        LigneCommandeResponse response =
                new LigneCommandeResponse();

        response.setId(ligne.getId());

        response.setOffreId(
                ligne.getOffreProduit().getIdOffre()
        );

        response.setNomProduit(
                ligne.getOffreProduit().getNomProduit()
        );

        response.setImageUrl(
                ligne.getOffreProduit().getImageUrl()
        );

        response.setUnite(
                ligne.getOffreProduit().getUnite()
        );

        response.setQte(ligne.getQte());

        response.setPrixUnit(ligne.getPrixUnit());

        response.setSousTotal(
                ligne.getPrixUnit().multiply(
                        BigDecimal.valueOf(ligne.getQte())
                )
        );

        return response;
    }


    // ==========================================
    // Convertir Commande en DTO Response
    // ==========================================

    private CommandeResponse toResponse(
            Commande commande) {

        CommandeResponse response =
                new CommandeResponse();

        response.setId(commande.getId());

        response.setDateCommande(
                commande.getDateCommande()
        );

        response.setMontantTotal(
                commande.getMontantTotal()
        );

        response.setAdresseLivraison(
                commande.getAdresseLivraison()
        );

        response.setStatut(commande.getStatut());


        // Informations du client

        response.setClientId(
                commande.getClient().getId()
        );

        response.setNomClient(
                commande.getClient().getNom()
        );

        response.setPrenomClient(
                commande.getClient().getPrenom()
        );


        // Informations de l'agriculteur

        response.setAgriculteurId(
                commande.getAgriculteur().getId()
        );

        response.setNomAgriculteur(
                commande.getAgriculteur().getNom()
        );

        response.setPrenomAgriculteur(
                commande.getAgriculteur().getPrenom()
        );


        // Informations des lignes de commande

        response.setLignesCommandes(
                commande.getLignesCommandes()
                        .stream()
                        .map(this::toLigneResponse)
                        .toList()
        );

        return response;
    }
    // ==========================================
// 16. COMMANDES PAYEES D'UN AGRICULTEUR
// ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<CommandeResponse> getCommandesPayeesByAgriculteur(
            Long agriculteurId) {

        verifierUtilisateur(
                agriculteurId,
                Role.AGRICULTEUR
        );

        return commandeRepository
                .findByAgriculteurIdAndPaymentStatutOrderByDateCommandeDesc(
                        agriculteurId,
                        StatutPayment.REUSSI
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }
}
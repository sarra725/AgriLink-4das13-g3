package com.example.FarmShopService.Interfservices;

import com.example.FarmShopService.dto.CommandeRequest;
import com.example.FarmShopService.dto.CommandeResponse;
import com.example.FarmShopService.enumeration.StatutCommande;

import java.util.List;

public interface ICommandeService {

    CommandeResponse passerCommande(
            CommandeRequest request);

    List<CommandeResponse> getAllCommandes();

    CommandeResponse getCommandeById(Long id);

    CommandeResponse modifierCommande(
            Long id,
            CommandeRequest request);

    void supprimerCommande(Long id);

    List<CommandeResponse> getCommandesByClient(
            Long clientId);

    List<CommandeResponse> getHistoriqueClient(
            Long clientId);

    CommandeResponse annulerCommande(Long id);

    List<CommandeResponse> getCommandesClientByStatut(
            Long clientId,
            StatutCommande statut);

    List<CommandeResponse> getCommandesByAgriculteur(
            Long agriculteurId);

    CommandeResponse accepterCommande(Long id);

    CommandeResponse refuserCommande(Long id);

    CommandeResponse modifierStatut(
            Long id,
            StatutCommande statut);

    List<CommandeResponse> getCommandesAgriculteurByStatut(
            Long agriculteurId,
            StatutCommande statut);

    List<CommandeResponse> getCommandesByStatut(
            StatutCommande statut);
    List<CommandeResponse> getCommandesPayeesByAgriculteur(
            Long agriculteurId
    );
}
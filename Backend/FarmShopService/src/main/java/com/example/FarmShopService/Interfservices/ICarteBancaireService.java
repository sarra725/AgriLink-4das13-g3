package com.example.FarmShopService.Interfservices;

import com.example.FarmShopService.dto.CarteBancaireRequest;
import com.example.FarmShopService.dto.CarteBancaireResponse;

import java.util.List;

public interface ICarteBancaireService {

    CarteBancaireResponse ajouterCarte(
            CarteBancaireRequest request);

    CarteBancaireResponse getCarteById(Long idCarte);

    List<CarteBancaireResponse> getCartesByClient(
            Long clientId);

    CarteBancaireResponse modifierCarte(
            Long idCarte,
            CarteBancaireRequest request);

    void supprimerCarte(Long idCarte);
}
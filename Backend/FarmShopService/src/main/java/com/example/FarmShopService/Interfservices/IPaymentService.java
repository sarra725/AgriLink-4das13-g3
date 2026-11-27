package com.example.FarmShopService.Interfservices;

import com.example.FarmShopService.dto.*;

import java.util.List;

public interface IPaymentService {

    PaymentResponse effectuerPayment(
            PaymentRequest request);

    PaymentResponse getPaymentById(
            Long id, Long userId);

    PaymentResponse getPaymentByCommande(
            Long commandeId, Long userId);

    List<PaymentResponse> getPaymentsByClient(
            Long clientId);

    List<PaymentResponse> getPaymentsByAgriculteur(
            Long agriculteurId);

    PaymentResponse modifierPayment(
            Long id, PaymentRequest request);

    PaymentResponse annulerPayment(
            Long id, Long clientId);

    PaymentResponse validerPayment(
            Long id, Long agriculteurId);

    void supprimerPayment(
            Long id, Long clientId);
}
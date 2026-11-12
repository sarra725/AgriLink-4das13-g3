# AgriLink-4das13-g3
Projet AgriLink – Application de gestion et de diagnostic de santé des cultures basée sur une architecture microservices
# 🌱 AgriLink

> **Du champ au client : une plateforme agricole en microservices.**

Projet réalisé dans le cadre du module **Architecture Web Distribuée (AWD)** à **Esprit School of Engineering**, classe **4DAS13**, groupe 3.

---

## 📌 Contexte et problématique

Aujourd'hui, de nombreux agriculteurs gèrent encore tout **à la main** : un cahier pour les cultures, la mémoire pour le stock, le téléphone pour trouver des acheteurs et suivre les livraisons. Les informations sont dispersées, les maladies sont détectées trop tard et la vente comme la livraison manquent de visibilité.

**Problématique :** comment passer d'une gestion manuelle à une solution plus simple, plus rapide et plus fiable ?

## 💡 Solution proposée

**AgriLink** est une plateforme web qui couvre tout le parcours d'un produit agricole :

**Planter → surveiller → récolter → vendre → livrer**

Elle repose sur une **architecture microservices** : chaque domaine métier est un service indépendant, avec sa propre base de données. Les services communiquent par des appels REST et par des événements asynchrones.

---

## 👥 Acteurs

| Acteur | Rôle |
|---|---|
| **Utilisateur** | S'inscrit, se connecte et partage ses expériences via des posts et des commentaires |
| **Agriculteur** | Gère sa ferme, ses cultures, ses tâches et ses récoltes, détecte les maladies et vend ses produits |
| **Client** | Consulte les produits disponibles, passe commande et paie en ligne |
| **Transporteur** | Gère ses véhicules, assure les livraisons et signale les incidents |
| **Administrateur** | Gère les utilisateurs, la base des maladies et affecte les transporteurs |

> L'Agriculteur, le Client et le Transporteur héritent de l'Utilisateur.

---

## 🧩 Microservices

| Microservice | Responsabilité | Base de données |
|---|---|---|
| **Utilisateur & Sécurité** *(commun)* | Inscription, authentification, rôles, génération du JWT | MySQL |
| **Blog** | Posts et commentaires | MySQL |
| **Gestion des exploitations** | Fermes, parcelles, cultures, tâches, récoltes, lots, entrepôts | MySQL |
| **Santé des cultures** | Observations, maladies, diagnostics, traitements | MySQL |
| **Marché agricole** | Offres de vente, commandes, paiement (simulé) | MySQL |
| **Transport & traçabilité** | Transporteurs, véhicules, livraisons, suivi | MySQL |

---

## 🏗️ Architecture

```
               Angular (Frontend)
                       │
           API Gateway (Spring Cloud Gateway)
     ── vérification JWT · routage · Eureka ──
                       │
 ┌─────────┬──────┬────────────┬─────────┬────────┬───────────┐
 │Utilisat.│ Blog │Exploitation│  Santé  │ Marché │ Transport │
 │(commun) │      │            │         │        │           │
 └────┬────┴──┬───┴─────┬──────┴────┬────┴───┬────┴─────┬─────┘
    MySQL   MySQL     MySQL       MySQL    MySQL      MySQL
      └────────────── RabbitMQ (événements) ──────────────┘
```

- **API Gateway** : point d'entrée unique ; elle vérifie le token JWT et route les requêtes vers le bon service.
- **Eureka** : découverte des services (aucune adresse codée en dur).
- **Config Server** : configuration centralisée.
- **Une base de données par microservice** : indépendance des données.

### 🔄 Communication entre microservices

**Synchrone : REST / OpenFeign**, quand une réponse immédiate est nécessaire.
- Marché → Exploitation : `GET /lots/{id}` (quantité disponible d'un lot)
- Transport → Exploitation : `GET /lots/{id}` (entrepôt de départ de la livraison)

**Asynchrone : RabbitMQ**, pour découpler les services.

| Événement | Publié par | Consommé par |
|---|---|---|
| `CultureCreee` | Exploitation | Santé des cultures |
| `MaladieDetectee` | Santé des cultures | Exploitation (création d'une tâche de traitement) |
| `RecolteEnregistree` | Exploitation | Marché |
| `CommandeConfirmee` | Marché | Exploitation (réservation du stock), Transport (création de la livraison) |
| `LivraisonTerminee` | Transport | Marché |

---

## 🛠️ Technologies

| Couche | Technologies |
|---|---|
| Frontend | Angular |
| Backend | Java, Spring Boot, Spring Data JPA |
| Microservices | Spring Cloud Gateway, Eureka, Config Server, OpenFeign |
| Sécurité | Spring Security, JWT |
| Messagerie | RabbitMQ |
| Base de données | MySQL |
| Déploiement | Docker, Docker Compose |
| Documentation API | Swagger / OpenAPI |
| Gestion de projet | GitHub, Trello |

---

## 📁 Structure du dépôt

```
AgriLink-4das13-g3/
├── Backend/            # Microservices Spring Boot, Gateway, Eureka, Config Server
├── Frontend/           # Application Angular
├── Documentation/
│   ├── Diagrams/       # Cas d'utilisation, classes, architecture
│   └── Presentation/   # Support de présentation
└── README.md
```

### 🌿 Organisation Git
- `main` : version stable
- `develop` : intégration
- `feature/<microservice>` : une branche par fonctionnalité ou microservice (ex. `feature/exploitation`)

Les modifications sont intégrées par **Pull Request**.

---

## 🚀 Lancement du projet

### Prérequis
- Java 17+
- Maven
- Node.js et Angular CLI
- Docker et Docker Compose

### Démarrage
```bash
git clone https://github.com/sarra725/AgriLink-4das13-g3.git
cd AgriLink-4das13-g3
docker compose up -d
```

> Ordre conseillé en local : Config Server → Eureka → API Gateway → microservices → Frontend.

---

## 👩‍💻 Équipe

| Membre | Microservice |
|---|---|
| Malek Challouf | Gestion des exploitations |
| Yosr Amamou | Gestion marche |
| Ghada Chelli | Transport & traçabilité |
| Sarra Lahbib | gestion sante des cultures |
| Youssef Boumallela | gestion Blog |

Le service commun **Utilisateur & Sécurité** est développé par toute l'équipe.

---

*Esprit School of Engineering · 2026-2027*

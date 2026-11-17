# UNIVERSITÉ ASSANE SECK DE ZIGUINCHOR (UASZ)
### UFR des Sciences et Technologies — Département d'Informatique
**Licence 3 Informatique — Semestre 6 (Année Académique 2025/2026)**  
**Module :** Développement d'Applications Orientées Services (DAOS)  
**Enseignant :** Dr Lamine FATY  

---

# RAPPORT DE PROJET : ARCHITECTURE MICROSERVICES & FRONTEND ANGULAR
## *Plateforme KasaTouristique — Valorisation et Gestion du Patrimoine de la Casamance*

**Membres du Groupe :**
- **Mame Khoudia GUEYE**
- **Adja Fatou Binetou SARR**
- **Adama DIOP**

---

## Table des Matières
1. [Introduction et Contexte du Projet](#1-introduction-et-contexte-du-projet)
2. [Architecture Fonctionnelle](#2-architecture-fonctionnelle)
3. [Architecture Technique & Communication Inter-Microservices](#3-architecture-technique--communication-inter-microservices)
4. [Dockerisation et Orchestration Complète](#4-dockerisation-et-orchestration-complète)
5. [Frontend SPA avec Angular 17](#5-frontend-spa-avec-angular-17)
6. [Guide des Captures d'Écran pour Illustration](#6-guide-des-captures-décran-pour-illustration)
7. [Conclusion & Perspectives](#7-conclusion--perspectives)

---

## 1. Introduction et Contexte du Projet

La région naturelle de la Basse-Casamance (Sénégal) recèle d'un patrimoine culturel, historique, architectural et écologique d'une richesse exceptionnelle, réparti sur les trois départements de **Ziguinchor**, **Oussouye** et **Bignona** (l'île historique de Carabane, les cases à impluvium d'Enampore et Mlomp, les forêts sacrées d'Oussouye, la presqu'île de Kafountine, etc.).

L'objectif de ce projet est de concevoir et déployer une solution logicielle moderne basée sur une **Architecture Orientée Services (SOA) et Microservices**, répondant aux exigences de scalabilité, d'indépendance de déploiement et de haute disponibilité. L'application se compose d'un **backend découpé en 7 microservices REST Spring Boot**, d'un **portail web Thymeleaf**, d'un **frontend monopage (SPA) Angular 17**, le tout orchestré de manière automatisée via **Docker et Docker Compose**.

---

## 2. Architecture Fonctionnelle

### 2.1. Rôles et Acteurs du Système
L'application met en place un contrôle d'accès basé sur les rôles (RBAC) :
- **Visiteur / Touriste (`VISITEUR`)** : Consulte le catalogue, géolocalise les sites, réserve des visites guidées, reçoit un billet numérique avec QR Code et dépose des avis.
- **Guide Touristique (`GUIDE`)** : Gère son planning de visites, consulte ses réservations et répond aux avis des visiteurs.
- **Conservateur du Patrimoine (`CONSERVATEUR`)** : Administre les fiches patrimoniales, photos, horaires et valide les données historiques.
- **Opérateur Touristique (`OPERATEUR_TOURISTIQUE`)** : Crée et publie des circuits touristiques multi-étapes.
- **Administrateur Communal & Super-Admin (`ADMINISTRATEUR_COMMUNAL`, `SUPER_ADMIN`)** : Supervise l'ensemble du système, valide les comptes guides et gère la modération.

### 2.2. Périmètre des 15 Sites Touristiques Couverts
1. *Ziguinchor* : Cathédrale Saint-Antoine de Padoue, Grande Mosquée de Santhiaba, Port et berges du fleuve Casamance, Centre Artisanal, Forêt classée de Djibélor.
2. *Oussouye* : Île de Carabane (patrimoine UNESCO), Cases à impluvium d'Enampore, Cases à étages de Mlomp, Forêt sacrée d'Oussouye, Plages et rizières de Cap Skirring, Éco-parc d'Égueye.
3. *Bignona* : Quinquina historique de Bignona, Port de pêche de Kafountine, Îles Karone, Réserve ornithologique de Kalissaye.

---

## 3. Architecture Technique & Communication Inter-Microservices

### 3.1. Principe Database-per-Service
Chaque microservice est strictement découplé et dispose de sa propre base de données dédiée (en mémoire avec H2 pour le développement, avec possibilité de bascule vers MySQL) :

| Module | Port | Base H2 Dédiée | Rôle Technique |
|---|---|---|---|
| `ms-patrimoine` | `8081` | `patrimoinedb` | Catalogue des 15 sites, catégories, coordonnées GPS, photos et avis |
| `ms-circuit` | `8082` | `circuitdb` | Gestion des circuits (étapes ordonnées, durée, distance, prix) |
| `ms-evenement` | `8083` | `evenementdb` | Planning des visites guidées, animations et gestion des jauges |
| `ms-reservation` | `8084` | `reservationdb` | Cycle de vie des réservations, calcul des montants et hachage QR Code |
| `ms-utilisateur` | `8085` | `utilisateurdb` | Authentification, profils, inscription et gestion des rôles |
| `ms-paiement` | `8086` | `paiementdb` | Transactions financières (Wave, Orange Money, carte, espèces) |
| `ms-notification` | `8087` | `notificationdb` | Envoi d'alertes, emails et rappels de réservation |
| `patrimoine-ziguinchor` | `8080` | `patrimoinedb` | Portail web Thymeleaf et supervision (`/microservices`) |
| `kasatouristique-frontend` | `4200` | N/A (SPA) | Application Angular 17 servie par Nginx |

### 3.2. Analyse Comparative des Outils de Communication Inter-Microservices
Le cours aborde les trois outils majeurs de communication HTTP dans l'écosystème Spring :

1. **`RestTemplate` (Choisi et implémenté dans le projet)** :  
   - *Fonctionnement* : Client synchrone bloquant historique de Spring (`spring-web`).
   - *Utilisation dans le projet* : Implémenté dans `MicroserviceGatewayService`, `ReservationService`, `CircuitService` et `EvenementService` via des méthodes robustes (`exchange`, `postForEntity`, `put`, `delete`).
   - *Avantages* : Très simple à mettre en œuvre, idéal pour les requêtes synchrones point-à-point, support natif de la sérialisation JSON via Jackson.

2. **`WebClient` (Alternative Réactive)** :  
   - *Fonctionnement* : Client HTTP non-bloquant et asynchrone issu de Spring WebFlux.
   - *Avantages* : Supporte la programmation réactive (Flux, Mono) et permet de gérer un très grand volume de requêtes concurrentes sans saturer les threads.

3. **`OpenFeign` (Alternative Déclarative Spring Cloud)** :  
   - *Fonctionnement* : Client déclaratif où le développeur écrit simplement une interface Java annotée (`@FeignClient`).
   - *Avantages* : Élimine le code technique d'appel HTTP, s'intègre naturellement avec Eureka et Ribbon pour l'équilibrage de charge côté client.

---

## 4. Dockerisation et Orchestration Complète

### 4.1. Approche Multi-Stage Build
Pour garantir des conteneurs légers et performants en production :
- **Pour les 8 modules Java** :
  - *Stage 1 (Build)* : Image `maven:3.9.6-eclipse-temurin-17` qui compile le code et package le JAR exécutable (`mvn clean package -DskipTests`).
  - *Stage 2 (Runtime)* : Image ultra-légère `eclipse-temurin:17-jre-alpine` (~170 Mo) n'embarquant que le JRE pour exécuter `app.jar`.
- **Pour Angular** :
  - *Stage 1* : Image `node:20-alpine` pour installer les dépendances et générer les fichiers statiques de production (`ng build`).
  - *Stage 2* : Serveur web haute performance `nginx:alpine` pour servir la SPA et agir comme reverse proxy vers les microservices.

### 4.2. Orchestration Docker Compose
Le fichier `docker-compose.yml` configure :
- Le réseau de conteneurs bridge isolé : `kasatouristique-net`.
- Les variables d'environnement dynamiques (`MICROSERVICES_PATRIMOINE_URL=http://ms-patrimoine:8081`, etc.) permettant aux microservices de dialoguer par leur nom de conteneur.
- La clause `depends_on` pour garantir le bon ordre d'initialisation des dépendances.

---

## 5. Frontend SPA avec Angular 17

L'application Angular 17 a été développée selon les standards modernes du framework :
- **Architecture Standalone Components** : Aucun fichier `NgModule` lourd, chaque composant déclare directement ses dépendances.
- **Routage et Navigation** : Pages dédiées (`/`, `/catalogue`, `/circuits`, `/visites`, `/reservations`, `/login`, `/register`).
- **Composant Menu de Compte** : Bouton capsule avec icône circulaire d'avatar utilisateur ouvrant un menu déroulant dynamique.
- **Consommation d'API avec `HttpClient`** : Service centralisé `ApiService` interrogeant les microservices en direct.

---

## 6. Guide des Captures d'Écran pour Illustration

*Insérez vos captures d'écran aux emplacements suivants dans votre document Word/PDF :*

1. **Capture 1 — Orchestration Docker :**
   - *Image à insérer* : Fenêtre **Docker Desktop** montrant les 9 conteneurs actifs avec le voyant vert (`frontend-angular`, `patrimoine-web`, `ms-patrimoine`, etc.).
   - *Légende* : *Figure 1 : Conteneurs de l'architecture microservices KasaTouristique en cours d'exécution sous Docker.*

2. **Capture 2 — Tableau de Supervision des Microservices :**
   - *Image à insérer* : Page web `http://localhost:8080/microservices`.
   - *Légende* : *Figure 2 : Tableau de bord de santé vérifiant la connectivité synchrone avec les 7 microservices REST.*

3. **Capture 3 — Frontend Angular :**
   - *Image à insérer* : Page d'accueil et catalogue `http://localhost:4200` avec la barre de navigation et le menu déroulant profil.
   - *Légende* : *Figure 3 : Interface utilisateur Angular 17 connectée aux microservices.*

4. **Capture 4 — Dépôt GitHub :**
   - *Image à insérer* : Page de votre dépôt `https://github.com/Adjasarr05/kasatouristique-microservices` affichant la branche `main` à jour avec les dossiers `kasatouristique-frontend`, `docker-compose.yml` et les Dockerfiles.
   - *Légende* : *Figure 4 : Dépôt GitHub officiel du projet avec l'ensemble des sources et livrables.*

---

## 7. Conclusion & Perspectives

Ce projet a permis de valider concrètement l'ensemble des concepts du cours **DAOS** :
1. La transformation d'un système vers une **architecture microservices indépendante** appliquant le principe Database-per-Service.
2. La maîtrise de la **communication synchrone REST** avec `RestTemplate` et la gestion des problématiques CORS.
3. L'automatisation complète du déploiement via **Docker, Dockerfiles multi-stage et Docker Compose**.
4. L'intégration réussie avec un **frontend moderne en Angular 17**.

En perspectives d'évolution, le système pourrait intégrer une passerelle d'API centralisée (**Spring Cloud Gateway**), un serveur de découverte (**Eureka Server**) ainsi qu'une politique de tolérance aux pannes avec **Resilience4j** (Circuit Breaker).

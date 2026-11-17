# UNIVERSITÉ ASSANE SECK DE ZIGUINCHOR (UASZ)
### UFR des Sciences & Technologies — Département d'Informatique
**Licence 3 Informatique — Semestre 6 (Année Académique 2025/2026)**  
**Module :** Développement d'Applications Orientées Services (DAOS)  
**Enseignant :** Dr Lamine FATY  

---

# 📖 GUIDE COMPLET DE MAÎTRISE ET DE SOUTENANCE
## *Projet KasaTouristique — Architecture 100% Microservices Spring Boot, Docker & Angular 17*

**Membres du Groupe :**
- **Mame Khoudia GUEYE**
- **Adja Fatou Binetou SARR**
- **Adama DIOP**

**Liens officiels du projet :**
- 🐙 **Dépôt GitHub :** https://github.com/Adjasarr05/kasatouristique-microservices
- 🌐 **Site Angular en ligne (Vercel) :** https://kasatouristique-microservice.vercel.app
- 🖥️ **Portail Web local :** `http://localhost:8080`
- 📱 **Frontend Angular local :** `http://localhost:4200`
- 🏥 **Supervision des microservices :** `http://localhost:8080/microservices`

---

## 1. De quoi parle le projet ? (Comprendre en 2 minutes)

La région naturelle de la **Basse-Casamance** (Sénégal) possède une richesse culturelle, historique, naturelle et spirituelle exceptionnelle.

Le projet **KasaTouristique** est une plateforme logicielle complète conçue pour :
1. **Numériser et valoriser le patrimoine** des **3 départements** de la région :
   - **Ziguinchor** (Cathédrale Saint-Antoine, Mémorial Le Joola, Marché Saint-Maur, etc.)
   - **Oussouye** (Île de Carabane, cases à impluvium d'Enampore, cases à étages de Mlomp, royaume d'Oussouye, Cap Skirring)
   - **Bignona** (Port de pêche de Kafountine, îles Karone, réserve ornithologique de Kalissaye)
2. **Permettre aux touristes** de parcourir 15 sites emblématiques, de choisir des circuits thématiques, de réserver des visites avec des guides locaux et d'obtenir un **billet électronique officiel avec QR Code unique**.
3. **Donner aux guides et gestionnaires** des outils pour suivre les réservations et réguler les jauges de visiteurs.

---

## 2. L'Architecture : Pourquoi n'y a-t-il plus de monolithe ?

Au début du cours, un projet démarre souvent sous la forme d'un **monolithe** (un seul gros programme qui fait tout). Si une partie tombe en panne, toute l'application s'arrête.

Dans ce projet, nous avons **complètement démantelé le monolithe** selon les règles de l'art du cours DAOS :
- **Le Backend est découpé en 7 microservices REST autonomes** (ports `8081` à `8087`).
- **Principe Database-per-Service :** Chaque microservice a sa propre base de données isolée en mémoire (**H2**).
- **Deux clients indépendants (Frontends)** consomment ces microservices :
  1. Le **Portail Web (port 8080)** développé en Java Spring Boot + Thymeleaf.
  2. L'**Application Angular 17 (port 4200)** développée en TypeScript, conteneurisée avec Nginx et déployée sur Vercel.

---

## 3. Le Rôle Précis des 7 Microservices

| Microservice | Port | Base de données H2 | Rôle Précis (Analogie de l'employé) |
|---|---|---|---|
| **`ms-patrimoine`** | `8081` | `jdbc:h2:mem:patrimoinedb` | **Le Conservateur culturel** : Gère les 15 fiches de monuments, coordonnées GPS, photos, descriptions et avis des visiteurs. |
| **`ms-circuit`** | `8082` | `jdbc:h2:mem:circuitdb` | **Le Concepteur de voyages** : Crée les circuits multi-étapes (durées en jours, distances, prix en FCFA). |
| **`ms-evenement`** | `8083` | `jdbc:h2:mem:evenementdb` | **Le Gestionnaire du planning** : Gère l'agenda des visites guidées, les guides locaux assignés et les jauges de places. |
| **`ms-reservation`** | `8084` | `jdbc:h2:mem:reservationdb` | **L'Agent de billetterie** : Orchestre la commande, vérifie les places, calcule le montant et génère le **QR Code** (`ZIG-2026-XXXX`). |
| **`ms-utilisateur`** | `8085` | `jdbc:h2:mem:utilisateurdb` | **La Sécurité & RH** : Authentification et gestion des rôles (Visiteur, Guide, Conservateur, Admin). Mots de passe hachés avec **BCrypt**. |
| **`ms-paiement`** | `8086` | `jdbc:h2:mem:paiementdb` | **Le Caissier** : Traite et valide les paiements par Wave, Orange Money, carte bancaire ou espèces. |
| **`ms-notification`** | `8087` | `jdbc:h2:mem:notificationdb` | **Le Facteur / Messager** : Trace et envoie les confirmations de billets par Email et SMS. |

---

## 4. Portail Web (Port 8080) vs Frontend Angular 17 (Port 4200 & Vercel)

| Critère | Portail Web (`:8080`) | Frontend Angular 17 (`:4200` & Vercel) |
|---|---|---|
| **Technologie** | Spring Boot 3 + Thymeleaf + HTML5/CSS3 | Angular 17 + TypeScript 5 + SCSS |
| **Type de rendu** | Server-Side Rendering (SSR) | Single Page Application (SPA / CSR) |
| **Expérience utilisateur** | Rechargement classique de la page | Instantané, zéro rechargement, comme une application mobile |
| **Hébergement** | Local sur la machine | Local (Docker) ET Cloud mondial sur **Vercel** |
| **Particularité unique** | Tableau de bord de santé des microservices (`/microservices`) | Composants autonomes (Standalone Components), reverse proxy Nginx |

---

## 5. Docker : Fonctionnement et Rôles

- **Pourquoi Docker ?** Docker empaquète chaque application dans une boîte isolée (**conteneur**) avec son propre environnement (Java 17, Node 20, Nginx). Cela garantit que le projet tourne à 100% de la même façon sur le PC de l'étudiant et sur celui du professeur.
- **`Dockerfile` :** C'est la recette de fabrication propre à chaque microservice.
- **`docker-compose.yml` :** C'est la télécommande générale qui démarre et relie les **9 conteneurs** en une seule commande (`docker compose up -d`).
- **Multi-Stage Build :** Technique utilisée dans nos Dockerfiles pour garder des conteneurs légers (~25 Mo pour Nginx, ~170 Mo pour Java) en séparant l'étape de compilation de l'étape d'exécution.
- **Réseau virtuel (`kasatouristique-net`) :** Réseau bridge privé qui permet aux conteneurs de communiquer entre eux directement par leur nom (ex: `http://ms-patrimoine:8081`).

---

## 6. Les 22 Questions & Réponses Clés pour la Soutenance

### Bloc 1 : Contexte & Métier
**Q1 : C'est quoi KasaTouristique et à quoi ça sert ?**  
> *« KasaTouristique est une plateforme numérique de valorisation et de réservation touristique pour la région naturelle de Casamance. Elle permet aux touristes de découvrir les 15 sites majeurs de Ziguinchor, Oussouye et Bignona, de réserver des visites avec des guides locaux et de recevoir un billet numérique avec QR Code. »*

**Q2 : Quels sont les territoires et sites couverts par votre application ?**  
> *« Nous couvrons les 3 départements : Ziguinchor (Cathédrale Saint-Antoine, Mémorial Le Joola, Marché Saint-Maur), Oussouye (Carabane, Enampore, Mlomp, Cap Skirring) et Bignona (Kafountine, Îles Karone, Kalissaye), classés en 4 catégories : Culturel, Naturel, Religieux et Historique. »*

### Bloc 2 : Architecture Microservices
**Q3 : Pourquoi avoir découpé l'application en microservices plutôt que de garder un monolithe ?**  
> *« Pour assurer la haute disponibilité, l'évolutivité et l'indépendance de déploiement. Si un service comme le paiement rencontre un problème, le reste du système (catalogue des sites, circuits) continue de fonctionner sans interruption. »*

**Q4 : C'est quoi le principe "Database-per-Service" ?**  
> *« Chaque microservice possède sa propre base de données privée et isolée. Un microservice ne peut jamais accéder directement par requête SQL aux tables d'un autre ; ils doivent obligatoirement communiquer via leurs API REST. »*

**Q5 : Pourquoi utiliser la base H2 en mémoire plutôt que MySQL ?**  
> *« H2 en mémoire démarre instantanément en 2 secondes sans nécessiter l'installation de serveur externe pour le développement et la soutenance. Grâce à Spring Data JPA et Hibernate, le passage à MySQL en production ne demande que 2 lignes de configuration dans application.yml sans retoucher le code Java. »*

**Q6 : Comment vos microservices communiquent-ils entre eux ?**  
> *« Ils communiquent en HTTP synchrone grâce au composant RestTemplate de Spring Boot, en échangeant des données au format standard JSON avec sérialisation Jackson. »*

**Q7 : Quelle est la différence entre RestTemplate, WebClient et OpenFeign ?**  
> *« RestTemplate est le client synchrone bloquant de Spring. WebClient (Spring WebFlux) est asynchrone et non-bloquant pour gérer un très grand volume de requêtes concurrentes. OpenFeign est un client déclaratif de Spring Cloud où l'on écrit juste une interface Java annotée. »*

### Bloc 3 : Les Deux Frontends
**Q8 : Pourquoi avez-vous deux interfaces graphiques (Port 8080 et Port 4200) ?**  
> *« Pour démontrer notre maîtrise des deux grandes approches du Web : le rendu côté serveur (SSR) avec Thymeleaf sur le port 8080 (incluant la page de supervision /microservices), et l'approche Single Page Application (SPA) moderne avec Angular 17 sur le port 4200 et déployée dans le Cloud sur Vercel. »*

**Q9 : À quoi sert la page http://localhost:8080/microservices ?**  
> *« C'est notre tableau de bord de supervision. Il teste la connectivité synchrone avec les 7 microservices REST et affiche des pastilles vertes 'En ligne' ou rouges 'Hors ligne' selon leur disponibilité. »*

### Bloc 4 : Docker & Déploiement
**Q10 : Quel est le rôle de Docker dans le projet ?**  
> *« Docker élimine le problème du 'ça marche sur ma machine mais pas sur celle du professeur'. Il enferme chaque service dans un conteneur standardisé avec toutes ses dépendances. »*

**Q11 : Quelle est la différence entre un Dockerfile et docker-compose.yml ?**  
> *« Le Dockerfile est la recette de fabrication d'un seul conteneur. Le fichier docker-compose.yml est le chef d'orchestre qui configure, relie au réseau virtuel et démarre les 9 conteneurs en une seule commande. »*

**Q12 : Qu'est-ce que le Multi-Stage Build dans vos Dockerfiles ?**  
> *« C'est une construction en 2 étapes : une première étape avec Maven/Node qui compile le code, puis une deuxième étape qui jette tous les outils lourds pour ne garder qu'un runtime minimal (JRE Alpine ou Nginx Alpine), réduisant la taille de l'image de 80%. »*

**Q13 : Comment les conteneurs se parlent-ils sans connaître leurs adresses IP ?**  
> *« Ils partagent le réseau virtuel bridge 'kasatouristique-net'. Le serveur DNS interne de Docker résout directement les noms de services (ex: http://ms-patrimoine:8081). »*

**Q14 : C'est quoi Vercel et comment Angular y est-il déployé ?**  
> *« Vercel est une plateforme Cloud d'hébergement pour applications frontend. Connectée à notre dépôt GitHub, elle compile et redéploie automatiquement l'application Angular à chaque commit poussé sur la branche main. »*

### Bloc 5 : Scénario Réel d'une Réservation
**Q15 : Que se passe-t-il exactement quand un touriste réserve une visite ?**  
> *« La demande arrive à ms-reservation (8084). Celui-ci vérifie la disponibilité des places auprès de ms-evenement (8083), enregistre la transaction auprès de ms-paiement (8086), génère le QR Code unique (ZIG-2026-XXXX), puis déclenche ms-notification (8087) pour enregistrer l'alerte. Le client reçoit son billet électronique avec QR code prêt à être scanné. »*

**Q16 : Quel est le rôle du QR Code sur le billet de réservation ?**  
> *« Il contient un hash unique sécurisé (ex: ZIG-2026-7841). À l'entrée d'un monument ou au départ d'une visite, le guide peut scanner ce QR Code avec son smartphone pour authentifier le billet, vérifier le nom du touriste et le nombre de places réservées. »*

### Bloc 6 : Sécurité & Rôles
**Q17 : Quels sont les rôles utilisateurs dans le système ?**  
> *« Nous utilisons un contrôle d'accès RBAC : VISITEUR (touriste), GUIDE (guide local certifié), CONSERVATEUR (responsable des fiches patrimoniales), OPERATEUR_TOURISTIQUE (créateur de circuits) et ADMINISTRATEUR_COMMUNAL / SUPER_ADMIN. Les mots de passe sont hachés avec BCrypt. »*

**Q18 : Un simple visiteur peut-il supprimer un monument ou créer un circuit ?**  
> *« Non, les méthodes sensibles du backend sont protégées par des vérifications de rôles (comme hasRole('CONSERVATEUR')), et les boutons correspondants sont masqués sur l'interface graphique. »*

### Bloc 7 : Problèmes Techniques & Solutions
**Q19 : Avez-vous rencontré des problèmes de CORS entre Angular et les microservices ?**  
> *« Oui, car Angular tourne sur le port 4200 et les microservices sur les ports 8081 à 8087. Nous avons configuré @CrossOrigin('*') sur les contrôleurs Spring Boot et utilisé Nginx en reverse proxy dans Docker pour relayer les requêtes /api/*. »*

**Q20 : Pourquoi Vercel affichait-il une erreur 404 lors du premier déploiement d'Angular 17 ?**  
> *« Angular 17 compile ses fichiers dans dist/.../browser alors que Vercel cherchait index.html à la racine de dist. Nous avons créé le script copy-dist.js qui synchronise automatiquement les fichiers à la racine pour que Vercel trouve la page immédiatement. »*

**Q21 : Pourquoi le symbole @ posait-il problème dans les templates HTML d'Angular 17 ?**  
> *« Parce qu'Angular 17 a introduit la syntaxe Control Flow (@if, @for, @switch). Écrire une adresse email comme visiteur@ziguinchor.sn était interprété comme un bloc de code non fermé. Il fallait échapper le caractère avec l'entité HTML &#64;. »*

### Bloc 8 : Perspectives d'Avenir
**Q22 : Si le projet devait passer à l'échelle (50 000 visiteurs par jour), que faudrait-il ajouter ?**  
> *« Une Spring Cloud Gateway pour un point d'entrée unique sécurisé, un serveur Netflix Eureka pour l'enregistrement dynamique des instances, un Circuit Breaker Resilience4j pour la tolérance aux pannes, et un bus de messages asynchrone (Kafka ou RabbitMQ). »*

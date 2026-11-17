import os
from docx import Document
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml import OxmlElement
from docx.oxml.ns import qn

def set_cell_background(cell, hex_color):
    tcPr = cell._element.get_or_add_tcPr()
    shd = OxmlElement('w:shd')
    shd.set(qn('w:val'), 'clear')
    shd.set(qn('w:color'), 'auto')
    shd.set(qn('w:fill'), hex_color)
    tcPr.append(shd)

def set_cell_margins(cell, top=100, bottom=100, left=150, right=150):
    tcPr = cell._element.get_or_add_tcPr()
    tcMar = OxmlElement('w:tcMar')
    for m, val in [('w:top', top), ('w:bottom', bottom), ('w:left', left), ('w:right', right)]:
        node = OxmlElement(m)
        node.set(qn('w:w'), str(val))
        node.set(qn('w:type'), 'dxa')
        tcMar.append(node)
    tcPr.append(tcMar)

def create_report():
    doc = Document()

    # Définition des marges
    for section in doc.sections:
        section.top_margin = Inches(1.0)
        section.bottom_margin = Inches(1.0)
        section.left_margin = Inches(1.0)
        section.right_margin = Inches(1.0)

    # Couleurs
    COLOR_PRIMARY = RGBColor(15, 61, 46)    # Vert Casamance foncé
    COLOR_SECONDARY = RGBColor(194, 138, 47) # Doré Kasa
    COLOR_DARK = RGBColor(40, 50, 45)
    COLOR_MUTED = RGBColor(100, 115, 105)

    # =========================================================================
    # PAGE DE GARDE
    # =========================================================================
    p_uni = doc.add_paragraph()
    p_uni.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_uni = p_uni.add_run("RÉPUBLIQUE DU SÉNÉGAL\n")
    r_uni.font.size = Pt(11)
    r_uni.font.bold = True
    r_uni.font.color.rgb = COLOR_DARK

    r_uni2 = p_uni.add_run("UNIVERSITÉ ASSANE SECK DE ZIGUINCHOR (UASZ)\n")
    r_uni2.font.size = Pt(13)
    r_uni2.font.bold = True
    r_uni2.font.color.rgb = COLOR_PRIMARY

    r_ufr = p_uni.add_run("UFR des Sciences et Technologies — Département d'Informatique\n"
                          "Licence 3 en Ingénierie Informatique (L3) — Semestre 6\n"
                          "Année Académique 2025/2026\n")
    r_ufr.font.size = Pt(10)
    r_ufr.font.color.rgb = COLOR_MUTED

    p_div = doc.add_paragraph()
    p_div.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_div = p_div.add_run("—" * 35)
    r_div.font.color.rgb = COLOR_SECONDARY
    r_div.font.bold = True

    p_mod = doc.add_paragraph()
    p_mod.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_mod = p_mod.add_run("MODULE : DÉVELOPPEMENT D'APPLICATIONS ORIENTÉES SERVICES (DAOS)\n")
    r_mod.font.size = Pt(12)
    r_mod.font.bold = True
    r_mod.font.color.rgb = COLOR_SECONDARY

    doc.add_paragraph("\n")

    # Titre du Projet
    p_titre = doc.add_paragraph()
    p_titre.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_titre1 = p_titre.add_run("RAPPORT DE PROJET\n")
    r_titre1.font.size = Pt(16)
    r_titre1.font.bold = True
    r_titre1.font.color.rgb = COLOR_MUTED

    r_titre2 = p_titre.add_run("ARCHITECTURE MICROSERVICES & FRONTEND ANGULAR\n")
    r_titre2.font.size = Pt(20)
    r_titre2.font.bold = True
    r_titre2.font.color.rgb = COLOR_PRIMARY

    r_titre3 = p_titre.add_run("Plateforme « KasaTouristique » pour la valorisation du patrimoine de la Basse-Casamance\n")
    r_titre3.font.size = Pt(13)
    r_titre3.font.italic = True
    r_titre3.font.color.rgb = COLOR_DARK

    doc.add_paragraph("\n")

    # Cadre des étudiants & professeur
    table_info = doc.add_table(rows=2, cols=2)
    table_info.alignment = WD_TABLE_ALIGNMENT.CENTER
    table_info.autofit = False

    table_info.rows[0].cells[0].paragraphs[0].add_run("RÉALISÉ PAR LE GROUPE :").font.bold = True
    table_info.rows[0].cells[0].paragraphs[0].runs[0].font.color.rgb = COLOR_PRIMARY
    
    p_etuds = table_info.rows[1].cells[0].paragraphs[0]
    p_etuds.add_run("• Mame Khoudia GUEYE\n• Adja Fatou Binetou SARR\n• Adama DIOP").font.size = Pt(11)

    table_info.rows[0].cells[1].paragraphs[0].add_run("SOUS LA DIRECTION DE :").font.bold = True
    table_info.rows[0].cells[1].paragraphs[0].runs[0].font.color.rgb = COLOR_PRIMARY

    p_prof = table_info.rows[1].cells[1].paragraphs[0]
    p_prof.add_run("Dr Lamine FATY\nEnseignant-Chercheur\nUASZ Ziguinchor").font.size = Pt(11)

    for row in table_info.rows:
        for cell in row.cells:
            set_cell_margins(cell, top=80, bottom=80, left=120, right=120)

    doc.add_page_break()

    # =========================================================================
    # SOMMAIRE
    # =========================================================================
    h_som = doc.add_heading(level=1)
    r_som = h_som.add_run("Table des Matières")
    r_som.font.color.rgb = COLOR_PRIMARY

    sommaire_items = [
        ("1. Introduction et Contexte du Projet", "3"),
        ("2. Architecture Fonctionnelle & Rôles Applicatifs", "4"),
        ("3. Architecture Technique des Microservices Spring Boot", "5"),
        ("4. Outils de Communication Inter-Microservices (RestTemplate, WebClient, OpenFeign)", "7"),
        ("5. Dockerisation et Orchestration (DevOps)", "9"),
        ("6. Frontend Monopage (SPA) avec Angular 17", "11"),
        ("7. Guide et Emplacements des Captures d'Écran pour Illustration", "13"),
        ("8. Conclusion et Perspectives d'Évolution", "15")
    ]

    for titre, page in sommaire_items:
        p_item = doc.add_paragraph()
        r_t = p_item.add_run(titre)
        r_t.font.bold = True
        r_t.font.color.rgb = COLOR_DARK
        p_item.add_run(f"  ........................................................................................... Page {page}").font.color.rgb = COLOR_MUTED

    doc.add_page_break()

    # =========================================================================
    # 1. INTRODUCTION
    # =========================================================================
    h1 = doc.add_heading(level=1)
    r1 = h1.add_run("1. Introduction et Contexte du Projet")
    r1.font.color.rgb = COLOR_PRIMARY

    p1 = doc.add_paragraph()
    p1.add_run(
        "La région naturelle de la Casamance, située au sud du Sénégal, est dotée d'un patrimoine historique, "
        "culturel, cultuel et écologique d'une richesse exceptionnelle. Réparti sur les trois départements de "
        "Ziguinchor, Oussouye et Bignona, ce patrimoine comprend des sites d'envergure mondiale tels que l'île de Carabane "
        "(classée au patrimoine mondial de l'UNESCO), les cases traditionnelles à impluvium du royaume d'Oussouye, "
        "les comptoirs coloniaux de Ziguinchor, les réserves ornithologiques de Kalissaye et les îles de Karone.\n\n"
        "Dans le cadre du cours de Développement d'Applications Orientées Services (DAOS), l'objectif principal "
        "consiste à transformer une application patrimoniale monolithique en une architecture distribuée, modulaire et "
        "hautement disponible basée sur des microservices autonomes."
    )

    doc.add_heading("1.1. Objectifs Pédagogiques et Techniques du Devoir", level=2)
    p_obj = doc.add_paragraph()
    p_obj.add_run(
        "Ce projet valide les compétences clés attendues en Licence 3 Informatique :\n"
        "1. La séparation des responsabilités selon le principe Database-per-Service (découplage strict des bases de données).\n"
        "2. L'orchestration et la communication synchrone/asynchrone entre microservices REST via RestTemplate.\n"
        "3. La conteneurisation intégrale de l'écosystème avec Docker et Docker Compose (multi-stage build).\n"
        "4. Le développement d'une interface utilisateur moderne en Angular 17 connectée directement aux APIs REST.\n"
        "5. La gestion de versions et le partage du projet via GitHub avec une documentation technique rigoureuse."
    )

    # =========================================================================
    # 2. ARCHITECTURE FONCTIONNELLE
    # =========================================================================
    h2 = doc.add_heading(level=1)
    r2 = h2.add_run("2. Architecture Fonctionnelle & Rôles Applicatifs")
    r2.font.color.rgb = COLOR_PRIMARY

    p2 = doc.add_paragraph()
    p2.add_run(
        "L'application KasaTouristique propose un ensemble de fonctionnalités destinées aux différents acteurs "
        "du secteur touristique en Casamance. La sécurité est assurée par un modèle RBAC (Role-Based Access Control)."
    )

    doc.add_heading("2.1. Acteurs et Permissions", level=2)
    table_roles = doc.add_table(rows=6, cols=3)
    table_roles.alignment = WD_TABLE_ALIGNMENT.CENTER
    headers = ["Rôle Utilisateur", "Code Système", "Missions & Permissions"]
    for i, h in enumerate(headers):
        cell = table_roles.rows[0].cells[i]
        set_cell_background(cell, "0F3D2E")
        r = cell.paragraphs[0].add_run(h)
        r.font.bold = True
        r.font.color.rgb = RGBColor(255, 255, 255)

    data_roles = [
        ("Visiteur / Touriste", "VISITEUR", "Consulter le catalogue, rechercher par proximité, réserver une visite guidée, obtenir un billet avec QR Code et déposer des avis notés."),
        ("Guide Touristique", "GUIDE", "Gérer son agenda de visites guidées, consulter les réservations de ses groupes et répondre aux avis des touristes."),
        ("Conservateur du Patrimoine", "CONSERVATEUR", "Créer et modifier les fiches de sites, ajouter les contextes historiques, horaires d'ouverture et géolocalisation GPS."),
        ("Opérateur Touristique", "OPERATEUR_TOURISTIQUE", "Concevoir des circuits touristiques multi-étapes (durée, distance, étapes ordonnées, tarif)."),
        ("Administrateur Communal & Super Admin", "ADMINISTRATEUR_COMMUNAL / SUPER_ADMIN", "Valider les demandes de comptes des guides professionnels, modérer les avis signalés et superviser la santé des microservices.")
    ]

    for idx, (role, code, desc) in enumerate(data_roles, start=1):
        row = table_roles.rows[idx]
        row.cells[0].paragraphs[0].add_run(role).font.bold = True
        row.cells[1].paragraphs[0].add_run(code).font.color.rgb = COLOR_SECONDARY
        row.cells[2].paragraphs[0].add_run(desc).font.size = Pt(9.5)
        bg = "F9FAF9" if idx % 2 == 1 else "FFFFFF"
        for c in row.cells:
            set_cell_background(c, bg)
            set_cell_margins(c, 80, 80, 100, 100)

    # =========================================================================
    # 3. ARCHITECTURE TECHNIQUE DES MICROSERVICES
    # =========================================================================
    h3 = doc.add_heading(level=1)
    r3 = h3.add_run("3. Architecture Technique des Microservices Spring Boot")
    r3.font.color.rgb = COLOR_PRIMARY

    p3 = doc.add_paragraph()
    p3.add_run(
        "L'architecture repose sur 7 microservices REST indépendants et autonomes construits avec Spring Boot 3.2.5 "
        "et Java 17, accompagnés du portail web Thymeleaf et du frontend SPA Angular 17. "
        "Chaque microservice applique strictement le principe Database-per-Service, évitant tout couplage fort au niveau des données."
    )

    table_ms = doc.add_table(rows=10, cols=4)
    table_ms.alignment = WD_TABLE_ALIGNMENT.CENTER
    ms_headers = ["Module", "Port", "Base Dédiée", "Responsabilité Métier"]
    for i, h in enumerate(ms_headers):
        cell = table_ms.rows[0].cells[i]
        set_cell_background(cell, "0F3D2E")
        r = cell.paragraphs[0].add_run(h)
        r.font.bold = True
        r.font.color.rgb = RGBColor(255, 255, 255)

    data_ms = [
        ("ms-patrimoine", "8081", "patrimoinedb", "Catalogue des 15 sites, catégories, coordonnées GPS, photos et avis visiteurs."),
        ("ms-circuit", "8082", "circuitdb", "Gestion des circuits touristiques, étapes ordonnées, durée, distance et tarifs."),
        ("ms-evenement", "8083", "evenementdb", "Planning des visites guidées, animations culturelles et gestion des capacités."),
        ("ms-reservation", "8084", "reservationdb", "Cycle de vie des réservations, validation inter-services et hachage QR Code."),
        ("ms-utilisateur", "8085", "utilisateurdb", "Comptes utilisateurs, inscription, connexion et attribution des rôles."),
        ("ms-paiement", "8086", "paiementdb", "Transactions financières (Wave, Orange Money, carte bancaire) et remboursements."),
        ("ms-notification", "8087", "notificationdb", "Envoi d'alertes par email et SMS, historique des rappels de visite."),
        ("patrimoine-ziguinchor", "8080", "patrimoinedb", "Portail client web Thymeleaf et tableau de bord de supervision."),
        ("frontend-angular", "4200", "N/A (Client)", "Application monopage (SPA) Angular 17 servie par serveur Nginx.")
    ]

    for idx, (mod, port, db, resp) in enumerate(data_ms, start=1):
        row = table_ms.rows[idx]
        row.cells[0].paragraphs[0].add_run(mod).font.bold = True
        row.cells[1].paragraphs[0].add_run(port).font.bold = True
        row.cells[2].paragraphs[0].add_run(db).font.color.rgb = COLOR_SECONDARY
        row.cells[3].paragraphs[0].add_run(resp).font.size = Pt(9.5)
        bg = "F9FAF9" if idx % 2 == 1 else "FFFFFF"
        for c in row.cells:
            set_cell_background(c, bg)
            set_cell_margins(c, 70, 70, 90, 90)

    # =========================================================================
    # 4. OUTILS DE COMMUNICATION
    # =========================================================================
    h4 = doc.add_heading(level=1)
    r4 = h4.add_run("4. Outils de Communication Inter-Microservices")
    r4.font.color.rgb = COLOR_PRIMARY

    p4 = doc.add_paragraph()
    p4.add_run(
        "Dans une architecture orientée services, la communication entre microservices peut être assurée "
        "par différents clients HTTP fournis ou intégrés à l'écosystème Spring. Le cours met en avant 3 solutions majeures :"
    )

    doc.add_heading("4.1. RestTemplate (Solution Implémentée dans le Projet)", level=2)
    p_rest = doc.add_paragraph()
    p_rest.add_run(
        "RestTemplate est le client HTTP synchrone central de Spring Web. Il est utilisé à travers l'ensemble de notre système :\n"
        "• Dans ms-reservation : vérifie en direct la disponibilité auprès de ms-evenement et ms-circuit, valide le profil via ms-utilisateur et déclenche ms-paiement et ms-notification.\n"
        "• Dans ms-circuit : contacte ms-patrimoine pour valider l'existence des sites composant le parcours.\n"
        "• Dans patrimoine-ziguinchor (MicroserviceGatewayService) : centralise les requêtes vers les 7 microservices REST via les méthodes exchange(), postForEntity(), put() et delete().\n\n"
        "Extrait d'appel avec RestTemplate dans notre projet :\n"
    )

    p_code = doc.add_paragraph()
    p_code.add_run(
        "ResponseEntity<Map<String, Object>> res = restTemplate.exchange(\n"
        "    patrimoineUrl + \"/api/patrimoines/\" + siteId,\n"
        "    HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {}\n"
        ");"
    ).font.size = Pt(9.5)
    p_code.runs[0].font.name = "Consolas"

    doc.add_heading("4.2. WebClient (Alternative Réactive Non-Bloquante)", level=2)
    p_web = doc.add_paragraph()
    p_web.add_run(
        "Introduit avec Spring 5 et Spring WebFlux, WebClient est un client HTTP asynchrone et non-bloquant "
        "reposant sur Project Reactor (Mono et Flux). Il excelle dans les environnements à fort trafic concurrentiel "
        "car il n'immobilise pas un thread par requête entrante."
    )

    doc.add_heading("4.3. OpenFeign (Alternative Déclarative Spring Cloud)", level=2)
    p_feign = doc.add_paragraph()
    p_feign.add_run(
        "Spring Cloud OpenFeign permet d'écrire des clients HTTP de manière purement déclarative via de simples "
        "interfaces annotées (@FeignClient). Le développeur ne manipule aucun code technique de bas niveau pour sérialiser "
        "ou désérialiser les requêtes, et l'intégration avec un annuaire de services (Eureka) est native."
    )

    # =========================================================================
    # 5. DOCKERISATION ET ORCHESTRATION
    # =========================================================================
    h5 = doc.add_heading(level=1)
    r5 = h5.add_run("5. Dockerisation et Orchestration Complète")
    r5.font.color.rgb = COLOR_PRIMARY

    p5 = doc.add_paragraph()
    p5.add_run(
        "Pour automatiser le déploiement et garantir une parité totale entre les environnements de développement et de "
        "production, l'ensemble de l'architecture a été dockerisée à l'aide de Docker et Docker Compose."
    )

    doc.add_heading("5.1. Dockerfiles Multi-Stage", level=2)
    p_stage = doc.add_paragraph()
    p_stage.add_run(
        "L'optimisation des images repose sur le patron Multi-Stage Build :\n"
        "• Pour les microservices Java : une première étape (maven:3.9.6-eclipse-temurin-17) assure la compilation Maven "
        "et l'empaquetage du JAR en sautant les tests (-DskipTests). Une seconde étape (eclipse-temurin:17-jre-alpine) "
        "très légère (~170 Mo) n'embarque que le binaire JRE nécessaire à l'exécution de app.jar.\n"
        "• Pour Angular : une étape de build avec node:20-alpine qui exécute ng build --configuration production, "
        "suivie d'une image finale nginx:alpine servant les fichiers web compilés."
    )

    doc.add_heading("5.2. Réseau Virtuel et Résolution de Noms (Docker Compose)", level=2)
    p_net = doc.add_paragraph()
    p_net.add_run(
        "Le fichier docker-compose.yml crée un réseau virtuel bridge nommé kasatouristique-net. "
        "Chaque conteneur est accessible par son container_name grâce au DNS interne de Docker. "
        "Les variables d'environnement (MICROSERVICES_PATRIMOINE_URL=http://ms-patrimoine:8081, etc.) "
        "remplacent dynamiquement les adresses localhost par les noms d'hôtes des conteneurs."
    )

    # =========================================================================
    # 6. FRONTEND ANGULAR 17
    # =========================================================================
    h6 = doc.add_heading(level=1)
    r6 = h6.add_run("6. Frontend Monopage (SPA) avec Angular 17")
    r6.font.color.rgb = COLOR_PRIMARY

    p6 = doc.add_paragraph()
    p6.add_run(
        "L'application kasatouristique-frontend a été conçue sous Angular 17 en utilisant l'architecture "
        "Standalone Components, gage de modularité et de performances accrues.\n\n"
        "Composants majeurs développés :\n"
        "• AccueilComponent : Page vitrine présentant le patrimoine casamançais et les statistiques du projet.\n"
        "• CatalogueComponent : Catalogue dynamique avec recherche textuelle et filtrage par catégorie en temps réel.\n"
        "• CircuitsComponent : Consultation des parcours touristiques avec durée, distance et tarif.\n"
        "• VisitesComponent : Consultation des visites guidées planifiées et formulaire de réservation dynamique.\n"
        "• ReservationsComponent : Suivi des réservations et visualisation du statut du billet QR Code.\n"
        "• LoginComponent / RegisterComponent : Authentification branchée sur le microservice ms-utilisateur.\n"
        "• Menu Profil Utilisateur : Capsule arrondie avec icône de profil utilisateur regroupant Connexion et Créer un compte."
    )

    # =========================================================================
    # 7. EMPLACEMENTS DES CAPTURES D'ECRAN
    # =========================================================================
    h7 = doc.add_heading(level=1)
    r7 = h7.add_run("7. Guide des Captures d'Écran pour Illustration")
    r7.font.color.rgb = COLOR_PRIMARY

    p7 = doc.add_paragraph()
    p7.add_run(
        "Collez directement vos captures d'écran aux emplacements prévus ci-dessous dans votre document final :"
    )

    captures = [
        ("Figure 1 : Docker Desktop avec les 9 conteneurs au vert",
         "[ INSÉRER ICI LA CAPTURE DE DOCKER DESKTOP MONTRANT TOUS LES CONTENEURS ACTIFS 🟢 ]\n"
         "Description : Démontre l'orchestration réussie des 7 microservices REST, du portail Thymeleaf et du frontend Angular."),

        ("Figure 2 : Tableau de bord de supervision des Microservices",
         "[ INSÉRER ICI LA CAPTURE DU NAVIGATEUR SUR http://localhost:8080/microservices ]\n"
         "Description : Démontre la connectivité synchrone RestTemplate entre le portail web et les 7 microservices REST."),

        ("Figure 3 : Catalogue du Patrimoine sur le Frontend Angular (Port 4200)",
         "[ INSÉRER ICI LA CAPTURE DU NAVIGATEUR SUR http://localhost:4200/catalogue ]\n"
         "Description : Démontre l'interface moderne Angular 17 avec les fiches de sites et le menu déroulant de compte."),

        ("Figure 4 : Dépôt GitHub officiel du projet",
         "[ INSÉRER ICI LA CAPTURE DE VOTRE PAGE GITHUB https://github.com/Adjasarr05/kasatouristique-microservices ]\n"
         "Description : Démontre la présence du code source complet, des Dockerfiles, de docker-compose.yml et du README.")
    ]

    for titre_cap, texte_cadre in captures:
        p_cap = doc.add_paragraph()
        r_c = p_cap.add_run(f"• {titre_cap}")
        r_c.font.bold = True
        r_c.font.color.rgb = COLOR_PRIMARY

        table_cadre = doc.add_table(rows=1, cols=1)
        table_cadre.alignment = WD_TABLE_ALIGNMENT.CENTER
        cell_c = table_cadre.rows[0].cells[0]
        set_cell_background(cell_c, "F2F5F3")
        set_cell_margins(cell_c, top=140, bottom=140, left=180, right=180)
        p_in = cell_c.paragraphs[0]
        p_in.alignment = WD_ALIGN_PARAGRAPH.CENTER
        r_in = p_in.add_run(texte_cadre)
        r_in.font.italic = True
        r_in.font.size = Pt(10)
        r_in.font.color.rgb = COLOR_MUTED
        doc.add_paragraph("")

    # =========================================================================
    # 8. CONCLUSION
    # =========================================================================
    h8 = doc.add_heading(level=1)
    r8 = h8.add_run("8. Conclusion et Perspectives d'Évolution")
    r8.font.color.rgb = COLOR_PRIMARY

    p8 = doc.add_paragraph()
    p8.add_run(
        "Ce projet de Développement d'Applications Orientées Services a permis d'appréhender de manière "
        "concrète l'ensemble des problématiques liées aux architectures distribuées en entreprise.\n\n"
        "Les trois livrables exigés ont été pleinement atteints :\n"
        "1. Un dépôt GitHub documenté et versionné intégrant un README d'exploitation complet.\n"
        "2. Une architecture backend distribuée en 7 microservices REST + portail Thymeleaf, orchestrée sous Docker et interconnectée avec un frontend monopage Angular 17.\n"
        "3. Un rapport technique et fonctionnel exhaustif détaillant l'architecture, les patterns de conception et les flux d'échanges.\n\n"
        "En guise de perspectives futures, l'architecture pourra être enrichie par l'intégration d'un serveur "
        "de découverte dynamique (Spring Cloud Netflix Eureka), d'une passerelle d'API intelligente (Spring Cloud Gateway) "
        "et d'un mécanisme de tolérance aux pannes avec disjoncteur (Resilience4j)."
    )

    # Sauvegarde dans le dossier accessible
    target_path = r"C:\Users\Admin\.gemini\antigravity\scratch\Rapport_Projet_KasaTouristique_UASZ.docx"
    doc.save(target_path)
    print(f"Document Word genere avec succes : {target_path}")

if __name__ == "__main__":
    create_report()

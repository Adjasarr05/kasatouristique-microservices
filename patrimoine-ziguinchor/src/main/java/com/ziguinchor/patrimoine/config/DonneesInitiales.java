package com.ziguinchor.patrimoine.config;

import com.ziguinchor.patrimoine.model.*;
import com.ziguinchor.patrimoine.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * S'exécute une fois au démarrage : crée les comptes de test, les catégories,
 * l'ensemble des 15 grands sites touristiques de la région de Ziguinchor
 * (départements de Ziguinchor, Oussouye et Bignona), les avis, circuits et visites.
 *
 * Mot de passe pour TOUS les comptes de test : admin123
 */
@Component
public class DonneesInitiales implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategorieRepository categorieRepository;
    private final SiteTouristiqueRepository siteRepository;
    private final AvisRepository avisRepository;
    private final CircuitRepository circuitRepository;
    private final VisiteRepository visiteRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public DonneesInitiales(UserRepository userRepository, PasswordEncoder passwordEncoder,
                            CategorieRepository categorieRepository,
                            SiteTouristiqueRepository siteRepository,
                            AvisRepository avisRepository,
                            CircuitRepository circuitRepository,
                            VisiteRepository visiteRepository,
                            ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.categorieRepository = categorieRepository;
        this.siteRepository = siteRepository;
        this.avisRepository = avisRepository;
        this.circuitRepository = circuitRepository;
        this.visiteRepository = visiteRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... args) {
        creerComptesDeTest();
        creerDonneesTouristiquesSiAbsentes();
        creerCircuitsVisitesEtReservationsSiAbsents();
    }

    private void creerComptesDeTest() {
        creerCompteSiAbsent("conservateur@ziguinchor.sn", "Conservateur Test", Role.CONSERVATEUR);
        creerCompteSiAbsent("operateur@ziguinchor.sn", "Opérateur Touristique Test", Role.OPERATEUR_TOURISTIQUE);
        creerCompteSiAbsent("admincommunal@ziguinchor.sn", "Administrateur Communal Test", Role.ADMINISTRATEUR_COMMUNAL);
        creerCompteSiAbsent("superadmin@ziguinchor.sn", "Super Admin Test", Role.SUPER_ADMIN);
        creerCompteSiAbsent("visiteur@ziguinchor.sn", "Visiteur Test", Role.VISITEUR);

        if (!userRepository.existsByEmail("guide@ziguinchor.sn")) {
            User guide = new User();
            guide.setNom("Ibrahima Sané");
            guide.setEmail("guide@ziguinchor.sn");
            guide.setMotDePasse(passwordEncoder.encode("admin123"));
            guide.setRole(Role.GUIDE);
            guide.setStatutValidation(StatutValidation.VALIDE);
            guide.setActif(true);
            guide.setTelephone("+221 77 321 45 67");
            guide.setSpecialite("Histoire coloniale, Mémorial Le Joola & Îles du fleuve");
            guide.setLanguesParlees("Français, Diola, Wolof, Anglais");
            guide.setZoneIntervention("Ziguinchor, Enampore, Carabane");
            guide.setAnneesExperience(7);
            guide.setBiographie("Guide touristique agréé en Basse-Casamance, passionné par le patrimoine architectural et l'histoire maritime de Ziguinchor.");
            guide.setDisponible(true);
            userRepository.save(guide);
        }

        if (!userRepository.existsByEmail("guide.attente@ziguinchor.sn")) {
            User guideAttente = new User();
            guideAttente.setNom("Mariama Diatta");
            guideAttente.setEmail("guide.attente@ziguinchor.sn");
            guideAttente.setMotDePasse(passwordEncoder.encode("admin123"));
            guideAttente.setRole(Role.GUIDE);
            guideAttente.setStatutValidation(StatutValidation.EN_ATTENTE);
            guideAttente.setActif(false);
            guideAttente.setTelephone("+221 78 654 32 10");
            guideAttente.setSpecialite("Écotourisme, mangroves & observation des oiseaux");
            guideAttente.setLanguesParlees("Français, Diola, Anglais");
            guideAttente.setZoneIntervention("Oussouye, Égueye, Cap Skirring");
            guideAttente.setAnneesExperience(4);
            guideAttente.setBiographie("Spécialiste des circuits nature dans les bolongs et réserves ornithologiques de Casamance.");
            userRepository.save(guideAttente);
        }
    }

    private void creerCompteSiAbsent(String email, String nom, Role role) {
        if (!userRepository.existsByEmail(email)) {
            User u = new User();
            u.setNom(nom);
            u.setEmail(email);
            u.setMotDePasse(passwordEncoder.encode("admin123"));
            u.setRole(role);
            u.setActif(true);
            userRepository.save(u);
        }
    }

    private SiteTouristique creerSite(String nom, String quartier, String adresse, String description,
                                      String imageUrl, double latitude, double longitude, Categorie categorie) {
        SiteTouristique site = new SiteTouristique();
        site.setNom(nom);
        site.setQuartier(quartier);
        site.setAdresse(adresse);
        site.setDescription(description);
        site.setImageUrl(imageUrl);
        site.setLatitude(latitude);
        site.setLongitude(longitude);
        site.setDateAjout(LocalDate.now());
        site.setCategorie(categorie);
        return siteRepository.save(site);
    }

    private void creerAvis(SiteTouristique site, String auteur, int note, String commentaire) {
        Avis avis = new Avis();
        avis.setSiteTouristique(site);
        avis.setAuteur(auteur);
        avis.setNote(note);
        avis.setCommentaire(commentaire);
        avis.setDateAvis(LocalDate.now());
        avisRepository.save(avis);
    }

    private void creerDonneesTouristiquesSiAbsentes() {
        if (categorieRepository.count() > 0) return;

        Categorie culturel = categorieRepository.save(new Categorie("Culturel", "Traditions diola, marchés artisanaux, centres culturels et cases traditionnelles"));
        Categorie naturel = categorieRepository.save(new Categorie("Naturel", "Fleuve Casamance, mangroves, bolongs, îles, plages et réserves naturelles"));
        Categorie religieux = categorieRepository.save(new Categorie("Religieux", "Édifices religieux historiques, royaume traditionnel et bois sacrés"));
        Categorie historique = categorieRepository.save(new Categorie("Historique", "Mémoriaux, architecture coloniale et anciens comptoirs historiques"));

        // --- Département de Ziguinchor ---
        SiteTouristique cathedrale = creerSite(
                "Cathédrale Saint-Antoine de Padoue de Ziguinchor",
                "Centre-ville • Ziguinchor",
                "Avenue du Général de Gaulle, Escale, Ziguinchor",
                "Édifice religieux emblématique du centre-ville de Ziguinchor, remarquable par son architecture coloniale et son atmosphère paisible au cœur du quartier historique de l'Escale.",
                "/images/sites/cathedrale.jpg",
                12.5819, -16.2719, religieux
        );

        SiteTouristique joola = creerSite(
                "Mémorial-Musée du Bateau Le Joola",
                "Escale • Port de Ziguinchor",
                "Quai du Port de Ziguinchor, Quartier Escale",
                "Grand espace muséal et lieu de mémoire inauguré au bord du fleuve Casamance pour honorer les victimes du naufrage du 26 septembre 2002 et transmettre l'histoire maritime de la région.",
                "/images/sites/memorial-joola.jpg",
                12.5835, -16.2719, historique
        );

        SiteTouristique marche = creerSite(
                "Marché Saint-Maur et Centre Artisanal",
                "Escale / Boucotte • Ziguinchor",
                "Boulevard du 54e Régiment, Centre-ville de Ziguinchor",
                "Grand marché central haut en couleur où se côtoient épices, mangues, noix de cajou, tissus pagnes tissés, poteries et vanneries d'art de toute la Casamance.",
                "/images/sites/marche-saint-maur.jpg",
                12.5762, -16.2704, culturel
        );

        SiteTouristique fleuve = creerSite(
                "Le Fleuve Casamance et Quai de Pêche de Boudody",
                "Boudody / Boucotte • Ziguinchor",
                "Rive sud du Fleuve Casamance, Quartier Boudody",
                "Point de départ idéal des excursions en pirogue traditionnelle au milieu des palétuviers, observation des oiseaux migrateurs et spectacle des retours de pêche au coucher du soleil.",
                "/images/sites/fleuve-casamance.jpg",
                12.5847, -16.2768, naturel
        );

        SiteTouristique caseEtageZiguinchor = creerSite(
                "Cases à Étage Coloniales et Gouvernance",
                "Escale • Ziguinchor",
                "Rue de France et Rue du Commerce, Escale, Ziguinchor",
                "Ensemble architectural témoignant du passé luso-africain et colonial de Ziguinchor : balcons en bois ouvragé, vérandas ombragées et bâtiments administratifs centenaires.",
                "/images/sites/case-etage-ziguinchor.jpg",
                12.5821, -16.2742, historique
        );

        SiteTouristique alliance = creerSite(
                "Alliance Franco-Sénégalaise de Ziguinchor",
                "Escale • Ziguinchor",
                "Rue du Capitaine Javelier, Ziguinchor",
                "Haut lieu culturel à l'architecture originale ornée de fresques sénégalaises, accueillant expositions d'art contemporain, concerts, spectacles de danse et bibliothèque.",
                "/images/sites/alliance-ziguinchor.jpg",
                12.5784, -16.2715, culturel
        );

        SiteTouristique djibelor = creerSite(
                "Ferme Animalière et Botanique de Djibélor",
                "Djibélor • Dép. de Ziguinchor",
                "Route du Cap Skirring, Village de Djibélor (à 7 km de Ziguinchor)",
                "Oasis de verdure réputée pour son bassin aux crocodiles sacrés, sa diversité d'arbres fruitiers tropicaux, ses fleurs exotiques et ses sentiers ombragés.",
                "/images/sites/djibelor.jpg",
                12.5489, -16.3228, naturel
        );

        SiteTouristique enampore = creerSite(
                "Cases à Impluvium Traditionnelles d'Enampore",
                "Enampore • Nyassia (Ziguinchor)",
                "Village d'Enampore, Arrondissement de Nyassia",
                "Chefs-d'œuvre d'architecture bioclimatique diola : vastes demeures collectives circulaires dont le toit en entonnoir capte les eaux de pluie vers une cour centrale.",
                "/images/sites/enampore.jpg",
                12.5172, -16.4539, culturel
        );

        // --- Département d'Oussouye ---
        SiteTouristique carabane = creerSite(
                "Île Historique de Carabane",
                "Carabane • Dép. d'Oussouye",
                "Embouchure du Fleuve Casamance, Commune de Diembéring",
                "Ancien comptoir commercial français accessible uniquement par pirogue : église bretonne du XIXe siècle, vestiges historiques, plages de cocotiers et douceur de vivre insulaire.",
                "/images/sites/carabane.jpg",
                12.5536, -16.7014, historique
        );

        SiteTouristique capSkirring = creerSite(
                "Plages du Cap Skirring et Village de Kabrousse",
                "Cap Skirring / Kabrousse • Oussouye",
                "Côte Atlantique, Commune de Diembéring, Oussouye",
                "Considérées parmi les plus belles plages d'Afrique de l'Ouest : sable fin doré bordé de cocotiers, eaux chaudes, sports nautiques et patrimoine mémoriel de la reine Aline Sitoé Diatta à Kabrousse.",
                "/images/sites/cap-skirring.jpg",
                12.3917, -16.7431, naturel
        );

        SiteTouristique mlomp = creerSite(
                "Musée des Traditions Diola et Cases à Étage de Mlomp",
                "Mlomp • Dép. d'Oussouye",
                "Village de Mlomp, Route d'Oussouye à Cap Skirring",
                "Célèbre pour ses authentiques cases à étage en terre crue (banco) uniques en Afrique de l'Ouest, ses fromagers géants centenaires et son musée consacré aux rites diola.",
                "/images/sites/mlomp.jpg",
                12.5586, -16.5831, culturel
        );

        SiteTouristique pointeSaintGeorges = creerSite(
                "Pointe Saint-Georges (Lamantins & Fromagers)",
                "Pointe Saint-Georges • Oussouye",
                "Rive gauche du Fleuve Casamance, Dép. d'Oussouye",
                "Site écotouristique paisible offrant un mirador perché dans un fromager géant et la possibilité rare d'observer des lamantins et dauphins dans le fleuve.",
                "/images/sites/pointe-saint-georges.jpg",
                12.6078, -16.6150, naturel
        );

        SiteTouristique oussouye = creerSite(
                "Cour Royale et Bois Sacrés d'Oussouye",
                "Oussouye • Région de Ziguinchor",
                "Commune d'Oussouye, Département d'Oussouye",
                "Cœur spirituel du Kassa et siège du Roi traditionnel d'Oussouye, garant de la paix sociale, des rites ancestraux diola, du festival Humabal et de la préservation des forêts sacrées.",
                "/images/sites/oussouye.jpg",
                12.4850, -16.5469, religieux
        );

        SiteTouristique egueye = creerSite(
                "Île d'Égueye et Bolongs de la Mangrove",
                "Égueye / Diakène • Oussouye",
                "Bolongs du Fleuve Casamance, Dép. d'Oussouye",
                "Îlot préservé niché au cœur d'un labyrinthe de bolongs et de palétuviers, idéal pour les randonnées en kayak, l'observation d'oiseaux et la déconnexion totale.",
                "/images/sites/egueye.jpg",
                12.4833, -16.6975, naturel
        );

        // --- Département de Bignona ---
        SiteTouristique kafountine = creerSite(
                "Kafountine, Îles Karone et Réserve de Kalissaye",
                "Kafountine • Dép. de Bignona",
                "Façade maritime nord, Commune de Kafountine, Bignona",
                "Grand port de pêche artisanal aux pirogues multicolores, ouvrant sur le paradis insulaire des Îles Karone et la réserve ornithologique de Kalissaye peuplée de tortues marines et pélicans.",
                "/images/sites/kafountine.jpg",
                12.9317, -16.7458, naturel
        );

        creerAvis(cathedrale, "Astou Diémé", 5, "Un monument paisible et magnifique en plein cœur de Ziguinchor.");
        creerAvis(joola, "Ousmane Sonko", 5, "Lieu émouvant et indispensable pour comprendre l'histoire contemporaine de la Casamance.");
        creerAvis(marche, "Moussa Sané", 4, "Très animé et parfait pour acheter de l'artisanat, du miel de mangrove et du bissap.");
        creerAvis(carabane, "Claire B.", 5, "Une traversée inoubliable en pirogue ! L'ambiance hors du temps sur l'île est magique.");
        creerAvis(capSkirring, "Cheikh Ndiaye", 5, "Des kilomètres de plages de rêve et un accueil chaleureux à Kabrousse.");
        creerAvis(enampore, "Fatou Badji", 5, "Architecture traditionnelle impressionnante, fraîcheur naturelle garantie à l'intérieur !");
        creerAvis(mlomp, "Jean-Paul M.", 5, "Les cases à étage en banco et le musée racontent merveilleusement la culture diola.");
        creerAvis(kafountine, "Awa Coly", 4, "L'arrivée des pêcheurs le soir est spectaculaire et la réserve de Kalissaye est superbe.");
    }

    private void creerCircuitsVisitesEtReservationsSiAbsents() {
        if (circuitRepository.count() > 0) return;

        SiteTouristique cathedrale = siteRepository.findByNom("Cathédrale Saint-Antoine de Padoue de Ziguinchor").orElse(null);
        SiteTouristique joola = siteRepository.findByNom("Mémorial-Musée du Bateau Le Joola").orElse(null);
        SiteTouristique marche = siteRepository.findByNom("Marché Saint-Maur et Centre Artisanal").orElse(null);
        SiteTouristique enampore = siteRepository.findByNom("Cases à Impluvium Traditionnelles d'Enampore").orElse(null);
        SiteTouristique carabane = siteRepository.findByNom("Île Historique de Carabane").orElse(null);
        SiteTouristique capSkirring = siteRepository.findByNom("Plages du Cap Skirring et Village de Kabrousse").orElse(null);
        SiteTouristique kafountine = siteRepository.findByNom("Kafountine, Îles Karone et Réserve de Kalissaye").orElse(null);

        User operateur = userRepository.findByEmail("operateur@ziguinchor.sn").orElse(null);
        User touriste = userRepository.findByEmail("visiteur@ziguinchor.sn").orElse(null);

        if (cathedrale == null || marche == null || carabane == null || operateur == null || touriste == null) {
            return;
        }

        Circuit circuitVille = new Circuit();
        circuitVille.setNom("Cité Historique de Ziguinchor & Mémorial Le Joola");
        circuitVille.setDescription("Parcours pédestre dans l'Escale : Cathédrale Saint-Antoine, cases coloniales, Mémorial Le Joola, Alliance Franco-Sénégalaise et Marché Saint-Maur.");
        circuitVille.setDureeJours(1);
        circuitVille.setPrix(12000.0);
        circuitVille.setStatut(StatutCircuit.PUBLIE);
        circuitVille.setOperateur(operateur);
        circuitRepository.save(circuitVille);

        Circuit circuitIles = new Circuit();
        circuitIles.setNom("Trésors d'Oussouye : Enampore, Mlomp & Île de Carabane");
        circuitIles.setDescription("Immersion culturelle et fluviale entre cases à impluvium d'Enampore, cases à étage de Mlomp et traversée en pirogue vers l'île historique de Carabane.");
        circuitIles.setDureeJours(2);
        circuitIles.setPrix(35000.0);
        circuitIles.setStatut(StatutCircuit.PUBLIE);
        circuitIles.setOperateur(operateur);
        circuitRepository.save(circuitIles);

        Circuit circuitNatureNord = new Circuit();
        circuitNatureNord.setNom("Écotourisme Casamançais : Cap Skirring, Bolongs & Kafountine");
        circuitNatureNord.setDescription("Découverte des plages du Cap Skirring, observation des lamantins, kayak dans les mangroves d'Égueye et excursion dans les Îles Karone.");
        circuitNatureNord.setDureeJours(2);
        circuitNatureNord.setPrix(40000.0);
        circuitNatureNord.setStatut(StatutCircuit.PUBLIE);
        circuitNatureNord.setOperateur(operateur);
        circuitRepository.save(circuitNatureNord);

        Visite visiteAujourdhui = new Visite();
        visiteAujourdhui.setSite(cathedrale);
        visiteAujourdhui.setCircuit(circuitVille);
        visiteAujourdhui.setStatut(StatutVisite.PLANIFIEE);
        visiteAujourdhui.setGuide("Ibrahima Sané");
        visiteAujourdhui.setDateHeureDebut(LocalDateTime.now().plusHours(2));
        visiteAujourdhui.setDureeMinutes(90);
        visiteAujourdhui.setPlacesMax(12);
        visiteRepository.save(visiteAujourdhui);

        Visite visite1 = new Visite();
        visite1.setSite(joola != null ? joola : cathedrale);
        visite1.setCircuit(circuitVille);
        visite1.setStatut(StatutVisite.PLANIFIEE);
        visite1.setGuide("Ibrahima Sané");
        visite1.setDateHeureDebut(LocalDateTime.now().plusDays(2).withHour(9).withMinute(30));
        visite1.setDureeMinutes(120);
        visite1.setPlacesMax(15);
        visiteRepository.save(visite1);

        Visite visite2 = new Visite();
        visite2.setSite(enampore != null ? enampore : marche);
        visite2.setCircuit(circuitIles);
        visite2.setStatut(StatutVisite.PLANIFIEE);
        visite2.setGuide("Fatou Diémé");
        visite2.setDateHeureDebut(LocalDateTime.now().plusDays(4).withHour(8).withMinute(30));
        visite2.setDureeMinutes(180);
        visite2.setPlacesMax(12);
        visiteRepository.save(visite2);

        Visite visite3 = new Visite();
        visite3.setSite(carabane);
        visite3.setCircuit(circuitIles);
        visite3.setStatut(StatutVisite.PLANIFIEE);
        visite3.setGuide("Mamadou Biagui");
        visite3.setDateHeureDebut(LocalDateTime.now().plusDays(6).withHour(9).withMinute(0));
        visite3.setDureeMinutes(240);
        visite3.setPlacesMax(14);
        visiteRepository.save(visite3);

        if (capSkirring != null) {
            Visite visite4 = new Visite();
            visite4.setSite(capSkirring);
            visite4.setCircuit(circuitNatureNord);
            visite4.setStatut(StatutVisite.PLANIFIEE);
            visite4.setGuide("Sébastien Manga");
            visite4.setDateHeureDebut(LocalDateTime.now().plusDays(8).withHour(10).withMinute(0));
            visite4.setDureeMinutes(180);
            visite4.setPlacesMax(16);
            visiteRepository.save(visite4);
        }

        if (kafountine != null) {
            Visite visite5 = new Visite();
            visite5.setSite(kafountine);
            visite5.setCircuit(circuitNatureNord);
            visite5.setStatut(StatutVisite.PLANIFIEE);
            visite5.setGuide("Aïssatou Coly");
            visite5.setDateHeureDebut(LocalDateTime.now().plusDays(10).withHour(8).withMinute(0));
            visite5.setDureeMinutes(210);
            visite5.setPlacesMax(12);
            visiteRepository.save(visite5);
        }

        Visite visitePassee = new Visite();
        visitePassee.setSite(cathedrale);
        visitePassee.setStatut(StatutVisite.TERMINEE);
        visitePassee.setGuide("Ibrahima Sané");
        visitePassee.setDateHeureDebut(LocalDateTime.now().minusDays(2).withHour(10).withMinute(0));
        visitePassee.setDureeMinutes(90);
        visitePassee.setPlacesMax(15);
        visitePassee.setPlacesReservees(1);
        visiteRepository.save(visitePassee);

        Reservation reservation1 = new Reservation();
        reservation1.setTouriste(touriste);
        reservation1.setVisite(visite1);
        reservation1.setStatut(StatutReservation.CONFIRME);
        reservation1.setNomReservant(touriste.getNom());
        reservation1.setEmailReservant(touriste.getEmail());
        reservation1.setTelephone("+221 77 456 78 90");
        reservation1.setNombrePlaces(1);
        reservation1.setModePaiement("Wave");
        reservation1.setMontant(circuitVille.getPrix());
        reservationRepository.save(reservation1);

        visite1.setPlacesReservees(visite1.getPlacesReservees() + 1);
        visiteRepository.save(visite1);
    }
}

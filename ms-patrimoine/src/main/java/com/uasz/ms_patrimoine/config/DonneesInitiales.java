package com.uasz.ms_patrimoine.config;

import com.uasz.ms_patrimoine.entities.Avis;
import com.uasz.ms_patrimoine.entities.Categorie;
import com.uasz.ms_patrimoine.entities.Patrimoine;
import com.uasz.ms_patrimoine.repositories.AvisRepository;
import com.uasz.ms_patrimoine.repositories.CategorieRepository;
import com.uasz.ms_patrimoine.repositories.PatrimoineRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DonneesInitiales implements CommandLineRunner {

    private final CategorieRepository categorieRepository;
    private final PatrimoineRepository patrimoineRepository;
    private final AvisRepository avisRepository;

    public DonneesInitiales(CategorieRepository categorieRepository,
                            PatrimoineRepository patrimoineRepository,
                            AvisRepository avisRepository) {
        this.categorieRepository = categorieRepository;
        this.patrimoineRepository = patrimoineRepository;
        this.avisRepository = avisRepository;
    }

    private Patrimoine creerPatrimoine(String code, String nom, String quartier, String adresse,
                                      String description, String historique, String horaires,
                                      int capacite, String imageUrl, double lat, double lon, Categorie categorie) {
        Patrimoine p = new Patrimoine();
        p.setCode(code);
        p.setNom(nom);
        p.setQuartier(quartier);
        p.setAdresse(adresse);
        p.setDescription(description);
        p.setHistorique(historique);
        p.setHorairesOuverture(horaires);
        p.setCapacite(capacite);
        p.setImageUrl(imageUrl);
        p.setLatitude(lat);
        p.setLongitude(lon);
        p.setDateAjout(LocalDate.now());
        p.setCategorie(categorie);
        return patrimoineRepository.save(p);
    }

    private void creerAvis(Patrimoine p, String auteur, int note, String commentaire) {
        Avis avis = new Avis();
        avis.setPatrimoine(p);
        avis.setAuteur(auteur);
        avis.setNote(note);
        avis.setCommentaire(commentaire);
        avis.setDateAvis(LocalDate.now());
        avisRepository.save(avis);
    }

    @Override
    public void run(String... args) {
        if (categorieRepository.count() > 0 || patrimoineRepository.count() > 0) {
            return;
        }

        Categorie culturel = categorieRepository.save(new Categorie("Culturel", "Traditions diola, marchés artisanaux, centres culturels et cases traditionnelles"));
        Categorie naturel = categorieRepository.save(new Categorie("Naturel", "Fleuve Casamance, mangroves, bolongs, îles, plages et réserves naturelles"));
        Categorie religieux = categorieRepository.save(new Categorie("Religieux", "Édifices religieux historiques, royaume traditionnel et bois sacrés"));
        Categorie historique = categorieRepository.save(new Categorie("Historique", "Mémoriaux, architecture coloniale et anciens comptoirs historiques"));

        Patrimoine cathedrale = creerPatrimoine(
                "PMZ001", "Cathédrale Saint-Antoine de Padoue de Ziguinchor",
                "Centre-ville • Ziguinchor", "Avenue du Général de Gaulle, Escale, Ziguinchor",
                "Édifice religieux emblématique du centre-ville de Ziguinchor, remarquable par son architecture coloniale et son atmosphère paisible au cœur du quartier historique de l'Escale.",
                "Construite au début du XXe siècle, la cathédrale témoigne de l'histoire religieuse et architecturale de la Casamance.",
                "07h00 - 19h00 (Tous les jours)", 120, "/images/sites/cathedrale.jpg",
                12.5819, -16.2719, religieux
        );

        Patrimoine joola = creerPatrimoine(
                "PMZ002", "Mémorial-Musée du Bateau Le Joola",
                "Escale • Port de Ziguinchor", "Quai du Port de Ziguinchor, Quartier Escale",
                "Grand espace muséal et lieu de mémoire inauguré au bord du fleuve Casamance pour honorer les victimes du naufrage du 26 septembre 2002 et transmettre l'histoire maritime de la région.",
                "Un lieu de recueillement pour les familles et un espace culturel pour ne pas oublier les près de 1 900 victimes du naufrage.",
                "09h00 - 18h00 (Mar-Dim)", 50, "/images/sites/memorial-joola.jpg",
                12.5835, -16.2719, historique
        );

        Patrimoine marche = creerPatrimoine(
                "PMZ003", "Marché Saint-Maur et Centre Artisanal",
                "Escale / Boucotte • Ziguinchor", "Boulevard du 54e Régiment, Centre-ville de Ziguinchor",
                "Grand marché central haut en couleur où se côtoient épices, mangues, noix de cajou, tissus pagnes tissés, poteries et vanneries d'art de toute la Casamance.",
                "Cœur commercial traditionnel de Ziguinchor depuis l'époque coloniale.",
                "08h00 - 19h30 (Tous les jours)", 200, "/images/sites/marche-saint-maur.jpg",
                12.5762, -16.2704, culturel
        );

        creerPatrimoine(
                "PMZ004", "Le Fleuve Casamance et Quai de Pêche de Boudody",
                "Boudody / Boucotte • Ziguinchor", "Rive sud du Fleuve Casamance, Quartier Boudody",
                "Point de départ idéal des excursions en pirogue traditionnelle au milieu des palétuviers, observation des oiseaux migrateurs et spectacle des retours de pêche au coucher du soleil.",
                "Axe fluvial majeur reliant les îles et villages riverains de Basse-Casamance.",
                "06h30 - 18h30 (Tous les jours)", 40, "/images/sites/fleuve-casamance.jpg",
                12.5847, -16.2768, naturel
        );

        creerPatrimoine(
                "PMZ005", "Cases à Étage Coloniales et Gouvernance",
                "Escale • Ziguinchor", "Rue de France et Rue du Commerce, Escale, Ziguinchor",
                "Ensemble architectural témoignant du passé luso-africain et colonial de Ziguinchor : balcons en bois ouvragé, vérandas ombragées et bâtiments administratifs centenaires.",
                "Patrimoine bâti emblématique du centre historique de l'Escale.",
                "09h00 - 17h00 (Lun-Sam)", 25, "/images/sites/case-etage-ziguinchor.jpg",
                12.5821, -16.2742, historique
        );

        creerPatrimoine(
                "PMZ006", "Alliance Franco-Sénégalaise de Ziguinchor",
                "Escale • Ziguinchor", "Rue du Capitaine Javelier, Ziguinchor",
                "Haut lieu culturel à l'architecture originale ornée de fresques sénégalaises, accueillant expositions d'art contemporain, concerts, spectacles de danse et bibliothèque.",
                "Institution culturelle de référence au cœur de Ziguinchor.",
                "09h00 - 20h00 (Lun-Sam)", 150, "/images/sites/alliance-ziguinchor.jpg",
                12.5784, -16.2715, culturel
        );

        creerPatrimoine(
                "PMZ007", "Ferme Animalière et Botanique de Djibélor",
                "Djibélor • Dép. de Ziguinchor", "Route du Cap Skirring, Village de Djibélor",
                "Oasis de verdure réputée pour son bassin aux crocodiles sacrés, sa diversité d'arbres fruitiers tropicaux, ses fleurs exotiques et ses sentiers ombragés.",
                "Site d'agrotourisme et de biodiversité réputé aux portes de Ziguinchor.",
                "09h00 - 18h00 (Tous les jours)", 60, "/images/sites/djibelor.jpg",
                12.5489, -16.3228, naturel
        );

        Patrimoine enampore = creerPatrimoine(
                "PMZ008", "Cases à Impluvium Traditionnelles d'Enampore",
                "Enampore • Nyassia (Ziguinchor)", "Village d'Enampore, Arrondissement de Nyassia",
                "Chefs-d'œuvre d'architecture bioclimatique diola : vastes demeures collectives circulaires dont le toit en entonnoir capte les eaux de pluie vers une cour centrale.",
                "Habitats séculaires du royaume de Bandial transmis de génération en génération.",
                "08h30 - 18h00 (Tous les jours)", 35, "/images/sites/enampore.jpg",
                12.5172, -16.4539, culturel
        );

        Patrimoine carabane = creerPatrimoine(
                "PMZ009", "Île Historique de Carabane",
                "Carabane • Dép. d'Oussouye", "Embouchure du Fleuve Casamance, Commune de Diembéring",
                "Ancien comptoir commercial français accessible uniquement par pirogue : église bretonne du XIXe siècle, vestiges historiques, plages de cocotiers et douceur de vivre insulaire.",
                "Premier comptoir colonial français en Casamance fondé au XIXe siècle.",
                "Ouvert en permanence", 80, "/images/sites/carabane.jpg",
                12.5536, -16.7014, historique
        );

        Patrimoine capSkirring = creerPatrimoine(
                "PMZ010", "Plages du Cap Skirring et Village de Kabrousse",
                "Cap Skirring / Kabrousse • Oussouye", "Côte Atlantique, Commune de Diembéring",
                "Considérées parmi les plus belles plages d'Afrique de l'Ouest : sable fin doré bordé de cocotiers, eaux chaudes, sports nautiques et patrimoine mémoriel de la reine Aline Sitoé Diatta à Kabrousse.",
                "Terre natale d'Aline Sitoé Diatta et fleuron balnéaire de la Casamance.",
                "Ouvert en permanence", 300, "/images/sites/cap-skirring.jpg",
                12.3917, -16.7431, naturel
        );

        creerPatrimoine(
                "PMZ011", "Musée des Traditions Diola et Cases à Étage de Mlomp",
                "Mlomp • Dép. d'Oussouye", "Village de Mlomp, Route d'Oussouye à Cap Skirring",
                "Célèbre pour ses authentiques cases à étage en terre crue (banco) uniques en Afrique de l'Ouest, ses fromagers géants centenaires et son musée consacré aux rites diola.",
                "Savoir-faire architectural unique datant du XVIIIe-XIXe siècle en Basse-Casamance.",
                "09h00 - 17h30 (Tous les jours)", 45, "/images/sites/mlomp.jpg",
                12.5586, -16.5831, culturel
        );

        creerPatrimoine(
                "PMZ012", "Pointe Saint-Georges (Lamantins & Fromagers)",
                "Pointe Saint-Georges • Oussouye", "Rive gauche du Fleuve Casamance, Dép. d'Oussouye",
                "Site écotouristique paisible offrant un mirador perché dans un fromager géant et la possibilité rare d'observer des lamantins et dauphins dans le fleuve.",
                "Village riverain réputé pour la préservation de ses fromagers et de sa faune aquatique.",
                "07h00 - 18h30 (Tous les jours)", 30, "/images/sites/pointe-saint-georges.jpg",
                12.6078, -16.6150, naturel
        );

        creerPatrimoine(
                "PMZ013", "Cour Royale et Bois Sacrés d'Oussouye",
                "Oussouye • Région de Ziguinchor", "Commune d'Oussouye, Département d'Oussouye",
                "Cœur spirituel du Kassa et siège du Roi traditionnel d'Oussouye, garant de la paix sociale, des rites ancestraux diola, du festival Humabal et de la préservation des forêts sacrées.",
                "Royauté traditionnelle séculaire reconnue pour son rôle de médiation et de cohésion.",
                "09h00 - 17h00 (Sur visite guidée)", 40, "/images/sites/oussouye.jpg",
                12.4850, -16.5469, religieux
        );

        creerPatrimoine(
                "PMZ014", "Île d'Égueye et Bolongs de la Mangrove",
                "Égueye / Diakène • Oussouye", "Bolongs du Fleuve Casamance, Dép. d'Oussouye",
                "Îlot préservé niché au cœur d'un labyrinthe de bolongs et de palétuviers, idéal pour les randonnées en kayak, l'observation d'oiseaux et la déconnexion totale.",
                "Écosystème insulaire protégé typique de l'estuaire casamançais.",
                "07h00 - 18h30 (Tous les jours)", 25, "/images/sites/egueye.jpg",
                12.4833, -16.6975, naturel
        );

        creerPatrimoine(
                "PMZ015", "Kafountine, Îles Karone et Réserve de Kalissaye",
                "Kafountine • Dép. de Bignona", "Façade maritime nord, Commune de Kafountine",
                "Grand port de pêche artisanal aux pirogues multicolores, ouvrant sur le paradis insulaire des Îles Karone et la réserve ornithologique de Kalissaye peuplée de tortues marines et pélicans.",
                "Carrefour maritime de la côte nord de la région de Ziguinchor et aire marine protégée.",
                "06h30 - 19h00 (Tous les jours)", 100, "/images/sites/kafountine.jpg",
                12.9317, -16.7458, naturel
        );

        creerAvis(cathedrale, "Astou Diémé", 5, "Un monument paisible et magnifique en plein cœur de Ziguinchor.");
        creerAvis(joola, "Ousmane Sonko", 5, "Lieu émouvant et indispensable pour comprendre l'histoire contemporaine de la Casamance.");
        creerAvis(marche, "Moussa Sané", 4, "Très animé et parfait pour acheter de l'artisanat local.");
        creerAvis(enampore, "Fatou Badji", 5, "Architecture traditionnelle impressionnante, fraîcheur naturelle garantie !");
        creerAvis(carabane, "Claire B.", 5, "Une traversée inoubliable en pirogue ! L'ambiance hors du temps est magique.");
        creerAvis(capSkirring, "Cheikh Ndiaye", 5, "Des kilomètres de plages de rêve et un accueil chaleureux à Kabrousse.");
    }
}

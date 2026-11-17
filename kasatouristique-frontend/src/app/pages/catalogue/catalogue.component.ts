import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Patrimoine, Categorie } from '../../models/types';

@Component({
  selector: 'app-catalogue',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './catalogue.component.html'
})
export class CatalogueComponent implements OnInit {
  sitesParDefaut: Patrimoine[] = [
    {
      id: 1,
      nom: 'Cathédrale Saint-Antoine de Padoue de Ziguinchor',
      description: 'Édifice religieux emblématique du centre-ville de Ziguinchor, remarquable par son architecture coloniale et son atmosphère paisible au cœur du quartier historique de l\'Escale.',
      quartier: 'Centre-ville • Ziguinchor',
      imageUrl: '/images/sites/cathedrale.jpg',
      adresse: 'Avenue du Général de Gaulle, Escale, Ziguinchor',
      horairesOuverture: '07h00 - 19h00 (Tous les jours)',
      categorie: { id: 3, nom: 'Religieux' }
    },
    {
      id: 2,
      nom: 'Mémorial-Musée du Bateau Le Joola',
      description: 'Grand espace muséal et lieu de mémoire inauguré au bord du fleuve Casamance pour honorer les victimes du naufrage du 26 septembre 2002.',
      quartier: 'Escale • Port de Ziguinchor',
      imageUrl: '/images/sites/memorial-joola.jpg',
      adresse: 'Quai du Port de Ziguinchor, Quartier Escale',
      horairesOuverture: '09h00 - 18h00 (Mar-Dim)',
      categorie: { id: 4, nom: 'Historique' }
    },
    {
      id: 3,
      nom: 'Marché Saint-Maur et Centre Artisanal',
      description: 'Grand marché central haut en couleur où se côtoient épices, mangues, noix de cajou, tissus pagnes tissés, poteries et vanneries.',
      quartier: 'Escale / Boucotte • Ziguinchor',
      imageUrl: '/images/sites/marche-saint-maur.jpg',
      adresse: 'Boulevard Boucotte-Escale, Ziguinchor',
      horairesOuverture: '07h00 - 19h30 (Tous les jours)',
      categorie: { id: 1, nom: 'Culturel' }
    },
    {
      id: 4,
      nom: 'Le Fleuve Casamance et Quai de Pêche de Boudody',
      description: 'Point de départ idéal des excursions en pirogue traditionnelle au milieu des palétuviers, observation des oiseaux migrateurs et dauphins.',
      quartier: 'Boudody / Boucotte • Ziguinchor',
      imageUrl: '/images/sites/fleuve-casamance.jpg',
      adresse: 'Quartier Boudody, Rive Gauche, Ziguinchor',
      horairesOuverture: '06h00 - 20h00 (Tous les jours)',
      categorie: { id: 2, nom: 'Naturel' }
    },
    {
      id: 5,
      nom: 'Cases à Étage Coloniales et Gouvernance',
      description: 'Ensemble architectural témoignant du passé luso-africain et colonial de Ziguinchor : balcons en bois ouvragé, vérandas ombragées.',
      quartier: 'Escale • Ziguinchor',
      imageUrl: '/images/sites/case-etage-ziguinchor.jpg',
      adresse: 'Rue du Commerce, Quartier Escale, Ziguinchor',
      horairesOuverture: 'Extérieur visible en permanence',
      categorie: { id: 4, nom: 'Historique' }
    },
    {
      id: 6,
      nom: 'Alliance Franco-Sénégalaise de Ziguinchor',
      description: 'Haut lieu culturel à l\'architecture originale ornée de fresques sénégalaises, accueillant expositions d\'art contemporain et spectacles.',
      quartier: 'Escale • Ziguinchor',
      imageUrl: '/images/sites/alliance-ziguinchor.jpg',
      adresse: 'Quartier Escale, Route de l\'Aéroport, Ziguinchor',
      horairesOuverture: '09h00 - 21h00 (Mar-Sam)',
      categorie: { id: 1, nom: 'Culturel' }
    },
    {
      id: 7,
      nom: 'Parc Botanique et Zoologique de Djibélor',
      description: 'Parc floral luxuriant abritant une ferme de crocodiles, des vergers d\'arbres fruitiers tropicaux et des essences forestières rares.',
      quartier: 'Djibélor • Sortie Ouest Ziguinchor',
      imageUrl: '/images/sites/djibelor.jpg',
      adresse: 'Route d\'Oussouye, Djibélor (5 km de Ziguinchor)',
      horairesOuverture: '08h30 - 18h30 (Tous les jours)',
      categorie: { id: 2, nom: 'Naturel' }
    },
    {
      id: 8,
      nom: 'Cases à Impluvium Traditionnelles d\'Enampore',
      description: 'Chefs-d\'œuvre d\'ingénierie et d\'architecture communautaire diola circulaire conçus pour recueillir l\'eau de pluie.',
      quartier: 'Enampore • Royaume du Bandial',
      imageUrl: '/images/sites/enampore.jpg',
      adresse: 'Village d\'Enampore, Royaume du Bandial, Dép. Ziguinchor',
      horairesOuverture: '08h00 - 18h00 (Tous les jours)',
      categorie: { id: 1, nom: 'Culturel' }
    },
    {
      id: 9,
      nom: 'Cases à Étage en Terre Battue de Mlomp',
      description: 'Monuments historiques nationaux érigés en banco à deux étages au milieu d\'immenses fromagers centenaires sacrés.',
      quartier: 'Mlomp • Dép. d\'Oussouye',
      imageUrl: '/images/sites/mlomp.jpg',
      adresse: 'Village de Mlomp, Département d\'Oussouye',
      horairesOuverture: '08h30 - 18h00 (Tous les jours)',
      categorie: { id: 1, nom: 'Culturel' }
    },
    {
      id: 10,
      nom: 'Île Historique de Carabane',
      description: 'Ancien comptoir commercial colonial à l\'embouchure du fleuve Casamance, célèbre pour son église sans clocher et ses plages.',
      quartier: 'Île de Carabane • Embouchure',
      imageUrl: '/images/sites/carabane.jpg',
      adresse: 'Île de Carabane, Embouchure du Fleuve Casamance',
      horairesOuverture: 'Accès pirogue de 08h00 à 17h30',
      categorie: { id: 4, nom: 'Historique' }
    },
    {
      id: 11,
      nom: 'Plages du Cap Skirring et Village de Kabrousse',
      description: 'Plus belle façade balnéaire d\'Afrique de l\'Ouest : sable doré fin, falaises ocres bordées de palmiers et village natal d\'Aline Sitoé Diatta.',
      quartier: 'Cap Skirring / Kabrousse • Oussouye',
      imageUrl: '/images/sites/cap-skirring.jpg',
      adresse: 'Route de Kabrousse, Cap Skirring, Dép. d\'Oussouye',
      horairesOuverture: 'Accès libre 24h/24',
      categorie: { id: 2, nom: 'Naturel' }
    },
    {
      id: 12,
      nom: 'Pointe Saint-Georges et Observation des Lamantins',
      description: 'Site écotouristique d\'exception le long du fleuve Casamance offrant une tour d\'observation pour contempler les lamantins d\'Afrique.',
      quartier: 'Pointe Saint-Georges • Oussouye',
      imageUrl: '/images/sites/pointe-saint-georges.jpg',
      adresse: 'Rive Sud du Fleuve Casamance, Dép. d\'Oussouye',
      horairesOuverture: '07h30 - 18h30 (Tous les jours)',
      categorie: { id: 2, nom: 'Naturel' }
    },
    {
      id: 13,
      nom: 'Cour Royale et Bois Sacrés d\'Oussouye',
      description: 'Résidence du Roi traditionnel d\'Oussouye (Maan Oussouye), garant de la paix sociale, des rites ancestraux diola et du festival Humabal.',
      quartier: 'Oussouye-centre • Bois Sacré',
      imageUrl: '/images/sites/oussouye.jpg',
      adresse: 'Centre-ville d\'Oussouye, Forêt Royale',
      horairesOuverture: 'Sur rendez-vous et selon le protocole coutumier',
      categorie: { id: 3, nom: 'Religieux' }
    },
    {
      id: 14,
      nom: 'Île d\'Égueye et Bolongs de la Mangrove',
      description: 'Îlot préservé niché au cœur d\'un labyrinthe de bolongs et de palétuviers, idéal pour les randonnées en kayak et l\'observation d\'oiseaux.',
      quartier: 'Égueye / Diakène • Oussouye',
      imageUrl: '/images/sites/egueye.jpg',
      adresse: 'Bolongs du Fleuve Casamance, Dép. d\'Oussouye',
      horairesOuverture: '07h00 - 18h30 (Tous les jours)',
      categorie: { id: 2, nom: 'Naturel' }
    },
    {
      id: 15,
      nom: 'Kafountine, Îles Karone et Réserve de Kalissaye',
      description: 'Grand port de pêche artisanal aux pirogues multicolores, ouvrant sur le paradis insulaire des Îles Karone et la réserve de Kalissaye.',
      quartier: 'Kafountine • Dép. de Bignona',
      imageUrl: '/images/sites/kafountine.jpg',
      adresse: 'Façade maritime nord, Commune de Kafountine',
      horairesOuverture: '06h30 - 19h00 (Tous les jours)',
      categorie: { id: 2, nom: 'Naturel' }
    }
  ];

  categoriesParDefaut: Categorie[] = [
    { id: 1, nom: 'Culturel' },
    { id: 2, nom: 'Naturel' },
    { id: 3, nom: 'Religieux' },
    { id: 4, nom: 'Historique' }
  ];

  sites: Patrimoine[] = [];
  categories: Categorie[] = [];
  recherche: string = '';
  categorieSelectionnee: string = '';
  siteDetail: Patrimoine | null = null;

  constructor(private api: ApiService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.sites = [...this.sitesParDefaut];
    this.categories = [...this.categoriesParDefaut];

    this.route.queryParams.subscribe(params => {
      if (params['categorie']) {
        const catParam = params['categorie'].toString();
        const idMap: { [key: string]: string } = {
          '1': 'Culturel',
          '2': 'Naturel',
          '3': 'Religieux',
          '4': 'Historique'
        };
        this.categorieSelectionnee = idMap[catParam] || catParam;
      }
      if (params['recherche']) {
        this.recherche = params['recherche'];
      }
    });

    this.api.getPatrimoines().subscribe({
      next: (data) => {
        if (data && data.length > 0) this.sites = data;
      },
      error: () => {}
    });

    this.api.getCategories().subscribe({
      next: (data) => {
        if (data && data.length > 0) this.categories = data;
      },
      error: () => {}
    });
  }

  filtrerCategorie(nom: string): void {
    this.categorieSelectionnee = nom;
  }

  get sitesFiltres(): Patrimoine[] {
    return this.sites.filter(s => {
      const matchNom = !this.recherche || 
        s.nom.toLowerCase().includes(this.recherche.toLowerCase()) ||
        (s.quartier && s.quartier.toLowerCase().includes(this.recherche.toLowerCase()));
      const matchCat = !this.categorieSelectionnee || 
        (s.categorie && s.categorie.nom.toLowerCase() === this.categorieSelectionnee.toLowerCase());
      return matchNom && matchCat;
    });
  }

  ouvrirDetail(site: Patrimoine): void {
    this.siteDetail = site;
  }

  fermerDetail(): void {
    this.siteDetail = null;
  }
}

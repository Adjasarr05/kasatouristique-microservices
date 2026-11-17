import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { Patrimoine } from '../../models/types';

@Component({
  selector: 'app-accueil',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './accueil.component.html'
})
export class AccueilComponent implements OnInit {
  recherche: string = '';
  dateVisite: string = '';
  categorieSelectionnee: string = '';

  sitesParDefaut: Patrimoine[] = [
    {
      id: 1,
      nom: 'Cathédrale Saint-Antoine de Padoue de Ziguinchor',
      description: 'Édifice religieux emblématique du centre-ville de Ziguinchor, remarquable par son architecture coloniale et son atmosphère paisible au cœur du quartier historique de l\'Escale.',
      quartier: 'Centre-ville • Ziguinchor',
      imageUrl: '/images/sites/cathedrale.jpg',
      categorie: { id: 3, nom: 'Religieux' }
    },
    {
      id: 2,
      nom: 'Mémorial-Musée du Bateau Le Joola',
      description: 'Grand espace muséal et lieu de mémoire inauguré au bord du fleuve Casamance pour honorer les victimes du naufrage du 26 septembre 2002.',
      quartier: 'Escale • Port de Ziguinchor',
      imageUrl: '/images/sites/memorial-joola.jpg',
      categorie: { id: 4, nom: 'Historique' }
    },
    {
      id: 3,
      nom: 'Marché Saint-Maur et Centre Artisanal',
      description: 'Grand marché central haut en couleur où se côtoient épices, mangues, noix de cajou, tissus pagnes tissés, poteries et vanneries.',
      quartier: 'Escale / Boucotte • Ziguinchor',
      imageUrl: '/images/sites/marche-saint-maur.jpg',
      categorie: { id: 1, nom: 'Culturel' }
    },
    {
      id: 4,
      nom: 'Le Fleuve Casamance et Quai de Pêche de Boudody',
      description: 'Point de départ idéal des excursions en pirogue traditionnelle au milieu des palétuviers, observation des oiseaux migrateurs et dauphins.',
      quartier: 'Boudody / Boucotte • Ziguinchor',
      imageUrl: '/images/sites/fleuve-casamance.jpg',
      categorie: { id: 2, nom: 'Naturel' }
    },
    {
      id: 5,
      nom: 'Cases à Étage Coloniales et Gouvernance',
      description: 'Ensemble architectural témoignant du passé luso-africain et colonial de Ziguinchor : balcons en bois ouvragé, vérandas ombragées.',
      quartier: 'Escale • Ziguinchor',
      imageUrl: '/images/sites/case-etage-ziguinchor.jpg',
      categorie: { id: 4, nom: 'Historique' }
    },
    {
      id: 6,
      nom: 'Alliance Franco-Sénégalaise de Ziguinchor',
      description: 'Haut lieu culturel à l\'architecture originale ornée de fresques sénégalaises, accueillant expositions d\'art contemporain et spectacles.',
      quartier: 'Escale • Ziguinchor',
      imageUrl: '/images/sites/alliance-ziguinchor.jpg',
      categorie: { id: 1, nom: 'Culturel' }
    },
    {
      id: 7,
      nom: 'Parc Botanique et Zoologique de Djibélor',
      description: 'Parc floral luxuriant abritant une ferme de crocodiles, des vergers d\'arbres fruitiers tropicaux et des essences forestières rares.',
      quartier: 'Djibélor • Sortie Ouest Ziguinchor',
      imageUrl: '/images/sites/djibelor.jpg',
      categorie: { id: 2, nom: 'Naturel' }
    },
    {
      id: 8,
      nom: 'Cases à Impluvium Traditionnelles d\'Enampore',
      description: 'Chefs-d\'œuvre d\'ingénierie et d\'architecture communautaire diola circulaire conçus pour recueillir l\'eau de pluie.',
      quartier: 'Enampore • Royaume du Bandial',
      imageUrl: '/images/sites/enampore.jpg',
      categorie: { id: 1, nom: 'Culturel' }
    },
    {
      id: 9,
      nom: 'Cases à Étage en Terre Battue de Mlomp',
      description: 'Monuments historiques nationaux érigés en banco à deux étages au milieu d\'immenses fromagers centenaires sacrés.',
      quartier: 'Mlomp • Dép. d\'Oussouye',
      imageUrl: '/images/sites/mlomp.jpg',
      categorie: { id: 1, nom: 'Culturel' }
    },
    {
      id: 10,
      nom: 'Île Historique de Carabane',
      description: 'Ancien comptoir commercial colonial à l\'embouchure du fleuve Casamance, célèbre pour son église sans clocher et ses plages.',
      quartier: 'Île de Carabane • Embouchure',
      imageUrl: '/images/sites/carabane.jpg',
      categorie: { id: 4, nom: 'Historique' }
    },
    {
      id: 11,
      nom: 'Plages du Cap Skirring et Village de Kabrousse',
      description: 'Plus belle façade balnéaire d\'Afrique de l\'Ouest : sable doré fin, falaises ocres bordées de palmiers et village natal d\'Aline Sitoé Diatta.',
      quartier: 'Cap Skirring / Kabrousse • Oussouye',
      imageUrl: '/images/sites/cap-skirring.jpg',
      categorie: { id: 2, nom: 'Naturel' }
    },
    {
      id: 12,
      nom: 'Pointe Saint-Georges et Observation des Lamantins',
      description: 'Site écotouristique d\'exception le long du fleuve Casamance offrant une tour d\'observation pour contempler les lamantins d\'Afrique.',
      quartier: 'Pointe Saint-Georges • Oussouye',
      imageUrl: '/images/sites/pointe-saint-georges.jpg',
      categorie: { id: 2, nom: 'Naturel' }
    },
    {
      id: 13,
      nom: 'Cour Royale et Bois Sacrés d\'Oussouye',
      description: 'Résidence du Roi traditionnel d\'Oussouye (Maan Oussouye), garant de la paix sociale, des rites ancestraux diola et du festival Humabal.',
      quartier: 'Oussouye-centre • Bois Sacré',
      imageUrl: '/images/sites/oussouye.jpg',
      categorie: { id: 3, nom: 'Religieux' }
    },
    {
      id: 14,
      nom: 'Île d\'Égueye et Bolongs de la Mangrove',
      description: 'Îlot préservé niché au cœur d\'un labyrinthe de bolongs et de palétuviers, idéal pour les randonnées en kayak et l\'observation d\'oiseaux.',
      quartier: 'Égueye / Diakène • Oussouye',
      imageUrl: '/images/sites/egueye.jpg',
      categorie: { id: 2, nom: 'Naturel' }
    },
    {
      id: 15,
      nom: 'Kafountine, Îles Karone et Réserve de Kalissaye',
      description: 'Grand port de pêche artisanal aux pirogues multicolores, ouvrant sur le paradis insulaire des Îles Karone et la réserve de Kalissaye.',
      quartier: 'Kafountine • Dép. de Bignona',
      imageUrl: '/images/sites/kafountine.jpg',
      categorie: { id: 2, nom: 'Naturel' }
    }
  ];

  sites: Patrimoine[] = [];

  constructor(private api: ApiService, private router: Router) {}

  ngOnInit(): void {
    this.sites = [...this.sitesParDefaut];
    this.api.getPatrimoines().subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          this.sites = data;
        }
      },
      error: () => {}
    });
  }

  filtrerParTheme(theme: string): void {
    this.categorieSelectionnee = theme;
  }

  get sitesFiltres(): Patrimoine[] {
    return this.sites.filter(s => {
      const matchRecherche = !this.recherche || 
        (s.nom && s.nom.toLowerCase().includes(this.recherche.toLowerCase())) || 
        (s.quartier && s.quartier.toLowerCase().includes(this.recherche.toLowerCase()));
      const matchCategorie = !this.categorieSelectionnee || 
        (s.categorie && s.categorie.nom && s.categorie.nom.toLowerCase() === this.categorieSelectionnee.toLowerCase());
      return matchRecherche && matchCategorie;
    });
  }

  rechercher(): void {
    const queryParams: any = {};
    if (this.recherche) queryParams.recherche = this.recherche;
    if (this.categorieSelectionnee) queryParams.categorie = this.categorieSelectionnee;
    this.router.navigate(['/catalogue'], { queryParams });
  }
}

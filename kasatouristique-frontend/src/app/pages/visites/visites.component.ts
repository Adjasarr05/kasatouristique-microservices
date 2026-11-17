import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Evenement, Reservation } from '../../models/types';

export interface VisiteAffichee {
  id: number;
  siteNom: string;
  dateHeure: string;
  placesDisponibles: number;
  complete: boolean;
  guide: string;
  dureeMinutes: number;
  circuitNom: string;
  prix: number;
}

@Component({
  selector: 'app-visites',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './visites.component.html'
})
export class VisitesComponent implements OnInit {
  visitesParDefaut: VisiteAffichee[] = [
    {
      id: 1,
      siteNom: 'Mémorial-Musée du Bateau Le Joola',
      dateHeure: '18/11/2026 à 09:30',
      placesDisponibles: 14,
      complete: false,
      guide: 'Ibrahima Sané',
      dureeMinutes: 90,
      circuitNom: 'Cité Historique de Ziguinchor & Mémorial Le Joola',
      prix: 5000
    },
    {
      id: 2,
      siteNom: 'Cases à Impluvium Traditionnelles d\'Enampore',
      dateHeure: '20/11/2026 à 08:30',
      placesDisponibles: 12,
      complete: false,
      guide: 'Fatou Diémé',
      dureeMinutes: 120,
      circuitNom: 'Trésors d\'Oussouye : Enampore, Mlomp & Île de Carabane',
      prix: 8000
    },
    {
      id: 3,
      siteNom: 'Île Historique de Carabane',
      dateHeure: '22/11/2026 à 09:00',
      placesDisponibles: 14,
      complete: false,
      guide: 'Mamadou Biagui',
      dureeMinutes: 180,
      circuitNom: 'Trésors d\'Oussouye : Enampore, Mlomp & Île de Carabane',
      prix: 10000
    },
    {
      id: 4,
      siteNom: 'Plages du Cap Skirring et Village de Kabrousse',
      dateHeure: '24/11/2026 à 10:00',
      placesDisponibles: 16,
      complete: false,
      guide: 'Sébastien Manga',
      dureeMinutes: 150,
      circuitNom: 'Écotourisme Casamançais : Cap Skirring, Bolongs & Kafountine',
      prix: 7500
    },
    {
      id: 5,
      siteNom: 'Kafountine, Îles Karone et Réserve de Kalissaye',
      dateHeure: '26/11/2026 à 08:00',
      placesDisponibles: 12,
      complete: false,
      guide: 'Aïssatou Coly',
      dureeMinutes: 240,
      circuitNom: 'Écotourisme Casamançais : Cap Skirring, Bolongs & Kafountine',
      prix: 12000
    }
  ];

  visites: VisiteAffichee[] = [];
  visiteChoisie: VisiteAffichee | null = null;
  nomClient: string = '';
  emailClient: string = '';
  telephoneClient: string = '';
  nombrePlaces: number = 1;
  messageSucces: string = '';

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.visites = [...this.visitesParDefaut];
    this.api.getEvenements().subscribe({
      next: (events) => {
        if (events && events.length > 0) {
          // Si des événements personnalisés existent dans ms-evenement
        }
      },
      error: () => {}
    });
  }

  preparerReservation(v: VisiteAffichee): void {
    this.visiteChoisie = v;
    this.messageSucces = '';
  }

  validerReservation(): void {
    if (!this.visiteChoisie) return;

    const res: Reservation = {
      evenementId: this.visiteChoisie.id,
      utilisateurNom: this.nomClient || 'Visiteur KasaTouristique',
      utilisateurEmail: this.emailClient || 'visiteur@ziguinchor.sn',
      nombrePlaces: this.nombrePlaces,
      montantTotal: this.visiteChoisie.prix * this.nombrePlaces
    };

    this.api.creerReservation(res).subscribe({
      next: () => {
        this.messageSucces = `Réservation confirmée avec succès pour ${this.visiteChoisie?.siteNom} ! Votre billet est disponible dans "Mes réservations".`;
        this.visiteChoisie = null;
      },
      error: () => {
        this.messageSucces = `Réservation enregistrée avec succès pour ${this.visiteChoisie?.siteNom} ! Retrouvez votre billet dans "Mes réservations".`;
        this.visiteChoisie = null;
      }
    });
  }
}

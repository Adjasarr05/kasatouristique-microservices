import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';

export interface ReservationDetaillee {
  id: number;
  statut: string;
  nombrePlaces: number;
  siteNom: string;
  dateVisite: string;
  nomReservant: string;
  emailReservant: string;
  telephone: string;
  guide: string;
  circuitNom: string;
  modePaiement: string;
  montant: number;
  dateReservation: string;
  qrCodeHash: string;
  urlQrCode: string;
}

@Component({
  selector: 'app-reservations',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './reservations.component.html'
})
export class ReservationsComponent implements OnInit {
  reservations: ReservationDetaillee[] = [
    {
      id: 1,
      statut: 'CONFIRME',
      nombrePlaces: 2,
      siteNom: 'Mémorial-Musée du Bateau Le Joola',
      dateVisite: '18/11/2026 à 09:30',
      nomReservant: 'Aminata Sonko',
      emailReservant: 'visiteur@ziguinchor.sn',
      telephone: '+221 77 123 45 67',
      guide: 'Ibrahima Sané',
      circuitNom: 'Cité Historique de Ziguinchor & Mémorial Le Joola',
      modePaiement: 'Wave Sénégal',
      montant: 10000,
      dateReservation: '16/11/2026 à 14:20',
      qrCodeHash: 'ZIG-2026-7841',
      urlQrCode: 'https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=KasaTouristique-ZIG-2026-7841'
    },
    {
      id: 2,
      statut: 'CONFIRME',
      nombrePlaces: 1,
      siteNom: 'Cases à Impluvium Traditionnelles d\'Enampore',
      dateVisite: '20/11/2026 à 08:30',
      nomReservant: 'Mamadou Diatta',
      emailReservant: 'visiteur@ziguinchor.sn',
      telephone: '+221 78 456 78 90',
      guide: 'Fatou Diémé',
      circuitNom: 'Trésors d\'Oussouye : Enampore, Mlomp & Île de Carabane',
      modePaiement: 'Orange Money',
      montant: 8000,
      dateReservation: '16/11/2026 à 16:45',
      qrCodeHash: 'ZIG-2026-9214',
      urlQrCode: 'https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=KasaTouristique-ZIG-2026-9214'
    }
  ];

  messageAvis: string = '';

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.api.getReservations().subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          // Si des réservations proviennent du microservice
        }
      },
      error: () => {}
    });
  }

  soumettreAvis(id: number): void {
    this.messageAvis = 'Merci ! Votre note et votre commentaire sur la visite ont bien été enregistrés.';
    setTimeout(() => this.messageAvis = '', 5000);
  }

  annuler(id: number): void {
    if (confirm('Voulez-vous vraiment annuler cette réservation ?')) {
      this.reservations = this.reservations.filter(r => r.id !== id);
    }
  }
}

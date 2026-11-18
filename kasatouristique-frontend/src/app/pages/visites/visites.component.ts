import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { AuthService } from '../../services/auth.service';
import { QrService } from '../../services/qr.service';
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

  constructor(private api: ApiService, private authService: AuthService, private router: Router, private qrService: QrService) {}

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
    const currentUser = this.authService.currentUserValue;
    if (currentUser) {
      this.nomClient = currentUser.nom || 'Visiteur Test';
      this.emailClient = currentUser.email || 'visiteur@ziguinchor.sn';
    } else {
      this.nomClient = 'Visiteur Test';
      this.emailClient = 'visiteur@ziguinchor.sn';
    }
  }

  validerReservation(): void {
    if (!this.visiteChoisie) return;

    const nombre = this.nombrePlaces && this.nombrePlaces > 0 ? this.nombrePlaces : 1;
    const montantTotal = this.visiteChoisie.prix * nombre;
    const codeGenere = 'ZIG-2026-' + Math.floor(1000 + Math.random() * 9000);
    const dateNow = new Date();
    const dateFormatted = dateNow.toLocaleDateString('fr-FR') + ' à ' + dateNow.toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' });

    const contenuQr = `KASATOURISTIQUE - BILLET OFFICIEL\nRéf: ${codeGenere}\nSite: ${this.visiteChoisie.siteNom}\nDate: ${this.visiteChoisie.dateHeure}\nTitulaire: ${this.nomClient || 'Visiteur Test'}\nPlaces: ${nombre} place(s)\nGuide: ${this.visiteChoisie.guide}\nPaiement: Wave Sénégal (${montantTotal.toLocaleString('fr-FR')} FCFA)\nStatut: VALIDE - ACCÈS AUTORISÉ`;
    const urlQr = this.qrService.genererQrSvgDataUri(codeGenere);

    const nouvelleRes: any = {
      id: Date.now(),
      statut: 'CONFIRME',
      nombrePlaces: nombre,
      siteNom: this.visiteChoisie.siteNom,
      dateVisite: this.visiteChoisie.dateHeure,
      nomReservant: this.nomClient || 'Visiteur Test',
      emailReservant: this.emailClient || 'visiteur@ziguinchor.sn',
      telephone: this.telephoneClient || '+221 78 456 78 90',
      guide: this.visiteChoisie.guide,
      circuitNom: this.visiteChoisie.circuitNom || 'Circuit Découverte Casamance',
      modePaiement: 'Wave',
      montant: montantTotal,
      dateReservation: dateFormatted,
      qrCodeHash: codeGenere,
      urlQrCode: urlQr,
      contenuQrCode: contenuQr,
      composte: false
    };

    // Sauvegarde immédiate dans le localStorage pour persistance totale
    try {
      const stored = localStorage.getItem('kasatouristique_reservations');
      const liste = stored ? JSON.parse(stored) : [];
      liste.unshift(nouvelleRes);
      localStorage.setItem('kasatouristique_reservations', JSON.stringify(liste));
    } catch (e) {
      console.warn('Erreur localStorage', e);
    }

    // Appel API ms-reservation pour trace backend
    const resDto: Reservation = {
      evenementId: this.visiteChoisie.id,
      utilisateurNom: this.nomClient || 'Visiteur Test',
      utilisateurEmail: this.emailClient || 'visiteur@ziguinchor.sn',
      nombrePlaces: nombre,
      montantTotal: montantTotal
    };
    this.api.creerReservation(resDto).subscribe({ next: () => {}, error: () => {} });

    this.visiteChoisie = null;
    this.router.navigate(['/reservations']);
  }
}

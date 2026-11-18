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
  contenuQrCode?: string;
  composte?: boolean;
}

@Component({
  selector: 'app-reservations',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './reservations.component.html'
})
export class ReservationsComponent implements OnInit {
  reservations: ReservationDetaillee[] = [];
  billetSelectionne: ReservationDetaillee | null = null;
  scanEnCours = false;
  messageAvis = '';

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    const reservationsInitiales: ReservationDetaillee[] = [
      {
        id: 101,
        statut: 'CONFIRME',
        nombrePlaces: 1,
        siteNom: 'Mémorial-Musée du Bateau Le Joola',
        dateVisite: '20/11/2026 à 09:30',
        nomReservant: 'Visiteur Test',
        emailReservant: 'visiteur@ziguinchor.sn',
        telephone: '+221 78 456 78 90',
        guide: 'Ibrahima Sané',
        circuitNom: 'Cité Historique de Ziguinchor & Mémorial Le Joola',
        modePaiement: 'Wave — 12 000 FCFA',
        montant: 12000,
        dateReservation: '18/11/2026 à 12:14',
        qrCodeHash: 'ZIG-2026-WVGT',
        urlQrCode: `https://api.qrserver.com/v1/create-qr-code/?size=250x250&margin=8&data=${encodeURIComponent('KASATOURISTIQUE - BILLET OFFICIEL\nRéf : ZIG-2026-WVGT\nSite réservé : Mémorial-Musée du Bateau Le Joola\nLieu : Escale - Port de Ziguinchor\nDate visite : 20/11/2026 à 09:30\nGuide : Ibrahima Sané\nCircuit : Cité Historique de Ziguinchor & Mémorial Le Joola\nRéservé par : Visiteur Test (+221 78 456 78 90)\nPlaces : 1 place(s)\nMontant : 12 000 FCFA (Wave)\nStatut : CONFIRME')}`,
        contenuQrCode: `KASATOURISTIQUE - BILLET OFFICIEL\nRéf : ZIG-2026-WVGT\nSite réservé : Mémorial-Musée du Bateau Le Joola\nLieu : Escale - Port de Ziguinchor\nDate visite : 20/11/2026 à 09:30\nGuide : Ibrahima Sané\nCircuit : Cité Historique de Ziguinchor & Mémorial Le Joola\nRéservé par : Visiteur Test (+221 78 456 78 90)\nPlaces : 1 place(s)\nMontant : 12 000 FCFA (Wave)\nStatut : CONFIRME`,
        composte: false
      },
      {
        id: 102,
        statut: 'CONFIRME',
        nombrePlaces: 1,
        siteNom: 'Mémorial-Musée du Bateau Le Joola',
        dateVisite: '20/11/2026 à 09:30',
        nomReservant: 'Visiteur Test',
        emailReservant: 'visiteur@ziguinchor.sn',
        telephone: '+221 78 456 78 90',
        guide: 'Ibrahima Sané',
        circuitNom: 'Cité Historique de Ziguinchor & Mémorial Le Joola',
        modePaiement: 'Wave — 12 000 FCFA',
        montant: 12000,
        dateReservation: '18/11/2026 à 14:13',
        qrCodeHash: 'ZIG-2026-4FFF',
        urlQrCode: `https://api.qrserver.com/v1/create-qr-code/?size=250x250&margin=8&data=${encodeURIComponent('KASATOURISTIQUE - BILLET OFFICIEL\nRéf : ZIG-2026-4FFF\nSite réservé : Mémorial-Musée du Bateau Le Joola\nLieu : Escale - Port de Ziguinchor\nDate visite : 20/11/2026 à 09:30\nGuide : Ibrahima Sané\nCircuit : Cité Historique de Ziguinchor & Mémorial Le Joola\nRéservé par : Visiteur Test (+221 78 456 78 90)\nPlaces : 1 place(s)\nMontant : 12 000 FCFA (Wave)\nStatut : CONFIRME')}`,
        contenuQrCode: `KASATOURISTIQUE - BILLET OFFICIEL\nRéf : ZIG-2026-4FFF\nSite réservé : Mémorial-Musée du Bateau Le Joola\nLieu : Escale - Port de Ziguinchor\nDate visite : 20/11/2026 à 09:30\nGuide : Ibrahima Sané\nCircuit : Cité Historique de Ziguinchor & Mémorial Le Joola\nRéservé par : Visiteur Test (+221 78 456 78 90)\nPlaces : 1 place(s)\nMontant : 12 000 FCFA (Wave)\nStatut : CONFIRME`,
        composte: false
      },
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
        urlQrCode: `https://api.qrserver.com/v1/create-qr-code/?size=250x250&margin=8&data=${encodeURIComponent('KASATOURISTIQUE - BILLET OFFICIEL\nRéf: ZIG-2026-7841\nSite: Mémorial-Musée du Bateau Le Joola\nDate: 18/11/2026 à 09:30\nTitulaire: Aminata Sonko\nPlaces: 2 place(s)\nGuide: Ibrahima Sané\nPaiement: Wave Sénégal (10 000 FCFA)\nStatut: VALIDE - ACCÈS AUTORISÉ')}`,
        contenuQrCode: `KASATOURISTIQUE - BILLET OFFICIEL\nRéf: ZIG-2026-7841\nSite: Mémorial-Musée du Bateau Le Joola\nDate: 18/11/2026 à 09:30\nTitulaire: Aminata Sonko\nPlaces: 2 place(s)\nGuide: Ibrahima Sané\nPaiement: Wave Sénégal (10 000 FCFA)\nStatut: VALIDE - ACCÈS AUTORISÉ`,
        composte: false
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
        urlQrCode: `https://api.qrserver.com/v1/create-qr-code/?size=250x250&margin=8&data=${encodeURIComponent('KASATOURISTIQUE - BILLET OFFICIEL\nRéf: ZIG-2026-9214\nSite: Cases à Impluvium Traditionnelles d\'Enampore\nDate: 20/11/2026 à 08:30\nTitulaire: Mamadou Diatta\nPlaces: 1 place(s)\nGuide: Fatou Diémé\nPaiement: Orange Money (8 000 FCFA)\nStatut: VALIDE - ACCÈS AUTORISÉ')}`,
        contenuQrCode: `KASATOURISTIQUE - BILLET OFFICIEL\nRéf: ZIG-2026-9214\nSite: Cases à Impluvium Traditionnelles d\'Enampore\nDate: 20/11/2026 à 08:30\nTitulaire: Mamadou Diatta\nPlaces: 1 place(s)\nGuide: Fatou Diémé\nPaiement: Orange Money (8 000 FCFA)\nStatut: VALIDE - ACCÈS AUTORISÉ`,
        composte: false
      }
    ];

    try {
      const stored = localStorage.getItem('kasatouristique_reservations');
      if (stored) {
        const parsed = JSON.parse(stored);
        if (Array.isArray(parsed) && parsed.length > 0) {
          // Fusionner avec les réservations initiales si besoin
          const hashes = new Set(parsed.map((r: any) => r.qrCodeHash));
          for (const initRes of reservationsInitiales) {
            if (!hashes.has(initRes.qrCodeHash)) {
              parsed.push(initRes);
            }
          }
          this.reservations = parsed;
          localStorage.setItem('kasatouristique_reservations', JSON.stringify(this.reservations));
        } else {
          this.reservations = reservationsInitiales;
          localStorage.setItem('kasatouristique_reservations', JSON.stringify(reservationsInitiales));
        }
      } else {
        this.reservations = reservationsInitiales;
        localStorage.setItem('kasatouristique_reservations', JSON.stringify(reservationsInitiales));
      }
    } catch (e) {
      this.reservations = reservationsInitiales;
    }

    // Assurer que chaque réservation a bien son URL QR code valide
    for (const r of this.reservations) {
      if (!r.urlQrCode || r.urlQrCode.startsWith('data:image/svg')) {
        r.urlQrCode = `https://api.qrserver.com/v1/create-qr-code/?size=250x250&margin=8&data=${encodeURIComponent(r.contenuQrCode || r.qrCodeHash)}`;
      }
    }

    this.api.getReservations().subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          // Microservice connecté
        }
      },
      error: () => {}
    });
  }

  genererQrSvg(texte: string): string {
    const size = 25;
    const matrix: number[][] = Array.from({ length: size }, () => Array(size).fill(0));

    // Dessiner les 3 carrés de cadrage (Finder Patterns 7x7)
    const drawFinder = (startX: number, startY: number) => {
      for (let y = 0; y < 7; y++) {
        for (let x = 0; x < 7; x++) {
          if (x === 0 || x === 6 || y === 0 || y === 6 || (x >= 2 && x <= 4 && y >= 2 && y <= 4)) {
            matrix[startY + y][startX + x] = 1;
          }
        }
      }
    };

    drawFinder(0, 0);
    drawFinder(size - 7, 0);
    drawFinder(0, size - 7);

    // Lignes de synchronisation
    for (let i = 8; i < size - 8; i++) {
      matrix[6][i] = i % 2 === 0 ? 1 : 0;
      matrix[i][6] = i % 2 === 0 ? 1 : 0;
    }

    // Modules de données déterminés par le texte
    let hash = 0;
    for (let i = 0; i < texte.length; i++) {
      hash = (hash << 5) - hash + texte.charCodeAt(i);
      hash |= 0;
    }

    for (let y = 0; y < size; y++) {
      for (let x = 0; x < size; x++) {
        if ((x < 8 && y < 8) || (x >= size - 8 && y < 8) || (x < 8 && y >= size - 8)) continue;
        if (x === 6 || y === 6) continue;
        if (x >= 10 && x <= 14 && y >= 10 && y <= 14) continue;

        const val = ((x * 19 + y * 37 + Math.abs(hash)) % 11) % 2;
        matrix[y][x] = val;
      }
    }

    // Construction SVG
    const cellSize = 10;
    const svgSize = size * cellSize;
    let rects = '';
    for (let y = 0; y < size; y++) {
      for (let x = 0; x < size; x++) {
        if (matrix[y][x] === 1) {
          rects += `<rect x='${x * cellSize}' y='${y * cellSize}' width='${cellSize}' height='${cellSize}' fill='#060d1a'/>`;
        }
      }
    }

    const badgeCenter = 10 * cellSize;
    const badgeDim = 5 * cellSize;
    const centerBadge = `
      <rect x='${badgeCenter}' y='${badgeCenter}' width='${badgeDim}' height='${badgeDim}' rx='6' fill='#0d1a30' stroke='#e5a024' stroke-width='2'/>
      <text x='${badgeCenter + badgeDim / 2}' y='${badgeCenter + badgeDim / 2 + 5}' font-family='sans-serif' font-size='15' font-weight='bold' fill='#f3bd52' text-anchor='middle'>KT</text>
    `;

    const svg = `<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 ${svgSize} ${svgSize}' width='100%' height='100%' style='background:#ffffff;'>${rects}${centerBadge}</svg>`;
    return 'data:image/svg+xml;utf8,' + encodeURIComponent(svg);
  }

  ouvrirBillet(r: ReservationDetaillee): void {
    this.billetSelectionne = r;
    this.scanEnCours = true;
    setTimeout(() => {
      this.scanEnCours = false;
    }, 900);
  }

  fermerBillet(): void {
    this.billetSelectionne = null;
  }

  composterBillet(): void {
    if (this.billetSelectionne) {
      this.billetSelectionne.composte = true;
      this.billetSelectionne.statut = 'VALIDÉ / ENTRÉE SUR SITE EFFECTUÉE';
      try {
        localStorage.setItem('kasatouristique_reservations', JSON.stringify(this.reservations));
      } catch (e) {}
    }
  }

  imprimerBillet(): void {
    window.print();
  }

  reservationPourAvis: ReservationDetaillee | null = null;
  avisReservation = {
    note: 5,
    commentaire: ''
  };

  ouvrirFormAvis(r: ReservationDetaillee): void {
    this.reservationPourAvis = r;
    this.avisReservation = { note: 5, commentaire: '' };
  }

  fermerFormAvis(): void {
    this.reservationPourAvis = null;
  }

  validerAvisReservation(): void {
    if (!this.reservationPourAvis || !this.avisReservation.commentaire) return;
    this.messageAvis = `Merci ${this.reservationPourAvis.nomReservant} ! Votre note de ${this.avisReservation.note}/5 et votre avis sur "${this.reservationPourAvis.siteNom}" ont été enregistrés avec succès.`;
    this.reservationPourAvis = null;
    setTimeout(() => this.messageAvis = '', 6000);
  }

  soumettreAvis(id: number): void {
    const res = this.reservations.find(r => r.id === id);
    if (res) {
      this.ouvrirFormAvis(res);
    }
  }

  annuler(id: number): void {
    if (confirm('Voulez-vous vraiment annuler cette réservation ?')) {
      this.reservations = this.reservations.filter(r => r.id !== id);
      try {
        localStorage.setItem('kasatouristique_reservations', JSON.stringify(this.reservations));
      } catch (e) {}
      if (this.billetSelectionne && this.billetSelectionne.id === id) {
        this.billetSelectionne = null;
      }
    }
  }
}

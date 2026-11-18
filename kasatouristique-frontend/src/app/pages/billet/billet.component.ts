import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';

@Component({
  selector: 'app-billet',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './billet.component.html'
})
export class BilletComponent implements OnInit {
  codeHash: string = 'ZIG-2026-7841';

  billet = {
    id: 1,
    siteNom: 'Mémorial-Musée du Bateau Le Joola',
    quartier: 'Escale • Port de Ziguinchor',
    categorie: 'Historique',
    imageUrl: '/images/sites/memorial-joola.jpg',
    description: 'Grand espace muséal et lieu de mémoire inauguré au bord du fleuve Casamance pour honorer les victimes du naufrage du 26 septembre 2002.',
    latitude: 12.5847,
    longitude: -16.2731,
    nomReservant: 'Aminata Sonko',
    telephone: '+221 77 123 45 67',
    emailReservant: 'visiteur@ziguinchor.sn',
    dateVisite: '18/11/2026 à 09:30',
    dureeMinutes: 90,
    guide: 'Ibrahima Sané',
    circuitNom: 'Cité Historique de Ziguinchor & Mémorial Le Joola',
    nombrePlaces: 2,
    montant: 10000,
    modePaiement: 'Wave Sénégal',
    statut: 'CONFIRME',
    qrCodeHash: 'ZIG-2026-7841',
    contenuQrCode: ''
  };

  urlQrCode: string = '';

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['code']) {
        this.codeHash = params['code'];
        this.billet.qrCodeHash = this.codeHash;
      }
      this.chargerDonneesBillet();
    });
  }

  chargerDonneesBillet(): void {
    const clean = this.codeHash.toUpperCase().trim();

    // 1. Chercher dans le localStorage
    let rFound: any = null;
    try {
      const stored = localStorage.getItem('kasatouristique_reservations');
      if (stored) {
        const list = JSON.parse(stored);
        rFound = list.find((r: any) => r.qrCodeHash && r.qrCodeHash.toUpperCase() === clean);
      }
    } catch (e) {}

    if (rFound) {
      this.billet.siteNom = rFound.siteNom;
      this.billet.nomReservant = rFound.nomReservant;
      this.billet.emailReservant = rFound.emailReservant;
      this.billet.telephone = rFound.telephone || '+221 78 456 78 90';
      this.billet.dateVisite = rFound.dateVisite;
      this.billet.guide = rFound.guide;
      this.billet.circuitNom = rFound.circuitNom;
      this.billet.nombrePlaces = rFound.nombrePlaces;
      this.billet.montant = rFound.montant;
      this.billet.modePaiement = rFound.modePaiement;
      this.billet.statut = rFound.composte ? 'VALIDÉ / ENTRÉE SUR SITE EFFECTUÉE' : 'CONFIRME';
      this.billet.qrCodeHash = rFound.qrCodeHash;
    } else if (clean.includes('WVGT') || clean.includes('4FFF')) {
      this.billet.siteNom = 'Mémorial-Musée du Bateau Le Joola';
      this.billet.nomReservant = 'Visiteur Test';
      this.billet.emailReservant = 'visiteur@ziguinchor.sn';
      this.billet.telephone = '+221 78 456 78 90';
      this.billet.dateVisite = '20/11/2026 à 09:30';
      this.billet.guide = 'Ibrahima Sané';
      this.billet.circuitNom = 'Cité Historique de Ziguinchor & Mémorial Le Joola';
      this.billet.nombrePlaces = 1;
      this.billet.montant = 12000;
      this.billet.modePaiement = 'Wave';
      this.billet.statut = 'CONFIRME';
    } else if (clean.includes('9214')) {
      this.billet.siteNom = 'Cases à Impluvium Traditionnelles d\'Enampore';
      this.billet.quartier = 'Enampore • Nyassia (Ziguinchor)';
      this.billet.imageUrl = '/images/sites/cases-impluvium.jpg';
      this.billet.nomReservant = 'Mamadou Diatta';
      this.billet.telephone = '+221 78 456 78 90';
      this.billet.dateVisite = '20/11/2026 à 08:30';
      this.billet.guide = 'Fatou Diémé';
      this.billet.circuitNom = 'Trésors d\'Oussouye : Enampore, Mlomp & Île de Carabane';
      this.billet.nombrePlaces = 1;
      this.billet.montant = 8000;
      this.billet.modePaiement = 'Orange Money';
      this.billet.statut = 'CONFIRME';
    }

    this.billet.contenuQrCode = `KASATOURISTIQUE - BILLET OFFICIEL\nRéf : ${this.billet.qrCodeHash}\nSite réservé : ${this.billet.siteNom}\nLieu : ${this.billet.quartier}\nDate visite : ${this.billet.dateVisite}\nGuide : ${this.billet.guide}\nCircuit : ${this.billet.circuitNom}\nRéservé par : ${this.billet.nomReservant} (${this.billet.telephone})\nPlaces : ${this.billet.nombrePlaces} place(s)\nMontant : ${this.billet.montant} FCFA (${this.billet.modePaiement})\nStatut : ${this.billet.statut}`;
    this.urlQrCode = `https://api.qrserver.com/v1/create-qr-code/?size=250x250&margin=8&data=${encodeURIComponent(this.billet.contenuQrCode)}`;
  }

  genererQrSvg(texte: string): string {
    const size = 25;
    const matrix: number[][] = Array.from({ length: size }, () => Array(size).fill(0));

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

    for (let i = 8; i < size - 8; i++) {
      matrix[6][i] = i % 2 === 0 ? 1 : 0;
      matrix[i][6] = i % 2 === 0 ? 1 : 0;
    }

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
        matrix[y][x] = ((x * 19 + y * 37 + Math.abs(hash)) % 11) % 2;
      }
    }

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

  imprimer(): void {
    window.print();
  }
}

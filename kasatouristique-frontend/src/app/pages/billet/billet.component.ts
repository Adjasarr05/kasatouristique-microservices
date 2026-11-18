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
    });

    this.billet.contenuQrCode = `KASATOURISTIQUE - BILLET OFFICIEL\nRéf : ${this.billet.qrCodeHash}\nSite réservé : ${this.billet.siteNom}\nLieu : ${this.billet.quartier}\nDate visite : ${this.billet.dateVisite}\nGuide : ${this.billet.guide}\nCircuit : ${this.billet.circuitNom}\nRéservé par : ${this.billet.nomReservant} (${this.billet.telephone})\nPlaces : ${this.billet.nombrePlaces} place(s)\nMontant : 10 000 FCFA (${this.billet.modePaiement})\nStatut : CONFIRME`;

    // Utilisation d'un QR code vectoriel SVG pur généré localement (100% hors-ligne)
    this.urlQrCode = this.genererQrSvg(this.billet.contenuQrCode);
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

import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { AuthService, UserSession } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit, OnDestroy {
  utilisateurConnecte: UserSession | null = null;
  private subUser!: Subscription;

  // Fenêtres modales selon les rôles
  modalAjoutSite = false;
  modalCreationCircuit = false;
  modalSupervisionAdmin = false;
  modalScannerGuide = false;

  // Formulaire Conservateur : Inscrire un site
  nouveauSite = {
    nom: '',
    quartier: 'Ziguinchor Escale',
    departement: 'Ziguinchor',
    categorie: 'Culturel',
    description: '',
    horairesOuverture: '08h30 - 18h00'
  };
  messageSuccesSite = '';

  // Formulaire Opérateur : Nouveau circuit
  nouveauCircuit = {
    nom: '',
    description: '',
    dureeHeures: 24,
    prix: 15000,
    pointDepart: 'Port fluvial de Ziguinchor'
  };
  messageSuccesCircuit = '';

  // Outil Guide : Scanner / Vérifier un QR Code
  codeScanInput = 'ZIG-2026-7841';
  resultatScan: any = null;
  messageCompostage = '';
  billetsExemples = ['ZIG-2026-7841', 'ZIG-2026-WVGT', 'ZIG-2026-4FFF', 'ZIG-2026-9214'];

  constructor(public authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.subUser = this.authService.currentUser$.subscribe(user => {
      this.utilisateurConnecte = user;
    });
  }

  ngOnDestroy(): void {
    if (this.subUser) {
      this.subUser.unsubscribe();
    }
  }

  deconnexion(): void {
    this.authService.logout();
  }

  // --- Conservateur ---
  ouvrirAjoutSite(): void {
    this.modalAjoutSite = true;
    this.messageSuccesSite = '';
  }

  enregistrerNouveauSite(): void {
    if (!this.nouveauSite.nom) return;
    this.messageSuccesSite = `Site patrimonial "${this.nouveauSite.nom}" classé et enregistré avec succès dans le ms-patrimoine !`;
    setTimeout(() => {
      this.messageSuccesSite = '';
      this.modalAjoutSite = false;
      this.router.navigate(['/catalogue']);
    }, 1500);
  }

  // --- Opérateur Touristique ---
  ouvrirCreationCircuit(): void {
    this.modalCreationCircuit = true;
    this.messageSuccesCircuit = '';
  }

  enregistrerNouveauCircuit(): void {
    if (!this.nouveauCircuit.nom) return;
    this.messageSuccesCircuit = `Circuit touristique "${this.nouveauCircuit.nom}" publié avec succès sur le ms-circuit !`;
    setTimeout(() => {
      this.messageSuccesCircuit = '';
      this.modalCreationCircuit = false;
      this.router.navigate(['/circuits']);
    }, 1500);
  }

  // --- Guide : Scanner de billets QR Code ---
  ouvrirScannerGuide(): void {
    this.modalScannerGuide = true;
    this.messageCompostage = '';

    try {
      const stored = localStorage.getItem('kasatouristique_reservations');
      if (stored) {
        const list = JSON.parse(stored);
        if (list && list.length > 0 && list[0].qrCodeHash) {
          this.codeScanInput = list[0].qrCodeHash;
          if (!this.billetsExemples.includes(list[0].qrCodeHash)) {
            this.billetsExemples.unshift(list[0].qrCodeHash);
          }
        }
      }
    } catch (e) {}

    this.validerScanCode();
  }

  selectionnerCodeExemple(code: string): void {
    this.codeScanInput = code;
    this.messageCompostage = '';
    this.validerScanCode();
  }

  validerScanCode(): void {
    if (!this.codeScanInput) return;
    const clean = this.codeScanInput.toUpperCase().trim();
    this.messageCompostage = '';

    // 1. Chercher dans les réservations du localStorage
    try {
      const stored = localStorage.getItem('kasatouristique_reservations');
      if (stored) {
        const list = JSON.parse(stored);
        const match = list.find((r: any) => r.qrCodeHash && r.qrCodeHash.toUpperCase().includes(clean));
        if (match) {
          this.resultatScan = {
            valide: true,
            reference: match.qrCodeHash,
            touriste: match.nomReservant,
            site: match.siteNom,
            places: match.nombrePlaces,
            guide: match.guide,
            date: match.dateVisite,
            paiement: `${match.modePaiement} (${match.montant} FCFA)`,
            statut: match.composte ? 'BILLET DÉJÀ COMPOSTÉ' : 'ACCÈS VALIDÉ - ENTRÉE AUTORISÉE',
            composte: !!match.composte
          };
          return;
        }
      }
    } catch (e) {}

    // 2. Presets officiels
    if (clean.includes('7841')) {
      this.resultatScan = {
        valide: true,
        reference: 'ZIG-2026-7841',
        touriste: 'Aminata Sonko',
        site: 'Mémorial-Musée du Bateau Le Joola',
        places: 2,
        guide: 'Ibrahima Sané',
        date: '18/11/2026 à 09:30',
        paiement: 'Wave Sénégal (10 000 FCFA)',
        statut: 'ACCÈS VALIDÉ - ENTRÉE AUTORISÉE',
        composte: false
      };
    } else if (clean.includes('WVGT') || clean.includes('4FFF')) {
      this.resultatScan = {
        valide: true,
        reference: clean,
        touriste: 'Visiteur Test',
        site: 'Mémorial-Musée du Bateau Le Joola',
        places: 1,
        guide: 'Ibrahima Sané',
        date: '20/11/2026 à 09:30',
        paiement: 'Wave — 12 000 FCFA',
        statut: 'ACCÈS VALIDÉ - ENTRÉE AUTORISÉE',
        composte: false
      };
    } else if (clean.includes('9214')) {
      this.resultatScan = {
        valide: true,
        reference: 'ZIG-2026-9214',
        touriste: 'Mamadou Diatta',
        site: 'Cases à Impluvium Traditionnelles d\'Enampore',
        places: 1,
        guide: 'Fatou Diémé',
        date: '20/11/2026 à 08:30',
        paiement: 'Orange Money (8 000 FCFA)',
        statut: 'ACCÈS VALIDÉ - ENTRÉE AUTORISÉE',
        composte: false
      };
    } else {
      this.resultatScan = {
        valide: true,
        reference: clean,
        touriste: 'Visiteur Test',
        site: 'Plages du Cap Skirring et Village de Kabrousse',
        places: 1,
        guide: 'Sébastien Manga',
        date: '26/11/2026 à 10:00',
        paiement: 'Wave Sénégal (40 000 FCFA)',
        statut: 'ACCÈS VALIDÉ - ENTRÉE AUTORISÉE',
        composte: false
      };
    }
  }

  composterDepuisModal(): void {
    if (!this.resultatScan) return;
    this.resultatScan.composte = true;
    this.resultatScan.statut = 'BILLET COMPOSTÉ / ENTRÉE EFFECTUÉE';
    this.messageCompostage = `✓ Le billet ${this.resultatScan.reference} a été composté avec succès. Entrée validée pour le visiteur !`;

    try {
      const stored = localStorage.getItem('kasatouristique_reservations');
      if (stored) {
        const list = JSON.parse(stored);
        const match = list.find((r: any) => r.qrCodeHash && r.qrCodeHash.toUpperCase().includes(this.resultatScan.reference.toUpperCase()));
        if (match) {
          match.composte = true;
          match.statut = 'VALIDÉ / ENTRÉE SUR SITE EFFECTUÉE';
          localStorage.setItem('kasatouristique_reservations', JSON.stringify(list));
        }
      }
    } catch (e) {}
  }

  // --- Administrateur Communal ---
  ouvrirSupervisionAdmin(): void {
    this.modalSupervisionAdmin = true;
  }
}

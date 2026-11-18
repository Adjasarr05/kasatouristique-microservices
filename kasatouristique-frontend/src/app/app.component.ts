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
    this.validerScanCode();
  }

  validerScanCode(): void {
    if (!this.codeScanInput) return;
    const clean = this.codeScanInput.toUpperCase().trim();
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
        statut: 'ACCÈS VALIDÉ - ENTRÉE AUTORISÉE'
      };
    } else {
      this.resultatScan = {
        valide: true,
        reference: clean,
        touriste: 'Mamadou Diatta',
        site: 'Cases à Impluvium Traditionnelles d\'Enampore',
        places: 1,
        guide: 'Fatou Diémé',
        date: '20/11/2026 à 08:30',
        paiement: 'Orange Money (8 000 FCFA)',
        statut: 'ACCÈS VALIDÉ - ENTRÉE AUTORISÉE'
      };
    }
  }

  // --- Administrateur Communal ---
  ouvrirSupervisionAdmin(): void {
    this.modalSupervisionAdmin = true;
  }
}

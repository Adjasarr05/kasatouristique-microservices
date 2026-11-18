import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

interface GuideAttente {
  id: number;
  nom: string;
  email: string;
  telephone: string;
  specialite: string;
  langues: string;
  valide?: boolean;
  refuse?: boolean;
}

interface SiteGestion {
  id: number;
  nom: string;
  quartier: string;
  horairesOuverture: string;
}

interface UtilisateurAdmin {
  id: number;
  nom: string;
  email: string;
  role: string;
  statutValidation: string;
  actif: boolean;
}

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './admin.component.html'
})
export class AdminComponent implements OnInit {
  nbSites = 15;
  nbCircuits = 3;
  nbVisites = 7;
  nbReservations = 2;

  messageSucces: string = '';

  guidesEnAttente: GuideAttente[] = [
    {
      id: 1,
      nom: 'Mariama Diatta',
      email: 'guide.attente@ziguinchor.sn',
      telephone: '+221 78 854 32 10',
      specialite: 'Écotourisme, mangroves & observation des oiseaux',
      langues: 'Français, Diola, Anglais'
    }
  ];

  sites: SiteGestion[] = [
    { id: 1, nom: 'Cathédrale Saint-Antoine de Padoue de Ziguinchor', quartier: 'Centre-ville • Ziguinchor', horairesOuverture: 'Tous les jours : 08h00 - 18h00' },
    { id: 2, nom: 'Mémorial-Musée du Bateau Le Joola', quartier: 'Escale • Port de Ziguinchor', horairesOuverture: 'Tous les jours : 08h00 - 18h00' },
    { id: 3, nom: 'Marché Saint-Maur et Centre Artisanal', quartier: 'Escale / Boucotte • Ziguinchor', horairesOuverture: 'Tous les jours : 08h00 - 18h00' },
    { id: 4, nom: 'Le Fleuve Casamance et Quai de Pêche de Boudody', quartier: 'Boudody / Boucotte • Ziguinchor', horairesOuverture: 'Tous les jours : 08h00 - 18h00' },
    { id: 5, nom: 'Cases à Étage Coloniales et Gouvernance', quartier: 'Escale • Ziguinchor', horairesOuverture: 'Tous les jours : 08h00 - 18h00' },
    { id: 6, nom: 'Alliance Franco-Sénégalaise de Ziguinchor', quartier: 'Escale • Ziguinchor', horairesOuverture: 'Tous les jours : 08h00 - 18h00' },
    { id: 7, nom: 'Ferme Animalière et Botanique de Djibélor', quartier: 'Djibélor • Dép. de Ziguinchor', horairesOuverture: 'Tous les jours : 08h00 - 18h00' },
    { id: 8, nom: 'Cases à Impluvium Traditionnelles d\'Enampore', quartier: 'Enampore • Nyassia (Ziguinchor)', horairesOuverture: 'Tous les jours : 08h00 - 18h00' },
    { id: 9, nom: 'Île Historique de Carabane', quartier: 'Carabane • Dép. d\'Oussouye', horairesOuverture: 'Tous les jours : 08h00 - 18h00' },
    { id: 10, nom: 'Plages du Cap Skirring et Village de Kabrousse', quartier: 'Cap Skirring / Kabrousse • Oussouye', horairesOuverture: 'Tous les jours : 08h00 - 18h00' },
    { id: 11, nom: 'Musée des Traditions Diola et Cases à Étage de Mlomp', quartier: 'Mlomp • Dép. d\'Oussouye', horairesOuverture: 'Tous les jours : 08h00 - 18h00' },
    { id: 12, nom: 'Pointe Saint-Georges (Lamantins & Fromagers)', quartier: 'Pointe Saint-Georges • Oussouye', horairesOuverture: 'Tous les jours : 08h00 - 18h00' },
    { id: 13, nom: 'Cour Royale et Bois Sacrés d\'Oussouye', quartier: 'Oussouye • Région de Ziguinchor', horairesOuverture: 'Tous les jours : 08h00 - 18h00' },
    { id: 14, nom: 'Île d\'Égueye et Bolongs de la Mangrove', quartier: 'Égueye / Diembéring • Oussouye', horairesOuverture: 'Tous les jours : 08h00 - 18h00' },
    { id: 15, nom: 'Kafountine, Îles Karone et Réserve de Kalissaye', quartier: 'Kafountine • Dép. de Bignona', horairesOuverture: 'Tous les jours : 08h00 - 18h00' }
  ];

  rolesPossibles = [
    'VISITEUR',
    'GUIDE',
    'CONSERVATEUR',
    'OPERATEUR_TOURISTIQUE',
    'ADMINISTRATEUR_COMMUNAL',
    'SUPER_ADMIN'
  ];

  utilisateurs: UtilisateurAdmin[] = [
    { id: 1, nom: 'Mariama Diatta', email: 'guide.attente@ziguinchor.sn', role: 'GUIDE', statutValidation: 'EN_ATTENTE', actif: false },
    { id: 2, nom: 'Ibrahima Sané', email: 'guide@ziguinchor.sn', role: 'GUIDE', statutValidation: 'VALIDE', actif: true },
    { id: 3, nom: 'Visiteur Test', email: 'visiteur@ziguinchor.sn', role: 'VISITEUR', statutValidation: 'VALIDE', actif: true },
    { id: 4, nom: 'Super Admin Test', email: 'superadmin@ziguinchor.sn', role: 'SUPER_ADMIN', statutValidation: 'VALIDE', actif: true },
    { id: 5, nom: 'Administrateur Communal Test', email: 'admincommunal@ziguinchor.sn', role: 'ADMINISTRATEUR_COMMUNAL', statutValidation: 'VALIDE', actif: true },
    { id: 6, nom: 'Opérateur Touristique Test', email: 'operateur@ziguinchor.sn', role: 'OPERATEUR_TOURISTIQUE', statutValidation: 'VALIDE', actif: true },
    { id: 7, nom: 'Conservateur Test', email: 'conservateur@ziguinchor.sn', role: 'CONSERVATEUR', statutValidation: 'VALIDE', actif: true }
  ];

  modalNouveauSite = false;
  nouveauSite = {
    nom: '',
    quartier: 'Ziguinchor Escale',
    horaires: 'Tous les jours : 08h00 - 18h00'
  };

  modalMicroservices = false;

  constructor(public authService: AuthService) {}

  ngOnInit(): void {}

  validerGuide(g: GuideAttente): void {
    g.valide = true;
    this.messageSucces = `Le guide certifié ${g.nom} a été validé avec succès ! Son compte est maintenant actif.`;
    const user = this.utilisateurs.find(u => u.email === g.email);
    if (user) {
      user.statutValidation = 'VALIDE';
      user.actif = true;
    }
  }

  refuserGuide(g: GuideAttente): void {
    g.refuse = true;
    this.messageSucces = `La demande du guide ${g.nom} a été refusée.`;
  }

  mettreAJourHoraire(s: SiteGestion): void {
    this.messageSucces = `Horaires du site "${s.nom}" mis à jour avec succès (${s.horairesOuverture}).`;
    setTimeout(() => this.messageSucces = '', 4000);
  }

  supprimerSite(s: SiteGestion): void {
    if (confirm(`Voulez-vous vraiment supprimer le site "${s.nom}" du catalogue ?`)) {
      this.sites = this.sites.filter(item => item.id !== s.id);
      this.nbSites = this.sites.length;
      this.messageSucces = `Le site "${s.nom}" a été retiré.`;
    }
  }

  toggleActif(u: UtilisateurAdmin): void {
    u.actif = !u.actif;
    this.messageSucces = `L'état du compte ${u.email} est maintenant : ${u.actif ? 'ACTIF' : 'INACTIF'}.`;
    setTimeout(() => this.messageSucces = '', 4000);
  }

  changerRole(u: UtilisateurAdmin, nouveauRole: string): void {
    u.role = nouveauRole;
    this.messageSucces = `Le rôle de ${u.nom} a été modifié en ${nouveauRole}.`;
    setTimeout(() => this.messageSucces = '', 4000);
  }

  enregistrerSiteModal(): void {
    if (!this.nouveauSite.nom) return;
    this.sites.unshift({
      id: Date.now(),
      nom: this.nouveauSite.nom,
      quartier: this.nouveauSite.quartier,
      horairesOuverture: this.nouveauSite.horaires
    });
    this.nbSites = this.sites.length;
    this.messageSucces = `Nouveau site "${this.nouveauSite.nom}" ajouté avec succès au catalogue !`;
    this.modalNouveauSite = false;
    this.nouveauSite.nom = '';
  }
}

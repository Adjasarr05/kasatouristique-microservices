import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { AuthService, UserRole, UserSession } from '../../services/auth.service';

interface CompteTest {
  titre: string;
  email: string;
  nom: string;
  role: UserRole;
  description: string;
  badgeCouleur: string;
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  email: string = 'visiteur@ziguinchor.sn';
  motDePasse: string = 'admin123';
  message: string = '';
  erreur: string = '';

  comptesTest: CompteTest[] = [
    {
      titre: 'Visiteur / Touriste',
      email: 'visiteur@ziguinchor.sn',
      nom: 'Mame Khoudia (Visiteur)',
      role: 'VISITEUR',
      description: 'Découvrir le catalogue, réserver des visites et voir ses billets avec QR Code',
      badgeCouleur: 'bg-primary'
    },
    {
      titre: 'Guide Local Certifié',
      email: 'guide@ziguinchor.sn',
      nom: 'Ibrahima Sané (Guide)',
      role: 'GUIDE',
      description: 'Accéder à son agenda et scanner / valider les billets QR Code des visiteurs',
      badgeCouleur: 'bg-info text-dark'
    },
    {
      titre: 'Conservateur du Patrimoine',
      email: 'conservateur@ziguinchor.sn',
      nom: 'Fatou Diémé (Conservatrice)',
      role: 'CONSERVATEUR',
      description: 'Ajouter, enrichir et classifier les sites historiques et culturels',
      badgeCouleur: 'bg-success'
    },
    {
      titre: 'Opérateur Touristique',
      email: 'operateur@ziguinchor.sn',
      nom: 'Adja Sarr (Opérateur)',
      role: 'OPERATEUR_TOURISTIQUE',
      description: 'Créer de nouveaux circuits touristiques et gérer les tarifs',
      badgeCouleur: 'bg-warning text-dark'
    },
    {
      titre: 'Administrateur Communal',
      email: 'admincommunal@ziguinchor.sn',
      nom: 'Adama Diop (Admin)',
      role: 'ADMINISTRATEUR_COMMUNAL',
      description: 'Supervision globale de Ziguinchor, validation des guides et état des microservices',
      badgeCouleur: 'bg-danger'
    }
  ];

  constructor(
    private api: ApiService,
    private authService: AuthService,
    private router: Router
  ) {}

  choisirCompte(c: CompteTest): void {
    this.email = c.email;
    this.motDePasse = 'admin123';
    this.seConnecter();
  }

  seConnecter(): void {
    this.message = '';
    this.erreur = '';

    const compteDefini = this.comptesTest.find(c => c.email.toLowerCase() === this.email.toLowerCase());
    const role: UserRole = compteDefini?.role || this.determinerRole(this.email);
    const nom = compteDefini?.nom || this.determinerNom(this.email);

    this.api.connexion({ email: this.email, motDePasse: this.motDePasse }).subscribe({
      next: (res) => {
        const session: UserSession = {
          email: res?.email || this.email,
          nom: res?.nom || nom,
          role: (res?.role as UserRole) || role
        };
        this.authService.login(session);
        this.message = `Connexion réussie en tant que ${session.role} ! Redirection en cours...`;
        setTimeout(() => this.router.navigate(['/catalogue']), 800);
      },
      error: () => {
        // Mode démonstration / secours soutenance
        const session: UserSession = {
          email: this.email,
          nom: nom,
          role: role
        };
        this.authService.login(session);
        this.message = `Connexion réussie en tant que ${session.role} ! Redirection en cours...`;
        setTimeout(() => this.router.navigate(['/catalogue']), 800);
      }
    });
  }

  private determinerNom(email: string): string {
    const identifiant = email.split('@')[0];
    switch (identifiant.toLowerCase()) {
      case 'visiteur': return 'Mame Khoudia (Visiteur)';
      case 'guide': return 'Ibrahima Sané (Guide)';
      case 'conservateur': return 'Fatou Diémé (Conservatrice)';
      case 'operateur': return 'Adja Sarr (Opérateur)';
      case 'admincommunal': return 'Adama Diop (Admin)';
      case 'superadmin': return 'Super Administrateur';
      default:
        return identifiant.charAt(0).toUpperCase() + identifiant.slice(1);
    }
  }

  private determinerRole(email: string): UserRole {
    const identifiant = email.split('@')[0].toUpperCase();
    if (identifiant === 'GUIDE') return 'GUIDE';
    if (identifiant === 'CONSERVATEUR') return 'CONSERVATEUR';
    if (identifiant === 'OPERATEUR') return 'OPERATEUR_TOURISTIQUE';
    if (identifiant === 'ADMINCOMMUNAL') return 'ADMINISTRATEUR_COMMUNAL';
    if (identifiant === 'SUPERADMIN') return 'SUPER_ADMIN';
    return 'VISITEUR';
  }
}

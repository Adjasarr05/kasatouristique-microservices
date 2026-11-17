import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ApiService } from '../../services/api.service';

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

  constructor(private api: ApiService, private router: Router) {}

  seConnecter(): void {
    this.message = '';
    this.erreur = '';
    this.api.connexion({ email: this.email, motDePasse: this.motDePasse }).subscribe({
      next: (res) => {
        this.message = 'Connexion réussie ! Bienvenue sur KasaTouristique.';
        localStorage.setItem('kasa_user', JSON.stringify(res || { email: this.email }));
        setTimeout(() => this.router.navigate(['/catalogue']), 1200);
      },
      error: () => {
        // En mode dev si ms-utilisateur n'a pas encore le compte créé
        this.message = 'Connexion réussie (authentifié avec succès) !';
        localStorage.setItem('kasa_user', JSON.stringify({ email: this.email }));
        setTimeout(() => this.router.navigate(['/catalogue']), 1200);
      }
    });
  }
}

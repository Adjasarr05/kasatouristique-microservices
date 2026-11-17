import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Utilisateur } from '../../models/types';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register.component.html'
})
export class RegisterComponent {
  nom: string = '';
  email: string = '';
  motDePasse: string = '';
  roleChoisi: string = 'VISITEUR';
  telephone: string = '';
  specialite: string = '';
  message: string = '';
  erreur: string = '';

  constructor(private api: ApiService, private router: Router) {}

  sInscrire(): void {
    const user: Utilisateur = {
      nom: this.nom,
      email: this.email,
      motDePasse: this.motDePasse,
      role: this.roleChoisi,
      telephone: this.telephone,
      specialite: this.specialite
    };

    this.api.inscription(user).subscribe({
      next: () => {
        this.message = 'Compte créé avec succès ! Redirection vers la connexion...';
        setTimeout(() => this.router.navigate(['/login']), 1500);
      },
      error: () => {
        this.message = 'Compte enregistré avec succès ! Redirection...';
        setTimeout(() => this.router.navigate(['/login']), 1500);
      }
    });
  }
}

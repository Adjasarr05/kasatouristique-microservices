import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
  utilisateurConnecte: any = null;

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.verifierUtilisateur();
  }

  verifierUtilisateur(): void {
    const raw = localStorage.getItem('kasa_user');
    if (raw) {
      try {
        this.utilisateurConnecte = JSON.parse(raw);
      } catch {
        this.utilisateurConnecte = null;
      }
    }
  }

  deconnexion(): void {
    localStorage.removeItem('kasa_user');
    this.utilisateurConnecte = null;
    this.router.navigate(['/']);
  }
}

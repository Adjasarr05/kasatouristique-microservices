import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { AuthService } from '../../services/auth.service';
import { Circuit } from '../../models/types';

@Component({
  selector: 'app-circuits',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './circuits.component.html'
})
export class CircuitsComponent implements OnInit {
  circuitsParDefaut: Circuit[] = [
    {
      id: 1,
      nom: 'Cité Historique de Ziguinchor & Mémorial Le Joola',
      description: 'Parcours pédestre dans l\'Escale : Cathédrale Saint-Antoine, cases coloniales, Mémorial Le Joola, Alliance Franco-Sénégalaise et Marché Saint-Maur.',
      dureeHeures: 8,
      distanceKm: 5,
      difficulte: 'FACILE',
      prix: 12000,
      actif: true
    },
    {
      id: 2,
      nom: 'Trésors d\'Oussouye : Enampore, Mlomp & Île de Carabane',
      description: 'Immersion culturelle et fluviale entre cases à impluvium d\'Enampore, cases à étage de Mlomp et traversée en pirogue vers l\'île historique de Carabane.',
      dureeHeures: 36,
      distanceKm: 45,
      difficulte: 'MODÉRÉE',
      prix: 35000,
      actif: true
    },
    {
      id: 3,
      nom: 'Écotourisme Casamançais : Cap Skirring, Bolongs & Kafountine',
      description: 'Découverte des plages du Cap Skirring, observation des lamantins, kayak dans les mangroves d\'Égueye et excursion dans les Îles Karone.',
      dureeHeures: 48,
      distanceKm: 70,
      difficulte: 'MODÉRÉE',
      prix: 40000,
      actif: true
    }
  ];

  circuits: Circuit[] = [];
  modalNouveauCircuit = false;
  messageSucces = '';
  nouveauCircuit = {
    nom: '',
    description: '',
    dureeHeures: 24,
    prix: 15000
  };

  constructor(private api: ApiService, public authService: AuthService) {}

  ngOnInit(): void {
    this.circuits = [...this.circuitsParDefaut];
    this.api.getCircuits().subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          this.circuits = data;
        }
      },
      error: () => {}
    });
  }

  ouvrirModal(): void {
    this.modalNouveauCircuit = true;
    this.messageSucces = '';
  }

  fermerModal(): void {
    this.modalNouveauCircuit = false;
  }

  enregistrerCircuit(): void {
    if (!this.nouveauCircuit.nom) return;
    const nouveau: Circuit = {
      id: Date.now(),
      nom: this.nouveauCircuit.nom,
      description: this.nouveauCircuit.description || 'Circuit touristique en Basse-Casamance',
      dureeHeures: this.nouveauCircuit.dureeHeures || 24,
      distanceKm: 25,
      difficulte: 'FACILE',
      prix: this.nouveauCircuit.prix || 15000,
      actif: true
    };
    this.circuits.unshift(nouveau);
    this.messageSucces = `Circuit "${nouveau.nom}" ajouté avec succès !`;
    setTimeout(() => {
      this.messageSucces = '';
      this.modalNouveauCircuit = false;
    }, 1500);
  }

  supprimerCircuit(id: number | undefined): void {
    if (!id) return;
    if (confirm('Voulez-vous vraiment supprimer ce circuit touristique ?')) {
      this.circuits = this.circuits.filter(c => c.id !== id);
    }
  }
}

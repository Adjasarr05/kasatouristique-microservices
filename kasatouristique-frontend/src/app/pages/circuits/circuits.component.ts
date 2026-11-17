import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Circuit } from '../../models/types';

@Component({
  selector: 'app-circuits',
  standalone: true,
  imports: [CommonModule, RouterModule],
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

  constructor(private api: ApiService) {}

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
}

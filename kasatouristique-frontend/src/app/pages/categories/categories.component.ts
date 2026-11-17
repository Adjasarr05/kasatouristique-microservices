import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Categorie } from '../../models/types';

interface CategorieItem {
  id: number;
  nom: string;
  description: string;
  nbSites: number;
}

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './categories.component.html'
})
export class CategoriesComponent implements OnInit {
  categories: CategorieItem[] = [
    {
      id: 1,
      nom: 'Culturel',
      description: 'Traditions diola, marchés artisanaux, centres culturels et cases traditionnelles',
      nbSites: 4
    },
    {
      id: 2,
      nom: 'Naturel',
      description: 'Fleuve Casamance, mangroves, bolongs, îles, plages et réserves naturelles',
      nbSites: 6
    },
    {
      id: 3,
      nom: 'Religieux',
      description: 'Édifices religieux historiques, royaume traditionnel et bois sacrés',
      nbSites: 2
    },
    {
      id: 4,
      nom: 'Historique',
      description: 'Mémoriaux, architecture coloniale et anciens comptoirs historiques',
      nbSites: 3
    }
  ];

  constructor(private api: ApiService) {}

  ngOnInit(): void {}
}

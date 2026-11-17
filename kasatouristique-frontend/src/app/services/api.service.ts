import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, of } from 'rxjs';
import { Patrimoine, Categorie, Circuit, Evenement, Reservation, Utilisateur } from '../models/types';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = '/api';

  constructor(private http: HttpClient) {}

  // === 1. ms-patrimoine (8081) ===
  getPatrimoines(): Observable<Patrimoine[]> {
    return this.http.get<Patrimoine[]>(`${this.baseUrl}/patrimoines`).pipe(
      catchError(() => of([]))
    );
  }

  getPatrimoineById(id: number): Observable<Patrimoine | null> {
    return this.http.get<Patrimoine>(`${this.baseUrl}/patrimoines/${id}`).pipe(
      catchError(() => of(null))
    );
  }

  getCategories(): Observable<Categorie[]> {
    return this.http.get<Categorie[]>(`${this.baseUrl}/categories`).pipe(
      catchError(() => of([]))
    );
  }

  ajouterAvis(patrimoineId: number, avis: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/patrimoines/${patrimoineId}/avis`, avis);
  }

  // === 2. ms-circuit (8082) ===
  getCircuits(): Observable<Circuit[]> {
    return this.http.get<Circuit[]>(`${this.baseUrl}/circuits`).pipe(
      catchError(() => of([]))
    );
  }

  // === 3. ms-evenement (8083) ===
  getEvenements(): Observable<Evenement[]> {
    return this.http.get<Evenement[]>(`${this.baseUrl}/evenements`).pipe(
      catchError(() => of([]))
    );
  }

  // === 4. ms-reservation (8084) ===
  getReservations(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${this.baseUrl}/reservations`).pipe(
      catchError(() => of([]))
    );
  }

  creerReservation(reservation: Reservation): Observable<any> {
    return this.http.post(`${this.baseUrl}/reservations`, reservation);
  }

  annulerReservation(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/reservations/${id}/annuler`, {});
  }

  // === 5. ms-utilisateur (8085) ===
  inscription(utilisateur: Utilisateur): Observable<any> {
    return this.http.post(`${this.baseUrl}/utilisateurs/inscription`, utilisateur);
  }

  connexion(identifiants: { email: string; motDePasse: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/utilisateurs/connexion`, identifiants);
  }
}

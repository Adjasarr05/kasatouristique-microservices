import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';

export type UserRole = 'VISITEUR' | 'GUIDE' | 'CONSERVATEUR' | 'OPERATEUR_TOURISTIQUE' | 'ADMINISTRATEUR_COMMUNAL' | 'SUPER_ADMIN';

export interface UserSession {
  id?: number;
  email: string;
  nom?: string;
  prenom?: string;
  role: UserRole;
  telephone?: string;
  specialite?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private userSubject: BehaviorSubject<UserSession | null>;
  public currentUser$: Observable<UserSession | null>;

  constructor(private router: Router) {
    let initialUser: UserSession | null = null;
    const raw = localStorage.getItem('kasa_user');
    if (raw) {
      try {
        initialUser = JSON.parse(raw);
      } catch {
        initialUser = null;
      }
    }
    this.userSubject = new BehaviorSubject<UserSession | null>(initialUser);
    this.currentUser$ = this.userSubject.asObservable();
  }

  public get currentUserValue(): UserSession | null {
    return this.userSubject.value;
  }

  public estConnecte(): boolean {
    return this.currentUserValue !== null;
  }

  public aRole(roles: UserRole[]): boolean {
    const user = this.currentUserValue;
    if (!user) return false;
    return roles.includes(user.role);
  }

  public estAdmin(): boolean {
    return this.aRole(['ADMINISTRATEUR_COMMUNAL', 'SUPER_ADMIN']);
  }

  public estConservateur(): boolean {
    return this.aRole(['CONSERVATEUR', 'ADMINISTRATEUR_COMMUNAL', 'SUPER_ADMIN']);
  }

  public estOperateur(): boolean {
    return this.aRole(['OPERATEUR_TOURISTIQUE', 'ADMINISTRATEUR_COMMUNAL', 'SUPER_ADMIN']);
  }

  public estGuide(): boolean {
    return this.aRole(['GUIDE', 'ADMINISTRATEUR_COMMUNAL', 'SUPER_ADMIN']);
  }

  public estVisiteur(): boolean {
    const user = this.currentUserValue;
    return !!user && user.role === 'VISITEUR';
  }

  public login(user: UserSession): void {
    localStorage.setItem('kasa_user', JSON.stringify(user));
    this.userSubject.next(user);
  }

  public logout(): void {
    localStorage.removeItem('kasa_user');
    this.userSubject.next(null);
    this.router.navigate(['/']);
  }
}

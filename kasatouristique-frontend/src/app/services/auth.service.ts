import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';

export interface UserSession {
  id?: number;
  email: string;
  nom?: string;
  prenom?: string;
  role?: string;
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

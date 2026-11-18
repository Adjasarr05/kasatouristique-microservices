import { Routes } from '@angular/router';
import { AccueilComponent } from './pages/accueil/accueil.component';
import { CatalogueComponent } from './pages/catalogue/catalogue.component';
import { CategoriesComponent } from './pages/categories/categories.component';
import { CircuitsComponent } from './pages/circuits/circuits.component';
import { VisitesComponent } from './pages/visites/visites.component';
import { ReservationsComponent } from './pages/reservations/reservations.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { AdminComponent } from './pages/admin/admin.component';
import { BilletComponent } from './pages/billet/billet.component';

export const routes: Routes = [
  { path: '', component: AccueilComponent },
  { path: 'catalogue', component: CatalogueComponent },
  { path: 'categories', component: CategoriesComponent },
  { path: 'circuits', component: CircuitsComponent },
  { path: 'visites', component: VisitesComponent },
  { path: 'reservations', component: ReservationsComponent },
  { path: 'reservations/billet/:code', component: BilletComponent },
  { path: 'admin', component: AdminComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: '**', redirectTo: '' }
];

import { Routes } from '@angular/router';
import {NotFoundComponent} from './pages/not-found/not-found.component';
import {MainPageComponent} from './pages/main-page/main-page.component';
import {AuthGuard} from './core/guards/auth.guard';
import {EtudiantListComponent} from './pages/etudiants/etudiant-list/etudiant-list.component';


export enum AppRoutes {
  Main = '',
  Etudiant = 'etudiants',
  Logout = 'logout',
  NotFound = '404',
}

export const routes: Routes = [
  {
    path: AppRoutes.Etudiant,
    canActivate: [AuthGuard],
    component: EtudiantListComponent,
  },
  {
    path: AppRoutes.NotFound,
    component: NotFoundComponent,
  },
  {
    path: '',
    loadComponent: () => MainPageComponent
  },
  {
    path: '**',
    loadComponent: () => NotFoundComponent
  },
];


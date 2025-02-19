import { Routes } from '@angular/router';
import {NotFoundComponent} from './pages/not-found/not-found.component';
import {MainPageComponent} from './pages/main-page/main-page.component';


export enum AppRoutes {
  Main = '',
  Protected = 'protected',
  Unprotected = 'unprotected',
  Logout = 'logout',
  NotFound = '404',
}

export const routes: Routes = [
  // {
  //   path: AppRoutes.Protected,
  //   canActivate: [AuthGuard],
  //   component: ProtectedRouteComponent,
  // },
  // {
  //   path: AppRoutes.Un
  //   protected,
  //   component: UnprotectedRouteComponent,
  // },
  // {
  //   path: AppRoutes.Logout,
  //   canActivate: [LogoutRouteGuard],
  //   component: LogoutScreenComponent,
  // },
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


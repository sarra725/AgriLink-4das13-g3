import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', loadComponent: () => import('./pages/index.page').then(m => m.IndexPageComponent) },
  { path: 'shop', loadComponent: () => import('./pages/FarmShop/shop.page').then(m => m.ShopPageComponent) },
  { path: 'visit', loadComponent: () => import('./pages/visit.page').then(m => m.VisitPageComponent) },
  { path: '**', redirectTo: '' },
];

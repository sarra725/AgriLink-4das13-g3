import {
  Component,
  OnInit,
  inject,
  signal,
  Injector,
  afterNextRender
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { OffreProduit } from '../../models/offre-produit';
import { OffreProduitService } from '../../services/offre-produit';

@Component({
  selector: 'app-shop-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './shop.page.html'
})
export class ShopPageComponent implements OnInit {

  // ==================== SERVICES ====================

  private offreService = inject(OffreProduitService);
  private injector = inject(Injector);

  // ==================== VARIABLES ====================

  readonly backendUrl = 'http://localhost:8084';

  offres = signal<OffreProduit[]>([]);
  loading = signal(true);
  errorMessage = signal('');

  // ==================== INITIALISATION ====================

  ngOnInit(): void {
    this.loadOffres();
  }

  // ==================== RECUPERATION DES OFFRES ====================

  loadOffres(): void {

    this.loading.set(true);
    this.errorMessage.set('');

    this.offreService.getAllOffres().subscribe({

      next: (data) => {

        this.offres.set(data);
        this.loading.set(false);

        console.log('Offres récupérées :', data);

        // Attendre l'affichage des produits dans le HTML
        afterNextRender(() => {
          this.initOriginalScript();
        }, { injector: this.injector });

      },

      error: (error) => {

        console.error('Erreur :', error);

        this.errorMessage.set(
          'Unable to load products.'
        );

        this.loading.set(false);

        afterNextRender(() => {
          this.initOriginalScript();
        }, { injector: this.injector });

      }

    });
  }

  // ==================== JAVASCRIPT ORIGINAL ====================

  private initOriginalScript(): void {

    // Eviter le chargement multiple du script
    if (document.querySelector('script[data-furrow-init]')) {
      return;
    }

    const script = document.createElement('script');

    script.src = '/js/main.js';

    script.dataset['furrowInit'] = 'true';

    document.body.appendChild(script);
  }

  // ==================== CATEGORIES : FILTRAGE ====================

  getCategoryKey(categorie: string): string {

    switch (categorie) {

      case 'VEG_FRUIT':
        return 'veg';

      case 'EGGS':
        return 'eggs';

      case 'HONEY':
        return 'honey';

      case 'PRESERVES':
        return 'preserves';

      case 'MEAT_BOXES':
        return 'meat';

      default:
        return categorie.toLowerCase();
    }
  }

  // ==================== CATEGORIES : AFFICHAGE ====================

  getCategoryLabel(categorie: string): string {

    switch (categorie) {

      case 'VEG_FRUIT':
        return 'Veg & fruit';

      case 'EGGS':
        return 'Eggs';

      case 'HONEY':
        return 'Honey';

      case 'PRESERVES':
        return 'Preserves';

      case 'MEAT_BOXES':
        return 'Meat boxes';

      default:
        return categorie;
    }
  }

  // ==================== COMPTEUR PAR CATEGORIE ====================

  countByCategory(categorie: string): number {

    return this.offres().filter(
      offre => offre.categorie === categorie
    ).length;

  }

}
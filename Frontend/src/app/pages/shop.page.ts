import { Component } from '@angular/core';
import { OriginalPageComponent } from '../shared/original-page.component';

@Component({
  selector: 'app-shop-page',
  standalone: true,
  imports: [OriginalPageComponent],
  template: '<app-original-page page="shop" />',
})
export class ShopPageComponent {}

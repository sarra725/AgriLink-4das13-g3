import { Component } from '@angular/core';
import { OriginalPageComponent } from '../shared/original-page.component';

@Component({
  selector: 'app-index-page',
  standalone: true,
  imports: [OriginalPageComponent],
  template: '<app-original-page page="index" />',
})
export class IndexPageComponent {}

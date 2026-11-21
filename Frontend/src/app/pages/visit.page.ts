import { Component } from '@angular/core';
import { OriginalPageComponent } from '../shared/original-page.component';

@Component({
  selector: 'app-visit-page',
  standalone: true,
  imports: [OriginalPageComponent],
  template: '<app-original-page page="visit" />',
})
export class VisitPageComponent {}

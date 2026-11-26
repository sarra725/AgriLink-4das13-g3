import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { OffreProduit } from '../models/offre-produit';

@Injectable({
  providedIn: 'root'
})
export class OffreProduitService {

  private http = inject(HttpClient);

  private apiUrl = 'http://localhost:8084/api/offres';

  getAllOffres(): Observable<OffreProduit[]> {
    return this.http.get<OffreProduit[]>(this.apiUrl);
  }

}
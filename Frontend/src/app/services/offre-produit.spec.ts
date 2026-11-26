import { TestBed } from '@angular/core/testing';

import { OffreProduit } from './offre-produit';

describe('OffreProduit', () => {
  let service: OffreProduit;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OffreProduit);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

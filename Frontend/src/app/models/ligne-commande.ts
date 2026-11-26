import { OffreProduit } from './offre-produit';

export interface LigneCommande {
  id?: number;
  qte: number;
  prixUnit: number;
  offreProduit: OffreProduit;
}
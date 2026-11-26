import { Agriculteur } from './agriculteur';
import { CategorieProduit } from '../enumeration/categorie-produit';
import { QualiteProduit } from '../enumeration/qualite-produit';
import { StatutOffre } from '../enumeration/statut-offre';

export interface OffreProduit {
  idOffre?: number;
  nomProduit: string;
  description: string;
  prixUnitaire: number;
  unite: string;
  quantiteDispo: number;
  qualite: QualiteProduit;
  statut?: StatutOffre;
  categorie: CategorieProduit;
  imageUrl: string;
  datePub?: string;
  agriculteur?: Agriculteur;
  agriculteurId?: number;
}
import { Client } from './client';
import { Agriculteur } from './agriculteur';
import { LigneCommande } from './ligne-commande';
import { Payment } from './payment';
import { StatutCommande } from '../enumeration/statut-commande';

export interface Commande {
  id?: number;
  dateCommande?: string;
  montantTotal: number;
  adresseLivraison: string;
  statut?: StatutCommande;

  client?: Client;
  agriculteur?: Agriculteur;
  lignesCommande: LigneCommande[];
  payment?: Payment;
}
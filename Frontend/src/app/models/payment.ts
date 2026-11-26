import { StatutPayment } from '../enumeration/statut-payment';
import { MethodePayment } from '../enumeration/methode-payment';

export interface Payment {
  id?: number;
  montant: number;
  datePayment?: string;
  methode: MethodePayment;
  statut?: StatutPayment;
}
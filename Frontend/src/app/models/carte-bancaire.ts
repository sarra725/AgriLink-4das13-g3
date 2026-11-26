import { TypeCarte } from '../enumeration/type-carte';

export interface CarteBancaire {
  idCarte?: number;
  derniersQuatreChiffres: string;
  expirationDate: string;
  type: TypeCarte;
}
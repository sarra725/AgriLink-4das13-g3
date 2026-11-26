import { Role } from '../enumeration/role';

export interface User {
  id?: number;
  nom: string;
  prenom: string;
  email: string;
  telephone: string;
  adresse: string;
  dateCreation?: string;
  actif?: boolean;
  role: Role;
}
import { User } from './user';

export interface Agriculteur extends User {
  localisation: string;
  specialite: string;
}
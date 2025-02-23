import {User} from './UserModel';

export interface Etudiant {
  user: User
  phoneNumber: string,
  address: string,
  id?: string
}

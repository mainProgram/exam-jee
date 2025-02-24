import { User } from "./UserModel"

export interface Professeur {
  user: User
  phoneNumber: string,
  address: string,
  id?: string
}
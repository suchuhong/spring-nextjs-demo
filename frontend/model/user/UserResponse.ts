export interface UserResponse {
  id: number
  role: Role
  firstName?: string
  lastName?: string
  email: string
  profileImageUrl?: string
}

export enum Role {
  USER = 'USER',
  ADMIN = 'ADMIN',
}

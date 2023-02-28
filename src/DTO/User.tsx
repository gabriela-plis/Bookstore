import DTO from "./DTO";

export interface User extends DTO {
    id: number;
    firstName: string,
    lastName: string,
    phone: string,
    email: string,
    password: string,
    employee: boolean
}

export default User;
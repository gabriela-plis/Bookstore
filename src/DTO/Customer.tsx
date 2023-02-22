import DTO from "./DTO";

export interface Customer extends DTO {
    id: number;
    firstName: string,
    lastName: string,
    phone: string,
    email: string,
    password: string
}
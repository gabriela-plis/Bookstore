import DTO from "./DTO";

interface Customer extends DTO {
    firstName: string,
    lastName: string,
    phone: string,
    email: string,
    password: string
}
import DTO from "./DTO";

interface RegisteredUserDTO extends DTO {
    firstName: string,
    lastName: string,
    phone: string,
    email: string,
    password: string,
    employee: boolean
}

export default RegisteredUserDTO;
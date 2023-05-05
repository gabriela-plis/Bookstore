import DTO from "./DTO";

interface UserDTO extends DTO {
    id: number;
    firstName: string,
    lastName: string,
    phone: string,
    email: string,
    roles: string[]
}

export default UserDTO;
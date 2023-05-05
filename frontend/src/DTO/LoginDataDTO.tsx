import DTO from "./DTO";

interface LoginDataDTO extends DTO {
    email: string,
    password: string
}

export default LoginDataDTO;
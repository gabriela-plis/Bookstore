import DTO from "./DTO";

interface LoginData extends DTO {
    email: string,
    password: string,
    employee: boolean
}

export default LoginData;
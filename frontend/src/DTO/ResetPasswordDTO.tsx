import DTO from "./DTO";

interface ResetPasswordDTO extends DTO {
    currentPassword: string,
    newPassword: string
}
 
export default ResetPasswordDTO;
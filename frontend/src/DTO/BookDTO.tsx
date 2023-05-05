import BookTypeDTO from "./BookTypeDTO";
import DTO from "./DTO";

interface BookDTO extends DTO {
    id?: number,
    title: string,
    author: string,
    publishYear: number,
    canBeBorrow: boolean,
    availableAmount: number, 
    type: BookTypeDTO
}
 
export default BookDTO;
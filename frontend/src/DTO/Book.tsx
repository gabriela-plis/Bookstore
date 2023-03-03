import DTO from "./DTO";

interface Book extends DTO {
    id?: number,
    title: string,
    author: string,
    publishYear: number,
    availableAmount: number, 
    type: string
}
 
export default Book;
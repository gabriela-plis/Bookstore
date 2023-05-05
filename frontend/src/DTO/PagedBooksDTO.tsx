import Book from "./BookDTO";
import DTO from "./DTO";

interface PagedBooksDTO extends DTO {
    totalPages: number,
    books: Book[]
}

export default PagedBooksDTO
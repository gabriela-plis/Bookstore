import DTO from "./DTO";

interface BookSortingCriteriaDTO extends DTO {
    minPublishYear: number,
    maxPublishYear: number,
    type: string
}

export default BookSortingCriteriaDTO;
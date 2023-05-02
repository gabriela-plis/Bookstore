import DTO from "./DTO";

interface BookSortingCriteriaDTO extends DTO {
    minPublishYear?: number,
    maxPublishYear?: number,
    types?: Set<string>
}

export default BookSortingCriteriaDTO;
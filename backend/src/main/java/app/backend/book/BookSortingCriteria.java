package app.backend.book;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class BookSortingCriteria {

    private Integer minPublishYear;
    private Integer maxPublishYear;
    private String type;

//    @JsonCreator
//    public BookSortingCriteria(@JsonProperty("type") String type, @JsonProperty("min") Integer publishYearMin, @JsonProperty("max") Integer publishYearMax) {
//        this.type = type;
//        this.publishYearMin = publishYearMin;
//        this.publishYearMax = publishYearMax;
//    }
}

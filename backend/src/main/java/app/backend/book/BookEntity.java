package app.backend.book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    @Column(name = "id")
    Integer id;

    @Column(name = "title")
    String title;

    @Column(name = "author")
    String author;

    @Column(name = "publish_year")
    Integer publishYear;

    @Column(name = "available_amount")
    Integer availableAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    BookTypeEntity bookType;
}


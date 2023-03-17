package app.backend.book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "book_types")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class BookTypeEntity {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    @Column(name = "id")
    Integer id;

    @Column(name = "name")
    String name;

    @OneToOne(mappedBy = "type")
    BookEntity book;
}

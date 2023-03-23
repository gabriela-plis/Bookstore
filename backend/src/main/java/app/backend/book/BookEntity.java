package app.backend.book;

import app.backend.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "publish_year")
    private Integer publishYear;

    @Column(name = "can_be_borrow")
    private Boolean canBeBorrow;

    @Column(name = "available_amount")
    private Integer availableAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private BookTypeEntity bookType;

    @ManyToMany
    @JoinTable(name = "users_to_books",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> ownerUsers;
}


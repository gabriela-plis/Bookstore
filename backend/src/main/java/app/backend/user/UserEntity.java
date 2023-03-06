package app.backend.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "USERS")
@Data
public class UserEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "phone")
    private String phone;

    @NotNull
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "is_employee", columnDefinition = "boolean default false")
    private boolean employee;
}

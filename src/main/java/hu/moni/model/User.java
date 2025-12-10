package hu.moni.model;

import hu.moni.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "family_name")
    private String familyName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "password")
    private String password;
}


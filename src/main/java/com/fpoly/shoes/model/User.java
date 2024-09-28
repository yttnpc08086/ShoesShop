package com.fpoly.shoes.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role;

    // New fields for the user registration form
    @Column(nullable = true, columnDefinition = "nvarchar(255)")
    private String fullname; // Nullable to allow for OAuth2 registration

    @Column(nullable = false, unique = true)
    private String email; // OAuth2 will provide this

    @Column(nullable = true, unique = true)
    private String phone; // Nullable to allow flexibility for OAuth2 registration

    @Column(nullable = true, columnDefinition = "nvarchar(255)")
    private String address; // Nullable to avoid issues during OAuth2 login

    // Constructors
    public User() {}

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(Long id, String username, String password, String role, String fullname, String email, String phone, String address) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

}

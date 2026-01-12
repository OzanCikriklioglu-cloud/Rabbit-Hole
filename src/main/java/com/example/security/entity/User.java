package com.example.security.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username cannot be empty") // Boşluk kabul etmez
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters") // Hoca kısa şifre sevmez
    @Column(nullable = false)
    private String password;

    private String role; // "ROLE_USER"  "ROLE_ADMIN"
}
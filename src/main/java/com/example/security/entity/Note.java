package com.example.security.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * The Rabbit Hole - Encrypted Asset Entity
 * v0.4.0 - Data Isolation Edition
 */
@Entity
@Table(name = "notes") // Veritabanında 'notes' tablosuna denk gelir
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // İleride test yazarken veya hızlı obje oluştururken çok işine yarar
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * Her notun bir sahibi (tavşanı) vardır.
     * FetchType.LAZY: Notu çektiğimizde kullanıcı bilgilerini sadece ihtiyaç duyarsak getirir (Performans için).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
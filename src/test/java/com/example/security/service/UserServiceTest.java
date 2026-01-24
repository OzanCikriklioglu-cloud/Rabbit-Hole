package com.example.security.service;

import com.example.security.entity.User;
import com.example.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_ShouldHashPasswordAndSave() {
        // Given
        String rawPassword = "Password123";
        String encodedPassword = "hashed_password";
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // When
        userService.registerUser("testuser", rawPassword);

        // Then
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder).encode(rawPassword);
    }

    @Test
    void registerUser_ShouldThrowException_WhenUserExists() {
        // Given
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));

        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            userService.registerUser("existingUser", "anyPassword123");
        });
    }
}
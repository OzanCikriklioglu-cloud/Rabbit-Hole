package com.example.security.service;

import com.example.security.entity.User;
import com.example.security.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Admin için kullanıcı listeleme
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void registerUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        // Şifreyi hashleyerek kaydediyoruz!
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER"); // Yeni kayıt olan herkes standart kullanıcıdır
        userRepository.save(user);
    }
}
package com.example.security.controller;

import com.example.security.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        // Flyway'in oluşturduğu tablodaki tüm kullanıcıları çekiyoruz
        model.addAttribute("users", userService.getAllUsers());
        return "admin/user-list"; // userlist.html dosyasına gönderiyoruz
    }
}
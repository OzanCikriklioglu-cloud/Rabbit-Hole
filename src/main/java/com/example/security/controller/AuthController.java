package com.example.security.controller;

import com.example.security.service.UserService;
import org.springframework.beans.factory.annotation.Value; // Bunu ekle
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Bunu ekle
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    // .env.properties içindeki değişkeni buraya çekiyoruz
    @Value("${CHAPTHA_HTML_API}")
    private String captchaSiteKey;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        // HTML'e 'siteKey' ismiyle gönderiyoruz
        model.addAttribute("siteKey", captchaSiteKey);
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            // Şifre politikası kontrolünü burada (veya tercihen UserService içinde) yapıyoruz
            String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
            if (!password.matches(passwordPattern)) {
                model.addAttribute("error", "Password must be at least 8 characters long and include numbers, uppercase, and lowercase letters.");
                return "register";
            }

            userService.registerUser(username, password);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
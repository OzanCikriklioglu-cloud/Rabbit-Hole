package com.example.security.controller;

import com.example.security.entity.User;
import com.example.security.repository.UserRepository;
import com.example.security.service.NoteService; // Yeni eklediğimiz servis
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private NoteService noteService; // İş mantığını buraya taşıdık

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String dashboard(Model model, Principal principal, HttpServletResponse response) {
        // Tarayıcı önbelleğini temizleme (Logout sonrası geri tuşunu engeller)
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // DTO dönüşümü ve veri çekme artık NoteService içinde yapılıyor
        model.addAttribute("notes", noteService.getNotesForUser(user));
        model.addAttribute("username", username);

        return "dashboard";
    }

    @PostMapping("/add")
    public String addNote(@RequestParam String content, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Kayıt işlemi servise devredildi
        noteService.createNote(content, user);
        return "redirect:/dashboard";
    }

    @PostMapping("/delete/{id}")
    public String deleteNote(@PathVariable Long id, Principal principal) {
        // Yetki kontrolü ve silme işlemi servis içinde merkezi olarak yapılıyor
        try {
            noteService.deleteNote(id, principal.getName());
        } catch (SecurityException e) {
            return "redirect:/dashboard?error=unauthorized";
        }
        return "redirect:/dashboard";
    }
}
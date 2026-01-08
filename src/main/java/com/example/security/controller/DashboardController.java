package com.example.security.controller;

import com.example.security.entity.Note;
import com.example.security.entity.User;
import com.example.security.repository.NoteRepository;
import com.example.security.repository.UserRepository;
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
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String dashboard(Model model, Principal principal) {
        // 1. Giriş yapan kullanıcının adını al
        String username = principal.getName();

        // 2. Kullanıcıyı DB'den bul
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 3. SADECE bu kullanıcıya ait notları modele ekle (Data Isolation)
        model.addAttribute("notes", noteRepository.findByUser(user));
        model.addAttribute("username", username);

        return "dashboard";
    }

    @PostMapping("/add")
    public String addNote(@RequestParam String content, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username).get();

        Note note = new Note();
        note.setContent(content);
        note.setUser(user);

        noteRepository.save(note);
        return "redirect:/dashboard";
    }
}
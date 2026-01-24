package com.example.security.service;

import com.example.security.dto.NoteDTO;
import com.example.security.entity.Note;
import com.example.security.entity.User;
import com.example.security.repository.NoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    // Kullanıcının notlarını getirirken DTO'ya çevirme mantığını buraya aldık
    public List<NoteDTO> getNotesForUser(User user) {
        return noteRepository.findByUser(user).stream()
                .map(note -> {
                    NoteDTO dto = new NoteDTO();
                    dto.setId(note.getId());
                    dto.setContent(note.getContent());
                    dto.setOwnerUsername(user.getUsername());
                    return dto;
                }).collect(Collectors.toList());
    }

    // Yeni not ekleme mantığı
    public void createNote(String content, User user) {
        Note note = new Note();
        note.setContent(content);
        note.setUser(user);
        noteRepository.save(note);
    }

    // SİLME MANTIĞI: Hocanın en önem verdiği güvenlik kontrolü (Data Isolation)
    private static final Logger logger = LoggerFactory.getLogger(NoteService.class);

    public void deleteNote(Long id, String currentUsername) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Resource not found: User {} tried to delete non-existent Note ID {}", currentUsername, id);
                    return new RuntimeException("System Error: Resource not found.");
                });

        if (!note.getUser().getUsername().equals(currentUsername)) {
            // KRİTİK LOG: Güvenlik ihlali girişimi
            logger.error("SECURITY ALERT: User '{}' attempted to delete Note ID {} which belongs to '{}'!",
                    currentUsername, id, note.getUser().getUsername());
            throw new SecurityException("Unauthorized deletion attempt!");
        }

        noteRepository.delete(note);
        logger.info("Note ID {} successfully deleted by user '{}'", id, currentUsername);
    }
}
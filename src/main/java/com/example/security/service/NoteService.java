package com.example.security.service;

import com.example.security.dto.NoteDTO;
import com.example.security.entity.Note;
import com.example.security.entity.User;
import com.example.security.repository.NoteRepository;
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
    public void deleteNote(Long id, String currentUsername) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("System Error: Resource not found."));

        // Güvenlik Duvarı: Notun sahibi, silmek isteyen kişi mi?
        if (!note.getUser().getUsername().equals(currentUsername)) {
            // Eğer biri ID tahmin ederek başkasının notunu silmeye çalışırsa burası engeller
            throw new SecurityException("Security Alert: Unauthorized deletion attempt!");
        }

        noteRepository.delete(note);
    }
}
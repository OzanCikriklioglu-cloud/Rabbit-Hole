package com.example.security.dto;

import lombok.Data;

@Data
public class NoteDTO {
    private Long id;
    private String content;
    // User objesinin tamamını değil, sadece adını gönderiyoruz (Güvenlik!)
    private String ownerUsername;
}
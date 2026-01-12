package com.example.security.repository; // PAKET YOLUNUN DOĞRU OLDUĞUNA EMİN OL

import com.example.security.entity.Note;
import com.example.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    // Sadece belirli bir kullanıcıya ait notları getiren can alıcı metod:
    List<Note> findByUser(User user);

}
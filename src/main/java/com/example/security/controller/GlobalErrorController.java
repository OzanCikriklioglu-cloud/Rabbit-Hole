package com.example.security.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.access.AccessDeniedException;

@ControllerAdvice
public class GlobalErrorController {

    @ExceptionHandler(Exception.class)
    public String handleAllErrors(Exception ex, Model model) {
        // Hata mesajını loglayabiliriz (isteğe bağlı)
        System.err.println("Global Error: " + ex.getMessage());

        // information leakage engellemek icin kendi hata mesaimizi atiyoruz.
        model.addAttribute("message", "Beklenmedik bir sorun oluştu. Lütfen daha sonra tekrar deneyiniz.");
        model.addAttribute("detail", ex.getMessage()); // Hocanın görmesi için detayı da ekledik

        return "error"; // src/main/resources/templates/error.html dosyasını arar
    }

    // Yetkisiz erişim denemeleri için özel yakalayıcı (Access Denied)
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(AccessDeniedException ex) {
        return "redirect:/dashboard?error=unauthorized";
    }
}
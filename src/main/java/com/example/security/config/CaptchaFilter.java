package com.example.security.config;

import com.example.security.service.CaptchaService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    private CaptchaService captchaService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // SADECE /login adresine POST (giriş yap butonu) geldiğinde araya gir
        if ("POST".equalsIgnoreCase(request.getMethod()) && "/login".equals(request.getServletPath())) {

            // Google'ın kutucuğundan gelen gizli kodu (token) alıyoruz
            String captchaResponse = request.getParameter("g-recaptcha-response");

            // Servise soruyoruz: "Kanka bu robot mu?"
            if (captchaResponse == null || !captchaService.verify(captchaResponse)) {
                // Robot veya işaretlenmemişse: Girişi engelle ve login'e hata mesajıyla at
                response.sendRedirect("/login?error=captcha");
                return;
            }
        }

        // Eğer login isteği değilse veya captcha doğruysa: Yoluna devam et
        filterChain.doFilter(request, response);
    }
}
package com.example.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CaptchaFilter captchaFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // --- EKLEDİĞİMİZ KISIM: TARAYICI ÖNBELLEĞİNİ (CACHE) DEVRE DIŞI BIRAKIR ---
                // Bu ayar, logout olduktan sonra geri tuşuna basıldığında eski sayfanın görünmesini engeller.
                .headers(headers -> headers
                        .cacheControl(cache -> cache.disable())
                )
                // -----------------------------------------------------------------------

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/style.css").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)

                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true) // Session'ı geçersiz kıl
                        .clearAuthentication(true)   // Yetkileri temizle
                        .deleteCookies("JSESSIONID") // Çerezleri sil
                        .permitAll()
                );

        return http.build();
    }
}
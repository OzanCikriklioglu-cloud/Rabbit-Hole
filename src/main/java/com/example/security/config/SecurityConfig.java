package com.example.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher; // Bunu import etmeyi unutma!

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
                .headers(headers -> headers
                        // burada otomatik olarak nosniff ekliyor
                        .contentTypeOptions(contentType -> {})
                        // i frame koymayi engeller DENY bu siteye hicbir iframe icine koyma demektir.
                        .frameOptions(frame -> frame.deny())

                        .contentSecurityPolicy(csp -> csp
                                //default-src 'self'; sadece benden gelen kodlari kullan!
                                .policyDirectives("default-src 'self'; " +
                                        // src benden yada google dan olmali ve burada captha icin src alimi var
                                        // google a pencere acip robot saldirilarini engelliyoruz
                                        "script-src 'self' https://www.google.com/recaptcha/ https://www.gstatic.com; " +
                                        // google dan gelecek captha iframe ine izin veriyoruz
                                        "frame-src https://www.google.com; " +
                                        //css kodlari sadece benden gelir ve html kodlarinin icindeki stilleri kabul et
                                        "style-src 'self' 'unsafe-inline';"))
                        // burada direkt referans vermeyi kapatiyoruzki url ler diger insanlar tarafindan gozukmesin.
                        .referrerPolicy(referrer -> referrer
                                .policy(org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/style.css", "/error").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)

                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                .sessionManagement(session -> session
                        .invalidSessionUrl("/login")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")    //cikis yapilicak adres
                        .logoutSuccessUrl("/")   //cikis sonrasi gidicilcek yer
                        .invalidateHttpSession(true) //sunucu tarafindaki session i yok eder
                        .clearAuthentication(true)  // authentication i temizler
                        .deleteCookies("JSESSIONID") // anahtar cerezi sil
                        .permitAll()
                );

        return http.build();
    }

    // --- YENİ EKLEDİĞİMİZ KISIM ---
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
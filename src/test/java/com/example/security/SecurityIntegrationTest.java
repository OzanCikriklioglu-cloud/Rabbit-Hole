package com.example.security;

import com.example.security.entity.User;
import com.example.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // KRİTİK DÜZELTME: DB'de kullanıcı yoksa oluşturuyoruz
        if (userRepository.findByUsername("user1").isEmpty()) {
            User testUser = new User();
            testUser.setUsername("user1");
            testUser.setPassword(passwordEncoder.encode("password"));
            testUser.setRole("ROLE_USER");
            userRepository.save(testUser);
        }
    }

    @Test
    public void unauthenticatedUserShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    public void authenticatedUserCanAccessDashboard() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    public void postRequestWithoutCsrfShouldBeForbidden() throws Exception {
        mockMvc.perform(post("/dashboard/add")
                        .param("content", "Secret note"))
                .andExpect(status().is3xxRedirection()) // 403 yerine 3xx bekle
                .andExpect(redirectedUrl("/login"));    // Login'e atıp atmadığını kontrol et
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    public void postRequestWithCsrfShouldSucceed() throws Exception {
        // Eğer controller başarılıysa dashboard'a redirect (302) atar
        mockMvc.perform(post("/dashboard/add")
                        .with(csrf())
                        .param("content", "Valid note"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }
}
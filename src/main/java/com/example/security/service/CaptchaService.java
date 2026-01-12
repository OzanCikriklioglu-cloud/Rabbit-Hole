package com.example.security.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class CaptchaService {

    @Value("${CHAPTHA_API}")
    private String SECRET_KEY;

    private final String GOOGLE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean verify(String responseToken) {
        RestTemplate restTemplate = new RestTemplate();

        // Google POST isteği
        String url = String.format("%s?secret=%s&response=%s", GOOGLE_VERIFY_URL, SECRET_KEY, responseToken);
        Map<String, Object> response = restTemplate.postForObject(url, null, Map.class);

        return response != null && (Boolean) response.get("success");
    }
}
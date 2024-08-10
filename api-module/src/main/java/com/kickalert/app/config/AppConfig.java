package com.kickalert.app.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * 사용자 인증 관련 설정 정보
 */
@Configuration
@RequiredArgsConstructor
public class AppConfig {

    @Bean
    public AuditorAware<String> auditorProvider(){
        return new SpringSecurityAuditor();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.create("https://api.sportmonks.com/v3/football");
    }

    @Bean
    public FirebaseAuth firebaseAuth() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("firebase/kick-alert-app-dev-firebase-adminsdk.json");
        // Firebase 서비스 계정 JSON 파일 경로
        InputStream serviceAccount = classPathResource.getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        // FirebaseApp 초기화
        FirebaseApp.initializeApp(options);

        return FirebaseAuth.getInstance(FirebaseApp.getInstance());
    }
}

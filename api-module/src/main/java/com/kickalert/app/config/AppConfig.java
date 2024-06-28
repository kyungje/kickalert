package com.kickalert.app.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.reactive.function.client.WebClient;

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
}

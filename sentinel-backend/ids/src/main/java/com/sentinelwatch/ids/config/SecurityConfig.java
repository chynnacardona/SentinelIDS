package com.sentinelwatch.ids.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        // 1. Disable CSRF for specific paths so external scripts (Python) and clients (WebSockets/H2) can connect seamlessly
        http.csrf(csrf -> csrf
            .ignoringRequestMatchers(new AntPathRequestMatcher("/api/alerts/**"))
            .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
            .ignoringRequestMatchers(new AntPathRequestMatcher("/ws-sentinel/**"))
        );

        // 2. Configure endpoint authorization permissions
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(new AntPathRequestMatcher("/api/alerts/**")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/ws-sentinel/**")).permitAll() // Allows WebSocket handshakes to connect without restriction
            .requestMatchers(new AntPathRequestMatcher("/dashboard")).permitAll() // Allows viewing the dashboard page
            .anyRequest().authenticated()
        );

        // 3. Fix H2 Console frame display blocking issues (allows embedding database frame layout views)
        http.headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions.sameOrigin())
        );

        return http.build();
    }
}
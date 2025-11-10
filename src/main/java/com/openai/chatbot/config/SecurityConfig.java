package com.openai.chatbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the application.
 * Configures security headers and access rules.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configure security filter chain with appropriate headers and access rules.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Configure security headers
            .headers(headers -> headers
                // Content Security Policy - restrict resource loading
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives("default-src 'self'; " +
                        "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
                        "style-src 'self' 'unsafe-inline'; " +
                        "img-src 'self' data: https:; " +
                        "font-src 'self' data:; " +
                        "connect-src 'self' http://localhost:3000")
                )
                // XSS Protection (enabled by default in Spring Security 6+)
                .xssProtection(xss -> {})
                // Prevent clickjacking
                .frameOptions(frame -> frame.deny())
                // MIME type sniffing protection
                .contentTypeOptions(contentType -> {})
                // Referrer Policy
                .referrerPolicy(referrer -> referrer
                    .policy(org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                )
            )
            // Configure authorization
            .authorizeHttpRequests(auth -> auth
                // Allow all requests to public endpoints
                .requestMatchers("/", "/index.html", "/*.js", "/*.css", "/*.ico", "/_next/**", "/static/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                // All other requests require authentication (future enhancement)
                .anyRequest().permitAll()
            )
            // Disable CSRF for API endpoints (REST API is stateless)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
            );

        return http.build();
    }
}

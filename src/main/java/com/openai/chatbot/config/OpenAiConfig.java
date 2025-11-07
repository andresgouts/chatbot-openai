package com.openai.chatbot.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configuration class for OpenAI service.
 * Creates and configures the OpenAI client bean.
 */
@Configuration
public class OpenAiConfig {

    @Value("${openai.api.key}")
    private String apiKey;

    /**
     * Creates an OpenAI service bean with configured timeout.
     *
     * @return OpenAiService instance
     * @throws IllegalStateException if API key is not properly configured
     */
    @Bean
    public OpenAiService openAiService() {
        if (apiKey == null || apiKey.isBlank() || apiKey.equals("your-api-key-here")) {
            throw new IllegalStateException(
                "OpenAI API key is not configured. Set OPENAI_API_KEY environment variable."
            );
        }
        return new OpenAiService(apiKey, Duration.ofSeconds(60));
    }
}

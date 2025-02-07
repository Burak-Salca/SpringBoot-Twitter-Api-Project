package com.example.SpringBoot_Twitter_Api_Project.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("twitter-api")
                .pathsToMatch("/**") // Tüm endpointler
                .build();
    }
}

package com.denizkpln.user_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateItem {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

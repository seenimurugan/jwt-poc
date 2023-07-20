package com.seeni.jwtpoc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.AntPathMatcher;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final JSONObjectConverter jsonObjectConverter;

    @Bean
    public AntPathMatcher antPathMatcher() {
        final var antPathMatcher = new AntPathMatcher();
        antPathMatcher.setCaseSensitive(false);
        return antPathMatcher;
    }

    @Bean
    public ConversionService conversionService() {
        DefaultConversionService service = new DefaultConversionService();
        service.addConverter(jsonObjectConverter);
        return service;
    }

}

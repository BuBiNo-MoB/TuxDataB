package com.database.TuxDataB.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        System.out.println("ModelMapper bean creato!");
        return new ModelMapper();
    }
}

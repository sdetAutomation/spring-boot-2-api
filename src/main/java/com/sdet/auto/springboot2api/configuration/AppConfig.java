package com.sdet.auto.springboot2api.configuration;

import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;

@Configuration
public class AppConfig {

    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}

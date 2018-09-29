package com.sminedata.eric.demo.config;

import com.sminedata.eric.demo.service.WeatherResource;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

// @Component
public class JerseyConfig extends ResourceConfig{
    public JerseyConfig() {
        register(WeatherResource.class);
        packages("com.sminedata.eric.demo");
    }
}
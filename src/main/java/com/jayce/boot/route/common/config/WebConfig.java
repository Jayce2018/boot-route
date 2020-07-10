package com.jayce.boot.route.common.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class WebConfig implements WebMvcRegistrations {
    @Bean
    public DispatcherServlet dispatcherServlet()
    {
        return new VersionDispatcherServlet();
    }
}
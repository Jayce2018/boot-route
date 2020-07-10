package com.jayce.boot.route.common.config;

import com.jayce.boot.route.function.apiversion.CustomRequestMappingHandlerMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Override
    public RequestMappingHandlerMapping createRequestMappingHandlerMapping () {
        return new CustomRequestMappingHandlerMapping();
    }

    /*@Bean
    protected RequestMappingHandlerMapping customRequestMappingHandlerMapping() {
        return new CustomRequestMappingHandlerMapping();
    }*/

}

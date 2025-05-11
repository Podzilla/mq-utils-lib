package com.podzilla.auth.config;

import com.podzilla.auth.interceptors.AllowedRolesInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AllowedRolesAutoConfig implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingBean
    public AllowedRolesInterceptor allowedRolesInterceptor() {
        return new AllowedRolesInterceptor();
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(allowedRolesInterceptor());
    }
}

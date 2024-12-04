package com.example.isdfarmersmarket.business.configurations.serverinfo;

import com.example.isdfarmersmarket.web.interceptors.RequestCategorizingInterceptor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableScheduling
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ServerInfoConfig implements WebMvcConfigurer {
    RequestCategorizingInterceptor requestCategorizingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestCategorizingInterceptor).addPathPatterns("/**");
    }
}

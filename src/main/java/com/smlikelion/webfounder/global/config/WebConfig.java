package com.smlikelion.webfounder.global.config;

import com.smlikelion.webfounder.security.AuthArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public AuthArgumentResolver authArgumentResolver() {
        return new AuthArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
      
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://54.180.201.99", "http://13.209.61.108:8080",
                        "http://sooklion-bucket.s3-website.ap-northeast-2.amazonaws.com","https://smwu-likelion.com",
                        "http://smadminlion.store", "http://smwu-likelion.com.s3-website.ap-northeast-2.amazonaws.com",
                        "http://smwu-likelion.com")  // 허용할 클라이언트 도메인
                .allowedMethods("*") // "GET", "POST", "PUT", "DELETE" 외에도 "OPTIONS", "HEAD" 등이 있음
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "Content-Type")
                .allowCredentials(true)
                .maxAge(3600);
    }
 }


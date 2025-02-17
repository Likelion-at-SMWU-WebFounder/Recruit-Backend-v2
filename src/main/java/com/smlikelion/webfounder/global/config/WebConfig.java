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
                .allowedOrigins("http://localhost:3000", "http://54.180.86.155:8080",
                        "http://smwu-likelion-deploy.s3-website.ap-northeast-2.amazonaws.com",
                        "https://smwulion.com", "https://admin-smwulion.netlify.app",
                        "https://d3vjgf9am7gpdo.cloudfront.net", "https://to4er5ywoj.execute-api.ap-northeast-2.amazonaws.com", // 2기
                        "http://sooklion-bucket.s3-website.ap-northeast-2.amazonaws.com",
                        "http://smadminlion.store", "http://smwu-likelion.com.s3-website.ap-northeast-2.amazonaws.com",
                        "https://api.smwulion.com", "http://15.164.213.172:8080", "http://13.209.13.138", "http://smwulion.com", "http://smwulion-admin.s3-website-us-east-1.amazonaws.com"
                )  // 허용할 클라이언트 도메인
                .allowedMethods("*") // "GET", "POST", "PUT", "DELETE" 외에도 "OPTIONS", "HEAD" 등이 있음
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "Content-Type")
                .allowCredentials(true)
                .maxAge(3600);
    }
 }


package com.essay_backend.controller.Interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//public class SpringMvcConfig implements WebMvcConfigurer {
//
//    @Bean
//    public DecryptInterceptor decryptInterceptor(){
//        return new DecryptInterceptor();
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(decryptInterceptor()).addPathPatterns("/**");
//    }
//}

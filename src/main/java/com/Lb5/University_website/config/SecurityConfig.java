package com.Lb5.University_website.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()  // Разрешаем ВСЕ запросы без авторизации
//                )
                .formLogin(form -> form.disable());  // Отключаем форму логина Spring
//                .httpBasic(basic -> basic.disable()) // Отключаем basic auth
//                .csrf(csrf -> csrf.disable());      // Отключаем CSRF

        return http.build();
    }
}
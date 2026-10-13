package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http,
                        JwtAuthenticationFilter jwtAuthenticationFilter)
                        throws Exception {

                return http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/usuarios/login",
                                                                "/usuarios/register",
                                                                "/h2-console/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .headers(headers -> headers
                                                .frameOptions(frame -> frame.sameOrigin()))
                                .addFilterBefore(
                                                jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class)
                                .build();
        }
}
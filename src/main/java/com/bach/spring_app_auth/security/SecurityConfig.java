package com.bach.spring_app_auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/register").permitAll()
                .anyRequest().authenticated()
            )

            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }
    //esta configuracion permite que lo usuarios puedas acceder al endpont de regustro mientras protege el resto

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

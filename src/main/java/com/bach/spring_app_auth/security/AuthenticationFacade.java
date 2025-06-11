package com.bach.spring_app_auth.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.bach.spring_app_auth.entities.User;
import com.bach.spring_app_auth.repository.UserRepository;

@Component
public class AuthenticationFacade {
    private final UserRepository userRepository;

    public AuthenticationFacade (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("usuario no encontrado"));
    }
}
//Este componente centraliza la lógica para obtener el usuario actual, facilitando su reutilización en diferentes partes de la aplicación.
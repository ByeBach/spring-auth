package com.bach.spring_app_auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bach.spring_app_auth.entities.Role;
import com.bach.spring_app_auth.entities.User;
import com.bach.spring_app_auth.repository.RoleRepository;
import com.bach.spring_app_auth.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;                  
    }

    public User registerUser(User user){
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new IllegalArgumentException("el nombre del usuario ya esta en uso");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("USER")
            .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));
        user.getRoles().add(userRole);

        return userRepository.save(user);
    }
}

///Este servicio asegura que las contrasenas sean codificadas y que los usuarios nuevos tengan asignado un rol por defecto /////
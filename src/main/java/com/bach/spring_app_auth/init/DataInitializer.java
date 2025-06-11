package com.bach.spring_app_auth.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.bach.spring_app_auth.entities.Role;
import com.bach.spring_app_auth.repository.RoleRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args){
        if(roleRepository.findByName("USER").isEmpty()){
            Role userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }

        if(roleRepository.findByName("ADMIN").isEmpty()){
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }
    }
    //este componente asegura que los roles necesarios exitan en la base de datos al iniciar la app
}

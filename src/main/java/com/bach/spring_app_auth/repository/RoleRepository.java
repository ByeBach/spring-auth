package com.bach.spring_app_auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bach.spring_app_auth.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    //Busca un rol por su nombre (ej: "ROLE_USER", "ROLE_ADMIN", etc)
    Optional<Role> findByName(String name);

    //Verifica si existe un rol con determinado nombre
    boolean exisexistsByName(String name);

}

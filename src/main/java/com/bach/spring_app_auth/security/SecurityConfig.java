package com.bach.spring_app_auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter){
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/notes/**")
                            .hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/products/**")
                            .hasRole("ADMIN")
                        .anyRequest().authenticated())
                    .sessionManagement(session -> session
                                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
    }
    //esta configuracion permite que lo usuarios puedas acceder al endpont de regustro mientras protege el resto

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
}
/*
 * En esta configuración, deshabilitamos CSRF porque nuestra API REST es sin estado (stateless) y no utiliza formularios. Especificamos que las solicitudes a "/api/auth/**" están permitidas sin autenticar (por ejemplo, para el login y registro), y que cualquier otra solicitud requiere autenticación.

Utilizamos sessionManagement para indicar que no se debe crear sesión de usuario en el servidor, ya que la autenticación 
se maneja mediante tokens JWT.
Finalmente, agregamos nuestro filtro JWT antes del filtro de autenticación por defecto de Spring Security mediante el método addFilterBefore. 
De esta manera, el JwtAuthenticationFilter interceptará las peticiones y realizará la autenticación basada en el token antes de que se apliquen
otros filtros.
Es importante recordar que el JwtAuthenticationFilter debe estar registrado como un @Component o ser definido como un @Bean para que 
Spring pueda inyectarlo y gestionarlo adecuadamente.
Con esta configuración, cualquier petición que incluya un token JWT válido en la cabecera Authorization podrá ser autenticada y el usuario 
será reconocido por Spring Security. Esto permite aplicar políticas de autorización basadas en los roles y permisos del usuario autenticado.
Además, es recomendable manejar las excepciones que puedan ocurrir durante la verificación del token, 
para proporcionar respuestas apropiadas al cliente en caso de errores. Esto se puede lograr extendiendo el filtro y 
capturando las excepciones específicas de JWT:
 * 
 */
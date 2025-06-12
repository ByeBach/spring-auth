package com.bach.spring_app_auth.security;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter (JwtService jwtService, UserDetailsService userDetailsService){
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        try{
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String jwtToken = authorizationHeader.substring(7);
            String username = jwtService.extractUsername(jwtToken);

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if(jwtService.isTokenValid(jwtToken, userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    
                    authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        }catch (JwtException e){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Token JWT invalido: " + e.getMessage());
            return;
        }
    filterChain.doFilter(request, response);
}
}
//En este filtro, primero verificamos si la cabecera Authorization está presente y comienza con el prefijo "Bearer ".
//Luego, extraemos el token JWT y utilizamos el JwtService para obtener el nombre de usuario contenido en el token. 
//Si el usuario no está autenticado en el contexto de seguridad, cargamos los detalles del usuario y validamos el token.
//La validación del token se realiza mediante el método isTokenValid del JwtService, que comprueba la firma y la expiración del token. 
//Si el token es válido, creamos un objeto UsernamePasswordAuthenticationToken y lo establecemos en el contexto de seguridad.
//Esto permite que Spring Security conozca al usuario autenticado y sus authorities para la autorización de futuras peticiones.
/*
 * Esta práctica mejora la seguridad y la experiencia del usuario al proporcionar mensajes claros y estados HTTP correctos.
Al crear un filtro JWT personalizado y configurarlo en el SecurityFilterChain, logramos que Spring Security autentique y 
autorice las solicitudes basándose en los tokens JWT proporcionados por los clientes. Esto es esencial para mantener una 
API REST segura y conforme a las mejores prácticas actuales.
 */
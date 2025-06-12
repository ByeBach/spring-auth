package com.bach.spring_app_auth.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.bach.spring_app_auth.entities.Role;
import com.bach.spring_app_auth.entities.User;

import io.jsonwebtoken.Jwts;


@Service
public class JwtService {
    
    //private final JwtBuilder jwtBuilder;
    private final SecretKey secretKey;

    public JwtService(SecretKey secretKey){
        this.secretKey = secretKey;
        //this.jwtBuilder = jwtBuilder;
    }
    public String extractUsername(String token){
        return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token){
        Date expiration = Jwts.parser()
                                .verifyWith(secretKey)
                                .build()
                                .parseSignedClaims(token)
                                .getPayload()
                                .getExpiration();
        return expiration.before(new Date());
    }

    public String generateToken(User user){
        Instant now = Instant.now();
        Date expiryDate = Date.from(now.plus(1, ChronoUnit.HOURS));

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles()
            .stream()
            .map(Role::getName)
            .collect(Collectors.toList()));

            return Jwts.builder()
                //id del usuario
                .subject(String.valueOf(user.getId()))
                //la clave secreta para firmar el token y saber que es nuestro cuando lleguen las peticiones del frontend
                .signWith(secretKey)
                //fecha de emision del token
                .issuedAt(Date.from(now))
                //fecha de expiracion del token
                .expiration(expiryDate)
                //informacion personalizada: rol o roles, username, email, avatar...
                //.claim("role", user.getRole())
                .claim("email", user.getUsername())
                //.claim("avatar", user.getAvatarurl())
                .compact();
    }
   
}
//Con estos métodos, el JwtService es capaz de extraer información del token y verificar su validez. Es crucial que la clave secreta utilizada
// para firmar y verificar el token sea la misma que en la generación del token durante el proceso de login.
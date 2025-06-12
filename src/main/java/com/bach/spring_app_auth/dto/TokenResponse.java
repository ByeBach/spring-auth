package com.bach.spring_app_auth.dto;

public class TokenResponse {
    private final String token;

    public TokenResponse(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}

//Es importante devolver una respuesta clara al cliente después de la autenticación. Para ello, se crea la clase TokenResponse, que contiene el token generado

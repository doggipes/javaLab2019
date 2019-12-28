package ru.javalab.socketsapp.models;

public class JwtResponse {
    private String token;

    public JwtResponse(String token){
        this.token = token;
    }

    public JwtResponse(){}

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}

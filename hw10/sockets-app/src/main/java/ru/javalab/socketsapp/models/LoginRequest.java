package ru.javalab.socketsapp.models;

public class LoginRequest {
    private String login;
    private String password;

    public LoginRequest(String login, String password){
        this.login = login;
        this.password = password;
    }

    public LoginRequest(){

    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}

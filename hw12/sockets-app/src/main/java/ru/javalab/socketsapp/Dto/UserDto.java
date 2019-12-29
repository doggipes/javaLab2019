package ru.javalab.socketsapp.Dto;

import ru.javalab.socketsapp.models.User;

public class UserDto implements Dto {
    private int id;
    private String login;
    private String token;

    public UserDto(int id, String login, String token) {
        this.id = id;
        this.login = login;
        this.token = token;
    }

    public UserDto(){}

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }
}

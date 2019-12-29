package ru.javalab.socketsapp.protocol;

public class AddFavoritesRequest {
    private String name;
    private String token;
    private Integer id;

    public AddFavoritesRequest(Integer id, String token){
        this.id = id;
        this.token = token;
    }

    public AddFavoritesRequest(){}


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

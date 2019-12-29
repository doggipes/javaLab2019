package ru.javalab.socketsapp.protocol;

public class DeleteProductRequest {
    private String name;
    private String token;
    private int id;

    public DeleteProductRequest(int id, String token){
        this.id = id;
        this.token = token;
    }

    public DeleteProductRequest(){}

    public void setToken(String token) {
        this.token = token;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
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

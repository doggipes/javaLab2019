package ru.javalab.socketsapp.models;

public class AddProductRequest {
    private String name;
    private String price;
    private String token;

    public AddProductRequest(String name, String price, String token){
        this.name = name;
        this.price = price;
        this.token = token;
    }

    public AddProductRequest(){}

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}

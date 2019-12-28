package ru.javalab.socketsapp.db.models;

public class Product {
    private int id;
    private String name;
    private String price;

    public Product(String name, String price){
        this.price = price;
        this.name = name;
    }

    public Product(){}

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}

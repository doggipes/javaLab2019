package ru.javalab.socketsapp.models;

public class Request<T> {
    private String header;
    private T payload;

    public Request(String header, T payload){
        this.header = header;
        this.payload = payload;
    }

    public Request(){

    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public String getHeader() {
        return header;
    }

    public T getPayload() {
        return payload;
    }
}

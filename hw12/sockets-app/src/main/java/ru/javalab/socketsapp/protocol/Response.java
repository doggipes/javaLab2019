package ru.javalab.socketsapp.protocol;

public class Response<T> {
    private String header;
    private T payload;

    public Response(String header, T payload){
        this.header = header;
        this.payload = payload;
    }

    public Response(){

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

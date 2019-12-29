package ru.javalab.socketsapp.protocol;

public class MessageRequest {
    private String message;

    public MessageRequest(String message){
        this.message = message;
    }

    public MessageRequest(){

    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

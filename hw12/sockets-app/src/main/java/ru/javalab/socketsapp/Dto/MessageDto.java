package ru.javalab.socketsapp.Dto;

public class MessageDto {
    private String message;
    private int user_id;

    public MessageDto(){}

    public MessageDto(String message, int user_id){
        this.message = message;
        this.user_id = user_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getMessage() {
        return message;
    }

    public int getUser_id() {
        return user_id;
    }
}

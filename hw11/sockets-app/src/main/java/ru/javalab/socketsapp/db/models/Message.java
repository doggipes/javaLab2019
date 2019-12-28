package ru.javalab.socketsapp.db.models;

import java.util.Date;

public class Message {
    private int id;
    private String message;
    private Date date;
    private int userID;

    public Message(int id, String message, int userID, Date date){
        this.id = id;
        this.message = message;
        this.date = date;
        this.userID = userID;
    }

    public Message(){}

    public int getID(){
        return id;
    }

    public void setID(int id){
        this.id = id;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userId) {
        this.userID = userId;
    }
}

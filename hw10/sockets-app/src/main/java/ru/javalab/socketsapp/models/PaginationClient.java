package ru.javalab.socketsapp.models;

import ru.javalab.socketsapp.db.models.Message;

import java.util.List;

public class PaginationClient {
    private List<Message> list;

    public PaginationClient(List<Message> list){
        this.list = list;
    }

    public PaginationClient(){

    }

    public void setList(List<Message> list) {
        this.list = list;
    }

    public List<Message> getList() {
        return list;
    }
}

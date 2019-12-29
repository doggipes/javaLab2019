package ru.javalab.socketsapp.protocol;

import ru.javalab.socketsapp.Dto.MessageDto;
import ru.javalab.socketsapp.models.Message;

import java.util.List;

public class PaginationClient {
    private List<MessageDto> list;

    public PaginationClient(List<MessageDto> list){
        this.list = list;
    }

    public PaginationClient(){

    }

    public void setList(List<MessageDto> list) {
        this.list = list;
    }

    public List<MessageDto> getList() {
        return list;
    }
}

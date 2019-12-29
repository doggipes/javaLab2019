package ru.javalab.socketsapp.services.message;

import ru.javalab.socketsapp.Dto.Dto;
import ru.javalab.socketsapp.context.Component;
import ru.javalab.socketsapp.models.Message;
import ru.javalab.socketsapp.repository.repository.Message.MessageRepositoryImpl;

public class SendMessageImpl implements SendMessage, Component {
    MessageRepositoryImpl messageRepositoryImpl;

    public SendMessageImpl(){}

    public SendMessageImpl(MessageRepositoryImpl messageRepositoryImpl){
        this.messageRepositoryImpl = messageRepositoryImpl;
    }

    @Override
    public String getName() {
        return "SendMessage";
    }

    @Override
    public Dto save(Message message) {
        messageRepositoryImpl.save(message);
        return null;
    }
}
